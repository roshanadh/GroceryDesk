package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.*;


public class Storekeeper extends JFrame implements ActionListener, DocumentListener, ItemListener
{
	static String loginID,databaseID,databasePassword,posCustomerID,creditCustomerID,inventoryColumnName,inventoryCellValue,creditAccountsColumnName,creditAccountsCellValue;
	static float creditsDue,amount;
	static boolean creditsDueFilled=false,customerIDFilled=false,outOfStockError=false,catchPhoneNotNumberException=true,continueDebitProcess=true,debitNoSuccessDialogRequired=false,customerIDNonExistentError=false;
	static int transactionQuantityRows,transactionAmountRows,inventorySelectedRowNumber,creditAccountsSelectedRowNumber,loginPrivilege;

	/*----------------------------------- Declarations and Prerequisites----------------------------------- */
	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost?useSSL=true";
	Connection connection;
	PreparedStatement pstatement;
	Statement statement;
	ResultSet resultSet;
	ResultSetMetaData meta;

	/* Dimension and Fonts */
	Dimension textFieldDimension,buttonDimension;
	Font textFieldFont,labelFont,buttonFont;

	/* For Layouts */
	SpringLayout frameLayout,aboutProjectDialogLayout,posTabLayout,creditTabLayout,dialogLayout,productDialogLayout,rowInfoDialogLayout;

	/* For Content Pane */
	Container frameContentPane,aboutProjectDialogContentPane,dialogContentPane,productDialogContentPane,rowInfoDialogContentPane;

	/* For Components */
	JMenuBar mainMenuBar;
	JMenu aboutMenu,settingsMenu,adminPanelMenu;
	JMenuItem aboutProjectMenuItem,logoutMenuItem;
	JTabbedPane storekeeperPanelTab;
	JPanel storekeeperPanelPos,storekeeperPanelCreditAccounts;
	JLabel aboutProjectDialogIconLabel,posTabLabel,creditAccountsTabLabel,storekeeperWelcomeLabel,productIDLabel,quantityLabel,posTransactionLabel,creditsDueLabel,posCustomerIDLabel,amountLabel,creditCustomerIDLabel,creditTransactionLabel,idAddNewLabel,fnameAddNewLabel,lnameAddNewLabel,addressAddNewLabel,phoneAddNewLabel,creditAmountAddNewLabel,confirmLogoutMessage,productIDAddNewLabel,productNameAddNewLabel,rateAddNewLabel,quantityAddNewLabel,inventoryAddedInLabel,inventoryGetAddedInLabel,inventoryAddedByLabel,inventoryGetAddedByLabel,inventoryLastUpdatedInLabel,inventoryGetLastUpdatedInLabel,inventoryLastUpdatedByLabel,inventoryGetLastUpdatedByLabel,creditAccountsAddedInLabel,creditAccountsAddedByLabel,creditAccountsLastUpdatedInLabel,creditAccountsLastUpdatedByLabel,creditAccountsGetAddedInLabel,creditAccountsGetAddedByLabel,creditAccountsGetLastUpdatedInLabel,creditAccountsGetLastUpdatedByLabel;
	JTextField productIDTextField,quantityTextField,creditsDueTextField,posCustomerIDTextField,creditCustomerIDTextField,amountTextField,idAddNewTextField,fnameAddNewTextField,lnameAddNewTextField,addressAddNewTextField,phoneAddNewTextField,creditAmountAddNewTextField,productIDAddNewTextField,productNameAddNewTextField,rateAddNewTextField,quantityAddNewTextField;
	JButton logoutMenuButton,aboutProjectDialogCloseButton,processTransactionButton,addNewCustomerButton,processCreditsButton,addNewProductButton,confirmNewProductButton,registerDialogButton,cancelDialogButton,confirmLogoutButton,cancelLogoutButton,inventoryCancelButton,inventoryRemoveEntryButton,cancelNewProductButton,creditAccountsRemoveEntryButton,creditAccountsCancelButton;
	JRadioButton saleRadioButton,receiptRadioButton,creditRadioButton,debitRadioButton;
	ButtonGroup creditTransactionButtonGroup,posTransactionButtonGroup;
	JTable inventoryTable,creditAccountsTable;
	JScrollPane inventoryTableSp,creditAccountsTableSp;
	JDialog aboutProjectDialog,addNewDialog,inventoryRowInfoDialog,creditAccountsRowInfoDialog,confirmLogoutDialog,addNewProductDialog;
	JTextPane aboutProjectTextPane;
	DefaultTableModel inventoryTableModel,creditAccountsTableModel;


	public static void main(String[] args)
	{
		final Storekeeper storekeeperFrame = new Storekeeper("root","");
		storekeeperFrame.startSystem("demo",0,0);
	}

