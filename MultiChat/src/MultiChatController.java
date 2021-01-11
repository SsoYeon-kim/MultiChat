import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
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
	
	String path = "C:/MultiChatInfo/msgSave";

	File Folder = new File(path);
	
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
						
					//로그인버튼
					if(obj == v.loginButton)
					{
						v.nickname = v.nick_input.getText();		
						v.outLabel.setText(" 닉네임 : " + v.nickname);
						v.cardLayout.show(v.tab, "logout");
						connectServer();
					}
					//로그아웃 버튼
					else if(obj == v.logoutButton)
					{
						// 로그아웃 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname,"","","logout")));
						// 대화 창 클리어
						v.msgOut.setText("");
						// 로그인 UI로 전환
						v.dispose();
						Login_GUI loginUI = new Login_GUI();
						
							
						outMsg.close();
						
						try{
								inMsg.close();
								socket.close();
							}catch (IOException ex){
								ex.printStackTrace();
							}
						//소켓 연결 끊기
						status = false;
					}
					//메세지 입력
					else if(obj == v.msgInput)
					{
						if(v.msgInput.getText().isEmpty()) {
							JOptionPane.showMessageDialog(v, "대화를 입력하세요.");
						}
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", v.msgInput.getText(), "msg")));
						// 입력 창 클리어
						v.msgInput.setText("");
					}
					//귓속말 버튼
					else if(obj == v.secretButton) 
					{
						v.secretReciever = v.recieverInput.getText();	
						
						if(v.secretReciever.isEmpty()) {
							JOptionPane.showMessageDialog(v, "귓속말 상대의 닉네임을 입력해주세요.");
						}
						else if(v.msgInput.getText().isEmpty()) {
							JOptionPane.showMessageDialog(v, "대화를 입력한 후 누르세요.");
						}
						else {
							outMsg.println(gson.toJson(new Message(v.nickname, v.secretReciever, v.msgInput.getText(), "secret")));
							v.msgInput.setText("");
						}
						
					}
					//저장 버튼
					else if(obj == v.saveButton)
					{
						//msgSave폴더 생성
						if (!Folder.exists()) {
							try{
							    	Folder.mkdir(); 
						        } catch(Exception eee){
							    eee.getStackTrace();
							}
							
					    }else {
							System.out.println("이미 폴더가 생성되어 있습니다.");
						}
						
						//대화내용 저장
						try {	
								SimpleDateFormat dataformat = new SimpleDateFormat ("yyyy년 MM월 dd일 HH시 mm분 ss초");
								String time = dataformat.format (System.currentTimeMillis());
								
								File saveFile = new File(path + File.separator + v.nickname + "님의 대화저장.txt");
								
	                            BufferedWriter msg_save = new BufferedWriter(new FileWriter(saveFile, true));  
	                            msg_save.write(v.msgOut.getText() + "\n[" + time + " 저장]\n\n"  );
	                            msg_save.close();
	                            JOptionPane.showMessageDialog(v, "대화 내용이 저장되었습니다.");
	                          }catch (Exception ee)
	                          {
	                             ee.printStackTrace();
	                          }
						
					}
					else if(obj == v.imageButton) {
						v.imagePanel.setVisible(true);
					}
					else if(obj == v.im_Love_Button) {
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", "(하트)", "img")));
						v.imagePanel.setVisible(false);
					}
					else if(obj == v.im_Angry_Button) {
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", "(화남)", "img")));
						v.imagePanel.setVisible(false);
					}
					else if(obj == v.im_Good_Button) {
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", "(따봉)", "img")));
						v.imagePanel.setVisible(false);
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
			m = new Message(v.nickname, "","", "login");
			outMsg.println(gson.toJson(m));
				
			// 메시지 수신을 위한 스레드 생성
			thread = new Thread(this);
			thread.start();
		}catch (Exception e){
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
				if(m.getMsg()!=null) {
					//로그인, 로그아웃
					if(m.getType().equals("login") || m.getType().equals("logout")) {
						chatData.refreshData("  ▶  " + m.getNickname() + m.getMsg() + "\n");
					}
					//귓속말채팅
					else if(m.getType().equals("secret")) {
						chatData.refreshData("  << " + m.getNickname() + ">>" + "  : " + m.getMsg() + "\n");
					}
					//채팅
					else if(m.getType().equals("msg")) {
						chatData.refreshData("  [ " + m.getNickname() + "]" + "  : " + m.getMsg() + "\n");
					}
					//이모티콘
					else {
						chatData.refreshData("  < " + m.getNickname() + "  님이 보낸  " + m.getMsg() + "이모티콘 \n");
					}
				}
				
				// 커서를 현재 대화 메시지에 표현
				v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
				
			}catch (IOException e)
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