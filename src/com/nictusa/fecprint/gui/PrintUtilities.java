package com.nictusa.fecprint.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;


public class PrintUtilities implements Printable {  

  public static void printComponent(Component c) {
    new PrintUtilities(c).print();
  }

  public PrintUtilities(Component componentToBePrinted) {
    
  }

  public HashPrintRequestAttributeSet print() {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    HashPrintRequestAttributeSet printParams = new HashPrintRequestAttributeSet();  
    printJob.setPrintable(this);
    if (printJob.printDialog(printParams))
    {
      /*try {
    	  PageRanges pageRanges = (PageRanges) printParams.get(PageRanges.class);
        
      } catch(PrinterException pe) {
        System.out.println("Error printing: " + pe);
      }*/
    	return printParams;
    }
    return null;
  }

  public int print(Graphics g, PageFormat pageFormat, int pageIndex) 
  {    
    return(NO_SUCH_PAGE);    
  }
  }
