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
   
   // 로그아웃 패널
   private JPanel logoutPanel;
   // 로그아웃 버튼
   protected JButton logoutButton;
   
   // 메시지 입력 패널 구성
   private JPanel msgPanel;
   // 메시지 입력 텍스트필드
   protected JTextField msgInput;
   // 종료 버튼
   

   // 화면 구성 전환을 위한 카드 레이아웃
   protected Container tab;
   protected CardLayout cardLayout;
   
   // 채팅 내용 출력창 
   protected JTextArea msgOut;
             
   protected String nickname;
   protected JButton saveButton;
     

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
         loginButton = new JButton("로그인");
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
         // 메시지 입력 테스트필드
         msgPanel.setLayout(new BorderLayout());
         msgInput = new JTextField(30);
         msgInput.setBackground(new Color(255, 255, 255));
         saveButton = new JButton("저장");
         saveButton.setBackground(new Color(216, 191, 216));
         
         msgPanel.add(msgInput, BorderLayout.WEST);
         msgPanel.add(saveButton, BorderLayout.EAST);
         
            // 로그인/로그아웃 패널 중 하나를 선택하는 CardLayout 패널
            tab = new JPanel();
            tab.setBackground(new Color(230, 230, 250));
            cardLayout = new CardLayout();
            tab.setLayout(cardLayout);
            tab.add(loginPanel, "login");
            tab.add(logoutPanel, "logout");
         
         // 메시지 출력 영역 초기화
         msgOut = new JTextArea("", 10, 30);
         msgOut.setBackground(new Color(248, 248, 255));
         // JTextArea의 내용을 수정하지 못하도록 한다. 즉, 출력 전용으로 사용한다.
         msgOut.setEditable(false);
         
         // 메시지 출력 영역 스크롤 바를 구성한다
         // 수직 스크롤 바는 항상 나타내고 수평 스크롤 바는 필요할 때 나타나도록 프로그래밍한다.
         JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
         jsp.setBackground(new Color(221, 160, 221));
         getContentPane().add(tab, BorderLayout.NORTH);
         getContentPane().add(jsp, BorderLayout.CENTER);
         getContentPane().add(msgPanel, BorderLayout.SOUTH);
         cardLayout.show(tab, "login");
         
         pack();
         setSize(400,600);
         setVisible(true);
   }
   
   public void addButtonActionListener(ActionListener listener)
   {
      // 이벤트 리스너 등록
      loginButton.addActionListener(listener);
      logoutButton.addActionListener(listener);
      msgInput.addActionListener(listener);
      saveButton.addActionListener(listener);
      
   }

}