package com.nictusa.fecprint.gui;
import java.awt.*;
import javax.swing.*;

public class LabeledTextField
{
	private TextField edit;
	private JLabel label;
	
	public LabeledTextField(String a_sLabel)
	{
		edit = new TextField();
		label = new JLabel(a_sLabel);
	}
	
	public String getFieldName()
	{
		return label.getText();
	}
	
	public String getFieldValue()
	{
		return edit.getText();
	}
	
	public void setFieldValue(String val)
	{
		edit.setText(val);
	}
	
	public void setFieldValue(int val)
	{
		edit.setText(String.valueOf(val));
	}
	
	public void setFocus()
	{
		edit.requestFocus();
	}
	
	public JLabel getLabel()
	{
		return label;
	}
	
	public TextField getTextField()
	{
		return edit;
	}
}
