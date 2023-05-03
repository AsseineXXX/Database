package Database.Managment.System.GUI;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Database.Managment.System.Services.QueryService;

public class DatabaseInterface extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel leftPanel, rightPanel, bottomPanel, queryPanel, resultPanel, connectionPanel;
    private JTextArea queryArea;
    private JButton executeButton, clearButton, connectButton, disconnectButton, refreshButton;
    private JLabel statusLabel, queryLabel, resultLabel, schemaLabel, tableLabel, columnLabel;
    private JComboBox<String> schemaCombo, tableCombo, columnCombo;
    private JTable resultTable;
    private JCheckBox distinctCheck, orderByCheck;
    private JRadioButton ascRadio, descRadio;
    private JTextField serverField, portField, databaseField, usernameField, searchField;
    private JPasswordField passwordField;
    private JScrollPane queryScrollPane, resultScrollPane;
    private JSplitPane splitPane;
    private DefaultTableModel resultModel;
    private Connection conn;

    public DatabaseInterface() {
        super("SQL Server Interface");
        setSize(15000, 1100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Create the left panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Create the query panel
        queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout());
        TitledBorder queryBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),
                "Query");
        queryBorder.setTitleFont(queryBorder.getTitleFont().deriveFont(Font.BOLD));
        queryPanel.setBorder(queryBorder);

        // Create the query area
        queryArea = new JTextArea(20, 60);
        queryArea.setLineWrap(true);
        queryArea.setWrapStyleWord(true);
        queryScrollPane = new JScrollPane(queryArea);
        queryPanel.add(queryScrollPane, BorderLayout.CENTER);

        // Create the execute button
        executeButton = new JButton("Execute");
        executeButton.addActionListener(this);
        queryPanel.add(executeButton, BorderLayout.SOUTH);

        // Add the query panel to the left panel
        leftPanel.add(queryPanel, BorderLayout.CENTER);
        
        // Create the result panel
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        TitledBorder resultBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray),
                "Results");
        resultBorder.setTitleFont(resultBorder.getTitleFont().deriveFont(Font.BOLD));
        resultPanel.setBorder(resultBorder);

        // Create the result table
        resultModel = new DefaultTableModel();
        resultTable = new JTable(resultModel);
        resultScrollPane = new JScrollPane(resultTable);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        // Create the status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        resultPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add the result panel to the left panel
        leftPanel.add(resultPanel, BorderLayout.SOUTH);

        // Create the right panel
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Create the connection panel
        connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridBagLayout());
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        JLabel titleLabel = new JLabel("SQL Server Connection");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        connectionPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 5);
        JLabel serverLabel = new JLabel("Server:");
        connectionPanel.add(serverLabel, gbc);
        gbc.gridx++;
        serverField = new JTextField("localhost");
        connectionPanel.add(serverField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel portLabel = new JLabel("Port:");
        connectionPanel.add(portLabel, gbc);
        gbc.gridx++;
        portField = new JTextField("1433");
        connectionPanel.add(portField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel databaseLabel = new JLabel("Database:");
        connectionPanel.add(databaseLabel, gbc);
        gbc.gridx++;
        databaseField = new JTextField();
        connectionPanel.add(databaseField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        connectionPanel.add(usernameLabel, gbc);
        gbc.gridx++;
        usernameField = new JTextField();
        connectionPanel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        connectionPanel.add(passwordLabel, gbc);
        gbc.gridx++;
        passwordField = new JPasswordField();
        connectionPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        connectionPanel.add(connectButton, gbc);
        gbc.gridy++;
        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(this);
        disconnectButton.setEnabled(false);
        connectionPanel.add(disconnectButton, gbc);

        // Create the bottom panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     // Create the table combo box
        tableCombo = new JComboBox<String>();
        tableCombo.addActionListener(this);
        tableCombo.setPreferredSize(new Dimension(150, 30));
        bottomPanel.add(new JLabel("Table:"));
        bottomPanel.add(tableCombo);

        // Add the connection panel and bottom panel to the right panel
        rightPanel.add(connectionPanel, BorderLayout.NORTH);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the left panel and right panel to the main panel
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Set the default button
        getRootPane().setDefaultButton(executeButton);

        // Set the window size and location
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /*private void connect() {
        // Get the connection parameters
        String server = serverField.getText();
        String port = portField.getText();
        String database = databaseField.getText();
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        // Create the connection string
        String connectionString = "jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database;

        // Connect to the database
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionString, username, password);
            statusLabel.setText("Connected to " + server + ":" + port + "/" + database);
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
            updateSchemaCombo();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "SQL Server JDBC driver not found", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Unable to connect to the database", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }*/
    
    
	@Override
	public void actionPerformed(ActionEvent e) {
	
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		        Object[] rowData = {"YESY TEST"};
		        resultModel.addRow(rowData);
		    }
		});
		
	}
}