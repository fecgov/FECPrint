package com.nictusa.fecprint.utility;

/**
* Class to handle exception generated while rendering the pdf file
*/
public class XYPException extends Exception
{
  String _message = "";

  /**
  * Constructer
  */
  public XYPException(String message)
  {
    _message = message;
  }

  public String getErrorMessage()
  {
    return _message;
  }

}