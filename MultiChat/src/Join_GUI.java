import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Join_GUI extends JFrame {

   private JPanel contentPane;
   private JTextField NumField_1;
   private Container buttonGroup_1;
   private JTextField IdText_J;
   private JPasswordField passwordField_J;
   private JPasswordField passwordField1_J;
   private JTextField NumField_2;
   private Container buttonGroup;
   File file = new File("C:/Member/member.txt");
   
   
   public Join_GUI() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 600, 800);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      contentPane.setLayout(new BorderLayout(0, 0));
      setContentPane(contentPane);
      
      JPanel MemberPanel = new JPanel();
      MemberPanel.setBackground(new Color(230, 230, 250));
      MemberPanel.setBounds(0, 0, 578, 744);
      contentPane.add(MemberPanel);
      MemberPanel.setLayout(null);
      
       JButton MemberButton_J = new JButton("\uAC00\uC785\uC644\uB8CC");
         MemberButton_J.setBackground(new Color(216, 191, 216));
         MemberButton_J.setFont(new Font("함초롬돋움", Font.BOLD, 18));
         MemberButton_J.setBounds(419, 677, 117, 52);
         MemberPanel.add(MemberButton_J);
         
         JLabel label = new JLabel("(\uD544\uC218)");
         label.setFont(new Font("함초롬돋움", Font.BOLD, 15));
         label.setBounds(384, 460, 45, 25);
         MemberPanel.add(label);
         
         JLabel Mem_Label = new JLabel("  JOIN US !");
         Mem_Label.setIcon(new ImageIcon(Join_GUI.class.getResource("/people.png")));
         Mem_Label.setBounds(74, 12, 462, 128);
         Mem_Label.setFont(new Font("함초롬돋움", Font.BOLD, 50));
         MemberPanel.add(Mem_Label);
         
         JLabel IdLabel_J = new JLabel("ID");
         IdLabel_J.setBounds(74, 177, 79, 31);
         IdLabel_J.setFont(new Font("함초롬돋움", Font.BOLD, 23));
         MemberPanel.add(IdLabel_J);
         
         JLabel PwdLabel_J = new JLabel("Pwd");
         PwdLabel_J.setBounds(74, 267, 82, 31);
         PwdLabel_J.setFont(new Font("함초롬돋움", Font.BOLD, 23));
         MemberPanel.add(PwdLabel_J);
         
         JLabel Pwd1Label_J = new JLabel("Pwd \uD655\uC778");
         Pwd1Label_J.setBounds(71, 361, 127, 40);
         Pwd1Label_J.setFont(new Font("함초롬돋움", Font.BOLD, 23));
         MemberPanel.add(Pwd1Label_J);
         
         JLabel AgeLabel_J = new JLabel("Age");
         AgeLabel_J.setBounds(74, 452, 72, 33);
         AgeLabel_J.setFont(new Font("함초롬돋움", Font.BOLD, 23));
         MemberPanel.add(AgeLabel_J);
         
         JLabel NumberLabel_J = new JLabel("Phone_Num");
         NumberLabel_J.setBounds(74, 532, 148, 34);
         NumberLabel_J.setFont(new Font("함초롬돋움", Font.BOLD, 23));
         MemberPanel.add(NumberLabel_J);
         
         JLabel SNSLabel_J = new JLabel(" (\uC120\uD0DD)    SNS \uB3D9\uC758");
         SNSLabel_J.setBounds(91, 605, 167, 34);
         SNSLabel_J.setFont(new Font("함초롬돋움", Font.BOLD, 18));
         MemberPanel.add(SNSLabel_J);
         
         JCheckBox WCheckBox = new JCheckBox("\uC5EC");
         //buttonGroup_1.add(WCheckBox);
         WCheckBox.setBounds(442, 455, 52, 27);
         WCheckBox.setBackground(new Color(230, 230, 250));
         WCheckBox.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
         MemberPanel.add(WCheckBox);
         
         JCheckBox MCheckBox_1 = new JCheckBox("\uB0A8");
        // buttonGroup_1.add(MCheckBox_1);
         MCheckBox_1.setBounds(503, 455, 52, 27);
         MCheckBox_1.setBackground(new Color(230, 230, 250));
         MCheckBox_1.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
         MemberPanel.add(MCheckBox_1);
         
         IdText_J = new JTextField();
         IdText_J.setBounds(231, 173, 127, 40);
         IdText_J.setFont(new Font("함초롬돋움", Font.BOLD, 20));
         IdText_J.setColumns(10);
         MemberPanel.add(IdText_J);
         
         passwordField_J = new JPasswordField();
         passwordField_J.setBounds(231, 264, 199, 40);
         passwordField_J.setFont(new Font("함초롬돋움", Font.BOLD, 20));
         MemberPanel.add(passwordField_J);
         
         passwordField1_J = new JPasswordField();
         passwordField1_J.setFont(new Font("함초롬돋움", Font.BOLD, 20));
         passwordField1_J.setBounds(231, 361, 199, 39);
         MemberPanel.add(passwordField1_J);
         
         NumField_2 = new JTextField();
         NumField_2.setFont(new Font("함초롬돋움", Font.BOLD, 15));
         NumField_2.setBounds(440, 532, 84, 36);
         MemberPanel.add(NumField_2);
         NumField_2.setColumns(10);
         
         JComboBox comboBox_Age = new JComboBox();
         comboBox_Age.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
         comboBox_Age.setForeground(new Color(0, 0, 0));
         comboBox_Age.setBackground(new Color(255, 255, 255));
         comboBox_Age.setBounds(231, 452, 93, 32);
         comboBox_Age.addItem("19");
         comboBox_Age.addItem("20");
         comboBox_Age.addItem("21");
         comboBox_Age.addItem("22");
         comboBox_Age.addItem("23");
         comboBox_Age.addItem("24");
         comboBox_Age.addItem("25");
         comboBox_Age.addItem("26");
         comboBox_Age.addItem("27");
         comboBox_Age.addItem("28");
         comboBox_Age.addItem("29");
         comboBox_Age.addItem("30");
         comboBox_Age.addItem("31");
         comboBox_Age.addItem("32");
         comboBox_Age.addItem("33");
         comboBox_Age.addItem("34");
         comboBox_Age.addItem("35");
         comboBox_Age.setSelectedItem(null);
         MemberPanel.add(comboBox_Age);
         
         JLabel NumLineLabel_1 = new JLabel("-");
         NumLineLabel_1.setBounds(419, 540, 17, 18);
         MemberPanel.add(NumLineLabel_1);
         
         NumField_1 = new JTextField();
         NumField_1.setFont(new Font("함초롬돋움", Font.BOLD, 15));
         NumField_1.setBounds(332, 530, 79, 36);
         MemberPanel.add(NumField_1);
         NumField_1.setColumns(10);
         
         JComboBox comboBox_Num = new JComboBox();
         comboBox_Num.setBackground(new Color(255, 255, 255));
         comboBox_Num.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
         comboBox_Num.setModel(new DefaultComboBoxModel(new String[] {"010", "011", "016", "017", "018", "019"}));
         comboBox_Num.setBounds(236, 532, 72, 33);
         
         comboBox_Num.setSelectedItem(null);
         MemberPanel.add(comboBox_Num);
         
         JLabel NumLineLabel_2 = new JLabel("-");
         NumLineLabel_2.setBounds(316, 539, 36, 18);
         MemberPanel.add(NumLineLabel_2);
         
         JCheckBox SnsCheckBox = new JCheckBox(" YES");
         //buttonGroup.add(SnsCheckBox);
         SnsCheckBox.setFont(new Font("함초롬돋움", Font.BOLD, 15));
         SnsCheckBox.setBackground(new Color(230, 230, 250));
         SnsCheckBox.setBounds(286, 612, 72, 27);
         MemberPanel.add(SnsCheckBox);
         
         JCheckBox SnsCheckBox_1 = new JCheckBox(" NO");
         //buttonGroup.add(SnsCheckBox_1);
         SnsCheckBox_1.setFont(new Font("함초롬돋움", Font.BOLD, 15));
         SnsCheckBox_1.setBackground(new Color(230, 230, 250));
         SnsCheckBox_1.setBounds(379, 611, 79, 27);
         MemberPanel.add(SnsCheckBox_1);
         
         JButton Idcheckbutton = new JButton("\uC911\uBCF5\uD655\uC778");
         Idcheckbutton.setFont(new Font("함초롬돋움", Font.BOLD, 19));
         Idcheckbutton.setBounds(431, 173, 111, 40);
         MemberPanel.add(Idcheckbutton);
         
         JLabel idcheck_label = new JLabel("\u203B \uD31D\uC5C5\uCC3D\uC774 \uC548\uB728\uBA74 \uC0AC\uC6A9\uAC00\uB2A5!");
         idcheck_label.setFont(new Font("함초롬돋움", Font.PLAIN, 15));
         idcheck_label.setBounds(365, 216, 203, 21);
         MemberPanel.add(idcheck_label);
         
         setVisible(true);
         
         MemberButton_J.addActionListener(new ActionListener() {
             @Override
              public void actionPerformed(ActionEvent e) {
                   
                if((IdText_J.getText().isEmpty() || passwordField_J.getText().isEmpty()))
                {
                   JOptionPane.showMessageDialog(contentPane, "아이디와 비밀번호를 확인해주세요.");
                }
                else
                {  
                    if(!(passwordField_J.getText().equals(passwordField1_J.getText())))
                          {
                             JOptionPane.showMessageDialog(contentPane, "비밀번호가 다릅니다. 다시 입력해주세요");
                          }
                    else
                    {
                       if(WCheckBox.isSelected() == true)
                         {
                          try {
                             
                             BufferedWriter id_save = new BufferedWriter(new FileWriter(file, true));  
                             id_save.write(IdText_J.getText() + "/");
                             id_save.write(passwordField_J.getText() + "/");
                             id_save.write(NumField_1.getText() + "-" + NumField_2.getText() +"/" + "\r\n");
                             id_save.close();
                             JOptionPane.showMessageDialog(contentPane, "환영합니다!");
                          }
                          catch (Exception ee)
                          {
                             ee.printStackTrace();
                          }
                          
                            dispose();
                            Login_GUI login_gui = new Login_GUI();
                         }
                       if(MCheckBox_1.isSelected() == true)
                       {
                          try {
                             
                             BufferedWriter id_save = new BufferedWriter(new FileWriter(file, true)); 
                             id_save.write(IdText_J.getText() + "/");
                             id_save.write(passwordField_J.getText() + "/");
                             id_save.write(NumField_1.getText() + "-" + NumField_2.getText()+"/" + "\r\n");
                             id_save.close();
                             JOptionPane.showMessageDialog(contentPane, "환영합니다!");
                          }
                          catch (Exception ew)
                          {
                             ew.printStackTrace();
                          }
                          
                          dispose();
                          Login_GUI login_gui = new Login_GUI();
                        
                       }
                    }
                }
             
              }
                    
             });
         
         Idcheckbutton.addActionListener(new ActionListener() {       //>>>>>>>>>>>>>>>중복확인
             @Override
             public void actionPerformed(ActionEvent arg0) {
              
              {
                 try {
                        String data;
                        String[] array;
                        BufferedReader id_save_read = new BufferedReader(new FileReader(file));  
                        while((data=id_save_read.readLine()) != null)  
                        {
                           array = data.split("/");
                           if(IdText_J.getText().equals(array[0]))
                           {
                              JOptionPane.showMessageDialog(contentPane, "이미 존재하는 ID 입니다.");
                           }
                        }
                        
                  }
                  catch (Exception e)
                  {
                     e.printStackTrace();
                  }
              }
             }
         });
              
          
   }
}