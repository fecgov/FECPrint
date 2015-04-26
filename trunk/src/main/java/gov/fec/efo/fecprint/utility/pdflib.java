/* pdflib.java
 * Copyright (C) 1997-2000 Thomas Merz. All rights reserved.
 *
 * PDFlib Java class
 */

package gov.fec.efo.fecprint.utility;

import java.io.*;

/** PDFlib -- a library for generating PDF on the fly.
    Note that this is only a syntax summary. For complete information
    please refer to the PDFlib API reference manual which is available
    in the PDF file PDFlib-manual.pdf in the PDFlib distribution.
    @author Thomas Merz
    @version 3.03
*/

public final class pdflib {

    // The initialization code for loading the PDFlib shared library.
    // The library name will be transformed into something platform-
    // specific by the VM, e.g. libpdf_java.so or pdf_java.dll.

    public static void load_lib() throws Exception
	{
		try {
			System.loadLibrary("pdf_java");
		} catch (UnsatisfiedLinkError e) {
			String msg = "Cannot load the PDFlib shared library/DLL for Java.\n" +
			"Make sure that LD_LIBRARY_PATH (Unix) or PATH (Win32)\n" +
			"point to the directory with the PDFlib shared libraries/DLLs,\n" +
			"on Unix including auxiliary libraries (TIFFlib/zlib/libpng).\n" + e ;
			System.err.println(msg) ;
			throw new Exception(msg) ;
		}
		PDF_boot();
    }

    // ------------------------------------------------------------------------
    // public functions

