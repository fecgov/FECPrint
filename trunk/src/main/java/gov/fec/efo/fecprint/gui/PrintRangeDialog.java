package gov.fec.efo.fecprint.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PrintRangeDialog extends JDialog implements ActionListener, ItemListener {
	public static final int ACTION_OK = 1;
	public static final int ACTION_CANCEL = 2;
	
	public static final int RANGE_ALL = 1;
	public static final int RANGE_USER_DEFINED = 2;
	
	int maxPage;
	JTextField from,to;
	JRadioButton rangeButton,allButton;
	int pageRangeOptionSelected = RANGE_ALL;
	int action = ACTION_CANCEL;
	int startPageNo = 1;
	int endPageNo;
	public PrintRangeDialog(Frame parent,int totalPages) {
		super(parent, "Print Options", true);
		
		maxPage = totalPages;
		endPageNo = maxPage;
		
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
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		allButton = new JRadioButton("All");
		allButton.setSelected(true);
		pMessage.add(allButton, c);
		
		c.gridx = 0;
		c.gridy = 1;
		rangeButton = new JRadioButton("Pages : ");
		pMessage.add(rangeButton, c);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(allButton);
	    group.add(rangeButton);
	    
	    allButton.addItemListener(this);
	    rangeButton.addItemListener(this);

		
	    c.gridx = 1;
		c.gridy = 1;
		c.weightx = 2.0;
		from = new JTextField("1");
		pMessage.add(from, c);
		from.setEnabled(false);
		
		c.gridx = 2;
		c.gridy = 1;
		JLabel label = new JLabel("  To  ");
		pMessage.add(label, c);
		
		c.gridx = 3;
		c.gridy = 1;
		to = new JTextField("" + maxPage);
		pMessage.add(to, c);
		to.setEnabled(false);
		
		c.gridx = 4;
		c.gridy = 1;
		label = new JLabel("     ");
		pMessage.add(label, c);

		pn.add(pMessage, BorderLayout.CENTER);

		Panel pButtons = new Panel();
		pButtons.setLayout(new FlowLayout());
		Button bt1 = new Button("OK");
		bt1.setActionCommand("ok");
		bt1.addActionListener(this);
		bt1.setSize(50, 50);
		pButtons.add(bt1);
		
		Button bt2 = new Button("CANCEL");
		bt2.setActionCommand("cancel");
		bt2.addActionListener(this);
		bt2.setSize(50, 50);
		pButtons.add(bt2);

		pn.add(pButtons, BorderLayout.SOUTH);

		pack();
		Point p = parent.getLocation();
		int width = parent.getWidth();
		int hieght = parent.getHeight();
		setLocation((int) (p.getX() + (width / 2)),
				(int) (p.getY() + (hieght / 2)));
		setSize(300,150);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals("ok")) 
		{
			action = ACTION_OK;
			boolean fromError = false;
			boolean toError = false;			
			if(pageRangeOptionSelected == RANGE_USER_DEFINED)
			{
				try 
				{
					startPageNo = Integer.parseInt(from.getText());
					if (startPageNo < 1 || startPageNo > maxPage) 
					{
						fromError = true;
					}
				} 
				catch (NumberFormatException eStart)			
				{
					fromError = true;
				}
	
				try 
				{
					endPageNo = Integer.parseInt(to.getText());
	
					if (endPageNo < 1 || endPageNo > maxPage
							|| endPageNo < startPageNo) 
					{
						toError = true;
					}
				} 
				catch (NumberFormatException eTo) 
				{
					toError = true;
				}
	
				if (fromError) 
				{
					from.setBackground(Color.RED);
				}
				else
				{
					from.setBackground(Color.WHITE);
				}
				
				if (toError) 
				{
					to.setBackground(Color.RED);
				}
				else
				{
					to.setBackground(Color.WHITE);
				}
			}

			if(fromError == false && toError == false)
			{
				setVisible(false);
			}			
				
		} else 
		{
			action = ACTION_CANCEL;
			setVisible(false);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getItemSelectable();
		
		if(source == rangeButton)
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				pageRangeOptionSelected = RANGE_USER_DEFINED;
				from.setEnabled(true);
				to.setEnabled(true);
			}
			else
			{
				pageRangeOptionSelected = RANGE_ALL;
				from.setEnabled(false);
				to.setEnabled(false);
			}
		}
	}

	public int getPageRangeOptionSelected() {
		return pageRangeOptionSelected;
	}

	public int getAction() {
		return action;
	}

	public int getStartPageNo() {
		return startPageNo;
	}

	public int getEndPageNo() {
		return endPageNo;
	}

}