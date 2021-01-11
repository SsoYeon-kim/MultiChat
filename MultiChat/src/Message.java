import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Message {

	private String msg;		  // 채팅메시지
	private String type;	  // 메시지 유형(로그인, 로그아웃, 메시지 전달)
	private String nickname;  // 닉네임
	private String secretReceiver; // 귓속말 상대
	private ImageIcon img;	//이모티콘
	
	public Message() 
	{
		// TODO Auto-generated constructor stub
	}

	public Message(String nickname, String secretReceiver, String msg ,String type) 
	{
		this.nickname = nickname;
		this.secretReceiver = secretReceiver;
		this.msg = msg;
		this.type = type;
	}
	
	public void imgMessage(String nickname, ImageIcon img) {
		this.nickname = nickname;
		this.img = img;
	}

	
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
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
	

}