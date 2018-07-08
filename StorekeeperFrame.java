package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class StorekeeperFrame extends JFrame
{
	/*----------------------------------- Declarations and Prerequisites----------------------------------- */

	/* For Components */
	JMenuBar mainMenuBar;
	JMenu aboutMenu;
	JMenuItem aboutProjectMenuItem;
	JTabbedPane storekeeperPanelTab;
	JPanel storekeeperPanelPos,storekeeperPanelCreditAccounts;
	JLabel storekeeperWelcomeLabel;
	JTable inventoryTable, creditAccountsTable;
	JScrollPane inventoryTableSp, creditAccountsTableSp;
	DefaultTableModel inventoryTableModel,creditAccountsTableModel;

	public static void main(String[] args)
	{
		final StorekeeperFrame storekeeperFrame = new StorekeeperFrame();
	}

	public StorekeeperFrame()
	{
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
		inventoryTableSp=new JScrollPane(inventoryTable);
		creditAccountsTableSp=new JScrollPane(creditAccountsTable);

		/* Other definitions */
		storekeeperWelcomeLabel = new JLabel();
		storekeeperPanelPos = new JPanel();
		storekeeperPanelCreditAccounts = new JPanel();
		storekeeperPanelTab = new JTabbedPane();
		storekeeperPanelTab.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		storekeeperPanelTab.setTabPlacement(JTabbedPane.TOP);

	}
	public void run(String loginID)
	{

		/* Attributes of storekeeperFrame */
		this.getContentPane().setBackground(Color.white);
		this.setSize(900, 700);
		this.setResizable(false);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Other attributes */
		storekeeperWelcomeLabel.setText("Welcome, "+loginID);

		/* Components for JTabbedPane */
		storekeeperPanelTab.addTab("Credit Accounts", storekeeperPanelCreditAccounts);
		storekeeperPanelTab.addTab("POS", storekeeperPanelPos);

		/* Bounds for components */
		storekeeperWelcomeLabel.setBounds(10, 10, 200, 50);
		storekeeperPanelTab.setBounds(10, 25, 865, 630);

		/* Components for storekeeperPanelPos */
		inventoryTableSp.setBounds(10, 35, 600, 600);
		storekeeperPanelPos.add(inventoryTableSp);

		/* Components of Menu */
		aboutMenu.add(aboutProjectMenuItem);
		mainMenuBar.add(aboutMenu);

		/* Components of storekeeperFrame */
		this.setJMenuBar(mainMenuBar);
		this.add(storekeeperWelcomeLabel);
		this.add(storekeeperPanelTab);
	}
}

