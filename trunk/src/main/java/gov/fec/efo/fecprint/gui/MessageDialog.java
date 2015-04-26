package gov.fec.efo.fecprint.gui;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
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

import javax.swing.JDialog;
import javax.swing.JLabel;

public class MessageDialog extends JDialog implements ActionListener
  {
	public MessageDialog(Frame parent,String title, String message)
  	{
  		super(parent,title);
  		setModal(true);
  		
  		Container cn = getContentPane();
		
		Panel pn = new Panel();
		cn.add(pn);
	 	  setFont(new Font("Helvetica", Font.PLAIN, 14));
	      pn.setLayout(new BorderLayout());
		  
		  Panel pMessage = new Panel();
		  GridBagLayout gridbag = new GridBagLayout();
		  GridBagConstraints c = new GridBagConstraints();
		  pMessage.setLayout(gridbag);
		  c.fill = GridBagConstraints.BOTH;
		  c.weightx = 1.0;
		  c.gridwidth = 3;
	      c.gridx =0;
	      c.insets = new Insets(0,10, 0, 10);
	      
	      int row = 1;
	      c.gridy = row;
	      pMessage.add(new JLabel(" "),c);
	      row++;
	      
		  String temp = message;
	      int ind = -1;
	      for(;temp.length() > 0;row++)
		  {		  
		  	c.gridy = row;
			ind = temp.indexOf('\n');
			if (ind != -1)
			{
				pMessage.add(new JLabel(ind == 0 ? " " : temp.substring(0,ind)),c);			
				temp = temp.substring(ind + 1);
			}
			else
			{
				pMessage.add(new JLabel(temp),c);			
				temp = "";
			}
			
		  }
	      c.gridy = row++;
	      pMessage.add(new JLabel(" "),c);
		  
		  pn.add(pMessage,BorderLayout.NORTH);
		  
		  Panel pButtons = new Panel();
		  pButtons.setLayout(new FlowLayout());
		  Button bt1 = new Button("OK");
	  	  bt1.addActionListener(this);
	  	  bt1.setSize(50,50);  		
		  pButtons.add(bt1);
		  
		  pn.add(pButtons,BorderLayout.SOUTH);  		
  		
		pack();
		Point p = parent.getLocation();  		
		int width = parent.getWidth();
		int hieght = parent.getHeight();
		setLocation((int)((width/2) - (getWidth()/2)),(int)((hieght/2) - (getHeight()/2)));
  	}
  	
  	
  	public void actionPerformed(ActionEvent e)
  	{
  		hide();
  	}
  	
  }