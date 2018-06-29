package com.np.roshanadhikary.GroceryDesk;
/* Implementation started: 6/24/2018 9:51 pm */
/* File Location: Subsidiary1 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import java.sql.*;
public class GroceryDesk implements ActionListener
{
	JFrame loginFrame,storekeeperFrame,adminFrame;
	JLabel loginIDLabel,loginPassLabel;
	JTextField loginIDTextField;
	JPasswordField loginPassTextField;
	JButton loginButton;
	
	public static void main(String[] args)
	{
		new GroceryDesk();
	}
	
	public GroceryDesk()
	{
		/* Three Frames' definition */
		loginFrame=new JFrame("GroceryDesk- Login");
		storekeeperFrame=new JFrame("GroceryDesk- Cashier Panel");
		adminFrame=new JFrame("GroceryDesk- Administrator Panel");
		
		/* Other definitions */
		loginIDLabel=new JLabel("ID");
		loginPassLabel=new JLabel("Password");
		loginIDTextField=new JTextField(10);
		loginPassTextField=new JPasswordField(10);
		loginButton=new JButton("Login");
		
		/* Bounds for components */
		loginIDLabel.setBounds(45,50,100,20);
		loginIDTextField.setBounds(45,70,270,40);
		loginPassLabel.setBounds(45,150,100,40);
		loginPassTextField.setBounds(45,180,270,40);
		loginButton.setBounds(130,250,100,50);
		
		/* Attributes of Button */
		loginButton.setBackground(Color.white);
	
		
		/* Attributes of loginFrame */
		loginFrame.setSize(380,400);
		loginFrame.setResizable(false);
		loginFrame.setLayout(null); //GridLayout requires java.awt package
		loginFrame.setVisible(true);
		
		/* Components of loginFrame */
		loginFrame.add(loginIDLabel);
		loginFrame.add(loginIDTextField);
		loginFrame.add(loginPassLabel);
		loginFrame.add(loginPassTextField);
		loginFrame.add(loginButton);
		
	}
	public void actionPerformed(ActionEvent e)
	{
		
	}
}
