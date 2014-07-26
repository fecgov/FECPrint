package gov.fec.efo.fecprint.burst;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.SimpleBookmark;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class BurstPDF {

	private static final Log logger = LogFactory.getLog(BurstPDF.class);

  public static void main(String[] args) throws Exception {

    File pdfSource = new File(args[0]);
    File dirPDF = new File(args[1]);
    try
    {
      String wholePDFPrefixName = pdfSource.getName().split("[.]")[0];
      String pdfNameFormatStr = wholePDFPrefixName + "_%06d.pdf";
      dirPDF = new File(dirPDF, wholePDFPrefixName.substring(wholePDFPrefixName.length() - 3) + "/" + wholePDFPrefixName);
      if (!dirPDF.exists()) {
        dirPDF.mkdirs();
      }
      PdfReader reader = new PdfReader(new RandomAccessFileOrArray(pdfSource.getAbsolutePath()), null);
      for (int i = 1; i <= reader.getNumberOfPages(); i++)
      {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(new File(dirPDF, String.format(pdfNameFormatStr, new Object[] { Integer.valueOf(i) }))));
        document.open();
        copy.addPage(copy.getImportedPage(reader, i));
        document.close();
      }
      copyFile(pdfSource, new File(dirPDF, pdfSource.getName()));
    }
    catch (Exception e)
    {
      logger.error(e.getMessage() + " " + pdfSource.getAbsolutePath(), e);
      System.exit(9);
    }
  }
  
  public static void convertTiffToPdf(File wholePDF, List<File> tiffFileList, List<File> pdfFileList)
    throws DocumentException, MalformedURLException, IOException
  {
    int numOfTiffFiles = tiffFileList.size();
    Document documentWhole = new Document(PageSize.LETTER, 0.0F, 0.0F, 0.0F, 0.0F);
    PdfWriter writerWhole = PdfWriter.getInstance(documentWhole, new FileOutputStream(wholePDF));
    writerWhole.setStrictImageSequence(true);
    documentWhole.open();
    for (int i = 0; i < numOfTiffFiles; i++)
    {
      Document document = new Document(PageSize.LETTER, 0.0F, 0.0F, 0.0F, 0.0F);
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream((File)pdfFileList.get(i)));
      writer.setStrictImageSequence(true);
      document.open();
      Image tiff = Image.getInstance(((File)tiffFileList.get(i)).getAbsolutePath());
      
      tiff.scaleAbsolute(PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight());
      
      document.add(tiff);
      document.close();
      documentWhole.add(tiff);
      if (i != numOfTiffFiles - 1) {
        documentWhole.newPage();
      }
    }
    documentWhole.close();
  }
  
  public static void copyFile(File sourceFile, File destFile)
    throws IOException
  {
    if (!destFile.exists()) {
      destFile.createNewFile();
    }
    FileChannel source = null;
    FileChannel destination = null;
    try
    {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0L, source.size());
    }
    finally
    {
      if (source != null) {
        source.close();
      }
      if (destination != null) {
        destination.close();
      }
    }
  }
  
  public static byte[] convertTiffToPdf(byte[] tiffFile)
    throws DocumentException, MalformedURLException, IOException
  {
    ByteArrayOutputStream outfile = new ByteArrayOutputStream();
    Document document = new Document(PageSize.LETTER);
    PdfWriter writer = PdfWriter.getInstance(document, outfile);
    writer.setStrictImageSequence(true);
    document.open();
    Image tiff = Image.getInstance(tiffFile);
    
    document.add(tiff);
    document.close();
    outfile.flush();
    return outfile.toByteArray();
  }
  
  public static void combinePdfFiles(List<byte[]> pdfs, File combinedPdfFile)
    throws Exception
  {
    PdfReader reader = null;
    Document document = null;
    PdfCopy writer = null;
    ArrayList master = new ArrayList();
    int pageOffset = 0;
    for (byte[] pdf : pdfs)
    {
      int size = pdf.length;
      reader = new PdfReader(pdf);
      reader.consolidateNamedDestinations();
      int n = reader.getNumberOfPages();
      List bookmarks = SimpleBookmark.getBookmark(reader);
      if (bookmarks != null)
      {
        if (pageOffset != 0) {
          SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
        }
        master.addAll(bookmarks);
      }
      pageOffset += n;
      if (document == null)
      {
        document = new Document(reader.getPageSizeWithRotation(1));
        
        writer = new PdfCopy(document, new FileOutputStream(combinedPdfFile));
        
        document.open();
      }
      for (int i = 0; i < n;)
      {
        i++;
        PdfImportedPage page = writer.getImportedPage(reader, i);
        writer.addPage(page);
      }
      PRAcroForm form = reader.getAcroForm();
      if (form != null) {
        writer.copyAcroForm(reader);
      }
    }
    if (!master.isEmpty()) {
      writer.setOutlines(master);
    }
    if (document != null) {
      document.close();
    }
  }
}
