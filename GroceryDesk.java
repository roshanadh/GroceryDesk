/* Implementation started: 6/24/2018 9:51 p.m. */
package com.np.roshanadhikary.GroceryDesk;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
public class GroceryDesk implements ActionListener
{
	/*----------------------------------- Definitions and Prerequisites------------------------------------ */
	
	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost/?useSSL=true";
	String databaseID="root";
	String databasePassword="";
	Connection connection;
	Statement statement;
	ResultSet resultSet;
		
	/* For Containers and Components */
	JFrame loginFrame,storekeeperFrame,adminFrame;
	JLabel loginIDLabel,loginPassLabel;
	JTextField loginIDTextField;
	JPasswordField loginPassTextField;
	JButton loginButton;
	
	JTabbedPane storekeeperPanelTab,adminPanelTab;
	JPanel storekeeperPanelPos,storekeeperPanelCreditAccounts,adminPanelUsers;
	JLabel storekeeperWelcomeLabel,adminWelcomeLabel;
	JTable inventoryTable, creditAccountsTable,usersTable;
	JScrollPane inventoryTableSp, creditAccountsTableSp, usersTableSp;
	DefaultTableModel inventoryTableModel,creditAccountsTableModel, usersTableModel;
	
	public static void main(String[] args)
	{
		new GroceryDesk();
	}
	
	public GroceryDesk()
	{
		try 
		{
		/* Connection to database initiated */
		connection=DriverManager.getConnection(databaseURL, databaseID, databasePassword);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		/* Three Frames' definition */
		loginFrame=new JFrame("GroceryDesk- Login");
		storekeeperFrame=new JFrame("GroceryDesk- Cashier Panel");
		adminFrame=new JFrame("GroceryDesk- Administrator Panel");
		
		/* Default Table Models' definitions */
		inventoryTableModel=new DefaultTableModel();
		creditAccountsTableModel=new DefaultTableModel();
		usersTableModel=new DefaultTableModel();
		
		/* JTable definitions */
		inventoryTable=new JTable();
		creditAccountsTable=new JTable();
		usersTable=new JTable();
		
		/* JScrollPane definitions */
		inventoryTableSp=new JScrollPane(inventoryTable);
		creditAccountsTableSp=new JScrollPane(creditAccountsTable);
		usersTableSp=new JScrollPane(usersTable);
		
		
		/*--------------------------------------------loginFrame--------------------------------------------*/
		
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
		
		/* Attributes of Components */
		loginIDLabel.setForeground(Color.white);
		loginPassLabel.setForeground(Color.white);
		
		loginIDTextField.setFont(new Font("Verdana",Font.PLAIN,16));
		loginIDTextField.setOpaque(false);
		loginIDTextField.setForeground(Color.white);
		
		loginPassTextField.setOpaque(false);
		loginPassTextField.setForeground(Color.white);
		
		/* Attributes of Button */
		loginButton.setBackground(new Color(236, 240, 241));
		loginButton.setBorderPainted(false);
		loginButton.setFocusPainted(false);
	
		/* Attributes of loginFrame */
		loginFrame.getContentPane().setBackground(new Color(41, 128, 185));
		loginFrame.setSize(380,400);
		loginFrame.setResizable(false);
		loginFrame.setLayout(null); 
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Components of loginFrame */
		loginFrame.add(loginIDLabel);
		loginFrame.add(loginIDTextField);
		loginFrame.add(loginPassLabel);
		loginFrame.add(loginPassTextField);
		loginFrame.add(loginButton);
		
		/* Adding Event Listeners */
		loginButton.addActionListener(this);
		
		/*--------------------------------------------storekeeperFrame--------------------------------------------*/
		
		/* Attributes of storekeeperFrame */
		storekeeperFrame.getContentPane().setBackground(Color.white);
		storekeeperFrame.setSize(900,700);
		storekeeperFrame.setResizable(false);
		storekeeperFrame.setLayout(null); 
		storekeeperFrame.setLocationRelativeTo(null);
		storekeeperFrame.setVisible(false);
		storekeeperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Other definitions */
		storekeeperWelcomeLabel=new JLabel();
		storekeeperPanelPos=new JPanel();
		storekeeperPanelCreditAccounts=new JPanel();
		storekeeperPanelTab=new JTabbedPane();
		storekeeperPanelTab.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		storekeeperPanelTab.setTabPlacement(JTabbedPane.TOP);
		
		/* Components for JTabbedPane */
		storekeeperPanelTab.addTab("Credit Accounts", storekeeperPanelCreditAccounts);
		storekeeperPanelTab.addTab("POS", storekeeperPanelPos);
		
		/* Bounds for components */
		storekeeperWelcomeLabel.setBounds(10,10,200,50);
		storekeeperPanelTab.setBounds(10, 25, 865, 630);
		
		/* Components for storekeeperPanelPos */
		inventoryTableSp.setBounds(10,35,600,600);
		storekeeperPanelPos.add(inventoryTableSp);
		
		/* Components of storekeeperFrame */
		storekeeperFrame.add(storekeeperWelcomeLabel);
		storekeeperFrame.add(storekeeperPanelTab);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JButton sourceButton=(JButton)event.getSource();
		
		if(sourceButton==loginButton)
		{
			//For Console Log
			System.out.println("Login Button Pressed");
			String loginID=loginIDTextField.getText();
			String loginPass=loginPassTextField.getText();
			try 
			{
				statement=connection.createStatement();
				statement.executeUpdate("USE grocerydesk;");
				resultSet=statement.executeQuery("SELECT * FROM users WHERE id='"+loginID+"' AND password=sha2('"+loginPass+"',256);");
			
				if(resultSet.next())
				{
					System.out.println("LOGIN SUCCESSFULL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					loginFrame.dispose();
					storekeeperWelcomeLabel.setText("Welcome, "+loginID);
					System.out.println(storekeeperWelcomeLabel.getText());
					storekeeperFrame.setVisible(true);
				}
				
				else
				{
					System.out.println("ERROR in LOGIN Process!");
				}
				
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
			
		}
	}
}
