package com.nictusa.fecprint.gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PDFGenerationProgressDialog extends JDialog implements ActionListener
  {
	private JProgressBar progressBar;
    private JTextArea taskOutput;
    
	public PDFGenerationProgressDialog(Frame parent,String title, int totalPages)
  	{
  		super(parent,title);
  		setModal(false);
  		
  		
  		Container cn = getContentPane();
  		
		
		JPanel pn = new JPanel();
		cn.add(pn);
	 	  setFont(new Font("Helvetica", Font.PLAIN, 14));
	      pn.setLayout(new BorderLayout());
		  
	      	progressBar = new JProgressBar(0, totalPages);
	        progressBar.setValue(0);
	        progressBar.setStringPainted(true);
	        

	        taskOutput = new JTextArea(5, 15);
	        taskOutput.setMargin(new Insets(5,5,5,5));
	        taskOutput.setEditable(false);

	        JPanel panel = new JPanel();	        
	        panel.add(progressBar);

	        pn.add(panel, BorderLayout.PAGE_START);
	        pn.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
	        pn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));		
  		
	        setPreferredSize(new Dimension(parent.getWidth()/2,200));
	        pack();
		
		
		
		Point p = parent.getLocation();  		
		int width = parent.getWidth();
		int hieght = parent.getHeight();
		setLocation((int)((width/2) - (getWidth()/2)),(int)((hieght/2) - (getHeight()/2)));
  	}  	
	
	public void updateProgress(int pages, String message)
	{
		taskOutput.append(message + "\n");
		taskOutput.setCaretPosition(taskOutput.getText().length() - 1);
		progressBar.setValue(pages);
	}
  	
  	public void actionPerformed(ActionEvent e)
  	{
  		hide();
  	}
  	
  }