package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;

public class StorekeeperFrame extends JFrame implements ActionListener, DocumentListener, ItemListener
{
	static String loginID,posCustomerID,creditCustomerID;
	static float creditsDue,amount;
	static boolean creditsDueFilled=false;
	static int transactionQuantityRows,transactionAmountRows;

	/*----------------------------------- Declarations and Prerequisites----------------------------------- */
	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost/?useSSL=true";
	String databaseID="root";
	String databasePassword="";
	Connection connection;
	Statement statement;
	ResultSet resultSet;
	ResultSetMetaData meta;

	/* Dimension and Fonts */
	Dimension textFieldDimension;
	Font textFieldFont,labelFont;

	/* For Layouts */
	SpringLayout frameLayout,posTabLayout,creditTabLayout,dialogLayout,productDialogLayout,rowInfoDialogLayout;

	/* For Content Pane */
	Container frameContentPane,dialogContentPane,productDialogContentPane,rowInfoDialogContentPane;

	/* For Components */
	JMenuBar mainMenuBar;
	JMenu aboutMenu,settingsMenu;
	JMenuItem aboutProjectMenuItem,logoutMenuItem;
	JTabbedPane storekeeperPanelTab;
	JPanel storekeeperPanelPos,storekeeperPanelCreditAccounts;
	JLabel storekeeperWelcomeLabel,productIDLabel,quantityLabel,posTransactionLabel,creditsDueLabel,posCustomerIDLabel,amountLabel,orLabel,creditCustomerIDLabel,creditTransactionLabel,idAddNewLabel,fnameAddNewLabel,lnameAddNewLabel,addressAddNewLabel,phoneAddNewLabel,confirmLogoutMessage,productIDAddNewLabel,productNameAddNewLabel,rateAddNewLabel,addedInLabel,addedByLabel,lastUpdatedInLabel,lastUpdatedByLabel;
	JTextField productIDTextField,quantityTextField,creditsDueTextField,posCustomerIDTextField,creditCustomerIDTextField,amountTextField,idAddNewTextField,fnameAddNewTextField,lnameAddNewTextField,addressAddNewTextField,phoneAddNewTextField,productIDAddNewTextField,productNameAddNewTextField,rateAddNewTextField;
	JButton processTransactionButton,addNewButton,processCreditsButton,addNewProductButton,confirmNewProductButton,registerDialogButton,cancelDialogButton,confirmLogoutButton,cancelLogoutButton,rowInfoOKButton,cancelNewProductButton;
	JRadioButton saleRadioButton,receiptRadioButton,creditRadioButton,debitRadioButton;
	ButtonGroup creditTransactionButtonGroup,posTransactionButtonGroup;
	JTable inventoryTable,creditAccountsTable;
	JScrollPane inventoryTableSp,creditAccountsTableSp;
	JDialog addNewDialog,rowInfoDialog,confirmLogoutDialog,addNewProductDialog;
	DefaultTableModel inventoryTableModel,creditAccountsTableModel;

	public static void main(String[] args)
	{
		final StorekeeperFrame storekeeperFrame = new StorekeeperFrame();
	}

