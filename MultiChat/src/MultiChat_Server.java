import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class MultiChat_Server {

	boolean status;
	// 서버 소켓 및 클라이언트 연결 소켓
	private ServerSocket ss = null;
	private Socket s = null;
	
	// 연결된 클라이언트 스레드를 관리하는 ArrayList
	ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();
	
	// 로거 객체
	Logger logger;
	
	// 멀티 채팅 메인 프로그램 부분
	public void start()
	{
		logger = Logger.getLogger(this.getClass().getName());
		
		try
		{
			// 서버 소켓 생성
			ss = new ServerSocket(7898);
			logger.info("MultiChatServer start");
			
			// 무한 루프를 돌면서 클라이언트 연결을 기다린다.
			while(true)
			{
				s = ss.accept();
				// 연결된 클라이언트에 대해 스레드 클래스 생성
				ChatThread chat = new ChatThread();
				// 클라이언트 리스트 추가
		 		chatThreads.add(chat);         // chatThreads : arrayList
				// 스레드 시작
				chat.start();
			}
		}
		catch (Exception e)
		{
			logger.info("[MultiChatServer]start() Exception 발생!!");
				e.printStackTrace();
		}
	}
	
	// 연결된 모든 클라이언트에 메시지 중계
	void msgSendAll(String msg)          
	{
		for(ChatThread ct : chatThreads)
		{
			ct.outMsg.println(msg);
		}
	}
	
	// 특정 클라이언트에 메시지 중계
	void msgSendOne(String msg)
	{
		
	}
	
	class ChatThread extends Thread
	{	
		// 수신 메세지 및 파싱 메시지 처리를 위한 변수 선언
		String msg;
		
		// 메시지 객체 생성
		Message m = new Message();
		//Message n = new Message();
		
		// JSON 파서 초기화
		Gson gson = new Gson();
		
		// 입출력 스트림
		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;
		
		public void run()
		{
			status = true;
			// 상태 정보가 true이면 루프를 돌면서 사용자에게서 수신된 메시지 처리
			logger.info("ChatThread start");
			
			try
			{
				inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
				outMsg = new PrintWriter(s.getOutputStream(),true);
				
				status = true;
				
				while(status)
				{
					// 수신된 메시지를 msg 변수에 저장
					msg = inMsg.readLine();
					//nickname = innick.readLine();
					
					// JSON 메시지를 Message 객체로 매핑
					m = gson.fromJson(msg, Message.class);
					
					// Message클래스 생성자 확인
					// 로그아웃 메시지일 때
					if(m.getType().equals("logout"))
					{
						chatThreads.remove(this);
						msgSendAll(gson.toJson(new Message(m.getNickname(), "님이 종료했습니다.", "sevrer")));
						// 해당 클라이언트 스레드 종료로 status를 false로 설정       => arraylist에서 현재 chatthread를 제거
						status = false;
					}
					//로그인 메시지일 때
					else if(m.getType().equals("login"))
					{
						msgSendAll(gson.toJson(new Message("닉네임 : " + m.getNickname(), "님이 로그인했습니다.", "server")));
					}
					else
					{
						msgSendAll(msg);
					}
				}
				// 루프를 벗어나면 클라이언트 연결이 종료되므로 스레드 인터럽트
				this.interrupt();
				logger.info(this.getName() + " 종료됨!!");          
			}
			catch (IOException e) 
			{
				// TODO: handle exception
				chatThreads.remove(this);
				logger.info("[ChatThread]run() ");
				e.printStackTrace();
			}		
		}		
	}
	
	public static void main(String[] args) {
		MultiChat_Server server = new MultiChat_Server();
		server.start();
	}
}