    /** Create a new PDF object. */
    public pdflib() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	p = PDF_new();
    }

    /** Open a new PDF file associated with p, using the supplied file name.
        @return -1 on error, 1 on success.
     */
    public final int open_file(String filename) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_open_file(p, filename);
    }

    /** Close the generated PDF file. */
    public final void close() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_close(p);
    }

    /** Get the contents of the PDF output buffer. The result must be used
        by the client before calling any other PDFlib function. Must not be
        called within page descriptions.
        @return A byte array containing the PDF output generated so far.
     */
    public final byte[] get_buffer() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_get_buffer(p);
    }

    /** Start a new page. */
    public final void begin_page(float width, float height) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_begin_page(p, width, height);
    }

    /** Finish the page. */
    public final void end_page() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_end_page(p);
    }

    /** Set some PDFlib parameters with string type */
    public final void set_parameter(String key, String value) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_parameter(p, key, value);
    }

    /** Get some PDFlib parameters with string type
        @return A string containing the requested parameter.
     */
    public final String get_parameter(String key, float mod) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_get_parameter(p, key, mod);
    }

    /** Set some PDFlib parameters with float type */
    public final void set_value(String key, float value) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_value(p, key, value);
    }

    /** Get the value of some PDFlib parameters with float type
        @return The requested parameter value.
     */
    public final float get_value(String key, float mod) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_get_value(p, key, mod);
    }

    /** Prepare a font for later use with PDF_setfont(). The metrics will be
        loaded, and if embed is nonzero, the font file will be checked (but not
        yet used. Encoding is one of "builtin", "macroman", "winansi", "host",
        or a user-defined name, or the name of a CMap.
	@return A valid font handle.
     */
    public final int findfont(String fontname, String encoding, int embed) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_findfont(p, fontname, encoding, embed);
    }

    /** Set the current font in the given size. The font descriptor must have
        been retrieved via PDF_findfont().
     */
    public final void setfont(int font, float fontsize) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setfont(p, font, fontsize);
    }

    /** Print text in the current font and size at the current position. */
    public final void show(String text) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_show(p, text);
    }

    /** Print text in the current font at (x, y). */
    public final void show_xy(String text, float x, float y) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_show_xy(p, text, x, y);
    }

    /** Print text at the next line. */
    public final void continue_text(String text) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_continue_text(p, text);
    }

    /** Format text in the current font and size into the supplied text box
        according to the requested formatting mode. If width and height
        are 0, only a single line is placed at the point (left, bottom) in
        the requested mode.
        @return Number of characters which didn't fit in the box.
     */
    public final int show_boxed(String text, float left, float bottom,
    	float width, float height, String hmode, String reserved) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_show_boxed(p, text, left, bottom, width, height,
	    	hmode, reserved);
    }

    /** Set the text output position to (x, y). */
    public final void set_text_pos(float x, float y) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_text_pos(p, x, y);
    }

    /** Get the width of text in an arbitrary font which has been selected
        with PDF_findfont().
        @return Width of text in the supplied font and size.
     */
    public final float stringwidth(String text, int font, float size) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_stringwidth(p, text, font, size);
    }

    /** Set the current dash pattern to b black and w white units. */
    public final void setdash(float b, float w) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setdash(p, b, w);
    }

    /** Set a more complicated dash pattern defined by an array. */
    public final void setpolydash(float[] dasharray) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setpolydash(p, dasharray);
    }

    /** Set the flatness to a value between 0 and 100 inclusive. */
    public final void setflat(float flatness) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setflat(p, flatness);
    }

    /** Set the line join parameter to a value between 0 and 2 inclusive. */
    public final void setlinejoin(int linejoin) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setlinejoin(p, linejoin);
    }

    /** Set the linecap parameter to a value between 0 and 2 inclusive. */
    public final void setlinecap(int linecap) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setlinecap(p, linecap);
    }

    /** Set the miter limit to a value greater than or equal to 1. */
    public final void setmiterlimit(float miter) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setmiterlimit(p, miter);
    }

    /** Set the current linewidth to width. */
    public final void setlinewidth(float width) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setlinewidth(p, width);
    }

    /** Save the current graphics state. */
    public final void save() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_save(p);
    }

    /** Restore the most recently saved graphics state. */
    public final void restore() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_restore(p);
    }

    /** Translate the origin of the coordinate system to (tx, ty). */
    public final void translate(float tx, float ty) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_translate(p, tx, ty);
    }

    /** Scale the coordinate system by (sx, sy). */
    public final void scale(float sx, float sy) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_scale(p, sx, sy);
    }

    /** Rotate the coordinate system by phi degrees. */
    public final void rotate(float phi) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_rotate(p, phi);
    }

    /** Skew the coordinate system in x and y direction by alpha and beta
        degrees
     */
    public final void skew(float alpha, float beta) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_skew(p, alpha, beta);
    }

    /** Concatenate a matrix to the CTM. a*d must not be equal to b*c. */
    public final void concat(float a, float b, float c, float d, float e, float f) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_concat(p, a, b, c, d, e, f);
    }

    /** Set the current point to (x, y). */
    public final void moveto(float x, float y) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_moveto(p, x, y);
    }

    /** Draw a line from the current point to (x, y). */
    public final void lineto(float x, float y) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_lineto(p, x, y);
    }

    /** Draw a Bezier curve from the current point, using 3 more control
        points.
     */
    public final void curveto(float x1, float y1, float x2, float y2, float x3, float y3) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_curveto(p, x1, y1, x2, y2, x3, y3);
    }

    /** Draw a circle with center (x, y) and radius r. */
    public final void circle(float x, float y, float r) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_circle(p, x, y, r);
    }

    /** Draw a circular arc with center (x, y) and radius r from alpha1
        to alpha2.
     */
    public final void arc(float x, float y, float r, float alpha1, float alpha2) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_arc(p, x, y, r, alpha1, alpha2);
    }

    /** Draw a rectangle at lower left (x, y) with width and height. */
    public final void rect(float x, float y, float width, float height) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_rect(p, x, y, width, height);
    }

    /** Close the current path. */
    public final void closepath() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_closepath(p);
    }

    /** Stroke the path with the current color and line width,and clear it. */
    public final void stroke() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_stroke(p);
    }

    /** Close the path, and stroke it. */
    public final void closepath_stroke() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_closepath_stroke(p);
    }

    /** Fill the interior of the path with the current fill color. */
    public final void fill() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_fill(p);
    }

    /** Fill and stroke the path with the current fill and stroke color. */
    public final void fill_stroke() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_fill_stroke(p);
    }

    /** Close the path, fill, and stroke it. */
    public final void closepath_fill_stroke() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_closepath_fill_stroke(p);
    }

    /** End the current path.
	@deprecated Use one of the stroke, fill, or clip functions instead.
    */
    public final void endpath() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_endpath(p);
    }

    /** Use the current path as clipping path. */
    public final void clip() throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_clip(p);
    }

    /** Set the current fill color to a gray value between 0 and 1 inclusive. */
    public final void setgray_fill(float g) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setgray_fill(p, g);
    }

    /** Set the current stroke color to a gray value between 0 and 1
        inclusive.
      */
    public final void setgray_stroke(float g) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setgray_stroke(p, g);
    }

    /** Set the current fill and stroke color. */
    public final void setgray(float g) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setgray(p, g);
    }

    /** Set the current fill color to the supplied RGB values. */
    public final void setrgbcolor_fill(float red, float green, float blue) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setrgbcolor_fill(p, red, green, blue);
    }

    /** Set the current stroke color to the supplied RGB values. */
    public final void setrgbcolor_stroke(float red, float green, float blue) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setrgbcolor_stroke(p, red, green, blue);
    }

    /** Set the current fill and stroke color to the supplied RGB values. */
    public final void setrgbcolor(float red, float green, float blue) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_setrgbcolor(p, red, green, blue);
    }

    /** Place an image at the lower left corner (x, y), and scale it. */
    public final void place_image(int image, float x, float y, float scale) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_place_image(p, image, x, y, scale);
    }

    /** Use image data from a variety of data sources.  Supported types
        are "jpeg", "ccitt", "raw".  Supported sources are "memory",x
	"fileref", "url". len is only used for type="raw", params is only
	used for type="ccitt".
	@return A valid image descriptor, or -1 on error.
     */
    public final int open_image(String type, String source, byte[] data, long length, int width, int height, int components, int bpc, String params) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_open_image(p, type, source, data, length, width, height, components, bpc, params);
    }

    /** Open an image for later use.  Supported types are "jpeg", "tiff",
        "gif", and "png" (depending on configuration, however). stringparam
	is either "", "mask", "masked", or "page". intparam is either 0,
	the image number of the applied mask, or the page.
	@return A valid image descriptor, or -1 on error.
     */
    public final int open_image_file(String type, String filename, String stringparam, int intparam) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_open_image_file(p, type, filename, stringparam, intparam);
    }

    /** Close an image retrieved with one of the PDF_open_image*() functions. */
    public final void close_image(int image) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_close_image(p, image);
    }

    /** Open a raw CCITT image for later use.
	@return A valid image descriptor, or -1 on error.
     */
    public final int open_CCITT(String filename, int width, int height, int BitReverse, int K, int BlackIs1) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_open_CCITT(p, filename, width, height, BitReverse, K, BlackIs1);
    }

    /** Add a nested bookmark under parent, or a new top-level bookmark if
        parent = 0. text may be Unicode.  If open = 1, child bookmarks will
	be folded out, and invisible if open = 0.
	@return A bookmark descriptor which may be used as parent for
	subsequent nested bookmarks.
     */
    public final int add_bookmark(String text, int parent, int open) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	return PDF_add_bookmark(p, text, parent, open);
    }

    /** Fill document information field key with value. value may be Unicode. */
    public final void set_info(String key, String value) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_info(p, key, value);
    }

    /** Add a file attachment annotation. description and author may be Unicode.
        icon is one of "graph, "paperclip", "pushpin", or "tag".
     */
    public final void attach_file(float llx, float lly, float urx, float ury, String filename, String description, String author, String mimetype, String icon) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_attach_file(p, llx, lly, urx, ury, filename, description, author, mimetype, icon);
    }

    /** Add a note annotation. contents and title may be Unicode. icon is one
        of "comment, "insert", "note", "paragraph", "newparagraph", "key",
	or "help".
     */
    public final void add_note(float llx, float lly, float urx, float ury, String contents, String title, String icon, int open) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_add_note(p, llx, lly, urx, ury, contents, title, icon, open);
    }

    /** Add a file link annotation (to a PDF file). */
    public final void add_pdflink(float llx, float lly, float urx, float ury, String filename, int page, String dest) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_add_pdflink(p, llx, lly, urx, ury, filename, page, dest);
    }

    /** Add a launch annotation (arbitrary file type). */
    public final void add_launchlink(float llx, float lly, float urx, float ury, String filename) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_add_launchlink(p, llx, lly, urx, ury, filename);
    }

    /** Add a link annotation with a target within the current file.
        dest can be "fullpage" or "fitwidth".
     */
    public final void add_locallink(float llx, float lly, float urx, float ury, int page, String dest) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_add_locallink(p, llx, lly, urx, ury, page, dest);
    }

    /** Add a weblink annotation. */
    public final void add_weblink(float llx, float lly, float urx, float ury, String url) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_add_weblink(p, llx, lly, urx, ury, url);
    }

    /** Set the border style for all kinds of annotations.
        These settings are used for all annotations until a new style is set.
         Supported border style names are "solid" and "dashed".
     */
    public final void set_border_style(String style, float width) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_border_style(p, style, width);
    }

    /** Set the border color for all kinds of annotations. */
    public final void set_border_color(float red, float green, float blue) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_border_color(p, red, green, blue);
    }

    /** Set the border dash style for all kinds of annotations.
        See PDF_setdash().
     */
    public final void set_border_dash(float b, float w) throws
    OutOfMemoryError, IOException, IllegalArgumentException,
    IndexOutOfBoundsException, ClassCastException, ArithmeticException,
    RuntimeException, InternalError, UnknownError
    {
	PDF_set_border_dash(p, b, w);
    }


    // ------------------------------------------------------------------------
    // private functions

    private long p;

    protected final void finalize()  throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError
    {
	PDF_delete(p);
    }

    private final static void classFinalize() {
	PDF_shutdown();
    }

    private final static native void PDF_boot();

    private final static native void PDF_shutdown();

    private final static native long PDF_new() throws
	OutOfMemoryError, IOException, IllegalArgumentException,
	IndexOutOfBoundsException, ClassCastException, ArithmeticException,
	RuntimeException, InternalError, UnknownError;

    private final static native void PDF_delete(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_open_file(long jarg0, String jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_close(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native byte[] PDF_get_buffer(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_begin_page(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_end_page(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_parameter(long jarg0, String jarg1, String jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native String PDF_get_parameter(long jarg0, String key, float mod) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_value(long jarg0, String jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native float PDF_get_value(long jarg0, String key, float mod) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_findfont(long jarg0, String jarg1, String jarg2, int jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setfont(long jarg0, int jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_show(long jarg0, String jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_show_xy(long jarg0, String jarg1, float jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_continue_text(long jarg0, String jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_show_boxed(long jarg0, String jarg1, float jarg2, float jarg3, float jarg4, float jarg5, String jarg6, String jarg7) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_text_pos(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native float PDF_stringwidth(long jarg0, String jarg1, int jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setdash(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setpolydash(long jarg0, float[] jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setflat(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setlinejoin(long jarg0, int jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setlinecap(long jarg0, int jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setmiterlimit(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setlinewidth(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_save(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_restore(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_translate(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_scale(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_rotate(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_skew(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_concat(long jarg0, float a, float b, float c, float d, float e, float f) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_moveto(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_lineto(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_curveto(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, float jarg5, float jarg6) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_circle(long jarg0, float jarg1, float jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_arc(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, float jarg5) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_rect(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_closepath(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_stroke(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_closepath_stroke(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_fill(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_fill_stroke(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_closepath_fill_stroke(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_endpath(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_clip(long jarg0) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setgray_fill(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setgray_stroke(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setgray(long jarg0, float jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setrgbcolor_fill(long jarg0, float jarg1, float jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setrgbcolor_stroke(long jarg0, float jarg1, float jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_setrgbcolor(long jarg0, float jarg1, float jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_get_image_width(long jarg0, int jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_get_image_height(long jarg0, int jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_place_image(long jarg0, int jarg1, float jarg2, float jarg3, float jarg4) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_open_image(long jarg0, String jarg1, String jarg2, byte[] jarg3, long jarg4, int jarg5, int jarg6, int jarg7, int jarg8, String jarg9) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_close_image(long jarg0, int jarg1) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_open_image_file(long jarg0, String jarg1, String jarg2, String jarg3, int jarg4) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_open_CCITT(long jarg0, String jarg1, int jarg2, int jarg3, int jarg4, int jarg5, int jarg6) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native int PDF_add_bookmark(long jarg0, String jarg1, int jarg2, int jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_info(long jarg0, String jarg1, String jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_attach_file(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, String jarg5, String jarg6, String jarg7, String jarg8, String jarg9) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_add_note(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, String jarg5, String jarg6, String jarg7, int jarg8) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_add_pdflink(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, String jarg5, int jarg6, String jarg7) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_add_launchlink(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, String jarg5) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_add_locallink(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, int jarg5, String jarg6) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_add_weblink(long jarg0, float jarg1, float jarg2, float jarg3, float jarg4, String jarg5) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_border_style(long jarg0, String jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_border_color(long jarg0, float jarg1, float jarg2, float jarg3) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;

    private final static native void PDF_set_border_dash(long jarg0, float jarg1, float jarg2) throws
        OutOfMemoryError, IOException, IllegalArgumentException,
        IndexOutOfBoundsException, ClassCastException, ArithmeticException,
        RuntimeException, InternalError, UnknownError;
}
