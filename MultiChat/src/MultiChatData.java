
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class MultiChatData {
	
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

}
