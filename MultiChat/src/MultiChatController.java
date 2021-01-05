import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.gson.Gson;

public class MultiChatController implements Runnable {
	
	private String ip = "127.0.0.1";
	private Socket socket;
	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;
	Gson gson = new Gson();
	
	MultiChat_Server mc;
	
	Message m;
	
	Thread thread;
	boolean status;
	
	static Chat_GUI mcui;
	
	Logger logger;
	
	File savefile = new File("C:/Member/msgsave.txt");
	
	// 뷰 클래스 참조 객체
			private final Chat_GUI v;
			// 데이터 클래스 참조 객체
			private final MultiChatData chatData;
			public MultiChatController(MultiChatData chatData, Chat_GUI v)
			{
				// 로거 객체 초기화
				logger = Logger.getLogger(this.getClass().getName());
				
				// 모델과 뷰 클래스 참조
				this.chatData = chatData;
				this.v = v;
			}
			
			// 데이터 객체에서 데이터 변화를 처리할 UI 객체 추가       
			public void appMain()        
			{
				chatData.addObj(v.msgOut);
			
				v.addButtonActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Object obj = e.getSource();
					
						
						if(obj == v.loginButton)
						{
							v.id = v.idInput.getText();
							v.nickname = v.nick_input.getText();						
							
							v.outLabel.setText(" 이름: " + v.id + "     닉네임 : " + v.nickname);
							v.cardLayout.show(v.tab, "logout");
							connectServer();
						}
						else if(obj == v.logoutButton)
						{
							// 로그아웃 메시지 전송
							outMsg.println(gson.toJson(new Message(v.id, v.nickname, "", "logout", "")));
							// 대화 창 클리어
							v.msgOut.setText("");
							// 로그인 패널로 전환
							v.cardLayout.show(v.tab, "login");
							try { 
	                             BufferedWriter msg_save = new BufferedWriter(new FileWriter(savefile, true));  
	                             msg_save.write("\r\n" + "다른 접속자 저장 : " + "\r" );
	                             
	                             msg_save.close();
	                          }
	                          catch (Exception ee)
	                          {
	                             ee.printStackTrace();
	                          }
							
							outMsg.close();
						
							try
							{
								inMsg.close();
								socket.close();
							}
							catch (IOException ex)
							{
								ex.printStackTrace();
							}
							status = false;
						}
						else if(obj == v.msgInput)
						{
							// 메시지 전송
							outMsg.println(gson.toJson(new Message("", v.nickname, v.msgInput.getText(), "msg", "")));
							// 입력 창 클리어
							v.msgInput.setText("");
						}
						else if(obj == v.exitButton)
						{
							try { 
	                             BufferedWriter msg_save = new BufferedWriter(new FileWriter(savefile, true));  
	                             msg_save.write("\r\n" + "다른 접속자 저장 : " + "\r" );
	                             
	                             msg_save.close();
	                          }
	                          catch (Exception ee)
	                          {
	                             ee.printStackTrace();
	                          }
							System.exit(0);
						}
						else if(obj == v.saveButton)
						{
							 try {
	                             
	                             BufferedWriter msg_save = new BufferedWriter(new FileWriter(savefile, true));  
	                             msg_save.write(v.msgOut.getText());
	                             msg_save.close();
	                             JOptionPane.showMessageDialog(v, "대화 내용이 저장되었습니다. 종료 버튼이나 로그아웃을 누르세요!");
	                          }
	                          catch (Exception ee)
	                          {
	                             ee.printStackTrace();
	                          }
						}
					}
				});
				
			}
			
		public void connectServer()
		{
			try
			{
				// 소켓 생성
				socket = new Socket(ip, 7898);
				System.out.println("[client]server 연결 성공!!");
				
				// 입출력 스트림 생성
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outMsg = new PrintWriter(socket.getOutputStream(), true);
				
				//서버에 로그인 메시지 전달
				m = new Message(v.id, v.nickname, "", "login", "");
				outMsg.println(gson.toJson(m));
				
				// 메시지 수신을 위한 스레드 생성
				thread = new Thread(this);
				thread.start();
			}
			catch (Exception e)
			{
				
				e.printStackTrace();
			}
		}
			
		public void run()
		{
			// 수신 메시지를 처리하는 데 필요한 변수 선언
			String msg;
			status = true;
			
			while(true)
			{
				try
				{
					// 메시지 수신 및 파싱
					msg = inMsg.readLine();
					m = gson.fromJson(msg, Message.class);
					
					//MultiChatData 객체로 데이터 갱신
					chatData.refreshData(m.getId() + "   << " + m.getNickname() + ">>" + "  : " + m.getMsg() + "\n");
					
					// 커서를 현재 대화 메시지에 표현
					v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
				}
				catch (IOException e)
				{
					System.out.println("[MultiChatUI]메시지 스트림 종료!!");
				}
			}
			//logger.info("[CHATUI]"+thread.getName());
		}
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			Login_GUI log_gui = new Login_GUI();
			
		}

}
	
