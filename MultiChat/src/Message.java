public class Message {

	private String id;        // 아이디
	private String passwd;    // 비밀번호
	private String msg;		  // 채팅메시지
	private String type;	  // 메시지 유형(로그인, 로그아웃, 메시지 전달)
	private String nickname;  // 닉네임
	
	
	public Message() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public Message(String id, String nickname, String msg ,String type, String passwd) 
	{
		this.id = id;
		this.nickname = nickname;
		this.msg =msg;
		this.type = type;
		this.passwd = passwd;
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getPasswd()
	{
		return passwd;
	}
	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
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
	

}