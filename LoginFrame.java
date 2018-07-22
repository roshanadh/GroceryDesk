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
		loginButton.setForeground(new Color(26, 188, 156));
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
		this.setTitle("GroceryDesk- Login");
		this.getContentPane().setBackground(new Color(26, 188, 156));
		this.setSize(380,400);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
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
				resultSet=statement.executeQuery("SELECT * FROM storekeeper WHERE BINARY id='"+loginID+"' AND password=sha2('"+loginPass+"',256);");

				if(resultSet.next())
				{
					System.out.println("User Login Successful!!!!!");
					System.out.println(loginID);
					resultSet.close();
					statement.close();
					connection.close();
					this.dispose();
					StorekeeperFrame storekeeperFrame=new StorekeeperFrame();
					storekeeperFrame.startSystem(loginID,0);
				}

				else
				{
					resultSet=statement.executeQuery("SELECT * FROM administrator WHERE BINARY id='"+loginID+"' AND password=sha2('"+loginPass+"',256);");
					if(resultSet.next())
					{
						System.out.println("Admin Login Successful!!!!!");
						this.dispose();
						AdminFrame adminFrame=new AdminFrame();
						resultSet.close();
						statement.close();
						connection.close();
						adminFrame.startSystem(loginID);
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
}
