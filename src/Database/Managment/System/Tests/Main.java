package Database.Managment.System.Tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import Database.Managment.System.Services.QueryService;
import Database.Managment.System.GUI.DatabaseInterface;

public class Main implements ActionListener{
	public String currentDB;

	  JLabel l1, l2;
	  JTextArea text;
	  
	  Main()
	  {
	    JFrame f = new JFrame();
	    
	    l1 = new JLabel();
	    l1.setBounds(45,175,100,30);
	    
	    l2 = new JLabel();
	    l2.setBounds(150,175,100,30);
	    
	    text = new JTextArea();
	    text.setBounds(15,20,650,150);
	    
	    JButton btn = new JButton("Run");
	    btn.setBounds(250,210,180,30);
	    btn.addActionListener(this);
	    
	    f.add(text);
	    f.add(l1);
	    f.add(l2);
	    f.add(btn);
	    
	    f.setSize(700,300);
	    f.setLayout(null);
	    f.setVisible(true);
	  }
	  
	  public void actionPerformed(ActionEvent e)
	  {
		  String str = text.getText();
		  QueryService query = new QueryService();
		  query.runSqlCommand(str, "ECOLE");
	  }
	  
  public static void main(String[] argv) throws Exception {
	  //new Main();
	  DatabaseInterface d = new DatabaseInterface();
	  

  }
}