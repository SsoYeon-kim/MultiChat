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
1-2. type에 따른 메시지 처리(귓속말, 이모티콘, 채팅 등) 
   
## MVC 패턴   
#### [Model, 메시지 규격] - M   
2-1. Model - M   
2-2. 메시지 규격   

#### [UI] - V   
3-1. 로그인   
3-2. 회원가입   
3-3. 채팅 - V   
 
#### [컨트롤러] - C   
4-1. 서버 연결   
4-2. 데이터 처리   
4-3. run 함수

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
	
	```
	public static void main(String[] args) {
		MultiChat_Server server = new MultiChat_Server();
		server.start();
	}
	
</code></pre>

### type에 따른 메시지 처리(귓속말, 이모티콘, 채팅 등) 

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
					//이모티콘 메시지일 때
					else if(m.getType().equals("img"))
					{
						msgSendAll(msg);
					}
					//전체 메시지일 때
					else if(m.getType().equals("msg"))
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
현재 type에는 login, logout, secret, msg, img가 있다.   
login type일 때 접속한 스레드의 이름을 사용자가 지정한 닉네임으로 설정하게 된다. 귓속말은 secret type으로 사용자가 귓속말 할 상대의 닉네임을 설정하게 되면 해당 닉네임의 이름을 가진 스레드에만 메시지가 출력되도록 설정했다.   
msg는 기본적인 채팅을 할 때 사용자가 입력한 것으로 연결된 모든 스레드에게 전송된다.   
img는 이모티콘을 보낼 때 사용되는 type으로 연결된 모든 스레드에 전송되도록 했다.

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
	JTextPane msgOut;
	
	//하트 이모티콘
	ImageIcon O_Heart_icon = new ImageIcon(MultiChatData.class.getResource("/Heart.png"));
	Image O_Heart_img = O_Heart_icon.getImage();
	Image Heart_img = O_Heart_img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	ImageIcon Heart_icon = new ImageIcon(Heart_img);
	//화남 이모티콘
	ImageIcon O_Angry_icon = new ImageIcon(MultiChatData.class.getResource("/Angry.png"));
	Image O_Angry_img = O_Angry_icon.getImage();
	Image Angry_img = O_Angry_img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	ImageIcon Angry_icon = new ImageIcon(Angry_img);
	//따봉 이모티콘
	ImageIcon O_Good_icon = new ImageIcon(MultiChatData.class.getResource("/Good.png"));
	Image O_Good_img = O_Good_icon.getImage();
	Image Good_img = O_Good_img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	ImageIcon Good_icon = new ImageIcon(Good_img);
		
	//데이터를 변경할 때 업데이트할 UI 컴포넌트를 등록
	public void addObj(JComponent comp)
	{
		this.msgOut = (JTextPane)comp;
	}
	
	//UI데이터(채팅,이모티콘)를 업데이트
	void refreshData(String msg) 
	{
		StyledDocument doc = msgOut.getStyledDocument();
		
		//하트 이모티콘
		if (msg.contains("(하트)")) {
			try {
				doc.insertString(doc.getLength(),msg, null);

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msgOut.insertComponent(new JLabel(Heart_icon));
			
		}
		//화남 이모티콘
		else if(msg.contains("(화남)")) {
			
			try {
				doc.insertString(doc.getLength(), msg, null);

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msgOut.insertComponent(new JLabel(Angry_icon));
			
		}
		//따봉 이모티콘
		else if(msg.contains("(따봉)")) {
			
			try {
				doc.insertString(doc.getLength(), msg, null);

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msgOut.insertComponent(new JLabel(Good_icon));
			
		}
		//채팅
		else {
			try {
				doc.insertString(doc.getLength(), msg, null);

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
</code></pre>

사용자의 아이디와 패스워드는 Controller와 연결되지 않는 별도의 UI로 채팅창에 접속하기 위해서만 사용되므로 Message규격에는 포함하지 않았다.   
사용자가 지정하는 본인의 닉네임과 귓속말을 할 때 귓속말 상대인 secretReceiver와 채팅 메시지인 msg와 앞서 언급한 type이 메시지 규격에 포함된다.   
이모티콘을 보낼 경우 후에 Controller에서 msg에 해당 이모티콘의 이름을 보내고 해당되는 이모티콘 이름에 따른 처리를 했다.   

## [V]iew UI

View는 화면에 보이는 영역을 담당한다.   
회원가입 화면인 Join_GUI.java와 로그인 화면인 Login_GUI.java, 채팅 화면인 Chat_GUI.jvava로 나뉜다. 회원가입과 로그인 화면은 컨트롤러에서 처리할 수 있도록 위임(Delegate)처리를 하지 않았다. Chat_GUI.java만 Controller인 MultiChatConroller.java에서 처리할 수 있도록 구성했다.   

### 로그인 

다음은 Login_GUI.java의 코드이다.    
UI의 구성은 생략하고 각 컴포너늩들의 이벤트 처리 부분만 보여준다.   

<pre><code>
	
	```
	
        File file = new File("C:/MultiChatInfo/member.txt");
	
	public Login_GUI() {
	
	```
	      
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
	        
		 
	}

</code></pre>

회원가입을 통하여 사용자가 작성한 아이디, 비밀번호 등을 .txt 파일로 저장하였으며 로그인 창에서 파일을 읽어 존재하는 회원이 맞는지 확인하는 과정이 포함된다.   

<img src= "https://user-images.githubusercontent.com/62587484/104087310-33abaa80-52a2-11eb-9baa-f8e44bec1117.PNG" width=30%><img src= "https://user-images.githubusercontent.com/62587484/104087350-81c0ae00-52a2-11eb-9878-f0c0259a36aa.PNG" width=30%><img src= "https://user-images.githubusercontent.com/62587484/104087369-90a76080-52a2-11eb-9b37-af188c24f1a7.PNG" width=30%><img src= "https://user-images.githubusercontent.com/62587484/104087391-a0bf4000-52a2-11eb-82aa-ee26712bf583.PNG" width=30%>



### 회원가입

다음은 Join_GUI.java의 코드이다.   
위와 마찬가지로 구성은 제외하고 컴포넌트의 이벤트만 보여준다.   

<pre><code>

	```
   String path = "C:/MultiChatInfo";
	
   File Folder = new File(path);
   File file = new File(path + File.separator + "member.txt");
   
   public Join_GUI() {
   
      ```
         
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
   						
   							//회원가입정보 저장
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
  						
  							//회원가입정보 저장
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
         
         //아이디 중복 확인
         Idcheckbutton.addActionListener(new ActionListener() {      
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
</code></pre>

사용자가 회원가입에서 입력한 정보를 저장할 폴더를 생성하고 폴더 안에 Member.txt를 만들어 "/"로 구분하여 정보를 저장하게 된다.   
아이디를 만들 때 Member.txt를 불러와 이미 존재하는 아이디인지 중복확인을 하게 된다.   

<img src= "https://user-images.githubusercontent.com/62587484/104087600-4c1cc480-52a4-11eb-8aed-2e80bd386b97.PNG" width=30%><img src= "https://user-images.githubusercontent.com/62587484/104087613-72426480-52a4-11eb-9ee5-21bfcccbda9d.PNG" width=30%>

### 채팅 - V

채팅UI는 MVC패턴 구조 중 V에 속한다. 이는 Chat_GUI.java에 해당한다.   
다음은 Chat_GUI.java의 코드이다.

<pre><code>
public Chat_GUI()
   {
      // 메인 프레임 구성
      super("::멀티챗::");
         
            ```
   
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

</code></pre>

앞선 Login_GUI.java와 Join_GUI.java와는 다르게 클래스 내부에서 해당하는 컴포넌트의 이벤트를 처리하지 않고 Controller에서 처리하도록 하였다.   
각 컴포넌트에 해당하는 이벤트는 다음으로 설명할 컨트롤러에서 보도록 한다.    
위의 코드에 msgInput은 사용자들의 채팅이 오가는 창이다. 이는 JTextArea로 설정할 시 이모티콘을 창에 띄울 수 없다. 따라서 JTextPane으로 설정해야 한다.   

## [C]ontroller 

컨트롤러는 MultiChatController.java에 해당한다.   

<pre><code>
```

	Logger logger;

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
		
```

</code></pre>

우선 MultiChatController 클래스는 Chat_GUI와 MultiChatData를 생성하고 참조하기 때문에 위와 같은 코드를 추가한다.   

### 서버 연결 

아래의 코드는 서버와 연결하는 connectServer함수의 코드이다.   

<pre><code>
	private String ip = "127.0.0.1";
	private Socket socket;
	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;

	Gson gson = new Gson();
	
	Message m;
	
	Thread thread;
	
	boolean status;
	
	```
	
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
	
	```
</code></pre>

소켓을 생성하고 서버에 로그인 메시지를 전달한다. Message.java에 정의한 메시지 규격인 nickname, secretReceiver, msg, type에 맞춰 전달하게 되고 이때 type을 login으로 하여 서버에서 type에 맞는 처리를 하도록 한다.   

### 데이터 처리 

View에 해당하는 Chat_GUI.java에서 위임한 동적인 요소를 처리하게 된다.   
다음은 appMain함수의 코드이다.   

<pre><code>
	String path = "C:/MultiChatInfo/msgSave";

	File Folder = new File(path);
	
	```

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
					//이모티콘 버튼
					else if(obj == v.imageButton) {
						v.imagePanel.setVisible(true);
					}
					//하트 이모티콘 버튼
					else if(obj == v.im_Love_Button) {
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", "(하트)", "img")));
						v.imagePanel.setVisible(false);
					}
					//화남 이모티콘 버튼
					else if(obj == v.im_Angry_Button) {
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", "(화남)", "img")));
						v.imagePanel.setVisible(false);
					}
					//따봉 이모티콘 버튼
					else if(obj == v.im_Good_Button) {
						// 메시지 전송
						outMsg.println(gson.toJson(new Message(v.nickname, "", "(따봉)", "img")));
						v.imagePanel.setVisible(false);
					}
				}
			});
				
		}
		
	```

</code></pre>

MultiChatData.java의 함수 addObj에 채팅이 출력되는 JText인 msgOut를 등록한 후 각 컴포넌트에 대한 이벤트 처리를 한다.   
저장 버튼을 누르게 되면 채팅내용을 저장할 폴더를 생성하고 폴더 안에 저장 버튼을 누른 사용자의 닉네임으로 .txt파일이 저장된다. 동일한 사용자가 저장 버튼을 다시 누르게 되면 같은 파일에 저장되지만 이를 구분하기 위해 저장 버튼을 누를 때 해당하는 년 / 월 / 일 / 시 / 분 / 초를 포함하여 구분할 수 있게 했다.   

<img src= "https://user-images.githubusercontent.com/62587484/104156765-20791600-542d-11eb-8859-8aa5ab53930c.PNG" width=60%>   

### run 함수

MultiChat_Server.java에서 Thread 클래스를 상속받았기 때문에 자바는 다중 상속을 지원하지 않으므로 Runnable 인터페이스를 구현하는 방법으로 하였다.   
다음은 run 함수의 코드이다.   

<pre><code>

```

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
	
```

</code></pre>

MultiChatData.java의 함수인 refreshData를 이용하여 메시지 규격에 맞춰 UI데이터를 업데이트 해준다.   
로그인, 로그아웃 / 귓속말 채팅 / 이모티콘 / 전체 채팅 의 경우로 나누어 출력의 형태를 다르게 설정하였다.   
아래의 사진은 실행 화면을 보여준다.   
<img src= "https://user-images.githubusercontent.com/62587484/104156884-58805900-542d-11eb-9642-3c8e06ae1aad.PNG" width=100%>
