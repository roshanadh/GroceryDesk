package com.teamGroceryDesk.GroceryDesk;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StorekeeperTable extends JDialog implements ActionListener
{
	/*----------------------------------------- Declarations and Prerequisites--------------------------------------- */

	static int storekeeperSelectedRowNumber;
	static String selectedCellValue,selectedColumnName;
	String[] storekeeperTableColumns={"id","fname","lname","address","phone"};

	/* For JDBC */
	String databaseURL="jdbc:mysql://localhost?useSSL=true";
	String databaseID="root";
	String databasePassword="";
	String query=null;
	Connection connection;
	PreparedStatement pstatement;
	Statement statement;
	ResultSet resultSet;
	ResultSetMetaData meta;

	/* For Layout */
	SpringLayout frameLayout;

	/* For Content Pane */
	Container frameContentPane,rowInfoDialogContentPane;

	/* For Dimension and Font */
	Dimension textFieldDimension;
	Font labelFont,buttonFont;

	/* For Components */
	JLabel messageLabel,storekeeperAddedInLabel,storekeeperGetAddedInLabel,storekeeperAddedByLabel,storekeeperGetAddedByLabel,storekeeperLastUpdatedInLabel,storekeeperGetLastUpdatedByLabel,storekeeperLastUpdatedByLabel,getStorekeeperGetLastUpdatedByLabel;
	JTable storekeeperTable;
	JScrollPane storekeeperTableSp;
	JDialog storekeeperRowInfoDialog;
	DefaultTableModel storekeeperTableModel;
	JButton removeButton,cancelButton;

	public static void main(String[] args)
	{
		StorekeeperTable viewStorekeeper=new StorekeeperTable();
		viewStorekeeper.startSystem();
	}

	public StorekeeperTable()
	{
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
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,"Connection to database couldn't be established!","GroceryDesk- Error",JOptionPane.ERROR_MESSAGE);
		}

		/* For Layout */
		frameLayout=new SpringLayout();

		/* For Content Pane */
		frameContentPane=this.getContentPane();

		/* Default Table Models' definitions */
		storekeeperTableModel=new DefaultTableModel();

		/* JTable defintions */
		storekeeperTable=new JTable();

		/* JScrollPane definitions */
		storekeeperTableSp=new JScrollPane();

		/* JDialog definitions */
		storekeeperRowInfoDialog=new JDialog(this,"GroceryDesk- Row Info",true);

		/* Other definitions */
		textFieldDimension=new Dimension(200,30);
		labelFont=new Font("Segoe UI", Font.PLAIN, 11);
		buttonFont=new Font("Helvetica",Font.PLAIN,11);

		messageLabel=new JLabel("Current Storekeepers");
		storekeeperAddedInLabel=new JLabel("Added In:",SwingConstants.CENTER);
		storekeeperAddedByLabel=new JLabel("Added By:",SwingConstants.CENTER);
		storekeeperGetAddedInLabel=new JLabel("",SwingConstants.CENTER);
		storekeeperGetAddedByLabel=new JLabel("",SwingConstants.CENTER);

		removeButton=new JButton("Remove Storekeeper");
		cancelButton=new JButton("Cancel");

	}
	public void startSystem()
	{
		/* Attributes of Components */
		messageLabel.setFont(new Font("Segoe UI",Font.PLAIN,12));
		messageLabel.setForeground(Color.WHITE);
		storekeeperGetAddedInLabel.setFont(labelFont);
		storekeeperGetAddedByLabel.setFont(labelFont);

		setStorekeeperTable();
		storekeeperTableSp.setViewportView(storekeeperTable);

		/* Adding Event Listeners */
		removeButton.addActionListener(this);
		cancelButton.addActionListener(this);

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

		/* Components of Frame */
		this.add(storekeeperTableSp);
		/*------------------------------------------------storekeeperRowInfoDialog-----------------------------------------------*/

		// Attributes of Components
		storekeeperAddedInLabel.setFont(labelFont);
		storekeeperAddedByLabel.setFont(labelFont);
		rowInfoDialogContentPane=this.getContentPane();
		storekeeperRowInfoDialog.setUndecorated(true);
		storekeeperRowInfoDialog.getContentPane().setBackground(new Color(34, 49, 63));
		storekeeperRowInfoDialog.setFont(new Font("Segoe UI",Font.PLAIN,14));
		storekeeperRowInfoDialog.setOpacity(0.95f);
		storekeeperRowInfoDialog.setLayout(new GridLayout(0,2));
		storekeeperRowInfoDialog.setSize(400,100);
		storekeeperRowInfoDialog.setLocationRelativeTo(null);

		storekeeperAddedInLabel.setForeground(Color.WHITE);
		storekeeperGetAddedInLabel.setForeground(Color.WHITE);
		storekeeperAddedByLabel.setForeground(Color.WHITE);
		storekeeperGetAddedByLabel.setForeground(Color.WHITE);

		// Components of storekeeperRowInfoDialog
		storekeeperRowInfoDialog.add(storekeeperAddedInLabel);
		storekeeperRowInfoDialog.add(storekeeperGetAddedInLabel);

		storekeeperRowInfoDialog.add(storekeeperAddedByLabel);
		storekeeperRowInfoDialog.add(storekeeperGetAddedByLabel);

		storekeeperRowInfoDialog.add(removeButton);
		storekeeperRowInfoDialog.add(cancelButton);

		cancelButton.setFocusPainted(false);
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(103, 128, 159));
		cancelButton.setBorderPainted(false);
		cancelButton.setFont(buttonFont);

		removeButton.setFocusPainted(false);
		removeButton.setBackground(new Color(231, 76, 60));
		removeButton.setForeground(Color.WHITE);
		removeButton.setBorderPainted(false);
		removeButton.setFont(buttonFont);


		/* Managing Frame Layout */