	public StorekeeperFrame()
	{
		/* Initialize JDBC */
		try
		{
			connection = DriverManager.getConnection(databaseURL, databaseID, databasePassword);
			statement = connection.createStatement();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Connection to database couldn't be established!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		/* Dimension and Fonts */
		textFieldDimension=new Dimension(200,30);
		textFieldFont=new Font("Verdana",Font.PLAIN,12);
		labelFont=new Font("Segoe UI",Font.BOLD,11);

		/* For Layout */
		frameContentPane=this.getContentPane();
		frameLayout=new SpringLayout();
		posTabLayout=new SpringLayout();
		creditTabLayout=new SpringLayout();
		dialogLayout=new SpringLayout();
		productDialogLayout=new SpringLayout();
		rowInfoDialogLayout=new SpringLayout();

		/* Menu definitions */
		mainMenuBar=new JMenuBar();
		aboutMenu=new JMenu("About");
		settingsMenu=new JMenu("Settings");
		aboutProjectMenuItem=new JMenuItem("Project");
		logoutMenuItem=new JMenuItem("Logout");

		/* Default Table Models' definitions */
		inventoryTableModel=new DefaultTableModel();
		creditAccountsTableModel=new DefaultTableModel();

		/* JTable definitions */
		inventoryTable=new JTable();
		creditAccountsTable=new JTable();

		/* JScrollPane definitions */
		creditAccountsTableSp=new JScrollPane();
		inventoryTableSp=new JScrollPane();

		/* JDialog definition */
		addNewDialog=new JDialog(this,"GroceryDesk- New Customer Account",true);
		rowInfoDialog=new JDialog(this,"GroceryDesk- Row Information",true);
		confirmLogoutDialog=new JDialog(this,"GroceryDesk- Confirm Logout",true);
		addNewProductDialog=new JDialog(this,"GroceryDesk- Add New Product",true);

		/* Other definitions */
		storekeeperWelcomeLabel = new JLabel();
		storekeeperPanelPos = new JPanel();
		storekeeperPanelCreditAccounts = new JPanel();
		storekeeperPanelTab = new JTabbedPane();
		storekeeperPanelTab.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		storekeeperPanelTab.setTabPlacement(JTabbedPane.TOP);
		productIDLabel=new JLabel("Product ID");
		quantityLabel=new JLabel("Quantity");
		posTransactionLabel=new JLabel("Transaction");
		creditsDueLabel=new JLabel("Credits due");
		posCustomerIDLabel=new JLabel("Customer ID");
		amountLabel=new JLabel("Amount");
		orLabel=new JLabel("Or");
		creditCustomerIDLabel=new JLabel("Customer ID");
		creditTransactionLabel=new JLabel("Transaction");
		idAddNewLabel=new JLabel("Customer ID");
		fnameAddNewLabel=new JLabel("First Name");
		lnameAddNewLabel=new JLabel("Last Name");
		addressAddNewLabel=new JLabel("Address");
		phoneAddNewLabel=new JLabel("Phone");
		confirmLogoutMessage=new JLabel("Are you sure you want to logout?");
		productIDAddNewLabel=new JLabel("Product ID");
		productNameAddNewLabel=new JLabel("Product Name");
		rateAddNewLabel=new JLabel("Rate");
		addedInLabel=new JLabel("Added In: ");
		addedByLabel=new JLabel("Added By: ");
		lastUpdatedInLabel=new JLabel("Last Updated In: ");
		lastUpdatedByLabel=new JLabel("Last Updated By: ");
		productIDTextField=new JTextField();
		quantityTextField=new JTextField();
		creditsDueTextField=new JTextField();
		posCustomerIDTextField=new JTextField();
		creditCustomerIDTextField=new JTextField();
		amountTextField=new JTextField();
		idAddNewTextField=new JTextField();
		fnameAddNewTextField=new JTextField();
		lnameAddNewTextField=new JTextField();
		addressAddNewTextField=new JTextField();
		phoneAddNewTextField=new JTextField();
		productIDAddNewTextField=new JTextField();
		productNameAddNewTextField=new JTextField();
		rateAddNewTextField=new JTextField();
		processTransactionButton=new JButton("Process Transaction");
		addNewButton=new JButton("Add New");
		registerDialogButton=new JButton("Register Storekeeper");
		cancelDialogButton=new JButton("Cancel");
		processCreditsButton=new JButton("Process Credits");
		confirmLogoutButton=new JButton("Yes");
		cancelLogoutButton=new JButton("Cancel");
		rowInfoOKButton=new JButton("OK");
		addNewProductButton=new JButton("New Product");
		confirmNewProductButton=new JButton("Confirm");
		cancelNewProductButton=new JButton("Cancel");
		saleRadioButton=new JRadioButton("Sale",true);
		receiptRadioButton=new JRadioButton("Receipt");
		creditRadioButton=new JRadioButton("Credit",true);
		debitRadioButton=new JRadioButton("Debit");
		creditTransactionButtonGroup=new ButtonGroup();
		posTransactionButtonGroup=new ButtonGroup();

		//TODO
		//(DELETE AFTER TESTING)For testing this frame without logging in:
		//Causes double operations, as this method is called two times- By the LoginFrame and by the Constructor.
		//startSystem("demo",0);

	}

	/* To set attributes to the components */
	public void startSystem(String loginID,int index)
	{
		String loggedInTimeStamp = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());
		StorekeeperFrame.loginID =loginID; //Referencing without the use of 'this' operator. Static loginID referenced directly via the class.
		setInventoryTable();
		setCreditAccountsTable();

		/* Components of Menu */
		aboutMenu.add(aboutProjectMenuItem);
		settingsMenu.add(logoutMenuItem);
		mainMenuBar.add(aboutMenu);
		mainMenuBar.add(settingsMenu);

		/* Other attributes */
		storekeeperWelcomeLabel.setFont(new Font("Segoe UI",Font.BOLD,12));
		storekeeperWelcomeLabel.setText("Logged in: "+loginID+" at "+loggedInTimeStamp);
		storekeeperWelcomeLabel.setOpaque(false);
		storekeeperWelcomeLabel.setForeground(new Color(26, 188, 156));

		productIDLabel.setFont(labelFont);
		productIDLabel.setForeground(Color.WHITE);
		
		quantityLabel.setFont(labelFont);
		quantityLabel.setForeground(Color.WHITE);
		
		posTransactionLabel.setFont(labelFont);
		posTransactionLabel.setForeground(Color.WHITE);
		
		creditsDueLabel.setFont(labelFont);
		creditsDueLabel.setForeground(Color.WHITE);
		
		posCustomerIDLabel.setFont(labelFont);
		posCustomerIDLabel.setForeground(Color.WHITE);
		
		amountLabel.setFont(labelFont);
		amountLabel.setForeground(Color.WHITE);
		
		orLabel.setFont(labelFont);
		orLabel.setForeground(Color.WHITE);
		
		creditCustomerIDLabel.setFont(labelFont);
		creditCustomerIDLabel.setForeground(Color.WHITE);
		
		creditTransactionLabel.setFont(labelFont);
		creditTransactionLabel.setForeground(Color.WHITE);

		productIDTextField.setOpaque(true);
		productIDTextField.setForeground(Color.BLACK);
		productIDTextField.setPreferredSize(textFieldDimension);
		productIDTextField.setFont(textFieldFont);

		quantityTextField.setOpaque(true);
		quantityTextField.setForeground(Color.BLACK);
		quantityTextField.setPreferredSize(textFieldDimension);
		quantityTextField.setFont(textFieldFont);

		creditsDueTextField.setOpaque(true);
		creditsDueTextField.setForeground(Color.BLACK);
		creditsDueTextField.setPreferredSize(textFieldDimension);
		creditsDueTextField.setFont(textFieldFont);

		posCustomerIDTextField.setOpaque(true);
		posCustomerIDTextField.setForeground(Color.BLACK);
		posCustomerIDTextField.setPreferredSize(textFieldDimension);
		posCustomerIDTextField.setFont(textFieldFont);
		if(posCustomerIDTextField.isEditable())
			posCustomerIDTextField.setBackground(new Color(189, 195, 199));

		creditCustomerIDTextField.setOpaque(true);
		creditCustomerIDTextField.setForeground(Color.BLACK);
		creditCustomerIDTextField.setPreferredSize(textFieldDimension);
		creditCustomerIDTextField.setFont(textFieldFont);

		amountTextField.setOpaque(true);
		amountTextField.setForeground(Color.BLACK);
		amountTextField.setPreferredSize(textFieldDimension);
		amountTextField.setFont(textFieldFont);

		processTransactionButton.setForeground(new Color(26, 188, 156));
		processTransactionButton.setBackground(Color.WHITE);
		processTransactionButton.setBorderPainted(false);
		processTransactionButton.setFocusPainted(false);
		processTransactionButton.setOpaque(true);

		addNewProductButton.setForeground(new Color(26, 188, 156));
		addNewProductButton.setBackground(Color.WHITE);
		addNewProductButton.setBorderPainted(false);
		addNewProductButton.setFocusPainted(false);
		addNewProductButton.setOpaque(true);

		confirmNewProductButton.setForeground(Color.WHITE);
		confirmNewProductButton.setBackground(new Color(26, 188, 156));
		confirmNewProductButton.setFocusPainted(false);

		//cancelNewProductButton.setForeground(Color.WHITE);
		//cancelNewProductButton.setBackground(Color.RED);
		cancelNewProductButton.setFocusPainted(false);

		processCreditsButton.setForeground(new Color(26, 188, 156));
		processCreditsButton.setBackground(Color.WHITE);
		processCreditsButton.setBorderPainted(false);
		processCreditsButton.setFocusPainted(false);
		processCreditsButton.setOpaque(true);

		addNewButton.setForeground(new Color(26, 188, 156));
		addNewButton.setBackground(Color.WHITE);
		addNewButton.setBorderPainted(false);
		addNewButton.setFocusPainted(false);
		addNewButton.setOpaque(true);

		confirmLogoutButton.setForeground(Color.WHITE);
		confirmLogoutButton.setBackground(new Color(26, 188, 156));
		confirmLogoutButton.setBorderPainted(false);
		confirmLogoutButton.setFocusPainted(false);
		confirmLogoutButton.setOpaque(true);

		cancelLogoutButton.setForeground(new Color(26, 188, 156));
		cancelLogoutButton.setBackground(Color.WHITE);
		cancelLogoutButton.setBorderPainted(false);
		cancelLogoutButton.setFocusPainted(false);
		cancelLogoutButton.setOpaque(true);

		posCustomerIDTextField.setEditable(false);

		saleRadioButton.setOpaque(false);
		saleRadioButton.setFocusPainted(false);
		saleRadioButton.setFont(new Font("Segoe UI",Font.PLAIN,11));
		saleRadioButton.setForeground(Color.WHITE);

		receiptRadioButton.setFocusPainted(false);
		receiptRadioButton.setOpaque(false);
		receiptRadioButton.setFont(new Font("Segoe UI",Font.PLAIN,11));
		receiptRadioButton.setForeground(Color.WHITE);

		creditRadioButton.setOpaque(false);
		creditRadioButton.setFocusPainted(false);
		creditRadioButton.setFont(new Font("Segoe UI",Font.PLAIN,11));
		creditRadioButton.setForeground(Color.WHITE);

		debitRadioButton.setOpaque(false);
		debitRadioButton.setFocusPainted(false);
		debitRadioButton.setFont(new Font("Segoe UI",Font.PLAIN,11));
		debitRadioButton.setForeground(Color.WHITE);

		posTransactionButtonGroup.add(saleRadioButton);
		posTransactionButtonGroup.add(receiptRadioButton);

		creditTransactionButtonGroup.add(creditRadioButton);
		creditTransactionButtonGroup.add(debitRadioButton);

		idAddNewTextField.setPreferredSize(textFieldDimension);
		fnameAddNewTextField.setPreferredSize(textFieldDimension);
		lnameAddNewTextField.setPreferredSize(textFieldDimension);
		addressAddNewTextField.setPreferredSize(textFieldDimension);
		phoneAddNewTextField.setPreferredSize(textFieldDimension);
		
		productIDAddNewTextField.setPreferredSize(textFieldDimension);
		productNameAddNewTextField.setPreferredSize(textFieldDimension);
		rateAddNewTextField.setPreferredSize(textFieldDimension);

		registerDialogButton.setFocusPainted(false);
		registerDialogButton.setBorderPainted(false);
		registerDialogButton.setBackground(new Color(26, 188, 156));
		registerDialogButton.setForeground(Color.WHITE);

		cancelDialogButton.setFocusPainted(false);
		cancelDialogButton.setBorderPainted(false);
		cancelDialogButton.setBackground(new Color(26, 188, 156));
		cancelDialogButton.setForeground(Color.WHITE);

		/*------------------------------------------------storekeeperPanelPos-----------------------------------------*/

		/* storekeeperPanelPos attributes */
		storekeeperPanelPos.setLayout(posTabLayout);
		storekeeperPanelPos.setOpaque(true);
		storekeeperPanelPos.setBackground(new Color(26, 188, 156));

		/* Components for storekeeperPanelPos */
		storekeeperPanelPos.add(inventoryTable);
		storekeeperPanelPos.add(productIDLabel);
		storekeeperPanelPos.add(productIDTextField);
		storekeeperPanelPos.add(quantityLabel);
		storekeeperPanelPos.add(quantityTextField);
		storekeeperPanelPos.add(posTransactionLabel);
		storekeeperPanelPos.add(saleRadioButton);
		storekeeperPanelPos.add(receiptRadioButton);
		storekeeperPanelPos.add(creditsDueLabel);
		storekeeperPanelPos.add(creditsDueTextField);
		storekeeperPanelPos.add(posCustomerIDLabel);
		storekeeperPanelPos.add(posCustomerIDTextField);
		storekeeperPanelPos.add(addNewProductButton);
		storekeeperPanelPos.add(processTransactionButton);
		storekeeperPanelPos.add(inventoryTableSp);

		inventoryTableSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		inventoryTableSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		inventoryTableSp.setViewportView(inventoryTable);

		/* Components of storekeeperFrame */
		this.setJMenuBar(mainMenuBar);
		this.add(storekeeperWelcomeLabel);
		this.add(storekeeperPanelTab);

		/* Adding Window Listener */
		this.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowGainedFocus(WindowEvent e)
			{
				super.windowGainedFocus(e);
				windowFocusedEvent();
			}
		});

		/* Components for JTabbedPane */
		storekeeperPanelTab.addTab("POS", storekeeperPanelPos);
		storekeeperPanelTab.addTab("Credit Accounts", storekeeperPanelCreditAccounts);
		processTransactionButton.setPreferredSize(new Dimension(150,30));
		addNewProductButton.setPreferredSize(new Dimension(150,30));
		processCreditsButton.setPreferredSize(new Dimension(150,30));

		/* Attributes for JTabbedPane */
		storekeeperPanelTab.setFocusable(false);
		storekeeperPanelTab.setSelectedIndex(index);

		/* Adding Action Listeners */

		//Logout Menu Item
		logoutMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				logoutMenuItemClicked();
			}
		});
		confirmLogoutButton.addActionListener(this);
		cancelLogoutButton.addActionListener(this);
		rowInfoOKButton.addActionListener(this);

		/* Constraints for Components */
		frameLayout.putConstraint(SpringLayout.NORTH,storekeeperWelcomeLabel,10,SpringLayout.NORTH,frameContentPane);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperWelcomeLabel,10,SpringLayout.WEST,frameContentPane);
		frameLayout.putConstraint(SpringLayout.NORTH,storekeeperPanelTab,-10,SpringLayout.SOUTH,storekeeperWelcomeLabel);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperPanelTab,0,SpringLayout.WEST,frameContentPane);
		frameLayout.putConstraint(SpringLayout.EAST,storekeeperPanelTab,689,SpringLayout.EAST,productIDTextField);
		frameLayout.putConstraint(SpringLayout.SOUTH,storekeeperPanelTab,0,SpringLayout.SOUTH,frameContentPane);

		posTabLayout.putConstraint(SpringLayout.NORTH,productIDLabel,25,SpringLayout.NORTH,frameContentPane);
		posTabLayout.putConstraint(SpringLayout.WEST,productIDLabel,110,SpringLayout.WEST,frameContentPane);

		posTabLayout.putConstraint(SpringLayout.NORTH,quantityLabel,50,SpringLayout.SOUTH,productIDLabel);
		posTabLayout.putConstraint(SpringLayout.EAST,quantityLabel,0,SpringLayout.EAST,productIDLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,posTransactionLabel,50,SpringLayout.SOUTH,quantityLabel);
		posTabLayout.putConstraint(SpringLayout.EAST,posTransactionLabel,0,SpringLayout.EAST,productIDLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,productIDTextField,-6,SpringLayout.NORTH,productIDLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,productIDTextField,30,SpringLayout.EAST,productIDLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,quantityTextField,-6,SpringLayout.NORTH,quantityLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,quantityTextField,30,SpringLayout.EAST,quantityLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,saleRadioButton,-6,SpringLayout.NORTH,posTransactionLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,saleRadioButton,0,SpringLayout.WEST,quantityTextField);

		posTabLayout.putConstraint(SpringLayout.NORTH,receiptRadioButton,10,SpringLayout.SOUTH,saleRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,receiptRadioButton,0,SpringLayout.WEST,saleRadioButton);

		posTabLayout.putConstraint(SpringLayout.NORTH,creditsDueLabel,0,SpringLayout.NORTH,productIDLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,creditsDueLabel,100,SpringLayout.EAST,productIDTextField);

		posTabLayout.putConstraint(SpringLayout.NORTH,posCustomerIDLabel,0,SpringLayout.NORTH,quantityLabel);
		posTabLayout.putConstraint(SpringLayout.EAST,posCustomerIDLabel,0,SpringLayout.EAST,creditsDueLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,creditsDueTextField,0,SpringLayout.NORTH,productIDTextField);
		posTabLayout.putConstraint(SpringLayout.WEST,creditsDueTextField,30,SpringLayout.EAST,creditsDueLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,posCustomerIDTextField,0,SpringLayout.NORTH,quantityTextField);
		posTabLayout.putConstraint(SpringLayout.WEST,posCustomerIDTextField,0,SpringLayout.WEST,creditsDueTextField);

		posTabLayout.putConstraint(SpringLayout.SOUTH,addNewProductButton,0,SpringLayout.SOUTH,receiptRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,addNewProductButton,100,SpringLayout.EAST,quantityTextField);

		posTabLayout.putConstraint(SpringLayout.SOUTH,processTransactionButton,0,SpringLayout.SOUTH,receiptRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,processTransactionButton,50,SpringLayout.EAST,addNewProductButton);

		posTabLayout.putConstraint(SpringLayout.NORTH,inventoryTableSp,10,SpringLayout.SOUTH,receiptRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,inventoryTableSp,-1,SpringLayout.WEST,frameContentPane);
		posTabLayout.putConstraint(SpringLayout.EAST,inventoryTableSp,40,SpringLayout.EAST,processTransactionButton);
		posTabLayout.putConstraint(SpringLayout.SOUTH,inventoryTableSp,395,SpringLayout.SOUTH,receiptRadioButton);

		/*---------------------------------------StorekeeperPanelCreditAccounts---------------------------------------*/

		storekeeperPanelCreditAccounts.setLayout(creditTabLayout);
		storekeeperPanelCreditAccounts.setBackground(new Color(26, 188, 156));

		/* Components for storekeeperPanelCreditAccounts */
		storekeeperPanelCreditAccounts.add(creditCustomerIDLabel);
		storekeeperPanelCreditAccounts.add(creditCustomerIDTextField);
		storekeeperPanelCreditAccounts.add(orLabel);
		storekeeperPanelCreditAccounts.add(addNewButton);
		storekeeperPanelCreditAccounts.add(amountLabel);
		storekeeperPanelCreditAccounts.add(amountTextField);
		storekeeperPanelCreditAccounts.add(creditTransactionLabel);
		storekeeperPanelCreditAccounts.add(creditRadioButton);
		storekeeperPanelCreditAccounts.add(debitRadioButton);
		storekeeperPanelCreditAccounts.add(processCreditsButton);
		storekeeperPanelCreditAccounts.add(creditAccountsTableSp);

		creditAccountsTableSp.setViewportView(creditAccountsTable);

		/* Adding Event Listeners */
		receiptRadioButton.addItemListener(this);
		processTransactionButton.addActionListener(this);
		creditsDueTextField.getDocument().addDocumentListener(this);
		processCreditsButton.addActionListener(this);
		addNewButton.addActionListener(this);
		addNewProductButton.addActionListener(this);
		confirmNewProductButton.addActionListener(this);
		cancelNewProductButton.addActionListener(this);

		/* Constraints for Layout */
		creditTabLayout.putConstraint(SpringLayout.NORTH,creditCustomerIDLabel,25,SpringLayout.NORTH,frameContentPane);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditCustomerIDLabel,100,SpringLayout.WEST,frameContentPane);

		creditTabLayout.putConstraint(SpringLayout.NORTH,orLabel,50,SpringLayout.SOUTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.EAST,orLabel,0,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,addNewButton,-6,SpringLayout.NORTH,orLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,addNewButton,30,SpringLayout.EAST,orLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,amountLabel,50,SpringLayout.SOUTH,orLabel);
		creditTabLayout.putConstraint(SpringLayout.EAST,amountLabel,0,SpringLayout.EAST,orLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH, creditCustomerIDTextField,-6,SpringLayout.NORTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditCustomerIDTextField,30,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH, amountTextField,-6,SpringLayout.NORTH,amountLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,amountTextField,0,SpringLayout.WEST,creditCustomerIDTextField);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditTransactionLabel,0,SpringLayout.NORTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditTransactionLabel,325,SpringLayout.EAST,addNewButton);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditRadioButton,-6,SpringLayout.NORTH,creditTransactionLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditRadioButton,30,SpringLayout.EAST,creditTransactionLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,debitRadioButton,10,SpringLayout.SOUTH,creditRadioButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,debitRadioButton,0,SpringLayout.WEST,creditRadioButton);

		creditTabLayout.putConstraint(SpringLayout.SOUTH,processCreditsButton,25,SpringLayout.SOUTH,amountTextField);
		creditTabLayout.putConstraint(SpringLayout.WEST,processCreditsButton,-10,SpringLayout.WEST,creditTransactionLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditAccountsTableSp,10,SpringLayout.SOUTH,processCreditsButton);
		creditTabLayout.putConstraint(SpringLayout.SOUTH,creditAccountsTableSp,449,SpringLayout.SOUTH,processCreditsButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditAccountsTableSp,-1,SpringLayout.WEST,frameContentPane);
		creditTabLayout.putConstraint(SpringLayout.EAST,creditAccountsTableSp,160,SpringLayout.EAST,processCreditsButton);

		/*------------------------------------------------AddNewDialog------------------------------------------------*/

		dialogContentPane=addNewDialog.getContentPane();
		addNewDialog.setLayout(dialogLayout);
		addNewDialog.setSize(400,400);
		addNewDialog.setLocationRelativeTo(this);
		addNewDialog.setResizable(false);

		/* Components of addNewDialog */
		addNewDialog.add(idAddNewLabel);
		addNewDialog.add(fnameAddNewLabel);
		addNewDialog.add(lnameAddNewLabel);
		addNewDialog.add(addressAddNewLabel);
		addNewDialog.add(phoneAddNewLabel);
		addNewDialog.add(idAddNewTextField);
		addNewDialog.add(fnameAddNewTextField);
		addNewDialog.add(lnameAddNewTextField);
		addNewDialog.add(addressAddNewTextField);
		addNewDialog.add(phoneAddNewTextField);
		addNewDialog.add(registerDialogButton);
		addNewDialog.add(cancelDialogButton);

		/* Attributes of addNewDialog */
		addNewDialog.setUndecorated(true);
		addNewDialog.setBackground(Color.WHITE);
		addNewDialog.setOpacity(0.95f);

		/* Adding Event Listeners */
		registerDialogButton.addActionListener(this);
		cancelDialogButton.addActionListener(this);

		/* Constraints for Layout */
		dialogLayout.putConstraint(SpringLayout.NORTH,idAddNewLabel,50,SpringLayout.NORTH,dialogContentPane);
		dialogLayout.putConstraint(SpringLayout.WEST,idAddNewLabel,35,SpringLayout.WEST,dialogContentPane);

		dialogLayout.putConstraint(SpringLayout.NORTH,fnameAddNewLabel,40,SpringLayout.SOUTH,idAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,fnameAddNewLabel,0,SpringLayout.EAST,idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,lnameAddNewLabel,40,SpringLayout.SOUTH,fnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,lnameAddNewLabel,0,SpringLayout.EAST,fnameAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,addressAddNewLabel,40,SpringLayout.SOUTH,lnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,addressAddNewLabel,0,SpringLayout.EAST,lnameAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,phoneAddNewLabel,40,SpringLayout.SOUTH,addressAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,phoneAddNewLabel,0,SpringLayout.EAST,addressAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,idAddNewTextField,-3,SpringLayout.NORTH,idAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,idAddNewTextField,30,SpringLayout.EAST,idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,fnameAddNewTextField,-3,SpringLayout.NORTH,fnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,fnameAddNewTextField,0,SpringLayout.WEST,idAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,lnameAddNewTextField,-3,SpringLayout.NORTH,lnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,lnameAddNewTextField,0,SpringLayout.WEST,fnameAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,addressAddNewTextField,-3,SpringLayout.NORTH,addressAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,addressAddNewTextField,0,SpringLayout.WEST,lnameAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,phoneAddNewTextField,-3,SpringLayout.NORTH,phoneAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,phoneAddNewTextField,0,SpringLayout.WEST,addressAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,registerDialogButton,40,SpringLayout.SOUTH,phoneAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,registerDialogButton,10, SpringLayout.WEST,phoneAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,cancelDialogButton,0,SpringLayout.NORTH,registerDialogButton);
		dialogLayout.putConstraint(SpringLayout.WEST,cancelDialogButton,50,SpringLayout.EAST,registerDialogButton);

		/*---------------------------------------------confirmLogoutDialog--------------------------------------------*/

		/* Components of confirmLogoutDialog */
		confirmLogoutDialog.add(confirmLogoutMessage);
		confirmLogoutDialog.add(confirmLogoutButton);
		confirmLogoutDialog.add(cancelLogoutButton);

		/* Attributes of confirmLogoutDialog */
		confirmLogoutDialog.setSize(300,150);
		confirmLogoutDialog.setUndecorated(true);
		confirmLogoutDialog.setOpacity(0.95f);
		confirmLogoutDialog.setLocationRelativeTo(null);
		confirmLogoutDialog.setLayout(new GridLayout(0,1));

		/*------------------------------------------------addNewProductDialog------------------------------------------------*/

		productDialogContentPane=addNewProductDialog.getContentPane();
		addNewProductDialog.setLayout(productDialogLayout);
		addNewProductDialog.setSize(400,280);
		addNewProductDialog.setLocationRelativeTo(this);
		addNewProductDialog.setResizable(false);

		/* Components of addNewProductDialog */
		addNewProductDialog.add(productIDAddNewLabel);
		addNewProductDialog.add(productNameAddNewLabel);
		addNewProductDialog.add(rateAddNewLabel);
		addNewProductDialog.add(productIDAddNewTextField);
		addNewProductDialog.add(productNameAddNewTextField);
		addNewProductDialog.add(rateAddNewTextField);
		addNewProductDialog.add(confirmNewProductButton);
		addNewProductDialog.add(cancelNewProductButton);

		/* Attributes of addNewProductDialog */
		addNewProductDialog.setUndecorated(true);
		addNewProductDialog.setBackground(Color.WHITE);
		addNewProductDialog.setOpacity(0.95f);

		/* Adding Event Listeners */


		/* Constraints for Layout */
		productDialogLayout.putConstraint(SpringLayout.NORTH,productIDAddNewLabel,50,SpringLayout.NORTH,productDialogContentPane);
		productDialogLayout.putConstraint(SpringLayout.WEST,productIDAddNewLabel,60,SpringLayout.WEST,productDialogContentPane);

		productDialogLayout.putConstraint(SpringLayout.NORTH,productNameAddNewLabel,40,SpringLayout.SOUTH,productIDAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.EAST,productNameAddNewLabel,0,SpringLayout.EAST,productIDAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,rateAddNewLabel,40,SpringLayout.SOUTH,productNameAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.EAST,rateAddNewLabel,0,SpringLayout.EAST,productNameAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,productIDAddNewTextField,-3,SpringLayout.NORTH,productIDAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,productIDAddNewTextField,30,SpringLayout.EAST,productIDAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,productNameAddNewTextField,-3,SpringLayout.NORTH,productNameAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,productNameAddNewTextField,30,SpringLayout.EAST,productNameAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,rateAddNewTextField,-3,SpringLayout.NORTH,rateAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,rateAddNewTextField,30,SpringLayout.EAST,rateAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,confirmNewProductButton,40,SpringLayout.SOUTH,rateAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,confirmNewProductButton,10, SpringLayout.WEST,rateAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,cancelNewProductButton,0,SpringLayout.NORTH,confirmNewProductButton);
		productDialogLayout.putConstraint(SpringLayout.WEST,cancelNewProductButton,50,SpringLayout.EAST,confirmNewProductButton);


		/*------------------------------------------------rowInfoDialog-----------------------------------------------*/
		rowInfoDialogContentPane=this.getContentPane();
		rowInfoDialog.setUndecorated(true);
		rowInfoDialog.setOpacity(0.95f);
		rowInfoDialog.setLayout(new GridLayout(0,1));
		rowInfoDialog.setSize(400,200);
		rowInfoDialog.setLocationRelativeTo(null);

		// Components of rowInfoDialog
		rowInfoDialog.add(addedInLabel);
		rowInfoDialog.add(addedByLabel);
		rowInfoDialog.add(lastUpdatedInLabel);
		rowInfoDialog.add(lastUpdatedByLabel);
		rowInfoDialog.add(rowInfoOKButton);

		rowInfoOKButton.setFocusPainted(false);

		/* Attributes of storekeeperFrame */
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Windows Look and Feel not found!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}
		SwingUtilities.updateComponentTreeUI(this);

		this.setTitle("GroceryDesk- Storekeeper Panel");
		this.getContentPane().setBackground(Color.WHITE);
		this.setSize(900, 700);
		this.setResizable(false);
		this.setLayout(frameLayout);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Setting default buttons */
		storekeeperPanelPos.getRootPane().setDefaultButton(processTransactionButton);
		storekeeperPanelCreditAccounts.getRootPane().setDefaultButton(processCreditsButton);

	}

	public void setInventoryTable()
	{
		/* ------------------------------------------Retrieving Table Data------------------------------------------- */

		try
		{
			statement.executeUpdate("USE grocerydesk;");
			resultSet=statement.executeQuery("SELECT * FROM inventory;");
			meta=resultSet.getMetaData();
			int columnCount=meta.getColumnCount();
			//System.out.println(columnCount);

			/* TO GET COLUMN NAMES FOR Inventory */
			String[] columnNames=new String[columnCount];

			for(int count=0;count<=columnCount-1;count++)
			{
				columnNames[count]=meta.getColumnName(count+1);
			}
			inventoryTableModel.setColumnIdentifiers(columnNames);

			/* TO GET ROWS FOR INVENTORY */
			String[] rowData=new String[columnCount];
			while(resultSet.next())
			{
				for(int count=0;count<=columnCount-1;count++)
				{
					rowData[count]=resultSet.getString(count+1);
				}
				inventoryTableModel.addRow(rowData);
			}
			inventoryTable.setModel(inventoryTableModel);
			//inventoryTable.setShowGrid(false);
			inventoryTable.setBackground(new Color(236, 240, 241));
			inventoryTable.setSelectionBackground(new Color(26, 188, 156));
			inventoryTable.setFont(new Font("Segoe UI",Font.PLAIN,11));
			inventoryTable.setFocusable(false);
			inventoryTable.setGridColor(Color.WHITE);
			inventoryTable.setDefaultEditor(Object.class,null);

			/* Adding Mouse Listener */
			inventoryTable.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					super.mouseReleased(e);
					if((e.getClickCount())==2 && !e.isConsumed())
					{
						showRowInfoDialog(e);
						e.consume(); // If removed, causes the poppedup dialog box to remain visible for some clicks even after clicking OK Button
					}
				}
			});
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Data couldn't be retrieved from the database!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setCreditAccountsTable()
	{
		try
		{
			statement.executeUpdate("USE grocerydesk;");
			resultSet = statement.executeQuery("SELECT * FROM customer;");
			meta = resultSet.getMetaData();
			int columnCount = meta.getColumnCount();
			System.out.println(columnCount);

			/* TO GET COLUMN NAMES FOR Inventory */
			String[] columnNames = new String[columnCount];

			for (int count = 0; count <= columnCount - 1; count++)
			{
				columnNames[count] = meta.getColumnName(count + 1);
			}
			creditAccountsTableModel.setColumnIdentifiers(columnNames);

			/* TO GET ROWS FOR INVENTORY */
			String[] rowData = new String[columnCount];
			while (resultSet.next())
			{
				for (int count = 0; count <= columnCount - 1; count++)
				{
					rowData[count] = resultSet.getString(count + 1);
				}
				creditAccountsTableModel.addRow(rowData);
			}
			creditAccountsTable.setModel(creditAccountsTableModel);
			creditAccountsTable.setBackground(new Color(236, 240, 241));
			creditAccountsTable.setSelectionBackground(new Color(26, 188, 156));
			creditAccountsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			creditAccountsTable.setFocusable(false);
			creditAccountsTable.setGridColor(Color.WHITE);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Data couldn't be retrieved from the database!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void actionPerformed(ActionEvent event)
	{
		String productID,query ;
		int quantity,quantityFromTable = 0,newQuantity=0;
		float newCreditsDue,creditsDueFromTable = 0;
		try
		{
			JButton sourceButton = (JButton) event.getSource();

			/*---------------------------------------------Process Transaction--------------------------------------------*/

			if (sourceButton == processTransactionButton)
			{
				productID = productIDTextField.getText();
				quantity = Integer.parseInt(quantityTextField.getText());
				try
				{
					query = "SELECT * FROM inventory WHERE BINARY id='" + productID + "';";
					resultSet = statement.executeQuery(query);

					/* -------------------------------------Sale or Receipt of Product----------------------------------- */

					if (resultSet.next())
					{
						quantityFromTable = Integer.parseInt(resultSet.getString("quantity"));
					} else
						System.out.println("Product is not available in the inventory!");

					if (saleRadioButton.isSelected())
					{

						if (quantityFromTable > quantity)
						{
							newQuantity = quantityFromTable - quantity;
						} else
							System.out.println("Out of Stock!");

						if (!creditsDueFilled)
						{
							query = "UPDATE inventory SET quantity='" + newQuantity + "' WHERE id='" + productID + "';";
							transactionQuantityRows=statement.executeUpdate(query);
						}
						if (creditsDueFilled)
						{
							creditsDue = Float.valueOf(creditsDueTextField.getText());
							posCustomerID = posCustomerIDTextField.getText();
							query = "SELECT * FROM customer WHERE id='" + posCustomerID + "';";
							resultSet = statement.executeQuery(query);
							if (resultSet.next())
							{
								creditsDueFromTable = Float.valueOf(resultSet.getString("credits_amount"));
							}
							newCreditsDue = creditsDue + creditsDueFromTable;
							query = "UPDATE inventory SET quantity='" + newQuantity + "' WHERE id='" + productID + "';";
							transactionQuantityRows=statement.executeUpdate(query);

							query = "UPDATE customer SET credits_amount='" + newCreditsDue + "' WHERE id='" + posCustomerID + "';";
							transactionAmountRows=statement.executeUpdate(query);

							System.out.println(newCreditsDue + " UPDATED IN THE CUSTOMER TABLE");
						}
					} else if (receiptRadioButton.isSelected())
					{
						newQuantity = quantityFromTable + quantity;
						query = "UPDATE inventory SET quantity='" + newQuantity + "' WHERE id='" + productID + "';";
						transactionQuantityRows=statement.executeUpdate(query);
					}

					if(transactionQuantityRows>0)
						transactionQuantitySuccess();
					else
						JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);

				} catch (SQLException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Database Error Occurred!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} catch (NumberFormatException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Number Format Mismatch!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}

			}

			/*----------------------------------------------Process Credits-----------------------------------------------*/

			else if (sourceButton == processCreditsButton)
			{
				creditCustomerID = creditCustomerIDTextField.getText();
				amount = Float.valueOf(amountTextField.getText());
				float amountFromTable = 0, newAmount = 0;
				try
				{
					query = "SELECT * FROM customer WHERE BINARY id='" + creditCustomerID + "';";
					resultSet = statement.executeQuery(query);
					if (resultSet.next())
					{
						//System.out.println("From Table: " + resultSet.getString("credits_amount"));
						amountFromTable = Float.valueOf(resultSet.getString("credits_amount"));
					}
					if (creditRadioButton.isSelected())
					{
						newAmount = amountFromTable + amount;
					} else if (debitRadioButton.isSelected())
					{
						newAmount = amountFromTable - amount;
						if (newAmount < 0)
						{
							System.out.println("THE CUSTOMER HAS NO CREDITS DUE!");
						}
					}
					query = "UPDATE customer SET credits_amount='" + newAmount + "' WHERE BINARY id='" + creditCustomerID + "';";
					int rowsAffected = statement.executeUpdate(query);
					if (rowsAffected > 0)
					{
						JOptionPane.showMessageDialog(this, "Database Updated Successfully!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
					} else
					{
						JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Database Error Occurred!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			/*------------------------------------------Add New Customer Account------------------------------------------*/

			else if (sourceButton == addNewButton)
			{
				addNewDialog.setVisible(true);
			}
			else if (sourceButton == registerDialogButton)
			{
				try
				{
					String newCustomerID = idAddNewTextField.getText(), customerFname = fnameAddNewTextField.getText(), customerLname = lnameAddNewTextField.getText(), customerAddress = addressAddNewTextField.getText(), customerPhone = phoneAddNewTextField.getText();
					query = "INSERT INTO customer(id,fname,lname,address,phone) VALUES('" + newCustomerID + "','" + customerFname + "','" + customerLname + "','" + customerAddress + "','" + customerPhone + "');";
					int rowsAffected = statement.executeUpdate(query);
					if (rowsAffected > 0)
						JOptionPane.showMessageDialog(this, "New Customer Added!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Database Error Occurred!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
				} finally
				{
					addNewDialog.dispose();
				}
			}
			else if (sourceButton == cancelDialogButton)
			{
				addNewDialog.dispose();
			}

			else if(sourceButton==confirmLogoutButton)
			{
				try
				{
					resultSet.close();
					statement.close();
					connection.close();
					this.dispose();

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					LoginFrame newLoginFrame = new LoginFrame();
				}
			}
			else if(sourceButton==cancelLogoutButton)
			{
				confirmLogoutDialog.setVisible(false);
			}
			else if(sourceButton==rowInfoOKButton)
			{
				rowInfoDialog.setVisible(false);
			}
			else if(sourceButton==addNewProductButton)
			{
				addNewProductDialog.setVisible(true);
			}
			else if(sourceButton==cancelNewProductButton)
			{
				addNewProductDialog.setVisible(false);
			}
			else if(sourceButton==confirmNewProductButton)
			{
				String newProductID=productIDAddNewTextField.getText(),newProductName=productNameAddNewTextField.getText(),newProductRate=rateAddNewTextField.getText();
				System.out.println(newProductID+newProductName+newProductRate);
				query="INSERT INTO inventory(id,product_name,quantity,rate) VALUES ('"+newProductID+"','"+newProductName+"',0,"+newProductRate+");";
				int rowsAffected=statement.executeUpdate(query);
				if(rowsAffected>0)
				{
					JOptionPane.showMessageDialog(this, "New Product Registered!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
					addNewProductDialog.dispose();
				}
				else
					JOptionPane.showMessageDialog(this,"Database Insertion Error!","GroceryDesk- Error!",JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void logoutMenuItemClicked()
	{
		confirmLogoutDialog.setVisible(true);
	}

	public void insertUpdate(DocumentEvent e)
	{
		try
		{
			if (Float.valueOf(creditsDueTextField.getText()) > 0)
			{
				posCustomerIDTextField.setEditable(true);
				posCustomerIDTextField.setBackground(Color.WHITE);
			}
			if (creditsDueFilled == false)
				creditsDueFilled = true;
		}
		catch(NumberFormatException exp)
		{
			exp.printStackTrace();
			handleNumberFormatException(creditsDueTextField,this);
		}

	}

	public void removeUpdate(DocumentEvent e)
	{
		try
		{
			if (posCustomerIDTextField.isEditable())
			{
				if (creditsDueTextField.getText().equals("") || Float.valueOf(creditsDueTextField.getText()) == 0)
				{
					posCustomerIDTextField.setEditable(false);
					posCustomerIDTextField.setText("");
					creditsDueFilled = false;
					posCustomerIDTextField.setBackground(new Color(189, 195, 199));
				}
			}

		}
		catch(NumberFormatException exp)
		{
			exp.printStackTrace();
			JOptionPane.showMessageDialog(this,"Number Format Mismatch!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	public void changedUpdate(DocumentEvent e)
	{}

	public void itemStateChanged(ItemEvent e)
	{
		if(e.getStateChange()==ItemEvent.SELECTED)
		{
			creditsDueTextField.setText("");
			creditsDueTextField.setEditable(false);
			creditsDueTextField.setBackground(new Color(189, 195, 199));
			posCustomerIDTextField.setText("");
			posCustomerIDTextField.setEditable(false);
		}
		if(e.getStateChange()==ItemEvent.DESELECTED)
		{
			creditsDueTextField.setEditable(true);
			creditsDueTextField.setBackground(Color.WHITE);
		}
	}

	public void handleNumberFormatException(JTextField textField,Container container)
	{
		Runnable handleThis=new Runnable()
		{
			public void run()
			{
				JOptionPane.showMessageDialog(container,"Credits due should be a number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				textField.setText("");
			}
		};
		SwingUtilities.invokeLater(handleThis);
	}

	//TODO
	//Add "Added by:","Added In:","Last Updated By:","Last Updated In"
	public void showRowInfoDialog(MouseEvent e)//Component component,String columnName)
	{
		int rowAtPoint=inventoryTable.rowAtPoint(e.getPoint());
		int colAtPoint=inventoryTable.columnAtPoint(e.getPoint());
		String cellValue=inventoryTable.getValueAt(rowAtPoint,colAtPoint).toString();
		System.out.println(cellValue);
		try
		{
			String query = "SELECT * FROM inventory WHERE " + colAtPoint + " ='" + cellValue + "';";
			resultSet = statement.executeQuery(query);
		}
		catch(SQLException event)
		{
			event.printStackTrace();
		}

		rowInfoDialog.setVisible(true);
	}

	//iteration1
	public void windowFocusedEvent()
	{
		DefaultTableModel inventoryTableModel1=(DefaultTableModel)inventoryTable.getModel();
		DefaultTableModel creditAccountsTableModel1=(DefaultTableModel)creditAccountsTable .getModel();
		inventoryTableModel1.setRowCount(0);
		creditAccountsTableModel1.setRowCount(0);
		setInventoryTable();
		setCreditAccountsTable();
	}

	public void transactionQuantitySuccess()
	{
		JOptionPane.showMessageDialog(this, "Transaction Recorded!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
	}

}

