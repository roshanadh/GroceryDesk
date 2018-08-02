package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener
{
	/*----------------------------------- Declarations and Prerequisites----------------------------------- */

	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost/grocerydesk?useSSL=true";
	String databaseID="root";
	String databasePassword="";
	String query;
	Connection connection;
	Statement statement;
	ResultSet resultSet;

	/* Other declarations */
	String errorMessage;

	/* For Components */
	JLabel loginIDLabel,loginPassLabel;
	JTextField loginIDTextField;
	JPasswordField loginPassTextField;
	JButton loginButton;
	JDialog loginErrorMessageDialog;
	JLabel loginErrorMessageLabel;
	JButton loginErrorMessageOKButton;

	public static void main(String[] args)
	{
		new LoginFrame();
	}

	public LoginFrame()
	{
		/* Initiation of connection to database */
		try
		{
			connection=DriverManager.getConnection(databaseURL, databaseID, databasePassword);
			statement=connection.createStatement();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			JOptionPane.showMessageDialog(this,"Connection to database couldn't be established!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		/* JDialog definition and attributes */
		loginErrorMessageDialog=new JDialog(this,"GroceryDesk- Login Error!");
		loginErrorMessageDialog.setLocationRelativeTo(null);

		/* Other definitions */
		loginIDLabel=new JLabel("ID");
		loginPassLabel=new JLabel("Password");
		loginIDTextField=new JTextField(10);
		loginPassTextField=new JPasswordField(10);
		loginButton=new JButton("Login");
		loginErrorMessageLabel=new JLabel();
		loginErrorMessageOKButton=new JButton("OK");
		errorMessage= "";

		/* Bounds for components */
		loginIDLabel.setBounds(45,50,100,20);
		loginIDTextField.setBounds(45,70,270,40);
		loginPassLabel.setBounds(45,150,100,40);
		loginPassTextField.setBounds(45,180,270,40);
		loginButton.setBounds(130,250,100,50);
		loginErrorMessageLabel.setBounds(48,0,400,100);
		loginErrorMessageOKButton.setBounds(155, 80, 60, 40);

		/* Attributes of Components */
		loginIDLabel.setForeground(Color.WHITE);
		loginPassLabel.setForeground(Color.WHITE);

		loginIDTextField.setFont(new Font("Verdana",Font.PLAIN,16));
		loginIDTextField.setOpaque(false);
		loginIDTextField.setForeground(Color.WHITE);

		loginPassTextField.setOpaque(false);
		loginPassTextField.setForeground(Color.WHITE);

		/* Attributes of JButton */
		loginButton.setForeground(new Color(103, 128, 159));
		loginButton.setBackground(Color.WHITE);
		//loginButton.setBorder(new LineBorder(new Color(41, 128, 185)));
		loginButton.setBorderPainted(false);
		loginButton.setFocusPainted(false);
		loginButton.setOpaque(true);
		//loginButton.setMargin(new Insets(0, 0, 0, 0));
		//loginButton.setIcon(new ImageIcon("D:\\GroceryDeskProject\\icons\\loginButtonIcon.png"));

		loginErrorMessageOKButton.setBorderPainted(false);
		loginErrorMessageOKButton.setBackground(Color.WHITE);
		loginErrorMessageOKButton.setFocusPainted(false);

		/* Attributes of LoginFrame */
		this.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
			}
		});
		this.setTitle("GroceryDesk- Login");
		this.getContentPane().setBackground(new Color(103, 128, 159));
		this.setSize(380,400);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Components of LoginFrame */
		this.add(loginIDLabel);
		this.add(loginIDTextField);
		this.add(loginPassLabel);
		this.add(loginPassTextField);
		this.add(loginButton);

		/* Setting default button */
		this.getRootPane().setDefaultButton(loginButton); //Instead of writing KeyListener for Enter key

		try
		{
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}

		SwingUtilities.updateComponentTreeUI(this);


		/* Attributes of loginErrorMessageDialog */
		loginErrorMessageDialog.setLayout(null);
		//loginErrorMessageDialog.setResizable(false);
		loginErrorMessageDialog.setSize(365,180);

		/* Other Attributes */
		loginErrorMessageLabel.setText(errorMessage);
		loginErrorMessageLabel.setFont(new Font("Segoe UI",Font.PLAIN,13));

		/* Components of loginErrorMessageDialog */
		loginErrorMessageDialog.add(loginErrorMessageLabel);
		loginErrorMessageDialog.add(loginErrorMessageOKButton);

		/* Setting default button */
		loginErrorMessageDialog.getRootPane().setDefaultButton(loginErrorMessageOKButton);

		/* Adding Event Listeners */
		this.addWindowListener(new WindowAdapter()
		                            {
			                            @Override
			                            public void windowOpened(WindowEvent e)
			                            {
				                            super.windowGainedFocus(e);
				                            setIconImage(new ImageIcon(getClass().getResource("grocerydeskIcon.png")).getImage());
				                            createDBSchema();
			                            }
		                            });
		loginButton.addActionListener(this);
		loginErrorMessageOKButton.addActionListener(this);
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

				//BINARY in the below query makes for a case-sensitive string comparison
				resultSet=statement.executeQuery("SELECT * FROM storekeeper WHERE BINARY id='"+loginID+"' AND password=sha2('"+loginPass+"',256) AND isDeleted!=1;");

				if(resultSet.next())
				{
					System.out.println("User Login Successful!!!!!");
					System.out.println(loginID);
					resultSet.close();
					statement.close();
					connection.close();
					this.dispose();
					StorekeeperFrame storekeeperFrame=new StorekeeperFrame();
					storekeeperFrame.startSystem(loginID,0,0);
				}

				else
				{
					resultSet=statement.executeQuery("SELECT * FROM administrator WHERE BINARY id='"+loginID+"' AND password=sha2('"+loginPass+"',256);");
					if(resultSet.next())
					{
						System.out.println("Admin Login Successful!!!!!");
						resultSet.close();
						statement.close();
						connection.close();
						this.dispose();
						AdminNavigation adminNav=new AdminNavigation();
						adminNav.startSystem(loginID);

					}
					else
					{
						errorMessage="Error: Login Credentials Mismatch!";
						loginErrorMessageLabel.setText(errorMessage);
						loginErrorMessageLabel.setIcon(new ImageIcon("D:\\GroceryDeskProject\\icons\\errorIcon.png"));
						loginErrorMessageDialog.setVisible(true);
						System.out.println("ERROR in LOGIN Process!");
						//JOptionPane.showMessageDialog(this,"No such credentials found!","Login Error",JOptionPane.ERROR_MESSAGE);

					}
				}

			}
			catch(Exception exception)
			{
				errorMessage="Sorry, an exception occurred. Try again later!";

				/* Dialog Box dimensions have to be changed because of the change in error message to be displayed. */
				loginErrorMessageDialog.setSize(352,200);
				loginErrorMessageOKButton.setBounds(138, 80, 60, 40);
				loginErrorMessageLabel.setText(errorMessage);
				loginErrorMessageDialog.setVisible(true);
				exception.printStackTrace();
			}

		}
		else if(sourceButton==loginErrorMessageOKButton)
		{
			this.dispose();
			LoginFrame newFrame=new LoginFrame();
			loginErrorMessageDialog.setVisible(false);
		}
	}

	public void createDBSchema()
	{
		query=null;
		//int firstRowCount;
		try
		{
			//query =;
			statement.executeUpdate( "CREATE DATABASE IF NOT EXISTS grocerydesk;");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS administrator(id varchar(25) primary key,password char(64),fname varchar(25),lname varchar(25));");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS customer(id varchar(25) primary key,fname varchar(25),lname varchar(25),address varchar(25),phone char(10),credits_amount float,added_in char(19),added_by varchar(25),last_updated_in char(19),last_updated_by varchar(25), isDeleted tinyint(1));");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS inventory(id char(6) primary key,product_name varchar(50),quantity int,rate float,added_in char(19),added_by varchar(25),last_updated_in char(19),last_updated_by varchar(25), isDeleted tinyint(1));");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS storekeeper(id varchar(25),password char(64),fname varchar(25),lname varchar(25),address varchar(25),phone char(10),added_in char(19),added_by varchar(25),last_updated_in char(19),last_updated_by varchar(25), isDeleted tinyint(1));");
			statement.executeUpdate("INSERT IGNORE into administrator set id='admin', password=sha2('admin',256),fname='Store',lname='Administrator';"); //INSERT IF NOT EXISTS
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("FINALLY BLOCK AFTER 'IF NOT EXISTS' SCHEMA CREATIONS AND RECORD INSERTION.");
		}
	}
}