	public Storekeeper(String databaseID, String databasePassword)
	{
		this.databaseID=databaseID;
		this.databasePassword=databasePassword;
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

		/* Initialize JDBC */
		try
		{
			connection = DriverManager.getConnection(databaseURL, databaseID, databasePassword);
			statement = connection.createStatement();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error Code:"+e.getErrorCode()+" Connection to database couldn't be established!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		/* Dimension and Fonts */
		textFieldDimension=new Dimension(200,30);
		buttonDimension=new Dimension(150,30);
		textFieldFont=new Font("Verdana",Font.PLAIN,12);
		labelFont=new Font("Segoe UI",Font.PLAIN,11);
		buttonFont=new Font("Helvetica",Font.PLAIN,11);

		/* For Layout */
		frameContentPane=this.getContentPane();
		aboutProjectDialogLayout=new SpringLayout();
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
		adminPanelMenu=new JMenu("Admin Panel");
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
		aboutProjectDialog=new JDialog(this,"GroceryDesk- About Project",true);
		addNewDialog=new JDialog(this,"GroceryDesk- New Customer Account",true);
		inventoryRowInfoDialog=new JDialog(this,"GroceryDesk- Row Information",true);
		creditAccountsRowInfoDialog=new JDialog(this,"GroceryDesk- Row Information",true);
		confirmLogoutDialog=new JDialog(this,"GroceryDesk- Confirm Logout",true);
		addNewProductDialog=new JDialog(this,"GroceryDesk- Add New Product",true);

		/* Other definitions */
		aboutProjectDialogIconLabel=new JLabel();
		aboutProjectTextPane=new JTextPane();
		posTabLabel=new JLabel("POS");
		creditAccountsTabLabel=new JLabel("Credit Accounts");
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
		creditCustomerIDLabel=new JLabel("Customer ID");
		creditTransactionLabel=new JLabel("Transaction");
		idAddNewLabel=new JLabel("Customer ID");
		fnameAddNewLabel=new JLabel("First Name");
		lnameAddNewLabel=new JLabel("Last Name");
		addressAddNewLabel=new JLabel("Address");
		phoneAddNewLabel=new JLabel("Phone");
		creditAmountAddNewLabel=new JLabel("Credit Amount");
		confirmLogoutMessage=new JLabel("Are you sure you want to logout?",SwingConstants.CENTER);
		productIDAddNewLabel=new JLabel("Product ID");
		productNameAddNewLabel=new JLabel("Product Name");
		quantityAddNewLabel=new JLabel("Quantity");
		rateAddNewLabel=new JLabel("Rate");
		inventoryAddedInLabel=new JLabel("Added In: ",SwingConstants.CENTER);
		inventoryGetAddedInLabel=new JLabel("",SwingConstants.CENTER);
		inventoryAddedByLabel=new JLabel("Added By: ",SwingConstants.CENTER);
		inventoryGetAddedByLabel=new JLabel("",SwingConstants.CENTER);
		inventoryLastUpdatedInLabel=new JLabel("Last Updated In: ",SwingConstants.CENTER);
		inventoryGetLastUpdatedInLabel=new JLabel("",SwingConstants.CENTER);
		inventoryLastUpdatedByLabel=new JLabel("Last Updated By: ",SwingConstants.CENTER);
		inventoryGetLastUpdatedByLabel=new JLabel("",SwingConstants.CENTER);
		creditAccountsAddedInLabel=new JLabel("Added In: ",SwingConstants.CENTER);
		creditAccountsAddedByLabel=new JLabel("Added By: ",SwingConstants.CENTER);
		creditAccountsLastUpdatedInLabel=new JLabel("Last Updated In: ",SwingConstants.CENTER);
		creditAccountsLastUpdatedByLabel=new JLabel("Last Updated By: ",SwingConstants.CENTER);
		creditAccountsGetAddedInLabel=new JLabel("",SwingConstants.CENTER);
		creditAccountsGetAddedByLabel=new JLabel("",SwingConstants.CENTER);
		creditAccountsGetLastUpdatedInLabel=new JLabel("",SwingConstants.CENTER);
		creditAccountsGetLastUpdatedByLabel=new JLabel("",SwingConstants.CENTER);
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
		creditAmountAddNewTextField=new JTextField();
		productIDAddNewTextField=new JTextField();
		productNameAddNewTextField=new JTextField();
		quantityAddNewTextField=new JTextField();
		rateAddNewTextField=new JTextField();
		logoutMenuButton=new JButton("Logout");
		aboutProjectDialogCloseButton=new JButton("Close");
		processTransactionButton=new JButton("Process Transaction");
		addNewCustomerButton=new JButton("New Customer ");
		registerDialogButton=new JButton("Register Storekeeper");
		cancelDialogButton=new JButton("Cancel");
		processCreditsButton=new JButton("Process Credits");
		confirmLogoutButton=new JButton("Yes");
		cancelLogoutButton=new JButton("Cancel");
		inventoryCancelButton=new JButton("Cancel");
		inventoryRemoveEntryButton=new JButton("Remove Entry");
		addNewProductButton=new JButton("New Product");
		confirmNewProductButton=new JButton("Register Product");
		cancelNewProductButton=new JButton("Cancel");
		creditAccountsRemoveEntryButton=new JButton("Remove Entry");
		creditAccountsCancelButton=new JButton("Cancel");
		saleRadioButton=new JRadioButton("Sale",true);
		receiptRadioButton=new JRadioButton("Receipt");
		creditRadioButton=new JRadioButton("Credit",true);
		debitRadioButton=new JRadioButton("Debit");
		creditTransactionButtonGroup=new ButtonGroup();
		posTransactionButtonGroup=new ButtonGroup();

	}

	/* To set attributes to the components */
	/*
	************************************** index 0 for POS panel, 1 for Credit Accounts Panel

	 ************************************* loginPrivilege 0 for Storekeepers, 1 for Administrator
	*/

	public void startSystem(String loginID,int index,int loginPrivilege)
	{
		this.loginPrivilege=loginPrivilege;
		String loggedInTimeStamp = new SimpleDateFormat("hh:mm a").format(new java.util.Date());
		Storekeeper.loginID =loginID; //Referencing without the use of 'this' operator. Static loginID referenced directly via the class.
		setInventoryTable();
		setCreditAccountsTable();

		/* Attributes of logoutMenuButton */
		logoutMenuButton.setFocusPainted(false);
		logoutMenuButton.setOpaque(false);
		logoutMenuButton.setBorder(null);
		logoutMenuButton.setFont(labelFont);
		logoutMenuButton.setForeground(new Color(150, 40, 27));
		logoutMenuButton.setPreferredSize(buttonDimension);
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

		/* Components of Menu */
		aboutMenu.add(aboutProjectMenuItem);
//		settingsMenu.add(logoutMenuItem);
		mainMenuBar.add(aboutMenu);
//		if(loginPrivilege==0)
//			mainMenuBar.add(settingsMenu);
		if(loginPrivilege==1)
			mainMenuBar.add(adminPanelMenu);
		mainMenuBar.add(Box.createHorizontalGlue());
		mainMenuBar.add(logoutMenuButton);
		if(loginPrivilege==1)
		{
			logoutMenuButton.setOpaque(false);
			logoutMenuButton.setContentAreaFilled(false);
			logoutMenuButton.setBorderPainted(false);
			logoutMenuButton.setText("");
			logoutMenuButton.setForeground(Color.WHITE);
			logoutMenuButton.setEnabled(false);
		}
		/* Other attributes */
		posTabLabel.setPreferredSize(new Dimension(160,18));
		creditAccountsTabLabel.setPreferredSize(new Dimension(150,15));
		storekeeperWelcomeLabel.setFont(new Font("Segoe UI",Font.PLAIN,12));
		storekeeperWelcomeLabel.setText("Logged in: "+loginID+" at "+loggedInTimeStamp);
		storekeeperWelcomeLabel.setOpaque(true);
		storekeeperWelcomeLabel.setForeground(new Color(44, 62, 80));

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

		processTransactionButton.setForeground(new Color(103, 128, 159));
		processTransactionButton.setBackground(Color.WHITE);
		processTransactionButton.setBorderPainted(false);
		processTransactionButton.setFocusPainted(false);
		processTransactionButton.setOpaque(true);
		processTransactionButton.setFont(buttonFont);

		addNewProductButton.setForeground(new Color(103, 128, 159));
		addNewProductButton.setBackground(Color.WHITE);
		addNewProductButton.setBorderPainted(false);
		addNewProductButton.setFocusPainted(false);
		addNewProductButton.setOpaque(true);
		addNewProductButton.setFont(buttonFont);

		confirmNewProductButton.setFocusPainted(false);
		confirmNewProductButton.setBorderPainted(false);
		confirmNewProductButton.setBackground(new Color(103, 128, 159));
		confirmNewProductButton.setForeground(Color.WHITE);
		confirmNewProductButton.setFont(buttonFont);
		confirmNewProductButton.setPreferredSize(buttonDimension);

		cancelNewProductButton.setFocusPainted(false);
		cancelNewProductButton.setBorderPainted(false);
		cancelNewProductButton.setFont(buttonFont);
		cancelNewProductButton.setPreferredSize(buttonDimension);

		processCreditsButton.setForeground(new Color(103, 128, 159));
		processCreditsButton.setBackground(Color.WHITE);
		processCreditsButton.setBorderPainted(false);
		processCreditsButton.setFocusPainted(false);
		processCreditsButton.setOpaque(true);
		processCreditsButton.setFont(buttonFont);

		addNewCustomerButton.setForeground(new Color(103, 128, 159));
		addNewCustomerButton.setBackground(Color.WHITE);
		addNewCustomerButton.setBorderPainted(false);
		addNewCustomerButton.setFocusPainted(false);
		addNewCustomerButton.setOpaque(true);
		addNewCustomerButton.setFont(buttonFont);

		confirmLogoutButton.setForeground(Color.WHITE);
		confirmLogoutButton.setBackground(new Color(103, 128, 159));
		confirmLogoutButton.setBorderPainted(false);
		confirmLogoutButton.setFocusPainted(false);
		confirmLogoutButton.setOpaque(true);
		confirmLogoutButton.setFont(buttonFont);

		cancelLogoutButton.setForeground(new Color(44, 62, 80));
		cancelLogoutButton.setBackground(SystemColor.WHITE);//Color.WHITE);
		cancelLogoutButton.setBorderPainted(false);
		cancelLogoutButton.setFocusPainted(false);
		cancelLogoutButton.setOpaque(true);
		cancelLogoutButton.setFont(buttonFont);

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
		idAddNewTextField.setFont(textFieldFont);
		fnameAddNewTextField.setPreferredSize(textFieldDimension);
		fnameAddNewTextField.setFont(textFieldFont);
		lnameAddNewTextField.setPreferredSize(textFieldDimension);
		lnameAddNewTextField.setFont(textFieldFont);
		addressAddNewTextField.setPreferredSize(textFieldDimension);
		addressAddNewTextField.setFont(textFieldFont);
		phoneAddNewTextField.setPreferredSize(textFieldDimension);
		phoneAddNewTextField.setFont(textFieldFont);
		creditAmountAddNewTextField.setPreferredSize(textFieldDimension);
		creditAmountAddNewTextField.setFont(textFieldFont);

		idAddNewLabel.setFont(labelFont);
		fnameAddNewLabel.setFont(labelFont);
		lnameAddNewLabel.setFont(labelFont);
		addressAddNewLabel.setFont(labelFont);
		phoneAddNewLabel.setFont(labelFont);
		creditAmountAddNewLabel.setFont(labelFont);

		productIDAddNewLabel.setFont(labelFont);
		productNameAddNewLabel.setFont(labelFont);
		quantityAddNewLabel.setFont(labelFont);
		rateAddNewLabel.setFont(labelFont);

		productIDAddNewTextField.setPreferredSize(textFieldDimension);
		productIDAddNewTextField.setFont(textFieldFont);
		productNameAddNewTextField.setPreferredSize(textFieldDimension);
		productNameAddNewTextField.setFont(textFieldFont);
		quantityAddNewTextField.setPreferredSize(textFieldDimension);
		quantityAddNewTextField.setFont(textFieldFont);
		rateAddNewTextField.setPreferredSize(textFieldDimension);
		rateAddNewTextField.setFont(textFieldFont);

		registerDialogButton.setFocusPainted(false);
		registerDialogButton.setBorderPainted(false);
		registerDialogButton.setBackground(new Color(103, 128, 159));
		registerDialogButton.setForeground(Color.WHITE);
		registerDialogButton.setFont(buttonFont);
		registerDialogButton.setPreferredSize(buttonDimension);

		cancelDialogButton.setFocusPainted(false);
		cancelDialogButton.setBorderPainted(false);
		cancelDialogButton.setFont(buttonFont);
		cancelDialogButton.setPreferredSize(buttonDimension);
//		cancelDialogButton.setForeground(Color.WHITE);

		/*------------------------------------------------storekeeperPanelPos-----------------------------------------*/

		/* storekeeperPanelPos attributes */
		storekeeperPanelPos.setLayout(posTabLayout);
		storekeeperPanelPos.setOpaque(true);
		storekeeperPanelPos.setBackground(new Color(103, 128, 159));

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

		/* Adding Event Listeners */
		quantityTextField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				try
				{
					int trial=Integer.parseInt(quantityTextField.getText());
				}
				catch(NumberFormatException exp)
				{
					handleNumberFormatException(quantityTextField,frameContentPane);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{

			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{

			}
		});

		posCustomerIDTextField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				if (posCustomerIDTextField.getText().trim().length() > 0)
				{
					customerIDFilled=true;
				}
				else
					customerIDFilled=false;
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if (posCustomerIDTextField.getText().length() > 0)
				{
					customerIDFilled=true;
				}
				else
					customerIDFilled=false;
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				if (posCustomerIDTextField.getText().length() > 0)
				{
					customerIDFilled=true;
				}
				else
					customerIDFilled=false;
			}
		});

		/* Components of storekeeperFrame */
		this.setJMenuBar(mainMenuBar);
		this.add(storekeeperWelcomeLabel);
		this.add(storekeeperPanelTab);

		/* Adding Window Listener */
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				super.windowGainedFocus(e);
				setIconImage(new ImageIcon(getClass().getResource("grocerydeskIcon.png")).getImage());
			}
		});
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
		storekeeperPanelTab.addTab("",storekeeperPanelPos);
		storekeeperPanelTab.setTabComponentAt(0,posTabLabel);
		storekeeperPanelTab.addTab("", storekeeperPanelCreditAccounts);
		storekeeperPanelTab.setTabComponentAt(1,creditAccountsTabLabel);
		processTransactionButton.setPreferredSize(buttonDimension);
		addNewProductButton.setPreferredSize(buttonDimension);
		processCreditsButton.setPreferredSize(buttonDimension);
		addNewCustomerButton.setPreferredSize(buttonDimension);

		/* Attributes for JTabbedPane */
		storekeeperPanelTab.setFocusable(false);
		storekeeperPanelTab.setSelectedIndex(index);

		/* Adding Action Listeners */

		//Logout Menu Item
