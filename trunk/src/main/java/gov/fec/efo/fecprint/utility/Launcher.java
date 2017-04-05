/*
$Archive: /nicusatech/XYPEditor/SRC/misc/Launcher.java $
$Revision: 1.1 $
$Date: 2011-03-04 03:01:45 $
$Author: milind $
*/
package gov.fec.efo.fecprint.utility;

import java.io.*;
import java.lang.*;
import java.util.*;


public class Launcher 
{
	public native int launchExeForFile(String fileName, String directory) ;
	public native String getExeForFile(String fileName, String directory) ;
	public native int launchExe(String command) ;

	static {
		try {
			System.loadLibrary("launcher") ;
		} catch ( Exception e ) {
			System.out.println("Could not load launcher library") ;
		}
	}
}

