package Database.Managment.System.GUI;
import Database.Managment.System.Services.QueryService;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;

public class SQLSimulatorGUI extends JFrame {
    private JList<String> dbList;
    private JList<String> tableList;
    private JTextArea queryArea;
    private JTextArea resultArea;
    private JPanel rightPanel;
    private JTable table; 
    private DefaultTableModel model;

    public SQLSimulatorGUI() {
        // Initialisation de la fenêtre
        setTitle("SQL Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création des composants
        dbList = new JList<String>(new String[] {"Database1      ", "Database2    ", "Database3    "});
        tableList = new JList<String>(new String[] {"Table1", "Table2", "Table3"});
        queryArea = new JTextArea();
        resultArea = new JTextArea();
        model = new DefaultTableModel();
        table = new JTable(model);

        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = queryArea.getText();
				QueryService qService = new QueryService();
				Map<String, Object> result = qService.runSqlCommand(query, "ECOLE");
                resultArea.setText((String)result.get("message"));
                
                if(result.keySet().contains("records")) {
                	 // Créer un DefaultTableModel pour stocker les données de la table
                	
                	ArrayList<Map<String,Object>> lines = (ArrayList<Map<String,Object>> ) result.get("records");
                	if(!(boolean)result.get("error")) {
                		Map<String, Integer> mapColIndex = (Map<String, Integer>) result.get("mapColIndex");
                		Object[][] data = new Object[lines.size()][mapColIndex.keySet().size()];
                        String[] columnNames = new String[lines.get(0).keySet().size()];
                    	for(int i=0; i<lines.size(); i++) {
                    		Map<String,Object> line = lines.get(i);
                    		Object[] row = new Object[line.keySet().size()];
                    		int j=0;
                    		for(String key : line.keySet()) {
                    			String value = (String) line.get(key);
                    			Integer index = mapColIndex.get(key);
                    			row[index] = value;
                    			if(i==0) {
                    				columnNames[index] = value;
                    			}else {
                    				data[i] = row;
                    			}
                    			
                    		}
							
                    		
                    		System.out.println("@@@ DATA TO DISPLAY : "+Arrays.asList(row));
                    	}
                    	System.out.println("@@@ columnNames : "+Arrays.asList(columnNames));
                		System.out.println("@@@ DATA: "+data);

                    	// Créer la table avec le modèle de données
                    	model.setDataVector(data, columnNames);
                    	table.repaint();
                    	table.revalidate();   
                	}
                    
                    
                    
                } 
            }
        });

        // Création du panneau gauche
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setSize(400, 400);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Database Explorer"));
        JScrollPane dbScrollPane = new JScrollPane(dbList);
        dbScrollPane.setBorder(BorderFactory.createTitledBorder("Databases"));
        JScrollPane tableScrollPane = new JScrollPane(tableList);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Tables"));
        leftPanel.add(dbScrollPane, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);

     

       

        
        // Création du panneau droit
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Query Executor"));
        
        JScrollPane queryScrollPane = new JScrollPane(queryArea);
        queryScrollPane.setBorder(BorderFactory.createTitledBorder("Enter SQL Query"));
        
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Query Result"));
        
        
        JScrollPane tScrollPane = new JScrollPane(table);
        tScrollPane.setBorder(BorderFactory.createTitledBorder("Table des enregistrements"));
        
        rightPanel.setLayout(new GridLayout(4, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        rightPanel.add(queryScrollPane, gbc);
        rightPanel.add(resultScrollPane, gbc);
        rightPanel.add(tScrollPane, gbc);
        rightPanel.add(executeButton, gbc);


        // Ajout des panneaux à la fenêtre principale
        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SQLSimulatorGUI().setVisible(true);
            }
        });
    }
}