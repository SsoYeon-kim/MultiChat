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
1-2. 귓속말(type에 따른 메시지 처리)   

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

### 소켓 생성 및 연결
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

### 귓속말(type에 따른 메시지 처리)   

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
login type일 때 접속한 스레드의 이름을 사용자가 지정한 닉네임으로 설정하게 된다. 귓속말은 secret type으로 사용자가 귓속말 할 상대의 닉네임을 설정하게 되면 해당 닉네임의 이름을 가진 스레드에만 메시지가 출력되도록 설정했다.   
msg는 기본적인 채팅을 할 때 사용자가 입력한 것으로 연결된 모든 스레드에게 전송된다.   

## [M]odel, 메시지 규격

### Model - M   

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

후에 Controller에서 데이터를 변경할 때 업데이트할 UI 컴포넌트를 등록하고 UI데이터를 업데이트하여 사용한다.   

### 메시지 규격

MVC패턴의 구성 요소는 아니지만 통신에 사용하는 JSON규격의 메시지를 자바 객체로 변환하는 데 필요한 클래스이다.    
이를 위해 구글에서 만든 JSON 파서인 Gson을 사용했다.   
   
아래의 코드는 Message.java이다.

<pre><code>
private String msg;		  // 채팅메시지
	private String type;	  // 메시지 유형(로그인, 로그아웃, 메시지 전달)
	private String nickname;  // 닉네임
	private String secretReceiver; // 귓속말 상대
	
	public Message() 
	{
		// TODO Auto-generated constructor stub
	}

	public Message(String nickname, String secretReceiver, String msg ,String type) 
	{
		this.nickname = nickname;
		this.secretReceiver = secretReceiver;
		this.msg =msg;
		this.type = type;
	}

	
	public String getSecretReceiver() 
	{
		return secretReceiver;
	}
	public void setSecretReceiver(String secretReceiver) 
	{
		this.secretReceiver = secretReceiver;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	public String getType() 
	{
		return type;
	}
	public void setType(String type) 
	{
		this.type = type;
	}
	public String getNickname() 
	{
		return nickname;
	}
	public void setNickname(String nickname) 
	{
		this.nickname = nickname;
	}
</code></pre>

사용자의 아이디와 패스워드는 Controller와 연결되지 않는 별도의 UI로 채팅창에 접속하기 위해서만 사용되므로 Message규격에는 포함하지 않았다.   
사용자가 지정하는 본인의 닉네임과 귓속말을 할 때 귓속말 상대인 secretReceiver와 채팅 메시지인 msg와 앞서 언급한 type이 메시지 규격에 포함된다.   

## [V]iew UI

View는 화면에 보이는 영역을 담당한다.   
회원가입 화면인 Join_GUI.java와 로그인 화면인 Login_GUI.java, 채팅 화면인 Chat_GUI.jvava로 나뉜다. 회원가입과 로그인 화면은 컨트롤러에서 처리할 수 있도록 위임(Delegate)처리를 하지 않았다. Chat_GUI.java만 Controller인 MultiChatConroller.java에서 처리할 수 있도록 구성했다.   

### 로그인 

다음은 Login_GUI.java의 코드이다.    
UI의 구성은 생략하고 각 컴포너늩들의 이벤트 처리 부분만 보여준다.   

<pre><code>

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
	             			 
	             			 //이미 존재하는 아이디일 경우
	             			 if(IdTextField.getText().equals(array[0]))    // 아이디가 이미 존재함
	             				 
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
	      
	      //회원가입 버튼
	      Joinbutton.addActionListener(new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent arg0) {
	             dispose();
	             Join_GUI join_gui = new Join_GUI();
	          }   
	       });
	      
	      //배경색 바꾸는 버튼
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
</code></pre>

## [C]ontroller 클라이언트
