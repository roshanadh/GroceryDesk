package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.table.*;

public class AdminFrame
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
		new AdminFrame();
	}

	public AdminFrame()
	{

	}

	public void startSystem(String loginID)
	{

	}
}
