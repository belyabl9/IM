package ui.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ActionsPanel extends JPanel {

	public ActionsPanel(NavigationPanel navPanel) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JButton sendTextMessageBtn = new JButton("Conversation");
		sendTextMessageBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				navPanel.changeCard(NavigationPanel.CONVERSATION_CARD_NAME);
			}
		});
		
		JButton sendAudioMessageBtn = new JButton("Send Audio Message");
		sendAudioMessageBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				navPanel.changeCard(NavigationPanel.SEND_VOICE_MESSAGE_CARD_NAME);
			}
		});
		
		JButton sendVideoMessageBtn = new JButton("Send Video Message");
		sendVideoMessageBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				navPanel.changeCard(NavigationPanel.SEND_VIDEO_MESSAGE_CARD_NAME);
			}
		});
		
		
		JButton sendFileBtn = new JButton("Send File");
		sendFileBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				navPanel.changeCard(NavigationPanel.SEND_FILE_CARD_NAME);
			}
		});
		
		sendFileBtn.setSize(500, 100);

		add(sendTextMessageBtn);
		add(sendAudioMessageBtn);
		add(sendVideoMessageBtn);
		add(sendFileBtn);
	}
	
}
