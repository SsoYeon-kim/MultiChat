# MultiChat
1학년 OOP프로그래밍설계 중 멀티 채팅

# 목차 

## 1. 주요 기술
#### [채팅]
* TCP/IP   
* 멀티스레드   
* 파일 입출력    

## 2. 코드
#### [서버]
1-1. 소켓 생성 및 연결
1-2. 귓속말

#### [Model, 메시지 규격] - M
2-1. Model - M
2-2. 메시지 규격

#### [UI] - V
3-1. 로그인
3-2. 회원가입
3-3. 채팅

#### [컨트롤러] - C
4-1. 서버 연결
4-2. 데이터 처리

<hr>

## 서버

Multichat_Server.java에 해당한다. 서버 소켓과 클라이언트 연결 소켓을 생성하고 클라이언트에 대한 스레드를 내부 클래스로 생성하여 관리한다.

<pre><code>
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
	
</code></pre>

아래의 코드는 내부클래스인 ChatThread이다.

<pre><code>
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
					
					// JSON 메시지를 Message 객체로 매핑
					m = gson.fromJson(msg, Message.class);
					
					// Message클래스 생성자 확인
					// 로그아웃 메시지일 때
					if(m.getType().equals("logout"))
					{
						chatThreads.remove(this);
						msgSendAll(gson.toJson(new Message(m.getNickname(),"", "님이 종료했습니다.", "logout")));
						// 해당 클라이언트 스레드 종료로 status를 false로 설정       => arraylist에서 현재 chatthread를 제거
						status = false;
					}
					//로그인 메시지일 때
					else if(m.getType().equals("login"))
					{
						//현재 스레드 이름을 입장한 닉네임 이름으로 설정
						Thread.currentThread().setName(m.getNickname());
						String threadName = Thread.currentThread().getName();
						System.out.println(threadName);
						
						msgSendAll(gson.toJson(new Message(m.getNickname(),"" ,"님이 로그인했습니다.", "login")));
						
					}
					//귓속말 메시지일 때
					else if(m.getType().equals("secret"))
					{	
						//귓속말 상대한테만 메시지 전달
						for (ChatThread ct : chatThreads) {
                            if (ct.getName().equals(m.getSecretReceiver())) {
                                ct.outMsg.println(gson.toJson(new Message(m.getNickname() + "님의 귓속말", "", m.getMsg(), "secret")));
                            }
                        }

					}
					//전체 메시지일 때
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
</code></pre>

클라이언트와 서버 간의 메시지 규격 중 type에 따라 구분한다. 메시지 규격은 Message.java에 정의되어 있으며 후에 설명하도록 한다.
현재 type에는 login, logout, secret, msg가 있다.

## [M]odel, 메시지 규격

본 채팅 프로젝트는 MVC패턴에 맞춰 설계했다. 데이터 처리를 하는 Model은 Controller를 이용하여 데이터 업데이트를 관리한다. 이는 MultiChatData.java에 해당한다.

<pre><code>
JTextArea msgOut;
	
	//데이터를 변경할 때 업데이트할 UI 컴포넌트를 등록
	public void addObj(JComponent comp)
	{
		this.msgOut = (JTextArea)comp;
	}
	
	//UI데이터를 업데이트
	void refreshData(String msg) 
	{
		msgOut.append(msg);
	}
</code></pre>

## [V]iew UI

View는 화면에 보이는 영역을 담당한다.
회원가입 화면인 Join_GUI.java와 로그인 화면인 Login_GUI.java, 채팅 화면인 Chat_GUI.jvava로 나뉜다. 회원가입과 로그인 화면은 컨트롤러에서 처리할 수 있도록 위임(Delegate)처리를 하지 않았다. Chat_GUI.java만 Controller인 MultiChatConroller.java에서 처리할 수 있도록 구성했다.

다음은 Login_GUI.java의 코드이다. 

## [C]ontroller 클라이언트
