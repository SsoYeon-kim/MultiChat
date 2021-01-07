import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Dimension;

public class Login_GUI extends JFrame {
	
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField IdTextField;
	private ButtonGroup bg = new ButtonGroup();
	protected JPanel LoginPanel;
	protected JLabel ChatLabel;
	
	File file = new File("C:/Member/member.txt");
	
	public Login_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 645);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel LoginPanel = new JPanel();
		LoginPanel.setBackground(new Color(230, 230, 250));
		contentPane.add(LoginPanel);
		LoginPanel.setLayout(null);
		
		JLabel IdLable = new JLabel("ID");
		IdLable.setBounds(99, 217, 79, 31);
	      IdLable.setFont(new Font("함초롬돋움", Font.BOLD, 25));
	      LoginPanel.add(IdLable);
	      
	      JLabel PwdLabel = new JLabel("Pwd");
	      PwdLabel.setBounds(99, 332, 82, 31);
	      PwdLabel.setFont(new Font("함초롬돋움", Font.BOLD, 25));
	      LoginPanel.add(PwdLabel);
	      
	      passwordField = new JPasswordField();
	      passwordField.setBounds(229, 325, 199, 46);
	      passwordField.setFont(new Font("함초롬돋움", Font.BOLD, 20));
	      LoginPanel.add(passwordField);
	      
	      IdTextField = new JTextField();
	      IdTextField.setBounds(229, 210, 199, 46);
	      IdTextField.setFont(new Font("함초롬돋움", Font.BOLD, 23));
	      IdTextField.setColumns(10);
	      LoginPanel.add(IdTextField);
	      
	      JButton Joinbutton = new JButton("Join");
	      Joinbutton.setBounds(381, 452, 143, 73);
	      Joinbutton.setForeground(Color.BLACK);
	      Joinbutton.setFont(new Font("함초롬돋움", Font.BOLD, 25));
	      Joinbutton.setBackground(new Color(216, 191, 216));
	      LoginPanel.add(Joinbutton);
	      
	      JLabel ChatLabel = new JLabel("");
	      ChatLabel.setBounds(228, 15, 191, 144);
	      ChatLabel.setIcon(new ImageIcon(Login_GUI.class.getResource("/Chatt.png")));
	      LoginPanel.add(ChatLabel);
	    
	      JButton Loginbutton = new JButton("Login");
	      Loginbutton.setBounds(209, 452, 143, 73);
	      Loginbutton.setForeground(Color.BLACK);
	      Loginbutton.setFont(new Font("함초롬돋움", Font.BOLD, 25));
	      Loginbutton.setBackground(new Color(216, 191, 216));
	      LoginPanel.add(Loginbutton);
	      
	      
	         JCheckBox B = new JCheckBox("B");
	         B.setBounds(13, 486, 39, 27);
	         B.setFont(new Font("함초롬돋움", Font.BOLD, 15));
	         B.setBackground(new Color(255, 255, 255));
	         LoginPanel.add(B);
	         bg.add(B);
	         
	         JCheckBox G = new JCheckBox("G\r\n");
	         G.setBounds(50, 486, 39, 27);
	         G.setFont(new Font("함초롬돋움", Font.BOLD, 15));
	         G.setBackground(new Color(255, 255, 255));
	         LoginPanel.add(G);
	         bg.add(G);
	         
	         JCheckBox O = new JCheckBox("O");
	         O.setBounds(89, 486, 45, 27);
	         O.setFont(new Font("함초롬돋움", Font.BOLD, 15));
	         O.setBackground(new Color(255, 255, 255));
	         LoginPanel.add(O);
	         bg.add(O);
	         
	      setVisible(true);
	      
	      Loginbutton.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent arg0) {
	             
	        	  if(IdTextField.getText().isEmpty() || passwordField.getText().isEmpty())
	              {
	                 JOptionPane.showMessageDialog(contentPane, "아이디와 비밀번호를 확인해주세요.");
	              }
	              else
	              {
	             	 try
	             	 {
	             		 String data;  
	             		 String[] array;
	             		 BufferedReader id_save_read = new BufferedReader(new FileReader(file));
	             		 
	             		 
	             		 while((data=id_save_read.readLine()) != null)  
	             		 {
	             			 array = data.split("/");
	             			 if(IdTextField.getText().equals(array[0]))    // 아이디가 있다!
	             				 
	             			 {
	             				 if(passwordField.getText().equals(array[1]))  // 비밀번호 같으면 닫아주고 chat이 열린다
	             				 {
	             					 dispose();
	             					MultiChatController app = new MultiChatController(new MultiChatData(), new Chat_GUI());
	             					app.appMain();
	             				 }
	             				 else                                          // 비밀번호가 틀리면 확인해달라
	             				 {
	             					JOptionPane.showMessageDialog(contentPane, "비밀번호를 확인해주세요.");
	             				 }
	             			 }
	             		 }
	             		 id_save_read.close();
	             	 }
	             	 catch (IOException e)
	             	 {
	             		 e.printStackTrace();
	             	 }                
	              }
	           }  
	       });
	      
	      Joinbutton.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent arg0) {
	             dispose();
	             Join_GUI join_gui = new Join_GUI();
	          }   
	       });
	      
	      B.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent arg0) {
	                LoginPanel.setBackground(new Color(224, 255, 255));
	             }   
	          });
	         
	         G.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent arg0) {
	                LoginPanel.setBackground(new Color(240, 255, 240));
	             }   
	          });
	         
	         O.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent arg0) {
	                LoginPanel.setBackground(new Color(255, 228, 196));
	             }   
	          });
	        
		
	}
}