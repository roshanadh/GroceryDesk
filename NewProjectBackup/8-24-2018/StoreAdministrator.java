package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.*;
import java.security.*;
import java.sql.*;
import java.text.*;

public class StoreAdministrator extends JFrame implements ActionListener
{
	static String loginID;
	private static boolean catchPhoneNotNumberException=true;

	/* For JDBC */
	private String databaseURL = "jdbc:mysql://localhost?useSSL=true", query = null;
	private static String databaseID ;
	private static String databasePassword;
	private Connection connection;
	private PreparedStatement pstatement;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData meta;

	/* For Content Pane */
	private Container aboutProjectDialogContentPane,frameContentPane, addDialogContentPane,updateDialogContentPane;

	/* For Layout */
	private SpringLayout aboutProjectDialogLayout,frameLayout, addDialogLayout,updateDialogLayout;

	/* For Dimensions and Fonts */
	private Dimension buttonDimension, textFieldDimension,dialogButtonDimension;
	private Font labelFont,buttonFont,textFieldFont;

	/* For Menu */
	private JMenuBar mainMenuBar;
	private JMenu aboutMenu, settingsMenu;
	private JMenuItem aboutProjectMenuItem,updateProfileMenuItem, logoutMenuItem;

	/* For JDialog */
	private JDialog aboutProjectDialog,addStorekeeperDialog,updateProfileDialog, confirmLogoutDialog;

	/* For Buttons */
	private JButton aboutProjectDialogCloseButton,logoutMenuButton,addStorekeepersButton, viewStorekeepersButton, storekeeperPanelButton, registerDialogButton, cancelDialogButton, confirmLogoutButton, cancelLogoutButton,confirmUpdateButton,cancelUpdateButton;

	/* For Labels */
	private JLabel aboutProjectDialogIconLabel,adminWelcomeLabel, idAddNewLabel, passwordAddNewLabel, confirmPasswordLabel, fnameAddNewLabel, lnameAddNewLabel, addressAddNewLabel, phoneAddNewLabel, passIncorrectMsg,updatePassIncorrectMsg, confirmLogoutMessage,updateIDLabel,updatePasswordLabel,updateConfirmPasswordLabel,updateFnameLabel,updateLnameLabel;

	/* For JTextFields */
	private JPasswordField passwordAddNewField, confirmPasswordField,updatePasswordField,updateConfirmPasswordField;
	private JTextField idAddNewTextField, fnameAddNewTextField, lnameAddNewTextField, addressAddNewTextField, phoneAddNewTextField,updateIDTextField,updateFnameTextField,updateLnameTextField;

	/* For JTextPanes */
	private JTextPane aboutProjectTextPane;

	public static void main(String[] args)
	{

		//final StoreAdministrator adminNavigation = new StoreAdministrator();
		//adminNavigation.startSystem("demo");

	}

	public StoreAdministrator(String databaseID, String databasePassword)
	{
		this.databaseID=databaseID;
		this.databasePassword=databasePassword;
		/* Setting Look and Feel */
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Windows Look and Feel not found!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
		}
		SwingUtilities.updateComponentTreeUI(this);

		/* Initialize JDBC */
		try
		{
			connection = DriverManager.getConnection(databaseURL, databaseID, databasePassword);
			statement = connection.createStatement();
		} catch (SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Connection to database couldn't be established!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
		}

		/* Layout defintion */
		aboutProjectDialogLayout=new SpringLayout();
		frameLayout = new SpringLayout();
		addDialogLayout = new SpringLayout();
		updateDialogLayout=new SpringLayout();

		/* Content Pane definition */
		frameContentPane = this.getContentPane();

		/* Dimension and Fonts definitions */
		buttonDimension = new Dimension(200, 50);
		dialogButtonDimension=new Dimension(150,30);
		textFieldDimension = new Dimension(200, 30);
		labelFont = new Font("Segoe UI", Font.PLAIN, 13);
		buttonFont=new Font("Helvetica",Font.PLAIN,11);
		textFieldFont=new Font("Verdana",Font.PLAIN,12);

		/* Menu definitions */
		mainMenuBar = new JMenuBar();
		aboutMenu = new JMenu("About");
		settingsMenu = new JMenu("Settings");
		aboutProjectMenuItem = new JMenuItem("Project");
		updateProfileMenuItem=new JMenuItem("Update Profile");
		logoutMenuItem = new JMenuItem("Logout");

		/* JDialog defintion */
		aboutProjectDialog=new JDialog(this,"GroceryDesk- About Project",true);
		addStorekeeperDialog = new JDialog(this, "GroceryDesk- Add New Storekeeper", true);
		updateProfileDialog=new JDialog(this,"GroceryDesk- Update Profile",true);
		confirmLogoutDialog = new JDialog(this,"GroceryDesk- Logout",true);

		/* Other definitions */
		aboutProjectDialogCloseButton=new JButton("Close");
		logoutMenuButton=new JButton("Logout");
		addStorekeepersButton = new JButton("Add Storekeeper");
		viewStorekeepersButton = new JButton("View Storekeepers");
		storekeeperPanelButton = new JButton("Storekeeper Panel");

		aboutProjectDialogIconLabel=new JLabel();

		adminWelcomeLabel = new JLabel();

		confirmLogoutMessage = new JLabel("Are you sure you want to logout?",SwingConstants.CENTER);

		idAddNewLabel = new JLabel("Storekeeper ID");
		passwordAddNewLabel = new JLabel("Password");
		confirmPasswordLabel = new JLabel("Confirm Password");
		fnameAddNewLabel = new JLabel("First Name");
		lnameAddNewLabel = new JLabel("Last Name");
		addressAddNewLabel = new JLabel("Address");
		phoneAddNewLabel = new JLabel("Phone");
		passIncorrectMsg = new JLabel("Passwords do not match!");
		updatePassIncorrectMsg=new JLabel("Passwords do not match!");
		updateIDLabel=new JLabel("New ID");
		updatePasswordLabel=new JLabel("New Password");
		updateConfirmPasswordLabel=new JLabel("Confirm Password");
		updateFnameLabel=new JLabel("New First Name");
		updateLnameLabel=new JLabel("New Last Name");

