package com.teamGroceryDesk.GroceryDesk;
/*
TODO Create
updateProfileDialog-
Update Fname, Lname, ID and Password of Administrator Account.
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.*;
import java.security.*;
import java.sql.*;
import java.text.*;

public class AdminNavigation extends JFrame implements ActionListener
{
	static String loginID;

	/* For JDBC */
	String databaseURL = "jdbc:mysql://localhost/?useSSL=true", query = null;
	static String databaseID ;
	static String databasePassword;
	Connection connection;
	PreparedStatement pstatement;
	Statement statement;
	ResultSet resultSet;
	ResultSetMetaData meta;

	/* For Content Pane */
	Container frameContentPane, addDialogContentPane;

	/* For Layout */
	SpringLayout frameLayout, dialogLayout;

	/* For Dimensions and Fonts */
	Dimension buttonDimension, textFieldDimension,dialogButtonDimension;
	Font labelFont,buttonFont,textFieldFont;

	/* For Menu */
	JMenuBar mainMenuBar;
	JMenu aboutMenu, settingsMenu;
	JMenuItem aboutProjectMenuItem,updateProfileMenuItem, logoutMenuItem;

	/* For JDialog */
	JDialog addStorekeeperDialog,updateProfileDialog, confirmLogoutDialog;

	/* For Buttons */
	JButton addStorekeepersButton, viewStorekeepersButton, storekeeperPanelButton, registerDialogButton, cancelDialogButton, confirmLogoutButton, cancelLogoutButton;

	/* For Labels */
	JLabel adminWelcomeLabel, idAddNewLabel, passwordAddNewLabel, confirmPasswordLabel, fnameAddNewLabel, lnameAddNewLabel, addressAddNewLabel, phoneAddNewLabel, passIncorrectMsg, confirmLogoutMessage;

	/* For JTextFields */
	JPasswordField passwordAddNewField, confirmPasswordField;
	JTextField idAddNewTextField, fnameAddNewTextField, lnameAddNewTextField, addressAddNewTextField, phoneAddNewTextField;

	public static void main(String[] args)
	{

		//final AdminNavigation adminNavigation = new AdminNavigation();
		//adminNavigation.startSystem("demo");

	}

	public AdminNavigation(String databaseID, String databasePassword)
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
		frameLayout = new SpringLayout();
		dialogLayout = new SpringLayout();

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
		addStorekeeperDialog = new JDialog(this, "GroceryDesk- Add New Storekeeper", true);
		updateProfileDialog=new JDialog(this,"GroceryDesk- Update Profile",true);
		confirmLogoutDialog = new JDialog(this,"GroceryDesk- Logout",true);

		/* Other definitions */
		addStorekeepersButton = new JButton("Add Storekeeper");
		viewStorekeepersButton = new JButton("View Storekeepers");
		storekeeperPanelButton = new JButton("Storekeeper Panel");
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

		idAddNewTextField = new JTextField();
		passwordAddNewField = new JPasswordField();
		confirmPasswordField = new JPasswordField();
		fnameAddNewTextField = new JTextField();
		lnameAddNewTextField = new JTextField();
		addressAddNewTextField = new JTextField();
		phoneAddNewTextField = new JTextField();

		confirmLogoutButton = new JButton("Yes");
		cancelLogoutButton = new JButton("Cancel");

		registerDialogButton = new JButton("Register Storekeeper");
		cancelDialogButton = new JButton("Cancel");

		// FOR TESTING, TODO delete afterwards
		//startSystem("demo!");
	}

	public void startSystem(String loginID)
	{

		this.loginID = loginID;
		String loggedInTimeStamp = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());

		/* Components of Menu */
		aboutMenu.add(aboutProjectMenuItem);
		settingsMenu.add(updateProfileMenuItem);
		settingsMenu.add(logoutMenuItem);
		mainMenuBar.add(aboutMenu);
		mainMenuBar.add(settingsMenu);

		/* Other attributes */
		frameContentPane.setLayout(frameLayout);
		adminWelcomeLabel.setFont(labelFont);
		adminWelcomeLabel.setText("Welcome, " + loginID + "!");
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
		confirmLogoutButton.setBackground(new Color(103, 128, 159));
		confirmLogoutButton.setBorderPainted(false);
		confirmLogoutButton.setFocusPainted(false);
		confirmLogoutButton.setOpaque(true);
		confirmLogoutButton.setFont(buttonFont);

		cancelLogoutButton.setForeground(new Color(103, 128, 159));
		cancelLogoutButton.setBackground(Color.WHITE);
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

		/* Adding Event Listeners */
		updateProfileMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateProfileMenuItemClicked();
			}
		});

		logoutMenuItem.addActionListener(new ActionListener()
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
				addStorekeepersButton.setForeground(new Color(44, 62, 80));
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
				viewStorekeepersButton.setForeground(new Color(44, 62, 80));
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
						storekeeperPanelButton.setForeground(new Color(44, 62, 80));
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

		/*--------------------------------------------addStorekeeperDialog--------------------------------------------*/

		addDialogContentPane = addStorekeeperDialog.getContentPane();
		addStorekeeperDialog.getContentPane().setBackground(new Color(44, 62, 80));
		addStorekeeperDialog.setLayout(dialogLayout);
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
		dialogLayout.putConstraint(SpringLayout.NORTH, idAddNewLabel, 50, SpringLayout.NORTH, addDialogContentPane);
		dialogLayout.putConstraint(SpringLayout.WEST, idAddNewLabel, 35, SpringLayout.WEST, addDialogContentPane);

		dialogLayout.putConstraint(SpringLayout.NORTH, passwordAddNewLabel, 40, SpringLayout.SOUTH, idAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST, passwordAddNewLabel, 0, SpringLayout.EAST, idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, confirmPasswordLabel, 40, SpringLayout.SOUTH, passwordAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST, confirmPasswordLabel, 0, SpringLayout.EAST, passwordAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, fnameAddNewLabel, 40, SpringLayout.SOUTH, confirmPasswordLabel);
		dialogLayout.putConstraint(SpringLayout.EAST, fnameAddNewLabel, 0, SpringLayout.EAST, confirmPasswordLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, lnameAddNewLabel, 40, SpringLayout.SOUTH, fnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST, lnameAddNewLabel, 0, SpringLayout.EAST, fnameAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, addressAddNewLabel, 40, SpringLayout.SOUTH, lnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST, addressAddNewLabel, 0, SpringLayout.EAST, lnameAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, phoneAddNewLabel, 40, SpringLayout.SOUTH, addressAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST, phoneAddNewLabel, 0, SpringLayout.EAST, addressAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, idAddNewTextField, -3, SpringLayout.NORTH, idAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, idAddNewTextField, 30, SpringLayout.EAST, idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, passwordAddNewField, -3, SpringLayout.NORTH, passwordAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, passwordAddNewField, 0, SpringLayout.WEST, idAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH, confirmPasswordField, -3, SpringLayout.NORTH, confirmPasswordLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, confirmPasswordField, 0, SpringLayout.WEST, passwordAddNewField);

		dialogLayout.putConstraint(SpringLayout.NORTH, passIncorrectMsg, 5, SpringLayout.SOUTH, confirmPasswordField);
		dialogLayout.putConstraint(SpringLayout.WEST, passIncorrectMsg, 0, SpringLayout.WEST, confirmPasswordField);

		dialogLayout.putConstraint(SpringLayout.NORTH, fnameAddNewTextField, -3, SpringLayout.NORTH, fnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, fnameAddNewTextField, 0, SpringLayout.WEST, confirmPasswordField);

		dialogLayout.putConstraint(SpringLayout.NORTH, lnameAddNewTextField, -3, SpringLayout.NORTH, lnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, lnameAddNewTextField, 0, SpringLayout.WEST, fnameAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH, addressAddNewTextField, -3, SpringLayout.NORTH, addressAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, addressAddNewTextField, 0, SpringLayout.WEST, lnameAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH, phoneAddNewTextField, -3, SpringLayout.NORTH, phoneAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, phoneAddNewTextField, 0, SpringLayout.WEST, addressAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH, registerDialogButton, 40, SpringLayout.SOUTH, phoneAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST, registerDialogButton, 20, SpringLayout.WEST, idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH, cancelDialogButton, 0, SpringLayout.NORTH, registerDialogButton);
		dialogLayout.putConstraint(SpringLayout.WEST, cancelDialogButton, 10, SpringLayout.EAST, registerDialogButton);

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
		confirmLogoutMessage.setFont(labelFont);

		/* Attributes of confirmLogoutDialog */
		confirmLogoutDialog.setSize(300,150);
		confirmLogoutDialog.setUndecorated(true);
		confirmLogoutDialog.getContentPane().setBackground(new Color(34, 49, 63));
		confirmLogoutDialog.setOpacity(0.95f);
		confirmLogoutDialog.setLocationRelativeTo(null);
		confirmLogoutDialog.setLayout(new GridLayout(0, 1));


		/*-----------------------------------------------Frame attributes---------------------------------------------*/


		frameContentPane.setBackground(new Color(103, 128, 159));
		this.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
			}
		});
		this.setTitle("GroceryDesk- Admin Navigation");
		this.setSize(515, 300);
		this.setResizable(false);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

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
		ViewStorekeeper viewStorekeeper=new ViewStorekeeper();
		viewStorekeeper.startSystem();
	}

	public void storekeeperPanelButtonClicked(String loginID)
	{
		//this.setVisible(false);
		//close();
		StorekeeperFrame storekeeperFrame = new StorekeeperFrame(databaseID,databasePassword);
		storekeeperFrame.startSystem(loginID, 0, 1);
	}

	public void actionPerformed(ActionEvent event)
	{
		JButton sourceButton = (JButton) event.getSource();
		String query, dateTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		if (sourceButton == registerDialogButton)
		{
			try
			{
				String storekeeperID = idAddNewTextField.getText(), storekeeperFname = fnameAddNewTextField.getText(), storekeeperLname = lnameAddNewTextField.getText(), storekeeperAddress = addressAddNewTextField.getText(), storekeeperPhone = phoneAddNewTextField.getText();
				String plainPassword = passwordAddNewField.getText();
				StringBuffer storekeeperPassword = shaFunction(plainPassword);
				String preparedPassword=new String(storekeeperPassword);
				statement.executeUpdate("USE grocerydesk;");
				System.out.println(storekeeperPassword);
				query = "INSERT INTO storekeeper(id,password,fname,lname,address,phone,added_in,added_by) VALUES(?,?,?,?,?,?,?,?);";
				pstatement=connection.prepareStatement(query);
				pstatement.setString(1,storekeeperID);
				pstatement.setString(2,preparedPassword);
				pstatement.setString(3,storekeeperFname);
				pstatement.setString(4,storekeeperLname);
				pstatement.setString(5,storekeeperAddress);
				pstatement.setString(6,storekeeperPhone);
				pstatement.setString(7,dateTimeStamp);
				pstatement.setString(8,loginID);

				int rowsAffected = pstatement.executeUpdate();
				if (rowsAffected > 0)
				{
					JOptionPane.showMessageDialog(this, "New Storekeeper Added!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
					addStorekeeperDialog.dispose();
				} else
					JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);

			} catch (SQLIntegrityConstraintViolationException e)
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

