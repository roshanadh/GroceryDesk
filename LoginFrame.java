package com.teamGroceryDesk.GroceryDesk;

import com.mysql.jdbc.exceptions.jdbc4.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener
{
	/*----------------------------------- Declarations and Prerequisites----------------------------------- */

	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost/grocerydesk?useSSL=true";
	static String databaseID;
	static String databasePassword;
	String query;
	Connection connection;
	PreparedStatement pstatement;
	Statement statement;
	ResultSet resultSet;

	/* For Layout and ContentPane */
	SpringLayout frameLayout;
	Container contentPane;

	/* For Dimensions and Fonts */
	Dimension userTextFieldDimension,databaseTextFieldDimension,buttonDimension;
	Font userLabelFont,databaseLabelFont,buttonFont;

	/* Other declarations */
	String errorMessage;

	/* For Components */
	JLabel loginIDLabel,loginPassLabel,databaseUsernameLabel,databasePasswordLabel;
	JTextField loginIDTextField,databaseUsernameTextField,databasePasswordTextField;
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
		/* Layout and Content Pane */
		frameLayout=new SpringLayout();
		contentPane=this.getContentPane();

		/* Dimensions and Fonts */
		userTextFieldDimension=new Dimension(270,40);
		databaseTextFieldDimension=new Dimension(125,25);
		buttonDimension=new Dimension(100,50);

		userLabelFont=new Font("Segoe UI",Font.PLAIN,13);
		databaseLabelFont=new Font("Segoe UI",Font.PLAIN,12);
		buttonFont=new Font("Segoe UI", Font.PLAIN,14);

		/* JDialog definition and attributes */
		loginErrorMessageDialog=new JDialog(this,"GroceryDesk- Login Error!");
		loginErrorMessageDialog.setLocationRelativeTo(null);

		/* Other definitions */
		loginIDLabel=new JLabel("ID");
		loginPassLabel=new JLabel("Password");
		loginIDTextField=new JTextField();
		loginPassTextField=new JPasswordField();
		databaseUsernameLabel=new JLabel("Database Username");
		databasePasswordLabel=new JLabel("Database Password");
		databaseUsernameTextField=new JTextField("root");
		databasePasswordTextField=new JTextField("");
		loginButton=new JButton("Login");
		loginErrorMessageLabel=new JLabel();
		loginErrorMessageOKButton=new JButton("OK");
		errorMessage= "";

		/* Bounds for JDialog's Components */

		loginErrorMessageLabel.setBounds(48,0,400,100);
		loginErrorMessageOKButton.setBounds(155, 80, 60, 40);

		/* Attributes of Components */
		loginIDLabel.setForeground(Color.WHITE);
		loginIDLabel.setFont(userLabelFont);

		loginPassLabel.setForeground(Color.WHITE);
		loginPassLabel.setFont(userLabelFont);

		databaseUsernameLabel.setFont(databaseLabelFont);
		databaseUsernameLabel.setForeground(Color.WHITE);

		databasePasswordLabel.setFont(databaseLabelFont);
		databasePasswordLabel.setForeground(Color.WHITE);

		loginIDTextField.setFont(new Font("Verdana",Font.PLAIN,16));
		loginIDTextField.setOpaque(false);
		loginIDTextField.setForeground(Color.WHITE);
		loginIDTextField.setPreferredSize(userTextFieldDimension);

		loginPassTextField.setOpaque(false);
		loginPassTextField.setForeground(Color.WHITE);
		loginPassTextField.setPreferredSize(userTextFieldDimension);

		databaseUsernameTextField.setFont(new Font("Verdana",Font.PLAIN,12));
		databaseUsernameTextField.setOpaque(false);
		databaseUsernameTextField.setForeground(Color.WHITE);
		databaseUsernameTextField.setPreferredSize(databaseTextFieldDimension);

		databasePasswordTextField.setFont(new Font("Verdana",Font.PLAIN,12));
		databasePasswordTextField.setOpaque(false);
		databasePasswordTextField.setForeground(Color.WHITE);
		databasePasswordTextField.setPreferredSize(databaseTextFieldDimension);

		/* Attributes of JButton */
		loginButton.setForeground(new Color(103, 128, 159));
		loginButton.setBackground(Color.WHITE);

		//loginButton.setBorder(new LineBorder(new Color(41, 128, 185)));
		loginButton.setBorderPainted(false);
		loginButton.setFocusPainted(false);
		loginButton.setOpaque(true);
		loginButton.setFont(buttonFont);
		loginButton.setPreferredSize(buttonDimension);

		loginErrorMessageOKButton.setBorderPainted(false);
		loginErrorMessageOKButton.setBackground(Color.WHITE);
		loginErrorMessageOKButton.setFocusPainted(false);

		/* Constraints for Layout */
		frameLayout.putConstraint(SpringLayout.NORTH,loginIDLabel,50,SpringLayout.NORTH,contentPane);
		frameLayout.putConstraint(SpringLayout.WEST,loginIDLabel,50,SpringLayout.WEST,contentPane);

		frameLayout.putConstraint(SpringLayout.NORTH,loginIDTextField,0,SpringLayout.SOUTH,loginIDLabel);
		frameLayout.putConstraint(SpringLayout.WEST,loginIDTextField,0,SpringLayout.WEST,loginIDLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,loginPassLabel,30,SpringLayout.SOUTH,loginIDTextField);
		frameLayout.putConstraint(SpringLayout.WEST,loginPassLabel,0,SpringLayout.WEST,loginIDLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,loginPassTextField,0,SpringLayout.SOUTH,loginPassLabel);
		frameLayout.putConstraint(SpringLayout.WEST,loginPassTextField,0,SpringLayout.WEST,loginPassLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,databaseUsernameLabel,30,SpringLayout.SOUTH,loginPassTextField);
		frameLayout.putConstraint(SpringLayout.WEST,databaseUsernameLabel,0,SpringLayout.WEST,loginIDLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,databasePasswordLabel,0,SpringLayout.NORTH,databaseUsernameLabel);
		frameLayout.putConstraint(SpringLayout.WEST,databasePasswordLabel,30,SpringLayout.EAST,databaseUsernameLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,databaseUsernameTextField,0,SpringLayout.SOUTH,databaseUsernameLabel);
		frameLayout.putConstraint(SpringLayout.WEST,databaseUsernameTextField,0,SpringLayout.WEST,databaseUsernameLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,databasePasswordTextField,0, SpringLayout.SOUTH,databasePasswordLabel);
		frameLayout.putConstraint(SpringLayout.WEST,databasePasswordTextField,0,SpringLayout.WEST,databasePasswordLabel);

		frameLayout.putConstraint(SpringLayout.NORTH,loginButton,50,SpringLayout.SOUTH,databaseUsernameTextField);
		frameLayout.putConstraint(SpringLayout.WEST,loginButton,85,SpringLayout.WEST,databaseUsernameTextField);


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
		this.setSize(385,450);
		this.setResizable(false);
		this.setLayout(frameLayout);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Components of LoginFrame */
		this.add(loginIDLabel);
		this.add(loginIDTextField);
		this.add(loginPassLabel);
		this.add(loginPassTextField);
		this.add(databaseUsernameLabel);
		this.add(databaseUsernameTextField);
		this.add(databasePasswordLabel);
		this.add(databasePasswordTextField);
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
				                            //createDBSchema();
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
			String loginID=loginIDTextField.getText(),loginPass=loginPassTextField.getText();
			databaseID=databaseUsernameTextField.getText();
			databasePassword=databasePasswordTextField.getText();

			/* Initiation of connection to database */
			try
			{
				connection=DriverManager.getConnection(databaseURL, databaseID, databasePassword);
				statement=connection.createStatement();

				pstatement=connection.prepareStatement("USE grocerydesk;");
				pstatement.executeUpdate();

				//BINARY in the below query makes for a case-sensitive string comparison
				pstatement=connection.prepareStatement("SELECT * FROM storekeeper WHERE BINARY id=? AND password=sha2(?,256) AND isDeleted!=1;");
				//Before pstatement
				//resultSet=pstatement.executeQuery("SELECT * FROM storekeeper WHERE BINARY id='"+loginID+"' AND password=sha2('"+loginPass+"',256) AND isDeleted!=1;");
				pstatement.setString(1,loginID);
				pstatement.setString(2,loginPass);
				resultSet=pstatement.executeQuery();
				if(resultSet.next())
				{
					System.out.println("User Login Successful!!!!!");
					System.out.println(loginID);
					this.dispose();
					StorekeeperFrame storekeeperFrame=new StorekeeperFrame(databaseID,databasePassword);
					storekeeperFrame.startSystem(loginID,0,0);
				}

				else
				{
					pstatement=connection.prepareStatement("SELECT * FROM administrator WHERE BINARY id=? AND password=sha2(?,256);");
					//Before pstatement
					//resultSet=statement.executeQuery("SELECT * FROM administrator WHERE BINARY id='"+loginID+"' AND password=sha2('"+loginPass+"',256);");
					pstatement.setString(1,loginID);
					pstatement.setString(2,loginPass);
					resultSet=pstatement.executeQuery();
					if(resultSet.next())
					{
						System.out.println("Admin Login Successful!!!!!");
						this.dispose();
						AdminNavigation adminNav=new AdminNavigation(databaseID,databasePassword);
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
				createDBSchema();

			}
			catch(SQLException exception)
			{
				exception.printStackTrace();
				JOptionPane.showMessageDialog(this,"Connection to database couldn't be established!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
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
		try
		{
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
