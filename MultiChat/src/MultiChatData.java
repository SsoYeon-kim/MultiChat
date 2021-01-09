import javax.swing.JComponent;
import javax.swing.JTextArea;

public class MultiChatData {
	
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
	
	
	

}
