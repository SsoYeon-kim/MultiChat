import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.JTextPane;

public class Chat_GUI extends JFrame  {

   // 로그인 패널
   private JPanel loginPanel;
   // 로그인 버튼
   protected JButton loginButton;
   // 대화명 출력 라벨
   protected JLabel outLabel;
   
   // 닉네임 라벨
   private JLabel nickLabel;
   // 닉네임 입력 텍스트 필드
   protected JTextField nick_input;
   
   // 귓속말 상대 입력 텍스트 필드
   protected JTextField recieverInput;
   
   // 로그아웃 패널
   private JPanel logoutPanel;
   // 로그아웃 버튼
   protected JButton logoutButton;
   
   // 메시지 입력 패널 구성
   private JPanel msgPanel;
   // 메시지 입력 텍스트필드
   protected JTextField msgInput;

   //귓속말 패널
   private JPanel panel;
   
   //이미지 패널
   protected JPanel imagePanel;

   // 화면 구성 전환을 위한 카드 레이아웃
   protected Container tab;
   protected CardLayout cardLayout;
   
   // 채팅 내용 출력창 
   protected JTextPane msgOut;
             
   protected String nickname;
   protected String secretReciever;
   protected JButton saveButton;
   protected JButton secretButton;
   protected JButton imageButton;
   protected JButton im_Love_Button;
   protected JButton im_Good_Button;
   protected JButton im_Angry_Button;
   
   private URL heart_im_URL;
   private ImageIcon O_heart_im_Icon;
   private ImageIcon heart_im_Icon;
   private Image O_heart_im;
   private Image heart_im;
   
   private URL angry_im_URL;
   private ImageIcon O_angry_im_Icon;
   private ImageIcon angry_im_Icon;
   private Image O_angry_im;
   private Image angry_im;
   
   private URL good_im_URL;
   private ImageIcon O_good_im_Icon;
   private ImageIcon good_im_Icon;
   private Image O_good_im;
   private Image good_im;   