		idAddNewTextField = new JTextField();
		passwordAddNewField = new JPasswordField();
		confirmPasswordField = new JPasswordField();
		fnameAddNewTextField = new JTextField();
		lnameAddNewTextField = new JTextField();
		addressAddNewTextField = new JTextField();
		phoneAddNewTextField = new JTextField();
		updateIDTextField=new JTextField();
		updatePasswordField=new JPasswordField();
		updateConfirmPasswordField=new JPasswordField();
		updateFnameTextField=new JTextField();
		updateLnameTextField=new JTextField();

		aboutProjectTextPane=new JTextPane();

		confirmLogoutButton = new JButton("Yes");
		cancelLogoutButton = new JButton("Cancel");

		registerDialogButton = new JButton("Register Storekeeper");
		cancelDialogButton = new JButton("Cancel");

		confirmUpdateButton=new JButton("Update Profile");
		cancelUpdateButton=new JButton("Cancel");

		// FOR TESTING, TODO delete afterwards
		//startSystem("demo!");
	}

	public void startSystem(String loginID)
	{
		setWelcomeLabel();
		this.loginID = loginID;
		String loggedInTimeStamp = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());

		/* Attributes of logoutMenuButton */
		logoutMenuButton.setFocusPainted(false);
		logoutMenuButton.setOpaque(false);
		logoutMenuButton.setBorder(null);
		logoutMenuButton.setFont(new Font("Segoe UI",Font.PLAIN,11));
		logoutMenuButton.setPreferredSize(new Dimension(150,30));
		logoutMenuButton.setForeground(new Color(150, 40, 27));
		logoutMenuButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				super.mouseEntered(e);
				logoutMenuButton.setForeground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				super.mouseExited(e);
				logoutMenuButton.setForeground(new Color(150, 40, 27));
			}
		});


		aboutProjectDialogCloseButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				aboutProjectDialog.dispose();
			}
		});

		aboutProjectDialogCloseButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				super.mouseEntered(e);
				aboutProjectDialogCloseButton.setForeground(Color.WHITE);
				aboutProjectDialogCloseButton.setBackground(new Color(103, 128, 159));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				super.mouseExited(e);
				aboutProjectDialogCloseButton.setForeground(Color.BLACK);
				aboutProjectDialogCloseButton.setBackground(Color.WHITE);
			}
		});

		aboutProjectMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showAboutProjectDialog();
			}
		});

		/* Components of Menu */
		aboutMenu.add(aboutProjectMenuItem);
		settingsMenu.add(updateProfileMenuItem);
//		settingsMenu.add(logoutMenuItem);
		mainMenuBar.add(aboutMenu);
		mainMenuBar.add(settingsMenu);
		mainMenuBar.add(Box.createHorizontalGlue());
		mainMenuBar.add(logoutMenuButton);

		/* Other attributes */
		frameContentPane.setLayout(frameLayout);
		adminWelcomeLabel.setFont(labelFont);