//		frameLayout.putConstraint(SpringLayout.NORTH,messageLabel,5,SpringLayout.NORTH,frameContentPane);
//		frameLayout.putConstraint(SpringLayout.WEST,messageLabel,0,SpringLayout.WEST,frameContentPane);

		frameLayout.putConstraint(SpringLayout.NORTH,storekeeperTableSp,0,SpringLayout.NORTH,frameContentPane);
		frameLayout.putConstraint(SpringLayout.WEST,storekeeperTableSp,0,SpringLayout.WEST,frameContentPane);
		frameLayout.putConstraint(SpringLayout.SOUTH,storekeeperTableSp,0,SpringLayout.SOUTH,frameContentPane);
		frameLayout.putConstraint(SpringLayout.EAST,storekeeperTableSp,0,SpringLayout.EAST,frameContentPane);

		/* Attributes of Frame */
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);
				dispose();
			}
		});

		frameContentPane.setBackground(Color.WHITE);
		this.setTitle("GroceryDesk- Storekeepers");
		this.setModal(true);
		this.setResizable(true);
		this.setLayout(frameLayout);
		this.getContentPane().setBackground(new Color(103, 128, 159));
		this.setSize(500,300); //Size should be set before setting location settings.
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); //To align the frame in the centre of the display
		//this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	public void setStorekeeperTable()
	{
		/* ------------------------------------------Retrieving Table Data------------------------------------------- */

		try
		{
			statement.executeUpdate("USE grocerydesk;");
			resultSet=statement.executeQuery("SELECT id AS 'Storekeeper ID', fname AS 'First Name', lname AS 'Last Name', address AS 'Address', phone AS 'Phone' FROM storekeeper WHERE isDeleted!=1;");
			meta=resultSet.getMetaData();

			int columnCount = meta.getColumnCount();
			System.out.println(columnCount);

			/* TO GET COLUMN NAMES FOR Storekeeper */
			String[] storekeeperColumnNames = new String[columnCount];
			for (int count = 0; count <= columnCount - 1; count++)
			{
				storekeeperColumnNames[count] = meta.getColumnLabel(count + 1);
			}
			storekeeperTableModel.setColumnIdentifiers(storekeeperColumnNames);

//			/* TO SET COLUMNS FOR TABLE */
//			storekeeperTableModel.setColumnIdentifiers(storekeeperTableColumns);

			/* TO GET ROWS FOR TABLE */
			String[] rowData=new String[columnCount];
			while(resultSet.next())
			{
				for(int count=0;count<=columnCount-1;count++)
				{
					rowData[count]=resultSet.getString(count+1);
				}
				storekeeperTableModel.addRow(rowData);
			}
			storekeeperTable.setModel(storekeeperTableModel);
			//storekeeperTable.setShowGrid(false);
			storekeeperTable.setBackground(new Color(236, 240, 241));
			storekeeperTable.setSelectionBackground(new Color(103, 128, 159));
			storekeeperTable.setFont(new Font("Segoe UI",Font.PLAIN,11));
			storekeeperTable.setFocusable(false);
			storekeeperTable.setGridColor(Color.WHITE);
			storekeeperTable.setDefaultEditor(Object.class,null);

			/* Adding Mouse Listener */
			storekeeperTable.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseReleased(MouseEvent e)
				{
					super.mouseReleased(e);
					if((e.getClickCount())==2 && !e.isConsumed())
					{
						showStorekeeperRowInfo(e);
						e.consume(); //If removed, causes the poppedup dialog box to remain visible for some clicks even after clicking OK Button
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

	public void showStorekeeperRowInfo(MouseEvent e)
	{
		int rowAtPoint=storekeeperTable.rowAtPoint(e.getPoint());
		int colAtPoint=storekeeperTable.columnAtPoint(e.getPoint());
		selectedCellValue = storekeeperTable.getValueAt(rowAtPoint, colAtPoint).toString();
		selectedColumnName=storekeeperTable.getColumnName(colAtPoint);
		storekeeperSelectedRowNumber=storekeeperTable.getSelectedRow();
		//storekeeperSelectedRowNumber=rowAtPoint;
		System.out.println("THIS IS THE ROW NUMBER "+storekeeperSelectedRowNumber);
		System.out.println("THIS IS CELL VALUE "+selectedCellValue);
		System.out.println("THIS IS COLUMNS NAME "+selectedColumnName);

		try
		{
			String query = "SELECT * FROM storekeeper WHERE isDeleted!=1 LIMIT ?,1;";
			pstatement=connection.prepareStatement(query);
			pstatement.setInt(1,storekeeperSelectedRowNumber);
			resultSet = pstatement.executeQuery();
			if(resultSet.next())
			{
				String addedIn = resultSet.getString(7);
				String addedBy = resultSet.getString(8);
				System.out.println(addedIn + addedBy );
				storekeeperGetAddedInLabel.setText(addedIn);
				storekeeperGetAddedByLabel.setText(addedBy);

			}
			resultSet.close();

		}
		catch(SQLException event)
		{
			event.printStackTrace();
		}

		storekeeperRowInfoDialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JButton sourceButton=(JButton)e.getSource();
		if(sourceButton==removeButton)
		{
			try
			{
				String idFromStorekeeperTable = null;
				query="SELECT id FROM storekeeper WHERE isDeleted!=1 LIMIT ?,1";
				pstatement=connection.prepareStatement(query);
				pstatement.setInt(1,storekeeperSelectedRowNumber);
				resultSet=pstatement.executeQuery();
				if(resultSet.next())
				{
					idFromStorekeeperTable=resultSet.getString(1);
				}
				query="UPDATE storekeeper SET isDeleted=1 WHERE id=?;";
				pstatement=connection.prepareStatement(query);
				pstatement.setString(1,idFromStorekeeperTable);
				int rowsAffected=pstatement.executeUpdate();
				if(rowsAffected>0)
				{
					JOptionPane.showMessageDialog(this,"Record removed successfully!","GroceryDesk- Success!",JOptionPane.INFORMATION_MESSAGE);
					storekeeperRowInfoDialog.dispose();
				}
				else
					JOptionPane.showMessageDialog(this,"Record couldn't be removed!","GroceryDesk- Error!",JOptionPane.ERROR_MESSAGE);

			}
			catch (SQLException exp)
			{
				exp.printStackTrace();
			}


		}
		else if(sourceButton==cancelButton)
		{
			storekeeperRowInfoDialog.dispose();
		}
	}

	public void windowFocusedEvent()
	{
		DefaultTableModel storekeeperTableModel1=(DefaultTableModel)storekeeperTable.getModel();
		storekeeperTableModel1.setRowCount(0);
		setStorekeeperTable();
	}
}
