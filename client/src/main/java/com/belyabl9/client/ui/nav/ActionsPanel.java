package com.belyabl9.client.ui.nav;

import javax.swing.*;
import java.awt.*;

public class ActionsPanel extends JPanel {

	public ActionsPanel(NavigationPanel navPanel) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton sendTextMessageBtn = new JButton("Conversation");
		sendTextMessageBtn.addActionListener(event -> navPanel.changeCard(NavigationPanel.CONVERSATION_CARD_NAME));
		sendTextMessageBtn.setMinimumSize(new Dimension(250, 40));
		sendTextMessageBtn.setMaximumSize(new Dimension(250, 40));
		
		JButton sendAudioMessageBtn = new JButton("Send Audio Message");
		sendAudioMessageBtn.addActionListener(event -> navPanel.changeCard(NavigationPanel.SEND_VOICE_MESSAGE_CARD_NAME));
		sendAudioMessageBtn.setMinimumSize(new Dimension(250, 40));
		sendAudioMessageBtn.setMaximumSize(new Dimension(250, 40));
		
		JButton sendVideoMessageBtn = new JButton("Send Video Message");
		sendVideoMessageBtn.addActionListener(event -> navPanel.changeCard(NavigationPanel.SEND_VIDEO_MESSAGE_CARD_NAME));
		sendVideoMessageBtn.setMinimumSize(new Dimension(250, 40));
		sendVideoMessageBtn.setMaximumSize(new Dimension(250, 40));

		add(sendTextMessageBtn);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(sendAudioMessageBtn);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(sendVideoMessageBtn);
		add(Box.createRigidArea(new Dimension(0, 10)));

		setMinimumSize(new Dimension(300, Integer.MAX_VALUE));
		setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
	}
	
}