   // 생성자
   public Chat_GUI()
   {
      // 메인 프레임 구성
      super("::멀티챗::");
         
         // 로그인 패널 화면 구성
         loginPanel = new JPanel();
         loginPanel.setBackground(new Color(230, 230, 250));
         
         // 로그인 패널 레이아웃 설정
         loginPanel.setLayout(new FlowLayout());
         loginButton = new JButton("입장");
         loginButton.setBackground(new Color(216, 191, 216));
         loginButton.setFont(new Font("함초롬돋움", Font.BOLD, 16));
         nick_input = new JTextField(15);
         nickLabel = new JLabel("닉네임");
         nickLabel.setFont(new Font("함초롬돋움", Font.BOLD, 15));
         loginPanel.add(nickLabel);
         loginPanel.add(nick_input);
         loginPanel.add(loginButton);
         
         // 로그아웃 패널 구성
         logoutPanel = new JPanel();
         logoutPanel.setBackground(new Color(230, 230, 250));
         
         // 로그아웃 패널 레이아웃 설정
         logoutPanel.setLayout(new BorderLayout());
         outLabel = new JLabel();
         logoutButton = new JButton("로그아웃");
         logoutButton.setBackground(new Color(216, 191, 216));
         
         // 로그아웃 패널에 위젯 구성
         logoutPanel.add(outLabel, BorderLayout.CENTER);
         logoutPanel.add(logoutButton, BorderLayout.EAST);
         
         // 메시지 입력 패널 구성
         msgPanel = new JPanel();
         msgInput = new JTextField(30);
         msgInput.setBackground(new Color(255, 255, 255));
         msgPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
         
         msgPanel.add(msgInput);
         
            // 로그인/로그아웃 패널 중 하나를 선택하는 CardLayout 패널
            tab = new JPanel();
            tab.setBackground(new Color(230, 230, 250));
            cardLayout = new CardLayout();
            tab.setLayout(cardLayout);
            tab.add(loginPanel, "login");
            tab.add(logoutPanel, "logout");
         
         // 메시지 출력 영역 초기화
         msgOut = new JTextPane();
         msgOut.setFont(new Font("함초롬돋움", Font.PLAIN, 18));
         msgOut.setBackground(new Color(248, 248, 255));
         // JTextArea의 내용을 수정하지 못하도록 한다. 즉, 출력 전용으로 사용한다.
         msgOut.setEditable(false);
         
         // 메시지 출력 영역 스크롤 바를 구성한다
         // 수직 스크롤 바는 항상 나타내고 수평 스크롤 바는 필요할 때 나타나도록 프로그래밍한다.
         JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         jsp.setBackground(SystemColor.menu);
         getContentPane().add(tab, BorderLayout.NORTH);
         getContentPane().add(jsp, BorderLayout.CENTER);
         
         panel = new JPanel();
         panel.setBackground(SystemColor.menu);
         jsp.setColumnHeaderView(panel);
         
         recieverInput = new JTextField();
         panel.add(recieverInput);
         recieverInput.setColumns(10);
         secretButton = new JButton("귓속말");
         secretButton.setBackground(new Color(240, 248, 255));
         secretButton.setFont(new Font("함초롬돋움", Font.BOLD, 16));
         panel.add(secretButton);
         
         imagePanel = new JPanel();
         imagePanel.setForeground(new Color(255, 255, 255));
         imagePanel.setBackground(SystemColor.controlLtHighlight);
         jsp.setRowHeaderView(imagePanel);
         imagePanel.setLayout(new GridLayout(0, 1, 0, 0));
         imagePanel.setVisible(false);
         
         heart_im_URL = Chat_GUI.class.getResource("/Heart.png");
         O_heart_im_Icon = new ImageIcon(heart_im_URL);
         O_heart_im = O_heart_im_Icon.getImage();
         heart_im = O_heart_im.getScaledInstance(60,60,Image.SCALE_SMOOTH);
         heart_im_Icon = new ImageIcon(heart_im);
         im_Love_Button = new JButton(heart_im_Icon);
         im_Love_Button.setBorderPainted(false);
         im_Love_Button.setFocusPainted(false);
         im_Love_Button.setContentAreaFilled(false);
         im_Love_Button.setSize(60, 60);;
         imagePanel.add(im_Love_Button);
         
         angry_im_URL = Chat_GUI.class.getResource("/Angry.png");
         O_angry_im_Icon = new ImageIcon(angry_im_URL);
         O_angry_im = O_angry_im_Icon.getImage();
         angry_im = O_angry_im.getScaledInstance(60,60,Image.SCALE_SMOOTH);
         angry_im_Icon = new ImageIcon(angry_im);
         im_Angry_Button = new JButton(angry_im_Icon);
         im_Angry_Button.setPreferredSize(new Dimension(60, 60));
         im_Angry_Button.setBorderPainted(false);
         im_Angry_Button.setFocusPainted(false);
         im_Angry_Button.setContentAreaFilled(false);
         im_Angry_Button.setSize(60,60);
         imagePanel.add(im_Angry_Button);
         
         good_im_URL = Chat_GUI.class.getResource("/Good.png");
         O_good_im_Icon = new ImageIcon(good_im_URL);
         O_good_im = O_good_im_Icon.getImage();
         good_im = O_good_im.getScaledInstance(60,60,Image.SCALE_SMOOTH);
         good_im_Icon = new ImageIcon(good_im);
         im_Good_Button = new JButton(good_im_Icon);
         im_Good_Button.setSize(new Dimension(60, 60));
         im_Good_Button.setPreferredSize(new Dimension(60, 60));
         im_Good_Button.setBorderPainted(false);
         im_Good_Button.setFocusPainted(false);
         im_Good_Button.setContentAreaFilled(false);
         im_Good_Button.setHorizontalAlignment(SwingConstants.RIGHT);
         im_Good_Button.setSize(40,40);
         imagePanel.add(im_Good_Button);
         getContentPane().add(msgPanel, BorderLayout.SOUTH);
         
         imageButton = new JButton("이모티콘");
         imageButton.setBackground(new Color(216, 191, 216));
         imageButton.setFont(new Font("함초롬돋움", Font.BOLD, 16));
         msgPanel.add(imageButton);
         saveButton = new JButton("저장");
         saveButton.setFont(new Font("함초롬돋움", Font.BOLD, 16));
         saveButton.setBackground(new Color(216, 191, 216));
         msgPanel.add(saveButton);
         cardLayout.show(tab, "login");
         
         pack();
         setSize(550,600);
         setVisible(true);
   } 

   
   public void addButtonActionListener(ActionListener listener)
   {
      // 이벤트 리스너 등록
      loginButton.addActionListener(listener);
      logoutButton.addActionListener(listener);
      msgInput.addActionListener(listener);
      saveButton.addActionListener(listener);
      secretButton.addActionListener(listener);
      imageButton.addActionListener(listener);
      im_Love_Button.addActionListener(listener);
      im_Angry_Button.addActionListener(listener);
      im_Good_Button.addActionListener(listener);
   }

}