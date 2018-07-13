package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.event.*;
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

	/*----------------------------------- Declarations and Prerequisites----------------------------------- */
	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost/?useSSL=true";
	String databaseID="root";
	String databasePassword="";
	Connection connection;
	Statement statement;
	ResultSet resultSet;
	ResultSetMetaData meta;

	/* Layout */
	SpringLayout frameLayout,posTabLayout,creditTabLayout;

	/* Content Pane */
	Container contentPane;

	/* For Components */
	JMenuBar mainMenuBar;
	JMenu aboutMenu;
	JMenuItem aboutProjectMenuItem;
	JTabbedPane storekeeperPanelTab;
	JPanel storekeeperPanelPos,storekeeperPanelCreditAccounts;
	JLabel storekeeperWelcomeLabel,productIDLabel,quantityLabel,transactionLabel,creditsDueLabel,customerIDLabel,amountLabel,orLabel,creditCustomerIDLabel,creditTransactionLabel;
	JTextField productIDTextField,quantityTextField,creditsDueTextField,posCustomerIDTextField,creditCustomerIDTextField,amountTextField;
	JButton processTransactionButton,addNewButton,processCreditsButton;
	JRadioButton saleRadioButton,receiptRadioButton,creditRadioButton,debitRadioButton;
	ButtonGroup creditTransactionButtonGroup,posTransactionButtonGroup;
	JTable inventoryTable,creditAccountsTable;
	JScrollPane inventoryTableSp,creditAccountsTableSp;
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
		}

		/* For Layout */
		contentPane=this.getContentPane();
		frameLayout=new SpringLayout();
		posTabLayout=new SpringLayout();
		creditTabLayout=new SpringLayout();

		/* Menu definitions */
		mainMenuBar=new JMenuBar();
		aboutMenu=new JMenu("About");
		aboutProjectMenuItem=new JMenuItem("Project");

		/* Default Table Models' definitions */
		inventoryTableModel=new DefaultTableModel();
		creditAccountsTableModel=new DefaultTableModel();

		/* JTable definitions */
		inventoryTable=new JTable();
		creditAccountsTable=new JTable();

		/* JScrollPane definitions */
		creditAccountsTableSp=new JScrollPane();
		inventoryTableSp=new JScrollPane();

		/* Other definitions */
		storekeeperWelcomeLabel = new JLabel();
		storekeeperPanelPos = new JPanel();
		storekeeperPanelCreditAccounts = new JPanel();
		storekeeperPanelTab = new JTabbedPane();
		storekeeperPanelTab.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		storekeeperPanelTab.setTabPlacement(JTabbedPane.TOP);
		productIDLabel=new JLabel("Product ID");
		quantityLabel=new JLabel("Quantity");
		transactionLabel=new JLabel("Transaction");
		creditsDueLabel=new JLabel("Credits due");
		customerIDLabel=new JLabel("Customer ID");
		amountLabel=new JLabel("Amount");
		orLabel=new JLabel("Or");
		creditCustomerIDLabel=new JLabel("Customer ID");
		creditTransactionLabel=new JLabel("Transaction");
		productIDTextField=new JTextField(10);
		quantityTextField=new JTextField(10);
		creditsDueTextField=new JTextField(10);
		posCustomerIDTextField=new JTextField(10);
		creditCustomerIDTextField=new JTextField(10);
		amountTextField=new JTextField(10);
		processTransactionButton=new JButton("Process Transaction");
		addNewButton=new JButton("Add New");
		processCreditsButton=new JButton("Process Credits");
		saleRadioButton=new JRadioButton("Sale",true);
		receiptRadioButton=new JRadioButton("Receipt");
		creditRadioButton=new JRadioButton("Credit",true);
		debitRadioButton=new JRadioButton("Debit");
		creditTransactionButtonGroup=new ButtonGroup();
		posTransactionButtonGroup=new ButtonGroup();

	}

	/* To set attributes to the components */
	public void run(String loginID,int index)
	{
		String loggedInTimeStamp = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());
		this.loginID=loginID;
		setInventoryTable();
		setCreditAccountsTable();

		/* Other attributes */
		storekeeperWelcomeLabel.setFont(new Font("Segoe UI",Font.PLAIN,12));
		storekeeperWelcomeLabel.setText("Storekeeper: "+loginID+". Logged In: "+loggedInTimeStamp);
		processTransactionButton.setFocusPainted(false);
		posCustomerIDTextField.setEditable(false);
		posTransactionButtonGroup.add(saleRadioButton);
		posTransactionButtonGroup.add(receiptRadioButton);
		creditTransactionButtonGroup.add(creditRadioButton);
		creditTransactionButtonGroup.add(debitRadioButton);

		/*------------------------------------------------StorekeeperPanelPos-----------------------------------------*/

		storekeeperPanelPos.setLayout(posTabLayout);

		/* Components for storekeeperPanelPos */
		storekeeperPanelPos.add(inventoryTable);
		storekeeperPanelPos.add(productIDLabel);
		storekeeperPanelPos.add(productIDTextField);
		storekeeperPanelPos.add(quantityLabel);
		storekeeperPanelPos.add(quantityTextField);
		storekeeperPanelPos.add(transactionLabel);
		storekeeperPanelPos.add(saleRadioButton);
		storekeeperPanelPos.add(receiptRadioButton);
		storekeeperPanelPos.add(creditsDueLabel);
		storekeeperPanelPos.add(creditsDueTextField);
		storekeeperPanelPos.add(customerIDLabel);
		storekeeperPanelPos.add(posCustomerIDTextField);
		storekeeperPanelPos.add(processTransactionButton);
		storekeeperPanelPos.add(inventoryTableSp);

		/* Components of Menu */
		aboutMenu.add(aboutProjectMenuItem);
		mainMenuBar.add(aboutMenu);

		inventoryTableSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inventoryTableSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		inventoryTableSp.setViewportView(inventoryTable);

		/* Components of storekeeperFrame */
		this.setJMenuBar(mainMenuBar);
		this.add(storekeeperWelcomeLabel);
		this.add(storekeeperPanelTab);

		/* Components for JTabbedPane */
		storekeeperPanelTab.addTab("POS", storekeeperPanelPos);
		storekeeperPanelTab.addTab("Credit Accounts", storekeeperPanelCreditAccounts);
		processTransactionButton.setPreferredSize(new Dimension(150,30));
		processCreditsButton.setPreferredSize(new Dimension(150,30));

		/* Attributes for JTabbedPane */
		storekeeperPanelTab.setFocusable(false);
		storekeeperPanelTab.setSelectedIndex(index);

		/* Constraints for Components */
		frameLayout.putConstraint(SpringLayout.NORTH,storekeeperWelcomeLabel,10,SpringLayout.NORTH,contentPane);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperWelcomeLabel,10,SpringLayout.WEST,contentPane);
		frameLayout.putConstraint(SpringLayout.NORTH, storekeeperPanelTab,-20,SpringLayout.SOUTH,storekeeperWelcomeLabel);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperPanelTab,10,SpringLayout.WEST,contentPane);
		frameLayout.putConstraint(SpringLayout.EAST,storekeeperPanelTab,780,SpringLayout.EAST,productIDTextField);
		frameLayout.putConstraint(SpringLayout.SOUTH,storekeeperPanelTab,-10,SpringLayout.SOUTH,contentPane);

		posTabLayout.putConstraint(SpringLayout.NORTH,productIDLabel,50,SpringLayout.NORTH,contentPane);
		posTabLayout.putConstraint(SpringLayout.WEST,productIDLabel,80,SpringLayout.WEST,contentPane);

		posTabLayout.putConstraint(SpringLayout.NORTH,quantityLabel,50,SpringLayout.SOUTH,productIDLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,quantityLabel,80,SpringLayout.WEST,contentPane);

		posTabLayout.putConstraint(SpringLayout.NORTH,transactionLabel,50,SpringLayout.SOUTH,quantityLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,transactionLabel,80,SpringLayout.WEST,contentPane);

		posTabLayout.putConstraint(SpringLayout.NORTH,productIDTextField,50,SpringLayout.SOUTH,contentPane);
		posTabLayout.putConstraint(SpringLayout.WEST,productIDTextField,30,SpringLayout.EAST,productIDLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,quantityTextField,50,SpringLayout.SOUTH,productIDTextField);
		posTabLayout.putConstraint(SpringLayout.WEST,quantityTextField,30,SpringLayout.EAST,quantityLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,saleRadioButton,50,SpringLayout.SOUTH,quantityLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,saleRadioButton,30,SpringLayout.EAST,transactionLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,receiptRadioButton,10,SpringLayout.SOUTH,saleRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,receiptRadioButton,30,SpringLayout.EAST,transactionLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,creditsDueLabel,50,SpringLayout.NORTH,contentPane);
		posTabLayout.putConstraint(SpringLayout.WEST,creditsDueLabel,260,SpringLayout.EAST,productIDTextField);

		posTabLayout.putConstraint(SpringLayout.NORTH,customerIDLabel,50,SpringLayout.SOUTH,creditsDueLabel);
		posTabLayout.putConstraint(SpringLayout.WEST,customerIDLabel,260,SpringLayout.EAST,quantityTextField);

		posTabLayout.putConstraint(SpringLayout.NORTH,creditsDueTextField,50,SpringLayout.NORTH,contentPane);
		posTabLayout.putConstraint(SpringLayout.WEST,creditsDueTextField,30,SpringLayout.EAST,creditsDueLabel);

		posTabLayout.putConstraint(SpringLayout.NORTH,posCustomerIDTextField,50,SpringLayout.SOUTH,creditsDueTextField);
		posTabLayout.putConstraint(SpringLayout.WEST,posCustomerIDTextField,30,SpringLayout.EAST,customerIDLabel);

		posTabLayout.putConstraint(SpringLayout.SOUTH,processTransactionButton,0,SpringLayout.SOUTH,receiptRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,processTransactionButton,0,SpringLayout.WEST,posCustomerIDTextField);

		posTabLayout.putConstraint(SpringLayout.NORTH,inventoryTableSp,20,SpringLayout.SOUTH,receiptRadioButton);
		posTabLayout.putConstraint(SpringLayout.WEST,inventoryTableSp,0,SpringLayout.WEST,contentPane);
		posTabLayout.putConstraint(SpringLayout.EAST,inventoryTableSp,130,SpringLayout.EAST,processTransactionButton);
		posTabLayout.putConstraint(SpringLayout.SOUTH,inventoryTableSp,380,SpringLayout.SOUTH,receiptRadioButton);

		/*--------------------------------------StorekeeperPanelCreditAccounts---------------------------------------*/

		storekeeperPanelCreditAccounts.setLayout(creditTabLayout);

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

		/* Constraints for Layout */
		creditTabLayout.putConstraint(SpringLayout.NORTH,creditCustomerIDLabel,100,SpringLayout.NORTH,contentPane);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditCustomerIDLabel,100,SpringLayout.WEST,contentPane);

		creditTabLayout.putConstraint(SpringLayout.NORTH,amountLabel,50,SpringLayout.SOUTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.EAST,amountLabel,0,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH, creditCustomerIDTextField,100,SpringLayout.NORTH,contentPane);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditCustomerIDTextField,30,SpringLayout.EAST,creditCustomerIDLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH, amountTextField,0,SpringLayout.NORTH,amountLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,amountTextField,0,SpringLayout.WEST,creditCustomerIDTextField);

		creditTabLayout.putConstraint(SpringLayout.NORTH,orLabel,0,SpringLayout.NORTH,creditCustomerIDTextField);
		creditTabLayout.putConstraint(SpringLayout.WEST,orLabel,30,SpringLayout.EAST,creditCustomerIDTextField);

		creditTabLayout.putConstraint(SpringLayout.NORTH,addNewButton,0,SpringLayout.NORTH,orLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,addNewButton,30,SpringLayout.EAST,orLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditTransactionLabel,0,SpringLayout.NORTH,creditCustomerIDLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditTransactionLabel,150,SpringLayout.EAST,addNewButton);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditRadioButton,0,SpringLayout.NORTH,creditTransactionLabel);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditRadioButton,30,SpringLayout.EAST,creditTransactionLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,debitRadioButton,30,SpringLayout.SOUTH,creditRadioButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,debitRadioButton,0,SpringLayout.WEST,creditRadioButton);

		creditTabLayout.putConstraint(SpringLayout.NORTH,processCreditsButton,50,SpringLayout.SOUTH,debitRadioButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,processCreditsButton,0,SpringLayout.WEST,creditTransactionLabel);

		creditTabLayout.putConstraint(SpringLayout.NORTH,creditAccountsTableSp,20,SpringLayout.SOUTH,processCreditsButton);
		creditTabLayout.putConstraint(SpringLayout.SOUTH,creditAccountsTableSp,352,SpringLayout.SOUTH,processCreditsButton);
		creditTabLayout.putConstraint(SpringLayout.WEST,creditAccountsTableSp,0,SpringLayout.WEST,contentPane);
		creditTabLayout.putConstraint(SpringLayout.EAST,creditAccountsTableSp,145,SpringLayout.EAST,processCreditsButton);

		/* Attributes of storekeeperFrame */
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e)
		{
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);

		this.setTitle("GroceryDesk- Storekeeper Panel");
		this.getContentPane().setBackground(Color.white);
		this.setSize(900, 700);
		this.setResizable(false);
		this.setLayout(frameLayout);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			System.out.println(columnCount);

			/* TO GET COLUMN NAMES FOR Inventory */
			String[] columnNames=new String[columnCount];

			for(int count=0;count<=columnCount-1;count++)
			{
				columnNames[count]=meta.getColumnName(count+1);
				System.out.println(columnNames[count]);
			}
			inventoryTableModel.setColumnIdentifiers(columnNames);

			/* TO GET ROWS FOR INVENTORY */
			String[] rowData=new String[columnCount];
			while(resultSet.next())
			{
				for(int count=0;count<=columnCount-1;count++)
				{
					rowData[count]=resultSet.getString(count+1);
					System.out.println(rowData[count]);
				}
				inventoryTableModel.addRow(rowData);
			}
			inventoryTable.setModel(inventoryTableModel);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void setCreditAccountsTable()
	{
		try
		{
			statement.executeUpdate("USE grocerydesk;");
			resultSet=statement.executeQuery("SELECT * FROM customer;");
			meta=resultSet.getMetaData();
			int columnCount=meta.getColumnCount();
			System.out.println(columnCount);

			/* TO GET COLUMN NAMES FOR Inventory */
			String[] columnNames=new String[columnCount];

			for(int count=0;count<=columnCount-1;count++)
			{
				columnNames[count]=meta.getColumnName(count+1);
				System.out.println(columnNames[count]);
			}
			creditAccountsTableModel.setColumnIdentifiers(columnNames);

			/* TO GET ROWS FOR INVENTORY */
			String[] rowData=new String[columnCount];
			while(resultSet.next())
			{
				for(int count=0;count<=columnCount-1;count++)
				{
					rowData[count]=resultSet.getString(count+1);
					System.out.println(rowData[count]);
				}
				creditAccountsTableModel.addRow(rowData);
			}
			creditAccountsTable.setModel(creditAccountsTableModel);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void actionPerformed(ActionEvent event)
	{
		String productID,query ;
		int quantity,quantityFromTable = 0,newQuantity=0;
		float newCreditsDue,creditsDueFromTable = 0;
		JButton sourceButton=(JButton)event.getSource();

		/*---------------------------------------------Process Transaction--------------------------------------------*/

		if(sourceButton==processTransactionButton)
		{
			productID = productIDTextField.getText();
			quantity = Integer.parseInt(quantityTextField.getText());
			try
			{
				query = "SELECT * FROM inventory WHERE id='" + productID + "'";
				resultSet = statement.executeQuery(query);

				/* -------------------------------------Sale or Receipt of Product----------------------------------- */

				if (resultSet.next())
				{
					quantityFromTable = Integer.parseInt(resultSet.getString("quantity"));
				}
				else
					System.out.println("Product is not available in the inventory!");

				if (saleRadioButton.isSelected())
				{

					if (quantityFromTable > quantity)
					{
						newQuantity = quantityFromTable - quantity;
					}
					else
						System.out.println("Out of Stock!");

					if (!creditsDueFilled)
					{
						query = "UPDATE inventory SET quantity='" + newQuantity + "' WHERE id='" + productID + "';";
						statement.executeUpdate(query);
					}
					if (creditsDueFilled)
					{
						creditsDue = Float.valueOf(creditsDueTextField.getText());
						posCustomerID = posCustomerIDTextField.getText();
						query="SELECT * FROM customer WHERE id='"+posCustomerID+"';";
						resultSet=statement.executeQuery(query);
						if(resultSet.next())
						{
							creditsDueFromTable=Float.valueOf(resultSet.getString("credits_amount"));
						}
						newCreditsDue=creditsDue+creditsDueFromTable;
						query="UPDATE inventory SET quantity='" + newQuantity +"' WHERE id='" +productID+"';";
						statement.executeUpdate(query);
						query="UPDATE customer SET credits_amount='"+newCreditsDue+"' WHERE id='"+posCustomerID+"';";
						statement.executeUpdate(query);
						System.out.println(newCreditsDue+" UPDATED IN THE CUSTOMER TABLE");
					}
				}
				else if (receiptRadioButton.isSelected())
				{
					newQuantity = quantityFromTable + quantity;
					query = "UPDATE inventory SET quantity='" + newQuantity + "' WHERE id='" + productID + "';";
					statement.executeUpdate(query);
				}
				/* To reload the JFrame and update the JTable */
				this.dispose();
				StorekeeperFrame refreshedFrame = new StorekeeperFrame();
				refreshedFrame.run(loginID,0);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		/*----------------------------------------------Process Credits-----------------------------------------------*/

		else if(sourceButton==processCreditsButton)
		{
			creditCustomerID=creditCustomerIDTextField.getText();
			amount=Float.valueOf(amountTextField.getText());
			float amountFromTable = 0,newAmount = 0;
			try
			{
				query = "SELECT * FROM customer WHERE id='" + creditCustomerID + "';";
				resultSet = statement.executeQuery(query);
				if (resultSet.next())
				{
					System.out.println("From Table: " + resultSet.getString("credits_amount"));
					amountFromTable = Float.valueOf(resultSet.getString("credits_amount"));
				}
				else{}
				if (creditRadioButton.isSelected())
				{
					newAmount = amountFromTable + amount;
				}
				else if(debitRadioButton.isSelected())
				{
					newAmount=amountFromTable-amount;
					if(newAmount<0)
					{
						System.out.println("THE CUSTOMER HAS NO CREDITS DUE!");
					}
				}
				query = "UPDATE customer SET credits_amount='" + newAmount + "' WHERE id='" + creditCustomerID + "';";
				statement.executeUpdate(query);
				this.dispose();
				StorekeeperFrame refreshedFrame = new StorekeeperFrame();
				refreshedFrame.run(loginID,1);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	public void insertUpdate(DocumentEvent e)
	{
		if(Float.valueOf(creditsDueTextField.getText())>0)
			posCustomerIDTextField.setEditable(true);
		if(creditsDueFilled==false)
			creditsDueFilled=true;
		System.out.println(creditsDueFilled);
	}

	public void removeUpdate(DocumentEvent e)
	{
		if(posCustomerIDTextField.isEditable())
		{
			if(creditsDueTextField.getText().equals("") || Float.valueOf(creditsDueTextField.getText())==0)
			{
				posCustomerIDTextField.setEditable(false);
				posCustomerIDTextField.setText("");
				creditsDueFilled=false;
			}
		}
		System.out.println(creditsDueFilled);
	}

	public void changedUpdate(DocumentEvent e)
	{}

	public void itemStateChanged(ItemEvent e)
	{
		//JRadioButton sourceRadioButton=(JRadioButton)e.getSource();
		if(e.getStateChange()==ItemEvent.SELECTED)
		{
			creditsDueTextField.setText("");
			creditsDueTextField.setEditable(false);
			posCustomerIDTextField.setText("");
			posCustomerIDTextField.setEditable(false);
		}
		if(e.getStateChange()==ItemEvent.DESELECTED)
		{
			creditsDueTextField.setEditable(true);
		}
	}
}