//		logoutMenuItem.addActionListener(new ActionListener(){
////			@Override
////			public void actionPerformed(ActionEvent e)
////			{
////				logoutMenuItemClicked();
////			}
////		});
		aboutProjectMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				showAboutProjectDialog();
			}
		});

		logoutMenuButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				logoutMenuItemClicked();
			}
		});
		adminPanelMenu.addMenuListener(new MenuListener()
		{
			@Override
			public void menuSelected(MenuEvent e)
			{
				adminPanelMenuClicked();
			}

			@Override
			public void menuDeselected(MenuEvent e)
			{
				//
			}

			@Override
			public void menuCanceled(MenuEvent e)
			{
				//
			}
		});
		confirmLogoutButton.addActionListener(this);
		cancelLogoutButton.addActionListener(this);
		inventoryCancelButton.addActionListener(this);
		inventoryRemoveEntryButton.addActionListener(this);
		creditAccountsRemoveEntryButton.addActionListener(this);
		creditAccountsCancelButton.addActionListener(this);

		/* Constraints for Components */
		frameLayout.putConstraint(SpringLayout.NORTH,storekeeperWelcomeLabel,10,SpringLayout.NORTH,frameContentPane);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperWelcomeLabel,10,SpringLayout.WEST,frameContentPane);
		frameLayout.putConstraint(SpringLayout.NORTH,storekeeperPanelTab,-10,SpringLayout.SOUTH,storekeeperWelcomeLabel);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperPanelTab,0,SpringLayout.WEST,frameContentPane);
		//frameLayout.putConstraint(SpringLayout.EAST,storekeeperPanelTab,689,SpringLayout.EAST,productIDTextField);
		frameLayout.putConstraint(SpringLayout.EAST,storekeeperPanelTab,0,SpringLayout.EAST,frameContentPane);
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
		posTabLayout.putConstraint(SpringLayout.EAST,inventoryTableSp,44,SpringLayout.EAST,processTransactionButton);
		posTabLayout.putConstraint(SpringLayout.SOUTH,inventoryTableSp,379,SpringLayout.SOUTH,receiptRadioButton);

		/*---------------------------------------storekeeperPanelCreditAccounts---------------------------------------*/

		storekeeperPanelCreditAccounts.setLayout(creditTabLayout);
		storekeeperPanelCreditAccounts.setBackground(new Color(103, 128, 159));

		/* Components for storekeeperPanelCreditAccounts */
		storekeeperPanelCreditAccounts.add(creditCustomerIDLabel);
		storekeeperPanelCreditAccounts.add(creditCustomerIDTextField);
		storekeeperPanelCreditAccounts.add(addNewCustomerButton);
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
		processTransactionButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				processTransactionButton.setBackground(new Color(224, 247, 250));
				processTransactionButton.setForeground(new Color(44, 62, 80));
			}
			public void mouseExited(MouseEvent e)
			{
				processTransactionButton.setBackground(Color.WHITE);
				processTransactionButton.setForeground(new Color(103, 128, 159));
			}
		});
		amountTextField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				try
				{
					float trial=Float.valueOf(amountTextField.getText());
				}
				catch(NumberFormatException exp)
				{
					handleNumberFormatException(amountTextField,frameContentPane);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{

			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{

			}
		});
		creditsDueTextField.getDocument().addDocumentListener(this);
		processCreditsButton.addActionListener(this);
		processCreditsButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				processCreditsButton.setBackground(new Color(224, 247, 250));
				processCreditsButton.setForeground(new Color(44, 62, 80));
			}
			public void mouseExited(MouseEvent e)
			{
				processCreditsButton.setBackground(Color.WHITE);
				processCreditsButton.setForeground(new Color(103, 128, 159));
			}
		});
		addNewCustomerButton.addActionListener(this);
		addNewCustomerButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				addNewCustomerButton.setBackground(new Color(224, 247, 250));
				addNewCustomerButton.setForeground(new Color(44, 62, 80));
			}
			public void mouseExited(MouseEvent e)
			{
				addNewCustomerButton.setBackground(Color.WHITE);
				addNewCustomerButton.setForeground(new Color(103, 128, 159));
			}
		});
		addNewProductButton.addActionListener(this);
		addNewProductButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseEntered(MouseEvent e)
			{
				addNewProductButton.setBackground(new Color(224, 247, 250));
				addNewProductButton.setForeground(new Color(44, 62, 80));
			}
			public void mouseExited(MouseEvent e)
			{
				addNewProductButton.setBackground(Color.WHITE);
				addNewProductButton.setForeground(new Color(103, 128, 159));
			}
		});
		confirmNewProductButton.addActionListener(this);
		cancelNewProductButton.addActionListener(this);

		/* Constraints for Layout */
		creditTabLayout.putConstraint(SpringLayout.NORTH,creditCustomerIDLabel,25,SpringLayout.NORTH,frameContentPane);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditCustomerIDLabel,100,SpringLayout.WEST,frameContentPane);

		creditTabLayout.putConstraint(SpringLayout.NORTH, creditCustomerIDTextField,-6,SpringLayout.NORTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditCustomerIDTextField,30,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,amountLabel,50,SpringLayout.SOUTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.EAST,amountLabel,0,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH, amountTextField,-6,SpringLayout.NORTH,amountLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,amountTextField,0,SpringLayout.WEST,creditCustomerIDTextField);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditTransactionLabel,50,SpringLayout.SOUTH,amountLabel);
		creditTabLayout.putConstraint(SpringLayout.EAST,creditTransactionLabel,0,SpringLayout.EAST,amountLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditRadioButton,-6,SpringLayout.NORTH,creditTransactionLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditRadioButton,30,SpringLayout.EAST,creditTransactionLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,debitRadioButton,10,SpringLayout.SOUTH,creditRadioButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,debitRadioButton,0,SpringLayout.WEST,creditRadioButton);

		creditTabLayout.putConstraint(SpringLayout.NORTH,addNewCustomerButton,60,SpringLayout.NORTH,frameContentPane);
		creditTabLayout.putConstraint(SpringLayout.WEST,addNewCustomerButton,450,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,processCreditsButton,30,SpringLayout.SOUTH,addNewCustomerButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,processCreditsButton,0,SpringLayout.WEST,addNewCustomerButton);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditAccountsTableSp,10,SpringLayout.SOUTH,debitRadioButton);
		creditTabLayout.putConstraint(SpringLayout.SOUTH,creditAccountsTableSp,379,SpringLayout.SOUTH,debitRadioButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditAccountsTableSp,-1,SpringLayout.WEST,frameContentPane);
		creditTabLayout.putConstraint(SpringLayout.EAST,creditAccountsTableSp,125,SpringLayout.EAST,processCreditsButton);

		/*------------------------------------------------AddNewDialog------------------------------------------------*/

		dialogContentPane=addNewDialog.getContentPane();
		addNewDialog.getContentPane().setBackground(new Color(44, 62, 80));
		addNewDialog.setLayout(dialogLayout);
		addNewDialog.setSize(425,450);
		addNewDialog.setFont(new Font("Segoe UI",Font.PLAIN,14));
		addNewDialog.setLocationRelativeTo(this);
		addNewDialog.setResizable(false);

		/* Components of addNewDialog */
		addNewDialog.add(idAddNewLabel);
		addNewDialog.add(fnameAddNewLabel);
		addNewDialog.add(lnameAddNewLabel);
		addNewDialog.add(addressAddNewLabel);
		addNewDialog.add(phoneAddNewLabel);
		addNewDialog.add(creditAmountAddNewLabel);
		addNewDialog.add(idAddNewTextField);
		addNewDialog.add(fnameAddNewTextField);
		addNewDialog.add(lnameAddNewTextField);
		addNewDialog.add(addressAddNewTextField);
		addNewDialog.add(phoneAddNewTextField);
		addNewDialog.add(creditAmountAddNewTextField);
		addNewDialog.add(registerDialogButton);
		addNewDialog.add(cancelDialogButton);

		/* Attributes of Components */
		idAddNewTextField.setBorder(null);
		idAddNewLabel.setForeground(Color.WHITE);
		fnameAddNewTextField.setBorder(null);
		fnameAddNewLabel.setForeground(Color.WHITE);
		lnameAddNewTextField.setBorder(null);
		lnameAddNewLabel.setForeground(Color.WHITE);
		addressAddNewTextField.setBorder(null);
		addressAddNewLabel.setForeground(Color.WHITE);
		phoneAddNewTextField.setBorder(null);
		phoneAddNewLabel.setForeground(Color.WHITE);
		creditAmountAddNewTextField.setBorder(null);
		creditAmountAddNewLabel.setForeground(Color.WHITE);

		/* Attributes of addNewDialog */
		addNewDialog.setUndecorated(true);
		addNewDialog.setBackground(Color.WHITE);
		addNewDialog.setOpacity(0.95f);

		/* Adding Event Listeners */
		registerDialogButton.addActionListener(this);
		cancelDialogButton.addActionListener(this);

		/* Constraints for Layout */
		dialogLayout.putConstraint(SpringLayout.NORTH,idAddNewLabel,50,SpringLayout.NORTH,dialogContentPane);
		dialogLayout.putConstraint(SpringLayout.WEST,idAddNewLabel,60,SpringLayout.WEST,dialogContentPane);

		dialogLayout.putConstraint(SpringLayout.NORTH,fnameAddNewLabel,40,SpringLayout.SOUTH,idAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,fnameAddNewLabel,0,SpringLayout.EAST,idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,lnameAddNewLabel,40,SpringLayout.SOUTH,fnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,lnameAddNewLabel,0,SpringLayout.EAST,fnameAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,addressAddNewLabel,40,SpringLayout.SOUTH,lnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,addressAddNewLabel,0,SpringLayout.EAST,lnameAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,phoneAddNewLabel,40,SpringLayout.SOUTH,addressAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,phoneAddNewLabel,0,SpringLayout.EAST,addressAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,creditAmountAddNewLabel,40,SpringLayout.SOUTH,phoneAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.EAST,creditAmountAddNewLabel,0,SpringLayout.EAST,phoneAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,idAddNewTextField,-6,SpringLayout.NORTH,idAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,idAddNewTextField,30,SpringLayout.EAST,idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,fnameAddNewTextField,-6,SpringLayout.NORTH,fnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,fnameAddNewTextField,0,SpringLayout.WEST,idAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,lnameAddNewTextField,-6,SpringLayout.NORTH,lnameAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,lnameAddNewTextField,0,SpringLayout.WEST,fnameAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,addressAddNewTextField,-6,SpringLayout.NORTH,addressAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,addressAddNewTextField,0,SpringLayout.WEST,lnameAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,phoneAddNewTextField,-6,SpringLayout.NORTH,phoneAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,phoneAddNewTextField,0,SpringLayout.WEST,addressAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,creditAmountAddNewTextField,-6,SpringLayout.NORTH,creditAmountAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,creditAmountAddNewTextField,0,SpringLayout.WEST,phoneAddNewTextField);

		dialogLayout.putConstraint(SpringLayout.NORTH,registerDialogButton,40,SpringLayout.SOUTH,creditAmountAddNewLabel);
		dialogLayout.putConstraint(SpringLayout.WEST,registerDialogButton,-5, SpringLayout.WEST,idAddNewLabel);

		dialogLayout.putConstraint(SpringLayout.NORTH,cancelDialogButton,0,SpringLayout.NORTH,registerDialogButton);
		dialogLayout.putConstraint(SpringLayout.WEST,cancelDialogButton,10,SpringLayout.EAST,registerDialogButton);

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
		confirmLogoutDialog.setLayout(new GridLayout(0,1));

		/*------------------------------------------------addNewProductDialog------------------------------------------------*/

		productDialogContentPane=addNewProductDialog.getContentPane();
		addNewProductDialog.setLayout(productDialogLayout);
		addNewProductDialog.setSize(400,340);
		addNewProductDialog.setLocationRelativeTo(this);
		addNewProductDialog.setResizable(false);

		/* Components of addNewProductDialog */
		addNewProductDialog.add(productIDAddNewLabel);
		addNewProductDialog.add(productNameAddNewLabel);
		addNewProductDialog.add(quantityAddNewLabel);
		addNewProductDialog.add(rateAddNewLabel);
		addNewProductDialog.add(productIDAddNewTextField);
		addNewProductDialog.add(productNameAddNewTextField);
		addNewProductDialog.add(quantityAddNewTextField);
		addNewProductDialog.add(rateAddNewTextField);
		addNewProductDialog.add(confirmNewProductButton);
		addNewProductDialog.add(cancelNewProductButton);

		/* Attributes of Components */
		productIDAddNewLabel.setForeground(Color.WHITE);
		productNameAddNewLabel.setForeground(Color.WHITE);
		quantityAddNewLabel.setForeground(Color.WHITE);
		rateAddNewLabel.setForeground(Color.WHITE);

		productIDAddNewTextField.setBorder(null);
		productNameAddNewTextField.setBorder(null);
		rateAddNewTextField.setBorder(null);
		quantityAddNewTextField.setBorder(null);

		/* Attributes of addNewProductDialog */
		addNewProductDialog.setUndecorated(true);
		addNewProductDialog.getContentPane().setBackground(new Color(34, 49, 63));
		addNewProductDialog.setOpacity(0.95f);

		/* Adding Event Listeners */


		/* Constraints for Layout */
		productDialogLayout.putConstraint(SpringLayout.NORTH,productIDAddNewLabel,50,SpringLayout.NORTH,productDialogContentPane);
		productDialogLayout.putConstraint(SpringLayout.WEST,productIDAddNewLabel,60,SpringLayout.WEST,productDialogContentPane);

		productDialogLayout.putConstraint(SpringLayout.NORTH,productNameAddNewLabel,40,SpringLayout.SOUTH,productIDAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.EAST,productNameAddNewLabel,0,SpringLayout.EAST,productIDAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,quantityAddNewLabel,40,SpringLayout.SOUTH,productNameAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.EAST,quantityAddNewLabel,0,SpringLayout.EAST,productNameAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,rateAddNewLabel,40,SpringLayout.SOUTH,quantityAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.EAST,rateAddNewLabel,0,SpringLayout.EAST,quantityAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,productIDAddNewTextField,-6,SpringLayout.NORTH,productIDAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,productIDAddNewTextField,30,SpringLayout.EAST,productIDAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,productNameAddNewTextField,-6,SpringLayout.NORTH,productNameAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,productNameAddNewTextField,30,SpringLayout.EAST,productNameAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,quantityAddNewTextField,-6,SpringLayout.NORTH,quantityAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,quantityAddNewTextField,30,SpringLayout.EAST,quantityAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,rateAddNewTextField,-6,SpringLayout.NORTH,rateAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,rateAddNewTextField,30,SpringLayout.EAST,rateAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,confirmNewProductButton,40,SpringLayout.SOUTH,rateAddNewLabel);
		productDialogLayout.putConstraint(SpringLayout.WEST,confirmNewProductButton,-15, SpringLayout.WEST,productIDAddNewLabel);

		productDialogLayout.putConstraint(SpringLayout.NORTH,cancelNewProductButton,0,SpringLayout.NORTH,confirmNewProductButton);
		productDialogLayout.putConstraint(SpringLayout.WEST,cancelNewProductButton,10,SpringLayout.EAST,confirmNewProductButton);

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
		aboutProjectDialogCloseButton.setPreferredSize(buttonDimension);
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
		aboutProjectDialog.setSize(425,400);
		aboutProjectDialog.setFont(new Font("Segoe UI",Font.PLAIN,14));
		aboutProjectDialog.setLocationRelativeTo(this);
		aboutProjectDialog.setResizable(false);

		String aboutText="<div style='color:white; font-family: Segoe UI; text-align:justify'><center><p><span style='color:rgb(38, 194, 129);'>GroceryDesk</span> is an attempt at making grocery storekeeping easier for every role involved in the business.</p></center>";
		String teamText="<center><p>It was developed as a Minor Project by the team of</p></center>";
		String teamMembersText="<center><p style='color: white'>Roshan Adhikari<br>Sakar Raman Parajuli<br>Sandhya Acharya</p><center></div>";
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

		/*------------------------------------------------inventoryRowInfoDialog-----------------------------------------------*/

		/* Attributes of Components */
		inventoryAddedInLabel.setFont(labelFont);
		inventoryAddedInLabel.setForeground(Color.WHITE);
		inventoryAddedByLabel.setFont(labelFont);
		inventoryAddedByLabel.setForeground(Color.WHITE);
		inventoryLastUpdatedInLabel.setFont(labelFont);
		inventoryLastUpdatedInLabel.setForeground(Color.WHITE);
		inventoryLastUpdatedByLabel.setFont(labelFont);
		inventoryLastUpdatedByLabel.setForeground(Color.WHITE);

		inventoryGetAddedInLabel.setFont(labelFont);
		inventoryGetAddedInLabel.setForeground(Color.WHITE);
		inventoryGetAddedByLabel.setFont(labelFont);
		inventoryGetAddedByLabel.setForeground(Color.WHITE);
		inventoryGetLastUpdatedInLabel.setFont(labelFont);
		inventoryGetLastUpdatedInLabel.setForeground(Color.WHITE);
		inventoryGetLastUpdatedByLabel.setFont(labelFont);
		inventoryGetLastUpdatedByLabel.setForeground(Color.WHITE);

		rowInfoDialogContentPane=this.getContentPane();
		inventoryRowInfoDialog.setUndecorated(true);
		inventoryRowInfoDialog.getContentPane().setBackground(new Color(34, 49, 63));
		inventoryRowInfoDialog.setFont(new Font("Segoe UI",Font.PLAIN,14));
		inventoryRowInfoDialog.setOpacity(0.95f);
		inventoryRowInfoDialog.setLayout(new GridLayout(0,2));
		inventoryRowInfoDialog.setSize(400,200);
		inventoryRowInfoDialog.setLocationRelativeTo(null);

		// Components of inventoryRowInfoDialog
		inventoryRowInfoDialog.add(inventoryAddedInLabel);
		inventoryRowInfoDialog.add(inventoryGetAddedInLabel);

		inventoryRowInfoDialog.add(inventoryAddedByLabel);
		inventoryRowInfoDialog.add(inventoryGetAddedByLabel);

		inventoryRowInfoDialog.add(inventoryLastUpdatedInLabel);
		inventoryRowInfoDialog.add(inventoryGetLastUpdatedInLabel);

		inventoryRowInfoDialog.add(inventoryLastUpdatedByLabel);
		inventoryRowInfoDialog.add(inventoryGetLastUpdatedByLabel);

		inventoryRowInfoDialog.add(inventoryRemoveEntryButton);
		inventoryRowInfoDialog.add(inventoryCancelButton);

		inventoryCancelButton.setFocusPainted(false);
		inventoryCancelButton.setForeground(Color.WHITE);
		inventoryCancelButton.setBackground(new Color(103, 128, 159));
		inventoryCancelButton.setBorderPainted(false);
		inventoryCancelButton.setFont(buttonFont);

		inventoryRemoveEntryButton.setFocusPainted(false);
		inventoryRemoveEntryButton.setBackground(new Color(231, 76, 60));
		inventoryRemoveEntryButton.setForeground(Color.WHITE);
		inventoryRemoveEntryButton.setBorderPainted(false);
		inventoryRemoveEntryButton.setFont(buttonFont);


		/*------------------------------------------------creditAccountsRowInfoDialog-----------------------------------------------*/

		/* Attributes of Components */
		creditAccountsAddedInLabel.setFont(labelFont);
		creditAccountsAddedInLabel.setForeground(Color.WHITE);
		creditAccountsAddedByLabel.setFont(labelFont);
		creditAccountsAddedByLabel.setForeground(Color.WHITE);
		creditAccountsLastUpdatedInLabel.setFont(labelFont);
		creditAccountsLastUpdatedInLabel.setForeground(Color.WHITE);
		creditAccountsLastUpdatedByLabel.setFont(labelFont);
		creditAccountsLastUpdatedByLabel.setForeground(Color.WHITE);

		creditAccountsGetAddedInLabel.setFont(labelFont);
		creditAccountsGetAddedInLabel.setForeground(Color.WHITE);
		creditAccountsGetAddedByLabel.setFont(labelFont);
		creditAccountsGetAddedByLabel.setForeground(Color.WHITE);
		creditAccountsGetLastUpdatedInLabel.setFont(labelFont);
		creditAccountsGetLastUpdatedInLabel.setForeground(Color.WHITE);
		creditAccountsGetLastUpdatedByLabel.setFont(labelFont);
		creditAccountsGetLastUpdatedByLabel.setForeground(Color.WHITE);

		rowInfoDialogContentPane=this.getContentPane();
		creditAccountsRowInfoDialog.setUndecorated(true);
		creditAccountsRowInfoDialog.getContentPane().setBackground(new Color(34, 49, 63));
		creditAccountsRowInfoDialog.setOpacity(0.95f);
		creditAccountsRowInfoDialog.setLayout(new GridLayout(0,2));
		creditAccountsRowInfoDialog.setSize(400,200);
		creditAccountsRowInfoDialog.setLocationRelativeTo(null);

		// Components of inventoryRowInfoDialog
		creditAccountsRowInfoDialog.add(creditAccountsAddedInLabel);
		creditAccountsRowInfoDialog.add(creditAccountsGetAddedInLabel);

		creditAccountsRowInfoDialog.add(creditAccountsAddedByLabel);
		creditAccountsRowInfoDialog.add(creditAccountsGetAddedByLabel);

		creditAccountsRowInfoDialog.add(creditAccountsLastUpdatedInLabel);
		creditAccountsRowInfoDialog.add(creditAccountsGetLastUpdatedInLabel);

		creditAccountsRowInfoDialog.add(creditAccountsLastUpdatedByLabel);
		creditAccountsRowInfoDialog.add(creditAccountsGetLastUpdatedByLabel);

		creditAccountsRowInfoDialog.add(creditAccountsRemoveEntryButton);
		creditAccountsRowInfoDialog.add(creditAccountsCancelButton);

		creditAccountsCancelButton.setFocusPainted(false);
		creditAccountsCancelButton.setForeground(Color.WHITE);
		creditAccountsCancelButton.setBackground(new Color(103, 128, 159));
		creditAccountsCancelButton.setBorderPainted(false);
		creditAccountsCancelButton.setFont(buttonFont);

		creditAccountsRemoveEntryButton.setFocusPainted(false);
		creditAccountsRemoveEntryButton.setBackground(new Color(231, 76, 60));
		creditAccountsRemoveEntryButton.setForeground(Color.WHITE);
		creditAccountsRemoveEntryButton.setBorderPainted(false);
		creditAccountsRemoveEntryButton.setFont(buttonFont);


		/* Attributes of storekeeperFrame  */
		this.addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				dispose();
			}
		});

		this.setTitle("GroceryDesk- Storekeeper Panel");
		this.getContentPane().setBackground(Color.WHITE);
		this.setSize(900, 700);
		this.setResizable(false);
		this.setLayout(frameLayout);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Setting default buttons */
		storekeeperPanelPos.getRootPane().setDefaultButton(processTransactionButton);

		//storekeeperPanelCreditAccounts.getRootPane().setDefaultButton(processCreditsButton);

	}

	public void setInventoryTable()
	{
		/* ------------------------------------------Retrieving Table Data------------------------------------------- */

		try
		{
//          THIS WORKS, BUT THE NEXT SECTION OF CODE PROVIDES COLUMN LABELS, AND NOT JUST COLUMN NAMES FROM THE TABLE
//			statement.executeUpdate("USE grocerydesk;");
//			resultSet=statement.executeQuery("SELECT * FROM inventory WHERE isDeleted!=1;");
//			meta=resultSet.getMetaData();
//			int columnCount=meta.getColumnCount();
//			//System.out.println(columnCount);
//
//			/* TO GET COLUMN NAMES FOR Inventory */
//			String[] inventoryColumnNames=new String[columnCount];
//
//			/*
//			for(int count=0;count<=columnCount-1;count++)
//			{
//				inventoryColumnNames[count]=meta.getColumnName(count+1);
//			}
//			*/
//			inventoryTableModel.setColumnIdentifiers(inventoryTableColumns);//inventoryColumnNames);
//
//			/* TO GET ROWS FOR INVENTORY */
//			String[] rowData=new String[columnCount];
//			while(resultSet.next())
//			{
//				for(int count=0;count<=columnCount-1;count++)
//				{
//					rowData[count]=resultSet.getString(count+1);
//				}
//				inventoryTableModel.addRow(rowData);
//			}
//
//			inventoryTable.setModel(inventoryTableModel);

			statement.executeUpdate("USE grocerydesk;");
			resultSet=statement.executeQuery("SELECT id AS 'Product ID',product_name AS 'Product Name',quantity AS 'In Stock', rate AS 'Rate' FROM inventory WHERE isDeleted!=1;");
			meta=resultSet.getMetaData();
			int columnCount=meta.getColumnCount();
//			System.out.println("THE COLUMN COUNT IS: "+columnCount);

			/* TO GET COLUMN NAMES FOR Inventory */
			String[] inventoryColumnNames=new String[columnCount];

			for(int count=0;count<=columnCount-1;count++)
			{
				inventoryColumnNames[count]=meta.getColumnLabel(count+1);
//				System.out.println("This is COLUMN NAME: "+inventoryColumnNames[count]);
			}

			inventoryTableModel.setColumnIdentifiers(inventoryColumnNames);

			/* TO GET ROWS FOR INVENTORY */
			resultSet=statement.executeQuery("SELECT * FROM inventory WHERE isDeleted!=1;");
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
			inventoryTable.setSelectionBackground(new Color(103, 128, 159));
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
						showInventoryRowInfo(e);
						e.consume(); // If removed, causes the poppedup dialog box to remain visible for some clicks even after clicking OK Button
					}
				}
			});
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error Code:"+e.getErrorCode()+" Data couldn't be retrieved from the database!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setCreditAccountsTable()
	{
		try
		{
//			statement.executeUpdate("USE grocerydesk;");
//			resultSet = statement.executeQuery("SELECT * FROM customer WHERE isDeleted!=1;");
//			meta = resultSet.getMetaData();
//			int columnCount = meta.getColumnCount();
//			System.out.println(columnCount);
//
//			/* TO GET COLUMN NAMES FOR Inventory */
//			/*
//			String[] inventoryColumnNames = new String[columnCount];
//
//			for (int count = 0; count <= columnCount - 1; count++)
//			{
//				inventoryColumnNames[count] = meta.getColumnName(count + 1);
//			}
//			*/
//			creditAccountsTableModel.setColumnIdentifiers(creditAccountsColumns);



			statement.executeUpdate("USE grocerydesk;");
			resultSet = statement.executeQuery("SELECT id AS 'Customer ID', fname AS 'First Name', lname AS 'Last Name', address AS 'Address', phone AS 'Phone',credits_amount AS 'Credits Amount' FROM customer WHERE isDeleted!=1;");
			meta = resultSet.getMetaData();
			int columnCount = meta.getColumnCount();
//			System.out.println(columnCount);

			/* TO GET COLUMN NAMES FOR Credit Accounts */

			String[] creditAccountsColumnNames = new String[columnCount];

			for (int count = 0; count <= columnCount - 1; count++)
			{
				creditAccountsColumnNames[count] = meta.getColumnLabel(count + 1);
			}

			creditAccountsTableModel.setColumnIdentifiers(creditAccountsColumnNames);

			/* TO GET ROWS FOR Credit Accounts */
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
			creditAccountsTable.setSelectionBackground(new Color(103, 128, 159));
			creditAccountsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
			creditAccountsTable.setFocusable(false);
			creditAccountsTable.setGridColor(Color.WHITE);
			creditAccountsTable.setDefaultEditor(Object.class,null);

			/* Adding Mouse Listener */
			creditAccountsTable.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					super.mouseReleased(e);
					if((e.getClickCount())==2 && !e.isConsumed())
					{
						showCreditAccountsRowInfo(e);
						e.consume(); // If removed, causes the poppedup dialog box to remain visible for some clicks even after clicking OK Button
					}
				}
			});
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error Code:"+e.getErrorCode()+" Data couldn't be retrieved from the database!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void actionPerformed(ActionEvent event)
	{
		String productID,query ;
		String dateTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		int quantity = 0,quantityFromTable = 0,newQuantity=0;
		float newCreditsDue = 0,creditsDueFromTable = 0;

		try
		{
			JButton sourceButton = (JButton) event.getSource();

			/*---------------------------------------------Process Transaction--------------------------------------------*/

			if (sourceButton == processTransactionButton)
			{
				System.out.println("CONTROL REACHED Process Transaction");
				int parsedQuantity = 0;
				boolean quantityNumberFormatExceptionCatch=false,quantitySizeExceeded=false;
				productID = productIDTextField.getText();
				try
				{
					parsedQuantity=Integer.parseInt(quantityTextField.getText());
				}
				catch (NumberFormatException e)
				{
					parsedQuantity=0;
				}

				if(productID.trim().length()<=0)
				{
					JOptionPane.showMessageDialog(this, "Product ID field cannot be blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(quantityTextField.getText().length()>=11 || parsedQuantity<=0)
				{
					quantitySizeExceeded=true;
					JOptionPane.showMessageDialog(this, "Specified quantity is invalid!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(!quantitySizeExceeded)
				{
					try
					{
						quantity = Integer.parseInt(quantityTextField.getText());
						System.out.println("quantity is: "+quantity);
						quantityNumberFormatExceptionCatch=false;
					} catch (NumberFormatException e)
					{
						JOptionPane.showMessageDialog(this, "Quantity should be a counting number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
						quantityNumberFormatExceptionCatch = true;
					}
					if (!quantityNumberFormatExceptionCatch)
					{
						if(quantity<0)
						{
							JOptionPane.showMessageDialog(this, "Quantity should be a positive number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							try
							{
								query = "SELECT * FROM inventory WHERE BINARY id=? AND isDeleted=0;";
								pstatement = connection.prepareStatement(query);
								pstatement.setString(1, productID);
								resultSet = pstatement.executeQuery();

								/*--------------------------------------Sale or Receipt of Product--------------------------------*/

								if (!resultSet.isBeforeFirst())
								{
//									System.out.println("NO ITEM IN INVENTORY!!!!!");
									JOptionPane.showMessageDialog(this, "Product is not available in the inventory!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
								} else
								{
									if (resultSet.next())
									{
										System.out.println("CONTROL REACHED Sale or Receipt");
										quantityFromTable = Integer.parseInt(resultSet.getString("quantity"));
										System.out.println("quantityFromTable is "+quantityFromTable);
										boolean comparison=quantityFromTable>quantity;
										System.out.println("IS quanitityFromTable>quantity? "+comparison);
									}

									if (saleRadioButton.isSelected())
									{
										System.out.println("CONTROL REACHED Sale Button Selected");
										if (quantityFromTable >= quantity)
										{

											newQuantity = quantityFromTable - quantity;
											System.out.println("New Quantity is : "+newQuantity);

											if (!creditsDueFilled)
											{
												System.out.println("CONTROL REACHED !creditsDueFilled");
												query = "UPDATE inventory SET quantity=?,last_updated_in=?,last_updated_by=? WHERE id=?;";
												pstatement = connection.prepareStatement(query);
												pstatement.setInt(1, newQuantity);
												pstatement.setString(2, dateTimeStamp);
												pstatement.setString(3, loginID);
												pstatement.setString(4, productID);
												transactionQuantityRows = pstatement.executeUpdate();

												/* Logging without creditsDueFilled */
												String nameString=null,rateString = null;
												resultSet=statement.executeQuery("SELECT product_name,rate from inventory WHERE id='"+productID+"';");
												if(resultSet.next())
												{
													nameString = resultSet.getString("product_name");
													rateString = resultSet.getString("rate");
												}
												float rateFloat=Float.valueOf(rateString),totalSum=rateFloat*quantity;
												String FOLDER = "SaleLogs";
												File folder=new File(FOLDER);
												if(!folder.exists())
												{
													folder.mkdir();
												}

												DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss"); // add S if you need milliseconds
												String fileName = df.format(new java.util.Date())+".csv";
												File logFile=new File("SaleLogs\\"+fileName);
												PrintWriter logger = new PrintWriter(logFile);
												StringBuilder sb = new StringBuilder();
												sb.append("Product ID");
												sb.append(',');
												sb.append("Product Name");
												sb.append(',');
												sb.append("Product Rate");
												sb.append(',');
												sb.append("Quantity Sold");
												sb.append(',');
												sb.append("Total Sum");
												sb.append(',');
												sb.append("Transaction By");
												sb.append('\n');

												sb.append(productID);
												sb.append(',');
												sb.append(nameString);
												sb.append(',');
												sb.append(rateFloat);
												sb.append(',');
												sb.append(quantity);
												sb.append(',');
												sb.append(totalSum);
												sb.append(',');
												sb.append(loginID);

												logger.write(sb.toString());
												logger.close();
												System.out.println("Logged the SALE details without creditsDueFilled!");
											}
											//iteration 1
											if (creditsDueFilled && !customerIDFilled)
											{
												JOptionPane.showMessageDialog(this,"Customer ID field should not be left blank!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
												customerIDNonExistentError=true;
											}
											else if(creditsDueFilled && customerIDFilled)
											{
												creditsDue = Float.valueOf(creditsDueTextField.getText());
												posCustomerID = posCustomerIDTextField.getText();

												try
												{
													query = "SELECT * FROM customer WHERE id=? AND isDeleted!=1;";
													pstatement = connection.prepareStatement(query);
													pstatement.setString(1, posCustomerID);
													System.out.println("The CUSTOMER ID is "+posCustomerID);
													resultSet = pstatement.executeQuery();
													if (!resultSet.isBeforeFirst())
													{
														JOptionPane.showMessageDialog(this, "The specified Customer ID doesn't exist!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);

													} else if (resultSet.next())
													{
														customerIDNonExistentError=false;
														creditsDueFromTable = Float.valueOf(resultSet.getString("credits_amount"));
														newCreditsDue = creditsDue + creditsDueFromTable;
														System.out.println("THE CUSTOMER ID IS " + posCustomerID);
														System.out.println("THE CREDITS FROM TABLE IS " + creditsDueFromTable);
														System.out.println("THE NEW CREDITS DUE IS " + newCreditsDue);

														query = "UPDATE inventory SET quantity=?,last_updated_in=?,last_updated_by=? WHERE id=?;";
														pstatement = connection.prepareStatement(query);
														pstatement.setInt(1, newQuantity);
														pstatement.setString(2, dateTimeStamp);
														pstatement.setString(3, loginID);
														pstatement.setString(4, productID);
														transactionQuantityRows = pstatement.executeUpdate();

														query = "UPDATE customer SET credits_amount=? WHERE id=?;";
														pstatement = connection.prepareStatement(query);
														pstatement.setFloat(1, newCreditsDue);
														pstatement.setString(2, posCustomerID);
														transactionAmountRows = pstatement.executeUpdate();
														/* Logging with creditsDueFilled */
														String nameString = null, rateString = null;
														resultSet = statement.executeQuery("SELECT product_name,rate from inventory WHERE id='" + productID + "';");
														if (resultSet.next())
														{
															nameString = resultSet.getString("product_name");
															rateString = resultSet.getString("rate");
														}
														float rateFloat = Float.valueOf(rateString), totalSum = rateFloat * quantity;
														String FOLDER = "SaleLogs";
														File folder = new File(FOLDER);
														if (!folder.exists())
														{
															folder.mkdir();
														}

														DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss"); // add S if you need milliseconds
														String fileName = df.format(new java.util.Date()) + ".csv";
														File logFile = new File("SaleLogs\\" + fileName);
														PrintWriter logger = new PrintWriter(logFile);
														StringBuilder sb = new StringBuilder();
														sb.append("Product ID");
														sb.append(',');
														sb.append("Product Name");
														sb.append(',');
														sb.append("Product Rate");
														sb.append(',');
														sb.append("Quantity Sold");
														sb.append(',');
														sb.append("Total Sum");
														sb.append(',');
														sb.append("Customer ID");
														sb.append(',');
														sb.append("Credits Amount");
														sb.append(',');
														sb.append("Sum Paid");
														sb.append(',');
														sb.append("Transaction By");
														sb.append('\n');
														sb.append(productID);
														sb.append(',');
														sb.append(nameString);
														sb.append(',');
														sb.append(rateFloat);
														sb.append(',');
														sb.append(quantity);
														sb.append(',');
														sb.append(totalSum);
														sb.append(',');
														sb.append(posCustomerID);
														sb.append(',');
														sb.append(newCreditsDue);
														sb.append(',');
														sb.append(totalSum - creditsDue);
														sb.append(',');
														sb.append(loginID);
														logger.write(sb.toString());
														logger.close();
														System.out.println("Logged the SALE details with creditsDueFilled!");

													}
												}catch (SQLException e)
												{
													e.printStackTrace();
													JOptionPane.showMessageDialog(this,"Error Code:"+e.getErrorCode()+" Database Error Occurred!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
												}
											}

										} else
										{
											JOptionPane.showMessageDialog(this, "Specified quantity is not available in stock!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
											outOfStockError = true;
										}
									} else if (receiptRadioButton.isSelected())
									{
										outOfStockError = false;
										newQuantity = quantityFromTable + quantity;
										query = "UPDATE inventory SET quantity=?,last_updated_in=?,last_updated_by=? WHERE id=?;";
										pstatement = connection.prepareStatement(query);
										pstatement.setInt(1, newQuantity);
										pstatement.setString(2, dateTimeStamp);
										pstatement.setString(3, loginID);
										pstatement.setString(4, productID);
										transactionQuantityRows = pstatement.executeUpdate();

										/* Logging Receipt */
										String nameString = null, rateString = null;
										resultSet = statement.executeQuery("SELECT product_name,rate from inventory WHERE id='" + productID + "';");
										if (resultSet.next())
										{
											nameString = resultSet.getString("product_name");
											rateString = resultSet.getString("rate");
										}
										float rateFloat = Float.valueOf(rateString), totalSum = rateFloat * quantity;
										String FOLDER = "ReceiptLogs";
										File folder = new File(FOLDER);
										if (!folder.exists())
										{
											folder.mkdir();
										}

										DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss"); // add S if you need milliseconds
										String fileName = df.format(new java.util.Date()) + ".csv";
										File logFile = new File("ReceiptLogs\\" + fileName);
										PrintWriter logger = new PrintWriter(logFile);
										StringBuilder sb = new StringBuilder();
										sb.append("Product ID");
										sb.append(',');
										sb.append("Product Name");
										sb.append(',');
										sb.append("Product Rate");
										sb.append(',');
										sb.append("Received Quantity");
										sb.append(',');
										sb.append("New Quantity In Stock");
										sb.append(',');
										sb.append("Transaction By");
										sb.append('\n');

										sb.append(productID);
										sb.append(',');
										sb.append(nameString);
										sb.append(',');
										sb.append(rateFloat);
										sb.append(',');
										sb.append(quantity);
										sb.append(',');
										sb.append(newQuantity);
										sb.append(',');
										sb.append(loginID);

										logger.write(sb.toString());
										logger.close();
										System.out.println("Logged the RECEIPT details!");
									}

									if (transactionQuantityRows > 0 && !outOfStockError && !customerIDNonExistentError)
										transactionQuantitySuccess();
//									else if (transactionQuantityRows <= 0)
//										JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
								}

							} catch (NumberFormatException e)
							{
								e.printStackTrace();
								JOptionPane.showMessageDialog(this, "Number Format Mismatch!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
							} catch (SQLException e)
							{
								e.printStackTrace();
								JOptionPane.showMessageDialog(this, "Error Code:"+e.getErrorCode()+" Database Error Occurred!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}

			}

			/*--------------------------------------------Process Credits---------------------------------------------*/

			else if (sourceButton == processCreditsButton)
			{
				creditCustomerID = creditCustomerIDTextField.getText();
				boolean amountNumberFormatExceptionCatch=false,amountSizeExceeded=false;
				if(creditCustomerIDTextField.getText().trim().length()<=0)
				{
					JOptionPane.showMessageDialog(this, "Customer ID field should not be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					try
					{
						amount = Float.valueOf(amountTextField.getText());
						amountNumberFormatExceptionCatch = false;
					} catch (NumberFormatException e)
					{
						JOptionPane.showMessageDialog(this, "Amount should be a number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
						amountNumberFormatExceptionCatch = true;
					}
					if (amountTextField.getText().length() >= 11)
					{
						JOptionPane.showMessageDialog(this, "Specified amount is invalid!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
						amountSizeExceeded = true;
					}
					else if (!amountNumberFormatExceptionCatch && !amountSizeExceeded)
					{
						float amountFromTable = 0, newAmount = 0;
						try
						{
							query = "SELECT * FROM customer WHERE BINARY id=? AND isDeleted=0;";
							pstatement = connection.prepareStatement(query);
							pstatement.setString(1, creditCustomerID);
							resultSet = pstatement.executeQuery();
							if (!resultSet.isBeforeFirst())
							{
								JOptionPane.showMessageDialog(this, "Customer ID does not exist!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
							} else
							{
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

									if(amount>amountFromTable)
									{
										JOptionPane.showMessageDialog(this, "The specified amount cannot be debited to the customer!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
									}
									else
									{
										newAmount = amountFromTable - amount;
										if (newAmount == 0)
										{
											JOptionPane.showMessageDialog(this, "DUE CLEARED- The customer has no credits due!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
											debitNoSuccessDialogRequired=true;
										}
									}
								}
								if(continueDebitProcess)
								{
									query = "UPDATE customer SET credits_amount=?, last_updated_in=?,last_updated_by=? WHERE BINARY id=?;";
									pstatement = connection.prepareStatement(query);
									pstatement.setFloat(1, newAmount);
									pstatement.setString(2, dateTimeStamp);
									pstatement.setString(3, loginID);
									pstatement.setString(4, creditCustomerID);
									int rowsAffected = pstatement.executeUpdate();
									if (rowsAffected > 0)
									{
										if(!debitNoSuccessDialogRequired)
											JOptionPane.showMessageDialog(this, "Database Updated Successfully!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);

										/* Logging Credit Details */
										String fnameString = null, lnameString = null;
										resultSet = statement.executeQuery("SELECT fname,lname from customer WHERE id='" + creditCustomerID + "';");
										if (resultSet.next())
										{
											fnameString = resultSet.getString("fname");
											lnameString = resultSet.getString("lname");
										}

										String FOLDER = "CreditLogs";
										File folder = new File(FOLDER);
										if (!folder.exists())
										{
											folder.mkdir();
										}

										DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss"); // add S if you need milliseconds
										String fileName = df.format(new java.util.Date()) + ".csv";
										File logFile = new File("CreditLogs\\" + fileName);
										PrintWriter logger = new PrintWriter(logFile);
										StringBuilder sb = new StringBuilder();
										sb.append("Customer ID");
										sb.append(',');
										sb.append("Customer First Name");
										sb.append(',');
										sb.append("Customer Last Name");
										sb.append(',');
										sb.append("Previous Credits Due");
										sb.append(',');
										sb.append("New Addition");
										sb.append(',');
										sb.append("Total Credits Due");
										sb.append(',');
										sb.append("Transaction By");
										sb.append('\n');

										sb.append(creditCustomerID);
										sb.append(',');
										sb.append(fnameString);
										sb.append(',');
										sb.append(lnameString);
										sb.append(',');
										sb.append(amountFromTable);
										sb.append(',');
										sb.append(amount);
										sb.append(',');
										sb.append(newAmount);
										sb.append(',');
										sb.append(loginID);

										logger.write(sb.toString());
										logger.close();
										System.out.println("Logged the Credit Account Details!");
									} else
									{
										JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						} catch (SQLException e)
						{
							e.printStackTrace();
							JOptionPane.showMessageDialog(this, "Error Code:"+e.getErrorCode()+" Database Error Occurred!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}

			/*------------------------------------------Add New Customer Account--------------------------------------*/

			else if (sourceButton == addNewCustomerButton)
			{
				addNewDialog.setVisible(true);
			}
			else if (sourceButton == registerDialogButton)
			{
				catchPhoneNotNumberException=true;
				if (idAddNewTextField.getText().trim().length() <= 0 && fnameAddNewTextField.getText().trim().length() <= 0 && lnameAddNewTextField.getText().trim().length() <= 0 && addressAddNewTextField.getText().trim().length() <= 0 && phoneAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "The fields cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (idAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "The Customer ID field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (fnameAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "The First Name field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (lnameAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "The Last Name field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (addressAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "The Address field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (phoneAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "The Phone field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (idAddNewTextField.getText().length() > 25)
				{
					JOptionPane.showMessageDialog(this, "The specified Customer ID exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (fnameAddNewTextField.getText().length() > 25)
				{
					JOptionPane.showMessageDialog(this, "The specified First Name exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (lnameAddNewTextField.getText().length() > 25)
				{
					JOptionPane.showMessageDialog(this, "The specified Last Name exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (addressAddNewTextField.getText().length() > 25)
				{
					JOptionPane.showMessageDialog(this, "The specified Address exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (phoneAddNewTextField.getText().length() > 10)
				{
					JOptionPane.showMessageDialog(this, "The specified Phone Number exceeds field size!!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
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

				else {
					try
					{
						String newCustomerID = idAddNewTextField.getText(), customerFname = fnameAddNewTextField.getText(), customerLname = lnameAddNewTextField.getText(), customerAddress = addressAddNewTextField.getText(), customerPhone = phoneAddNewTextField.getText();
						long newPhoneNumber = Long.parseLong(phoneAddNewTextField.getText());
						catchPhoneNotNumberException = false;
						float newCustomerAmount = Float.valueOf(creditAmountAddNewTextField.getText());
						query = "INSERT INTO customer(id,fname,lname,address,phone,credits_amount,added_in,added_by) VALUES(?,?,?,?,?,?,?,?);";
						pstatement = connection.prepareStatement(query);
						pstatement.setString(1, newCustomerID);
						pstatement.setString(2, customerFname);
						pstatement.setString(3, customerLname);
						pstatement.setString(4, customerAddress);
						pstatement.setString(5, customerPhone);
						pstatement.setFloat(6, newCustomerAmount);
						pstatement.setString(7, dateTimeStamp);
						pstatement.setString(8, loginID);

						int rowsAffected = pstatement.executeUpdate();
						if (rowsAffected > 0)
						{
							JOptionPane.showMessageDialog(this, "New Customer Added!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
							addNewDialog.dispose();
						} else
							JOptionPane.showMessageDialog(this, "Database couldn't be updated!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);

					} catch (NumberFormatException e)
					{
						e.printStackTrace();
						if (catchPhoneNotNumberException == true)
							JOptionPane.showMessageDialog(this, "Phone Number must match format 0XXXXXXXX or 9XXXXXXXXX!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(this, "Credit Amount should be a number!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
					} catch (SQLIntegrityConstraintViolationException e)
					{
						JOptionPane.showMessageDialog(this, "Error Code:"+e.getErrorCode() + " The specified Customer ID is not available!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e)
					{
						e.printStackTrace();
						JOptionPane.showMessageDialog(this, "Error Code:"+e.getErrorCode() + " Database Error Occurred!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
					}
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
					confirmLogoutDialog.dispose();
					this.dispose();

				}
				catch(Exception e)
				{
					e.printStackTrace();
					JOptionPane.showMessageDialog(this,"Error occurred while closing database connections!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
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
			else if(sourceButton==inventoryCancelButton)
			{
				inventoryRowInfoDialog.setVisible(false);
			}
			else if(sourceButton==creditAccountsCancelButton)
			{
				creditAccountsRowInfoDialog.setVisible(false);
			}
			else if(sourceButton==inventoryRemoveEntryButton)
			{
				String idFromInventoryTable = null;
				query="SELECT id FROM inventory WHERE isDeleted!=1 LIMIT ?,1;";
				pstatement=connection.prepareStatement(query);
				pstatement.setInt(1,inventorySelectedRowNumber);
				resultSet=pstatement.executeQuery();
				if(resultSet.next())
				{
					idFromInventoryTable=resultSet.getString(1);
				}
				query="UPDATE inventory SET isDeleted=1 WHERE id=?;";
				pstatement=connection.prepareStatement(query);
				pstatement.setString(1,idFromInventoryTable);
				int rowsAffected=pstatement.executeUpdate();
				if(rowsAffected>0)
				{
					JOptionPane.showMessageDialog(this,"Record removed successfully!","GroceryDesk- Success!",JOptionPane.INFORMATION_MESSAGE);
					inventoryRowInfoDialog.dispose();
				}
				else
					JOptionPane.showMessageDialog(this,"Record couldn't be removed!","GroceryDesk- Error!",JOptionPane.ERROR_MESSAGE);

			}
			else if(sourceButton==creditAccountsRemoveEntryButton)
			{
				String idFromCreditAccountsTable = null;
				query="SELECT id FROM customer WHERE isDeleted!=1 LIMIT ?,1";
				pstatement=connection.prepareStatement(query);
				pstatement.setInt(1,creditAccountsSelectedRowNumber);
				resultSet=pstatement.executeQuery();
				if(resultSet.next())
				{
					idFromCreditAccountsTable=resultSet.getString(1);
				}
				query="UPDATE customer SET isDeleted=1 WHERE id=?;";
				pstatement=connection.prepareStatement(query);
				pstatement.setString(1,idFromCreditAccountsTable);

				int rowsAffected=pstatement.executeUpdate();
				if(rowsAffected>0)
				{
					JOptionPane.showMessageDialog(this,"Record removed successfully!","GroceryDesk- Success!",JOptionPane.INFORMATION_MESSAGE);
					creditAccountsRowInfoDialog.dispose();
				}
				else
					JOptionPane.showMessageDialog(this,"Record couldn't be removed!","GroceryDesk- Error!",JOptionPane.ERROR_MESSAGE);

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
				boolean rateNumberFormatExceptionCatch = true,quantityNumberFormatExceptionCatch=true;
				String newProductID = productIDAddNewTextField.getText(), newProductName = productNameAddNewTextField.getText();
				float newProductRate = 0;
				int newProductQuantity = 0;
				if (productIDAddNewTextField.getText().trim().length() <= 0 && productNameAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "Product ID and Product Name fields cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (productIDAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "Product ID field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (productNameAddNewTextField.getText().trim().length() <= 0)
				{
					JOptionPane.showMessageDialog(this, "Product ID field cannot be left blank!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else
				{
					try
					{
						newProductRate = Float.valueOf(rateAddNewTextField.getText());
						rateNumberFormatExceptionCatch = false;
					} catch (NumberFormatException e)
					{
						JOptionPane.showMessageDialog(this, "Rate must be a number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
						rateNumberFormatExceptionCatch = true;
					}
					try
					{
						newProductQuantity = Integer.parseInt(quantityAddNewTextField.getText());
						quantityNumberFormatExceptionCatch = false;
					} catch (NumberFormatException e)
					{
						if(quantityAddNewTextField.getText().trim().equals(""))
						{
							quantityNumberFormatExceptionCatch = false;
						}
						else
						{
							JOptionPane.showMessageDialog(this, "Quantity must be a number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
							quantityNumberFormatExceptionCatch = true;
						}
					}

					if (newProductID.length() > 6)
					{
						JOptionPane.showMessageDialog(this, "The specified Product ID exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
					} else if (newProductName.length() > 50)
					{
						JOptionPane.showMessageDialog(this, "The specified Product Name exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
					} else if (quantityAddNewTextField.getText().length() > 11)
					{
						JOptionPane.showMessageDialog(this, "The specified Quantity exceeds field size!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
					} else if (!rateNumberFormatExceptionCatch && !quantityNumberFormatExceptionCatch)
					{
//						System.out.println(newProductID + newProductName + newProductRate);
						query = "INSERT INTO inventory(id,product_name,quantity,rate,added_in,added_by) VALUES (?,?,?,?,?,?);";
						pstatement = connection.prepareStatement(query);
						pstatement.setString(1, newProductID);
						pstatement.setString(2, newProductName);
						pstatement.setInt(3, newProductQuantity);
						pstatement.setFloat(4, newProductRate);
						pstatement.setString(5, dateTimeStamp);
						pstatement.setString(6, loginID);

						int rowsAffected = pstatement.executeUpdate();
						if (rowsAffected > 0)
						{
							JOptionPane.showMessageDialog(this, "New Product Registered!", "GroceryDesk- Success!", JOptionPane.INFORMATION_MESSAGE);
							addNewProductDialog.dispose();
						} else
							JOptionPane.showMessageDialog(this, "Database Insertion Error!", "GroceryDesk- Error!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		catch(SQLIntegrityConstraintViolationException e)
		{
			JOptionPane.showMessageDialog(this,"Error Code:"+e.getErrorCode()+" The specified Product ID is not available!","GroceryDesk- Error!",JOptionPane.ERROR_MESSAGE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Exception Occurred!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

	}

	public void showAboutProjectDialog()
	{
		aboutProjectDialog.setVisible(true);
	}
	public void logoutMenuItemClicked()
	{
		confirmLogoutDialog.setVisible(true);
	}

	public void adminPanelMenuClicked()
	{
		close();
	}

	public void insertUpdate(DocumentEvent e)
	{
		try
		{
			if (creditsDueTextField.getText().trim()=="")
			{
				creditsDueFilled=false;
			}
			else if (Float.valueOf(creditsDueTextField.getText()) > 0)
			{
				posCustomerIDTextField.setEditable(true);
				posCustomerIDTextField.setBackground(Color.WHITE);
				creditsDueFilled=true;
			}
//			if (creditsDueFilled == false)
//			{
//				creditsDueFilled = true;
//				System.out.println("creditsDueFilled IS "+creditsDueFilled);
//			}
			System.out.println("creditsDueFilled IS "+creditsDueFilled);
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
					customerIDFilled=false;
					posCustomerIDTextField.setEditable(false);
					posCustomerIDTextField.setText("");
					creditsDueFilled = false;
					System.out.println("creditsDueFilled IS "+creditsDueFilled);

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
	{
	}

	public void itemStateChanged(ItemEvent e)
	{
		if(e.getStateChange()==ItemEvent.SELECTED)
		{
			creditsDueTextField.setText("");
			creditsDueTextField.setEditable(false);
			creditsDueTextField.setBackground(new Color(189, 195, 199));
			posCustomerIDTextField.setText("");
			customerIDFilled=false;
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

				if (textField == creditsDueTextField)
				{
					JOptionPane.showMessageDialog(container, "Credits due should be a number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (textField == amountTextField)
				{
					JOptionPane.showMessageDialog(container, "Amount should be a number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				} else if (textField == quantityTextField)
				{
					JOptionPane.showMessageDialog(container, "Quantity should be a counting number!", "GroceryDesk- Error", JOptionPane.ERROR_MESSAGE);
				}

				textField.setText("");
			}
		};
		SwingUtilities.invokeLater(handleThis);
	}

	public void showInventoryRowInfo(MouseEvent e)
	{
		int rowAtPoint=inventoryTable.rowAtPoint(e.getPoint());
		int colAtPoint=inventoryTable.columnAtPoint(e.getPoint());
		inventoryColumnName=inventoryTable.getColumnName(colAtPoint);
		inventoryCellValue=inventoryTable.getValueAt(rowAtPoint,colAtPoint).toString();
		inventorySelectedRowNumber=inventoryTable.getSelectedRow();
		String selectedColumnName=inventoryTable.getColumnName(colAtPoint);
//		System.out.println("THIS IS THE ROW NUMBER "+inventorySelectedRowNumber);
//		System.out.println("THIS IS COLUMN NAME "+inventoryCellValue);
//		System.out.println("THIS IS Cell Value "+inventoryCellValue);

		try
		{
			String query = "SELECT * FROM inventory WHERE isDeleted!=1 LIMIT ?,1;";
			pstatement=connection.prepareStatement(query);
			pstatement.setInt(1,inventorySelectedRowNumber);
			resultSet = pstatement.executeQuery();
			if(resultSet.next())
			{
				String addedIn = resultSet.getString(5);
				String addedBy = resultSet.getString(6);
				String lastUpdatedIn = resultSet.getString(7);
				String lastUpdatedBy = resultSet.getString(8);
//				System.out.println(addedIn + addedBy + lastUpdatedIn + lastUpdatedBy);
				inventoryGetAddedInLabel.setText(addedIn); //creditAccountsAddedInLabel.getText90.toString() must be declated a separate variable.
				inventoryGetAddedByLabel.setText(addedBy);
				inventoryGetLastUpdatedInLabel.setText(lastUpdatedIn);
				inventoryGetLastUpdatedByLabel.setText(lastUpdatedBy);

			}
			resultSet.close();

		}
		catch(SQLException exp)
		{
			exp.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error Code:"+exp.getErrorCode()+" Database Exception Occurred!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		inventoryRowInfoDialog.setVisible(true);
	}

	public void showCreditAccountsRowInfo(MouseEvent e)
	{
		int rowAtPoint=creditAccountsTable.rowAtPoint(e.getPoint());
		int colAtPoint=creditAccountsTable.columnAtPoint(e.getPoint());
		creditAccountsColumnName=creditAccountsTable.getColumnName(colAtPoint);
		creditAccountsCellValue=creditAccountsTable.getValueAt(rowAtPoint,colAtPoint).toString();
		creditAccountsSelectedRowNumber=creditAccountsTable.getSelectedRow();
//		System.out.println("THIS IS COLUMNNAME "+creditAccountsCellValue);
		try
		{
			String query = "SELECT * FROM customer WHERE isDeleted!=1 LIMIT ?,1;";
			pstatement=connection.prepareStatement(query);
			pstatement.setInt(1,creditAccountsSelectedRowNumber);
			resultSet = pstatement.executeQuery();
			if(resultSet.next())
			{
				String addedIn = resultSet.getString(7);
				String addedBy = resultSet.getString(8);
				String lastUpdatedIn = resultSet.getString(9);
				String lastUpdatedBy = resultSet.getString(10);
//				System.out.println(addedIn + addedBy + lastUpdatedIn + lastUpdatedBy);
				creditAccountsGetAddedInLabel.setText(addedIn); //creditAccountsAddedInLabel.getText90.toString() must be declated a separate variable.
				creditAccountsGetAddedByLabel.setText(addedBy);
				creditAccountsGetLastUpdatedInLabel.setText(lastUpdatedIn);
				creditAccountsGetLastUpdatedByLabel.setText(lastUpdatedBy);
			}
			resultSet.close();

		}
		catch(SQLException exp)
		{
			exp.printStackTrace();
			JOptionPane.showMessageDialog(this,"Error Code:"+exp.getErrorCode()+"- Database Exception Occurred!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		creditAccountsRowInfoDialog.setVisible(true);
	}

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

	public void close()
	{
		dispose();
	}

}