//		adminWelcomeLabel.setText("Welcome, " + loginID + "!");
		adminWelcomeLabel.setForeground(Color.WHITE);
		adminWelcomeLabel.setOpaque(false);

		addStorekeepersButton.setFocusPainted(false);
		addStorekeepersButton.setBorderPainted(false);
		addStorekeepersButton.setBackground(Color.WHITE);
		addStorekeepersButton.setForeground(new Color(103, 128, 159));
		addStorekeepersButton.setPreferredSize(buttonDimension);
		addStorekeepersButton.setFont(buttonFont);

		viewStorekeepersButton.setFocusPainted(false);
		viewStorekeepersButton.setBorderPainted(false);
		viewStorekeepersButton.setBackground(Color.WHITE);
		viewStorekeepersButton.setForeground(new Color(103, 128, 159));
		viewStorekeepersButton.setPreferredSize(buttonDimension);
		viewStorekeepersButton.setFont(buttonFont);

		storekeeperPanelButton.setFocusPainted(false);
		storekeeperPanelButton.setBorderPainted(false);
		storekeeperPanelButton.setBackground(Color.WHITE);
		storekeeperPanelButton.setForeground(new Color(103, 128, 159));
		storekeeperPanelButton.setPreferredSize(buttonDimension);
		storekeeperPanelButton.setFont(buttonFont);

		confirmLogoutButton.setForeground(Color.WHITE);
		confirmLogoutButton.setBackground(new Color(231, 76, 60));
		confirmLogoutButton.setBorderPainted(false);
		confirmLogoutButton.setFocusPainted(false);
		confirmLogoutButton.setOpaque(true);
		confirmLogoutButton.setFont(buttonFont);

		cancelLogoutButton.setForeground(Color.WHITE);
		cancelLogoutButton.setBackground(new Color(103, 128, 159));
		cancelLogoutButton.setBorderPainted(false);
		cancelLogoutButton.setFocusPainted(false);
		cancelLogoutButton.setOpaque(true);
		cancelDialogButton.setFont(buttonFont);

		idAddNewTextField.setPreferredSize(textFieldDimension);
		idAddNewTextField.setFont(textFieldFont);
		idAddNewTextField.setBorder(null);

		passwordAddNewField.setPreferredSize(textFieldDimension);
		passwordAddNewField.setFont(textFieldFont);
		passwordAddNewField.setBorder(null);

		confirmPasswordField.setPreferredSize(textFieldDimension);
		confirmPasswordField.setFont(textFieldFont);
		confirmPasswordField.setBorder(null);

		fnameAddNewTextField.setPreferredSize(textFieldDimension);
		fnameAddNewTextField.setFont(textFieldFont);
		fnameAddNewTextField.setBorder(null);

		lnameAddNewTextField.setPreferredSize(textFieldDimension);
		lnameAddNewTextField.setFont(textFieldFont);
		lnameAddNewTextField.setBorder(null);

		addressAddNewTextField.setPreferredSize(textFieldDimension);
		addressAddNewTextField.setFont(textFieldFont);
		addressAddNewTextField.setBorder(null);

		phoneAddNewTextField.setPreferredSize(textFieldDimension);
		phoneAddNewTextField.setFont(textFieldFont);
		phoneAddNewTextField.setBorder(null);

		idAddNewLabel.setFont(labelFont);
		passwordAddNewLabel.setFont(labelFont);
		confirmPasswordLabel.setFont(labelFont);
		fnameAddNewLabel.setFont(labelFont);
		lnameAddNewLabel.setFont(labelFont);
		addressAddNewLabel.setFont(labelFont);
		phoneAddNewLabel.setFont(labelFont);

		passIncorrectMsg.setFont(new Font("Segoe UI", Font.PLAIN, 9));
		passIncorrectMsg.setForeground(Color.RED);
		passIncorrectMsg.setVisible(false);

		updatePassIncorrectMsg.setFont(new Font("Segoe UI", Font.PLAIN, 9));
		updatePassIncorrectMsg.setForeground(Color.RED);
		updatePassIncorrectMsg.setVisible(false);

		updateIDLabel.setFont(labelFont);
		updatePasswordLabel.setFont(labelFont);
		updateConfirmPasswordLabel.setFont(labelFont);
		updateFnameLabel.setFont(labelFont);
		updateLnameLabel.setFont(labelFont);

		updateIDTextField.setPreferredSize(textFieldDimension);
		updateIDTextField.setFont(textFieldFont);
		updateIDTextField.setBorder(null);

		updatePasswordField.setPreferredSize(textFieldDimension);
		updatePasswordField.setFont(textFieldFont);
		updatePasswordField.setBorder(null);

		updateConfirmPasswordField.setPreferredSize(textFieldDimension);
		updateConfirmPasswordField.setFont(textFieldFont);
		updateConfirmPasswordField.setBorder(null);

		updateFnameTextField.setPreferredSize(textFieldDimension);
		updateFnameTextField.setFont(textFieldFont);
		updateFnameTextField.setBorder(null);

		updateLnameTextField.setPreferredSize(textFieldDimension);
		updateLnameTextField.setFont(textFieldFont);
		updateLnameTextField.setBorder(null);

		registerDialogButton.setFocusPainted(false);
		registerDialogButton.setBorderPainted(false);
		registerDialogButton.setBackground(new Color(103, 128, 159));
		registerDialogButton.setForeground(Color.WHITE);
		registerDialogButton.setPreferredSize(dialogButtonDimension);
		registerDialogButton.setFont(buttonFont);

		cancelDialogButton.setFocusPainted(false);
		cancelDialogButton.setBorderPainted(false);
		cancelDialogButton.setPreferredSize(dialogButtonDimension);
		cancelDialogButton.setFont(buttonFont);
		cancelDialogButton.setOpaque(true);

		confirmUpdateButton.setFocusPainted(false);
		confirmUpdateButton.setBorderPainted(false);
		confirmUpdateButton.setBackground(new Color(103, 128, 159));
		confirmUpdateButton.setForeground(Color.WHITE);
		confirmUpdateButton.setPreferredSize(dialogButtonDimension);
		confirmUpdateButton.setFont(buttonFont);

		cancelUpdateButton.setFocusPainted(false);
		cancelUpdateButton.setBorderPainted(false);
		cancelUpdateButton.setPreferredSize(dialogButtonDimension);
		cancelUpdateButton.setFont(buttonFont);
		cancelUpdateButton.setOpaque(true);

		/* Adding Event Listeners */
		updateProfileMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateProfileMenuItemClicked();
			}
		});

		logoutMenuButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				logoutMenuItemClicked();
			}
		});

		confirmLogoutButton.addActionListener(this);
		cancelLogoutButton.addActionListener(this);

		confirmPasswordField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				if (!confirmPasswordField.getText().equals(passwordAddNewField.getText()))
				{
					passIncorrectMsg.setVisible(true);
				} else
				{
					passIncorrectMsg.setVisible(false);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if (!confirmPasswordField.getText().equals(passwordAddNewField.getText()))
				{
					passIncorrectMsg.setVisible(true);
				} else
				{
					passIncorrectMsg.setVisible(false);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				if (!confirmPasswordField.getText().equals(passwordAddNewField.getText()))
				{
					passIncorrectMsg.setVisible(true);
				} else
				{
					passIncorrectMsg.setVisible(false);
				}
			}
		});

		updateConfirmPasswordField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				if (!updateConfirmPasswordField.getText().equals(updatePasswordField.getText()))
				{
					updatePassIncorrectMsg.setVisible(true);
					System.out.println("PASSWORDS NOT SAME!");
				} else
				{
					updatePassIncorrectMsg.setVisible(false);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if (!updateConfirmPasswordField.getText().equals(updatePasswordField.getText()))
				{
					updatePassIncorrectMsg.setVisible(true);
					System.out.println("PASSWORDS NOT SAME!");
				} else
				{
					updatePassIncorrectMsg.setVisible(false);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				if (!updateConfirmPasswordField.getText().equals(updatePasswordField.getText()) )
				{
					updatePassIncorrectMsg.setVisible(true);
					System.out.println("PASSWORDS NOT SAME!");
				} else
				{
					updatePassIncorrectMsg.setVisible(false);
				}
			}
		});
		addStorekeepersButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addStorekeepersButtonClicked();
			}
		});
		viewStorekeepersButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				viewStorekeepersButtonClicked();
			}
		});
		storekeeperPanelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				storekeeperPanelButtonClicked(loginID);
			}
		});

		addStorekeepersButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				addStorekeepersButton.setBackground(new Color(224, 247, 250));
				addStorekeepersButton.setForeground(new Color(34, 49, 63));
			}

			public void mouseExited(MouseEvent e)
			{
				addStorekeepersButton.setBackground(Color.WHITE);
				addStorekeepersButton.setForeground(new Color(103, 128, 159));
			}
		});

		viewStorekeepersButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				viewStorekeepersButton.setBackground(new Color(224, 247, 250));
				viewStorekeepersButton.setForeground(new Color(34, 49, 63));
			}

			public void mouseExited(MouseEvent e)
			{
				viewStorekeepersButton.setBackground(Color.WHITE);
				viewStorekeepersButton.setForeground(new Color(103, 128, 159));
			}
		});

		storekeeperPanelButton.addMouseListener(
				new MouseAdapter()
				{
					@Override
					public void mouseEntered(MouseEvent e)
					{
						storekeeperPanelButton.setBackground(new Color(224, 247, 250));
						storekeeperPanelButton.setForeground(new Color(34, 49, 63));
					}

					public void mouseExited(MouseEvent e)
					{
						storekeeperPanelButton.setBackground(Color.WHITE);
						storekeeperPanelButton.setForeground(new Color(103, 128, 159));
					}
				});

		/* Adding Components to the Frame */
		this.add(adminWelcomeLabel);
		this.add(addStorekeepersButton);
		this.add(viewStorekeepersButton);
		this.add(storekeeperPanelButton);
		this.setJMenuBar(mainMenuBar);

		/*------------------------------------------------aboutProjectDialog-----------------------------------------------*/

		ImageIcon aboutDialogImageIcon = new ImageIcon(new ImageIcon("grocerydeskIcon.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));

		/* Components of aboutProjectDialog */
		aboutProjectDialog.add(aboutProjectDialogIconLabel);
		aboutProjectDialog.add(aboutProjectTextPane);
		aboutProjectDialog.add(aboutProjectDialogCloseButton);

		/* Attributes of Components */
		aboutProjectDialogCloseButton.setFocusPainted(false);
		aboutProjectDialogCloseButton.setOpaque(true);
		aboutProjectDialogCloseButton.setBorderPainted(false);
		aboutProjectDialogCloseButton.setBorder(null);
		aboutProjectDialogCloseButton.setFont(labelFont);
		aboutProjectDialogCloseButton.setPreferredSize(new Dimension(150,30));
		aboutProjectDialogIconLabel.setIcon(aboutDialogImageIcon);
		aboutProjectDialogIconLabel.setPreferredSize(new Dimension(200,100));
		aboutProjectDialogIconLabel.setToolTipText("Image by Pixel Perfect");

		aboutProjectTextPane.setEditable(false);
		aboutProjectTextPane.setOpaque(false);
		aboutProjectTextPane.setBorder(null);
		aboutProjectTextPane.setForeground(Color.WHITE);
		aboutProjectTextPane.setContentType("text/html");

		/* Attributes of aboutProjectDialog */
		aboutProjectDialogContentPane=aboutProjectDialog.getContentPane();
		aboutProjectDialog.setUndecorated(true);
		aboutProjectDialog.getContentPane().setBackground(new Color(34, 49, 63));
		aboutProjectDialog.setOpacity(0.95f);
		aboutProjectDialog.setLayout(aboutProjectDialogLayout);
		aboutProjectDialog.setSize(425,430);
		aboutProjectDialog.setFont(new Font("Segoe UI",Font.PLAIN,14));
		aboutProjectDialog.setLocationRelativeTo(this);
		aboutProjectDialog.setResizable(false);

		String aboutText="<div style='color:white; font-family: Segoe UI; text-align:justify'><center><p><span style='color:rgb(38, 194, 129);'>GroceryDesk</span> is an attempt at making grocery storekeeping easier for every role involved in the business.</p></center>";
		String teamText="<center><p>It was developed as a Minor Project by the team of</p></center>";
		String teamMembersText="<center><p style='color: white'>Roshan Adhikari<br>Sakar Raman Parajuli<br>Sandhya Acharya</p><br>Supervised by Er. Ramesh Thapa<center></div>";
		String githubLinkText="<div style='color:rgb(38, 194, 129); font-family: Segoe UI; text-align:justify'><center>github.com/nahsorad/grocerydesk</center></div>";

		/* aboutProjectTextPane Content */
		aboutProjectTextPane.setText(aboutText+teamText+teamMembersText+githubLinkText);

		/* Constraints for Components */
		aboutProjectDialogLayout.putConstraint(SpringLayout.NORTH,aboutProjectDialogIconLabel,10,SpringLayout.NORTH,aboutProjectDialogContentPane);
		aboutProjectDialogLayout.putConstraint(SpringLayout.WEST,aboutProjectDialogIconLabel,195,SpringLayout.WEST,aboutProjectDialogContentPane);

		aboutProjectDialogLayout.putConstraint(SpringLayout.NORTH,aboutProjectTextPane,80,SpringLayout.NORTH,aboutProjectDialogContentPane);
		aboutProjectDialogLayout.putConstraint(SpringLayout.WEST,aboutProjectTextPane,30,SpringLayout.WEST,aboutProjectDialogContentPane);
		aboutProjectDialogLayout.putConstraint(SpringLayout.EAST,aboutProjectTextPane,-30,SpringLayout.EAST,aboutProjectDialogContentPane);

		aboutProjectDialogLayout.putConstraint(SpringLayout.SOUTH,aboutProjectDialogCloseButton,-50,SpringLayout.SOUTH,aboutProjectDialogContentPane);
		aboutProjectDialogLayout.putConstraint(SpringLayout.WEST,aboutProjectDialogCloseButton,140,SpringLayout.WEST,aboutProjectDialogContentPane);

		/*--------------------------------------------addStorekeeperDialog--------------------------------------------*/

		addDialogContentPane = addStorekeeperDialog.getContentPane();
		addStorekeeperDialog.getContentPane().setBackground(new Color(34,49,63));
		addStorekeeperDialog.setLayout(addDialogLayout);
		addStorekeeperDialog.setSize(420, 530);
		addStorekeeperDialog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		addStorekeeperDialog.setLocationRelativeTo(this);
		addStorekeeperDialog.setResizable(false);

		/* Components of addStorekeeperDialog */
		addStorekeeperDialog.add(idAddNewLabel);
		addStorekeeperDialog.add(passwordAddNewLabel);
		addStorekeeperDialog.add(confirmPasswordLabel);
		addStorekeeperDialog.add(passIncorrectMsg);
		addStorekeeperDialog.add(fnameAddNewLabel);
		addStorekeeperDialog.add(lnameAddNewLabel);
		addStorekeeperDialog.add(addressAddNewLabel);
		addStorekeeperDialog.add(phoneAddNewLabel);
		addStorekeeperDialog.add(idAddNewTextField);
		addStorekeeperDialog.add(passwordAddNewField);
		addStorekeeperDialog.add(confirmPasswordField);
		addStorekeeperDialog.add(fnameAddNewTextField);
		addStorekeeperDialog.add(lnameAddNewTextField);
		addStorekeeperDialog.add(addressAddNewTextField);
		addStorekeeperDialog.add(phoneAddNewTextField);
		addStorekeeperDialog.add(registerDialogButton);
		addStorekeeperDialog.add(cancelDialogButton);

		/* Attributes of Components */
		idAddNewLabel.setForeground(Color.WHITE);
		passwordAddNewLabel.setForeground(Color.WHITE);
		confirmPasswordLabel.setForeground(Color.WHITE);
		fnameAddNewLabel.setForeground(Color.WHITE);
		lnameAddNewLabel.setForeground(Color.WHITE);
		addressAddNewLabel.setForeground(Color.WHITE);
		phoneAddNewLabel.setForeground(Color.WHITE);

		/* Attributes of addStorekeeperDialog */
		addStorekeeperDialog.setUndecorated(true);
		addStorekeeperDialog.setBackground(Color.WHITE);
		addStorekeeperDialog.setOpacity(0.95f);

		/* Adding Event Listeners */
		registerDialogButton.addActionListener(this);
		cancelDialogButton.addActionListener(this);

		/* Constraints for Layout */
		addDialogLayout.putConstraint(SpringLayout.NORTH, idAddNewLabel, 50, SpringLayout.NORTH, addDialogContentPane);
		addDialogLayout.putConstraint(SpringLayout.WEST, idAddNewLabel, 35, SpringLayout.WEST, addDialogContentPane);

		addDialogLayout.putConstraint(SpringLayout.NORTH, passwordAddNewLabel, 40, SpringLayout.SOUTH, idAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.EAST, passwordAddNewLabel, 0, SpringLayout.EAST, idAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, confirmPasswordLabel, 40, SpringLayout.SOUTH, passwordAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.EAST, confirmPasswordLabel, 0, SpringLayout.EAST, passwordAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, fnameAddNewLabel, 40, SpringLayout.SOUTH, confirmPasswordLabel);
		addDialogLayout.putConstraint(SpringLayout.EAST, fnameAddNewLabel, 0, SpringLayout.EAST, confirmPasswordLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, lnameAddNewLabel, 40, SpringLayout.SOUTH, fnameAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.EAST, lnameAddNewLabel, 0, SpringLayout.EAST, fnameAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, addressAddNewLabel, 40, SpringLayout.SOUTH, lnameAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.EAST, addressAddNewLabel, 0, SpringLayout.EAST, lnameAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, phoneAddNewLabel, 40, SpringLayout.SOUTH, addressAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.EAST, phoneAddNewLabel, 0, SpringLayout.EAST, addressAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, idAddNewTextField, -3, SpringLayout.NORTH, idAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, idAddNewTextField, 30, SpringLayout.EAST, idAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, passwordAddNewField, -3, SpringLayout.NORTH, passwordAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, passwordAddNewField, 0, SpringLayout.WEST, idAddNewTextField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, confirmPasswordField, -3, SpringLayout.NORTH, confirmPasswordLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, confirmPasswordField, 0, SpringLayout.WEST, passwordAddNewField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, passIncorrectMsg, 5, SpringLayout.SOUTH, confirmPasswordField);
		addDialogLayout.putConstraint(SpringLayout.WEST, passIncorrectMsg, 0, SpringLayout.WEST, confirmPasswordField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, fnameAddNewTextField, -3, SpringLayout.NORTH, fnameAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, fnameAddNewTextField, 0, SpringLayout.WEST, confirmPasswordField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, lnameAddNewTextField, -3, SpringLayout.NORTH, lnameAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, lnameAddNewTextField, 0, SpringLayout.WEST, fnameAddNewTextField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, addressAddNewTextField, -3, SpringLayout.NORTH, addressAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, addressAddNewTextField, 0, SpringLayout.WEST, lnameAddNewTextField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, phoneAddNewTextField, -3, SpringLayout.NORTH, phoneAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, phoneAddNewTextField, 0, SpringLayout.WEST, addressAddNewTextField);

		addDialogLayout.putConstraint(SpringLayout.NORTH, registerDialogButton, 40, SpringLayout.SOUTH, phoneAddNewLabel);
		addDialogLayout.putConstraint(SpringLayout.WEST, registerDialogButton, 20, SpringLayout.WEST, idAddNewLabel);

		addDialogLayout.putConstraint(SpringLayout.NORTH, cancelDialogButton, 0, SpringLayout.NORTH, registerDialogButton);
		addDialogLayout.putConstraint(SpringLayout.WEST, cancelDialogButton, 10, SpringLayout.EAST, registerDialogButton);

		/* Contrains for frameLayout */

		frameLayout.putConstraint(SpringLayout.NORTH, adminWelcomeLabel, 10, SpringLayout.NORTH, frameContentPane);
		frameLayout.putConstraint(SpringLayout.WEST, adminWelcomeLabel, 10, SpringLayout.NORTH, frameContentPane);

		frameLayout.putConstraint(SpringLayout.NORTH, addStorekeepersButton, 30, SpringLayout.SOUTH, adminWelcomeLabel);
		frameLayout.putConstraint(SpringLayout.WEST, addStorekeepersButton, 30, SpringLayout.WEST, frameContentPane);

		frameLayout.putConstraint(SpringLayout.NORTH, viewStorekeepersButton, 0, SpringLayout.NORTH, addStorekeepersButton);
		frameLayout.putConstraint(SpringLayout.WEST, viewStorekeepersButton, 40, SpringLayout.EAST, addStorekeepersButton);

		frameLayout.putConstraint(SpringLayout.NORTH, storekeeperPanelButton, 30, SpringLayout.SOUTH, addStorekeepersButton);
		frameLayout.putConstraint(SpringLayout.WEST, storekeeperPanelButton, 125, SpringLayout.WEST, addStorekeepersButton);

		/*---------------------------------------------confirmLogoutDialog--------------------------------------------*/

		/* Components of confirmLogoutDialog */
		confirmLogoutDialog.add(confirmLogoutMessage);
		confirmLogoutDialog.add(confirmLogoutButton);
		confirmLogoutDialog.add(cancelLogoutButton);

		/* Attributes of Components */
		confirmLogoutMessage.setForeground(Color.WHITE);
		confirmLogoutMessage.setFont(new Font("Segoe UI",Font.PLAIN,11));

		/* Attributes of confirmLogoutDialog */
		confirmLogoutDialog.setSize(300,150);
		confirmLogoutDialog.setUndecorated(true);
		confirmLogoutDialog.getContentPane().setBackground(new Color(34, 49, 63));
		confirmLogoutDialog.setOpacity(0.95f);
		confirmLogoutDialog.setLocationRelativeTo(null);
		confirmLogoutDialog.setLayout(new GridLayout(0, 1));

		/*--------------------------------------------updateProfileDialog--------------------------------------------*/

		String idPlaceHolder=null,fnamePlaceHolder=null,lnamePlaceHolder=null;
		updateDialogContentPane = addStorekeeperDialog.getContentPane();
		updateProfileDialog.getContentPane().setBackground(new Color(34,49,63));
		updateProfileDialog.setLayout(updateDialogLayout);
		updateProfileDialog.setSize(460, 410);
		updateProfileDialog.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		updateProfileDialog.setLocationRelativeTo(this);
		updateProfileDialog.setResizable(false);

		/* Components of updateProfileDialog */
		updateProfileDialog.add(updateIDLabel);
		updateProfileDialog.add(updateIDTextField);
		updateProfileDialog.add(updatePasswordLabel);
		updateProfileDialog.add(updatePasswordField);
		updateProfileDialog.add(updateConfirmPasswordLabel);
		updateProfileDialog.add(updateConfirmPasswordField);
		updateProfileDialog.add(updatePassIncorrectMsg);
		updateProfileDialog.add(updateFnameLabel);
		updateProfileDialog.add(updateFnameTextField);
		updateProfileDialog.add(updateLnameLabel);
		updateProfileDialog.add(updateLnameTextField);
		updateProfileDialog.add(confirmUpdateButton);
		updateProfileDialog.add(cancelUpdateButton);

		/* Attributes of Components */
		updateIDLabel.setForeground(Color.WHITE);
		updatePasswordLabel.setForeground(Color.WHITE);
		updateConfirmPasswordLabel.setForeground(Color.WHITE);
		updateFnameLabel.setForeground(Color.WHITE);
		updateLnameLabel.setForeground(Color.WHITE);

		try
		{
			statement.executeUpdate("USE grocerydesk");
			pstatement=connection.prepareStatement("SELECT fname,lname FROM administrator WHERE id=?");
			pstatement.setString(1,loginID);
			resultSet=pstatement.executeQuery();
			if(resultSet.next())
			{
				idPlaceHolder=loginID;
				fnamePlaceHolder=resultSet.getString("fname");
				lnamePlaceHolder=resultSet.getString("lname");
			}
			updateIDTextField.setText(idPlaceHolder);
			updateFnameTextField.setText(fnamePlaceHolder);
			updateLnameTextField.setText(lnamePlaceHolder);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error Code:"+e.getErrorCode()+" Database Error Occurred!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		/* Attributes of updateProfileDialog */
		updateProfileDialog.setUndecorated(true);
		updateProfileDialog.setBackground(Color.WHITE);
		updateProfileDialog.setOpacity(0.95f);

		/* Adding Event Listeners */
		confirmUpdateButton.addActionListener(this);
		cancelUpdateButton.addActionListener(this);

		/* Constraints for Layout */
		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateIDLabel, 50, SpringLayout.NORTH, updateDialogContentPane);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updateIDLabel, 110, SpringLayout.WEST, updateDialogContentPane);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updatePasswordLabel, 40, SpringLayout.SOUTH, updateIDLabel);
		updateDialogLayout.putConstraint(SpringLayout.EAST, updatePasswordLabel, 0, SpringLayout.EAST, updateIDLabel);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateConfirmPasswordLabel, 40, SpringLayout.SOUTH, updatePasswordLabel);
		updateDialogLayout.putConstraint(SpringLayout.EAST, updateConfirmPasswordLabel, 0, SpringLayout.EAST, updatePasswordLabel);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateFnameLabel, 40, SpringLayout.SOUTH,updateConfirmPasswordLabel);
		updateDialogLayout.putConstraint(SpringLayout.EAST, updateFnameLabel, 0, SpringLayout.EAST, updateConfirmPasswordLabel);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateLnameLabel, 40, SpringLayout.SOUTH, updateFnameLabel);
		updateDialogLayout.putConstraint(SpringLayout.EAST, updateLnameLabel, 0, SpringLayout.EAST, updateFnameLabel);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateIDTextField, -3, SpringLayout.NORTH, updateIDLabel);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updateIDTextField, 30, SpringLayout.EAST, updateIDLabel);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updatePasswordField, -3, SpringLayout.NORTH, updatePasswordLabel);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updatePasswordField, 0, SpringLayout.WEST, updateIDTextField);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateConfirmPasswordField, -3, SpringLayout.NORTH, updateConfirmPasswordLabel);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updateConfirmPasswordField, 0, SpringLayout.WEST, updatePasswordField);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updatePassIncorrectMsg, 5, SpringLayout.SOUTH, updateConfirmPasswordField);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updatePassIncorrectMsg, 0, SpringLayout.WEST, updateConfirmPasswordField);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateFnameTextField, -3, SpringLayout.NORTH, updateFnameLabel);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updateFnameTextField, 0, SpringLayout.WEST, updateConfirmPasswordField);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, updateLnameTextField, -3, SpringLayout.NORTH, updateLnameLabel);
		updateDialogLayout.putConstraint(SpringLayout.WEST, updateLnameTextField, 0, SpringLayout.WEST, updateFnameTextField);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, confirmUpdateButton, 40, SpringLayout.SOUTH, updateLnameLabel);
		updateDialogLayout.putConstraint(SpringLayout.WEST, confirmUpdateButton, -35, SpringLayout.WEST, updateIDLabel);

		updateDialogLayout.putConstraint(SpringLayout.NORTH, cancelUpdateButton, 0, SpringLayout.NORTH, confirmUpdateButton);
		updateDialogLayout.putConstraint(SpringLayout.WEST, cancelUpdateButton, 10, SpringLayout.EAST, confirmUpdateButton);


		/*-----------------------------------------------Frame attributes---------------------------------------------*/

		frameContentPane.setBackground(new Color(103, 128, 159));
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				super.windowGainedFocus(e);
				setIconImage(new ImageIcon(getClass().getResource("grocerydeskIcon.png")).getImage());
			}
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
			}
		});
		this.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowGainedFocus(WindowEvent e)
			{
				super.windowGainedFocus(e);
				adminWelcomeLabel.setText("");
				setWelcomeLabel();
			}
		});
		this.setTitle("GroceryDesk- Admin Navigation");
		this.setSize(515, 300);
		this.setResizable(false);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public void showAboutProjectDialog()
	{
		aboutProjectDialog.setVisible(true);
	}
	public void setWelcomeLabel()
	{
		adminWelcomeLabel.setText("Welcome, "+loginID+"!");
	}
	public void updateProfileMenuItemClicked()
	{
		updateProfileDialog.setVisible(true);
	}
	public void logoutMenuItemClicked()
	{
		confirmLogoutDialog.setVisible(true);
	}
	public void addStorekeepersButtonClicked()
	{
		addStorekeeperDialog.setVisible(true);
	}

	public void viewStorekeepersButtonClicked()
	{
		StorekeeperTable viewStorekeeper=new StorekeeperTable();
		viewStorekeeper.startSystem();
	}

	public void storekeeperPanelButtonClicked(String loginID)
	{
		//this.setVisible(false);
		//close();
		Storekeeper storekeeperFrame = new Storekeeper(databaseID,databasePassword);
		storekeeperFrame.startSystem(loginID, 0, 1);
	}

	public void actionPerformed(ActionEvent event)
	{
		JButton sourceButton = (JButton) event.getSource();
		String query, dateTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		if (sourceButton == registerDialogButton)
		{
			catchPhoneNotNumberException=true;
			try
			{
				String storekeeperID = idAddNewTextField.getText(), storekeeperFname = fnameAddNewTextField.getText(), storekeeperLname = lnameAddNewTextField.getText(), storekeeperAddress = addressAddNewTextField.getText(), storekeeperPhone = phoneAddNewTextField.getText();
				String plainPassword = passwordAddNewField.getText(),confirmPassword=confirmPasswordField.getText();

				if(!plainPassword.equals(confirmPassword) )
				{
					JOptionPane.showMessageDialog(this,"The passwords do not match!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(storekeeperID.trim().length() <=0  || plainPassword.trim().length() <=0 || storekeeperFname.trim().length() <=0 || storekeeperLname.trim().length() <=0 || storekeeperAddress.trim().length() <=0 || storekeeperPhone.trim().length() <=0)
				{
					JOptionPane.showMessageDialog(this,"The fields cannot be left blank!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(storekeeperID.length()>25)
				{
					JOptionPane.showMessageDialog(this,"The specified Storekeeper ID exceeds field size!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(storekeeperFname.length()>25)
				{
					JOptionPane.showMessageDialog(this,"The specified First Name exceeds field size!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(storekeeperLname.length()>25)
				{
					JOptionPane.showMessageDialog(this,"The specified Last Name exceeds field size!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(storekeeperAddress.length()>25)
				{
					JOptionPane.showMessageDialog(this,"The specified Address exceeds field size!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(storekeeperPhone.length()>10)
				{
					JOptionPane.showMessageDialog(this,"The specified Phone Number exceeds field size!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(!fnameAddNewTextField.getText().matches("[^0-9]+") && !lnameAddNewTextField.getText().matches("[^0-9]+") && !addressAddNewTextField.getText().matches("[^0-9]+")  )
				{
					JOptionPane.showMessageDialog(this,"The Name and Address fields must not contain digits!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(!fnameAddNewTextField.getText().matches("[^0-9]+"))
				{
					JOptionPane.showMessageDialog(this,"The Name fields must not contain digits!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(!lnameAddNewTextField.getText().matches("[^0-9]+"))
				{
					JOptionPane.showMessageDialog(this,"The Name fields must not contain digits!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if(!addressAddNewTextField.getText().matches("[^0-9]+"))
				{
					JOptionPane.showMessageDialog(this,"The Address field must not contain digits!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else if ((phoneAddNewTextField.getText().charAt(0)=='0' && phoneAddNewTextField.getText().length() !=9) || (phoneAddNewTextField.getText().charAt(0)=='9' && phoneAddNewTextField.getText().length() !=10))
				{
					JOptionPane.showMessageDialog(this, "Phone Number must match format 0XXXXXXXX or 9XXXXXXXXX!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (phoneAddNewTextField.getText().charAt(0) !='0' && phoneAddNewTextField.getText().charAt(0) !='9')
				{
					JOptionPane.showMessageDialog(this, "Phone Number must match format 0XXXXXXXX or 9XXXXXXXXX!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					long newPhoneNumber=Long.parseLong(phoneAddNewTextField.getText());
					catchPhoneNotNumberException=false;
					StringBuffer storekeeperPassword = shaFunction(plainPassword);
					String preparedPassword = new String(storekeeperPassword);
					statement.executeUpdate("USE grocerydesk;");
					System.out.println(storekeeperPassword);
					query = "INSERT INTO storekeeper(id,password,fname,lname,address,phone,added_in,added_by) VALUES(?,?,?,?,?,?,?,?);";
					pstatement = connection.prepareStatement(query);
					pstatement.setString(1, storekeeperID);
					pstatement.setString(2, preparedPassword);
					pstatement.setString(3, storekeeperFname);
					pstatement.setString(4, storekeeperLname);
					pstatement.setString(5, storekeeperAddress);
					pstatement.setString(6, storekeeperPhone);
					pstatement.setString(7, dateTimeStamp);
					pstatement.setString(8, loginID);

					int rowsAffected = pstatement.executeUpdate();
					if (rowsAffected > 0)
					{
						JOptionPane.showMessageDialog(this, "New Storekeeper Added!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
						addStorekeeperDialog.dispose();
					} else
						JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);


				}
			}
			catch (NumberFormatException e)
			{
				if(catchPhoneNotNumberException==true)
				{
					JOptionPane.showMessageDialog(this, "Phone number must match format 0XXXXXXXX or 9XXXXXXXXX!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch (SQLIntegrityConstraintViolationException e)
			{
				JOptionPane.showMessageDialog(this, "The Storekeeper ID is not available!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e)
			{
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Database Error Occurred!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
			}
		} else if (sourceButton == cancelDialogButton)
		{
			addStorekeeperDialog.dispose();
		} else if (sourceButton == confirmLogoutButton)
		{
			try
			{
				statement.close();
				connection.close();
				confirmLogoutDialog.dispose();
				this.dispose();

			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				LoginFrame newLoginFrame = new LoginFrame();
			}
		} else if (sourceButton == cancelLogoutButton)
		{
			confirmLogoutDialog.dispose();
		}
		else if(sourceButton==confirmUpdateButton)
		{
			try
			{
				String newID=updateIDTextField.getText(),newPassword=updatePasswordField.getText(),newConfirmedPassword=updateConfirmPasswordField.getText(),newFname=updateFnameTextField.getText(),newLname=updateLnameTextField.getText();

				if(!newPassword.equals(newConfirmedPassword))
				{
					JOptionPane.showMessageDialog(this, "The Passwords do not match!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
//				else if(newID.equals("") || newPassword.equals("") || newFname.equals("") || newLname.equals("") )
//				{
//					JOptionPane.showMessageDialog(this,"The fields cannot be left blank!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
//				}
				else if(newID.trim().length() <=0  || newPassword.trim().length() <=0 || newFname.trim().length() <=0 || newLname.trim().length() <=0)
				{
					JOptionPane.showMessageDialog(this,"The fields cannot be left blank!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					System.out.println(newID+newPassword+newConfirmedPassword+newFname+newLname);
					pstatement=connection.prepareStatement("USE grocerydesk");
					pstatement.executeUpdate();
					query = "UPDATE administrator SET id=?, password=sha2(?,256),fname=?,lname=? WHERE id=?;";
					pstatement = connection.prepareStatement(query);
					pstatement.setString(1,newID);
					pstatement.setString(2,newPassword);
					pstatement.setString(3,newFname);
					pstatement.setString(4,newLname);
					pstatement.setString(5,loginID);
					int rowsAffected=pstatement.executeUpdate();
					if(rowsAffected>0)
					{
						JOptionPane.showMessageDialog(this, "Profile has been successfully updated!", "GroceryDesk- Success", JOptionPane.INFORMATION_MESSAGE);
						loginID=newID;
						updateProfileDialog.dispose();
					}
					else
						JOptionPane.showMessageDialog(this, "Profile could not be updated!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		else if(sourceButton==cancelUpdateButton)
		{
			updateProfileDialog.dispose();
		}

	}

	public static StringBuffer shaFunction(String plainText)
	{
		StringBuffer hexString = new StringBuffer();
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] encodedHash = md.digest(plainText.getBytes(StandardCharsets.UTF_8));
			for (int i = 0; i < encodedHash.length; i++)
			{
				String hex = Integer.toHexString(0xff & encodedHash[i]); //to convert bytes to hexadecimal equivalent and that to string
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			System.out.println(hexString);
			return hexString;
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return hexString;
	}

	public void close()
	{
		dispose();
	}
}

