package com.belyabl9.client.ui.nav;

import com.belyabl9.client.TextMessageQueue;
import com.belyabl9.client.capture.audio.AudioRecorderPanel;
import com.belyabl9.client.capture.video.VideoRecorderPanel;
import com.belyabl9.api.TextMessage;
import com.belyabl9.api.User;
import com.belyabl9.client.ui.contacts.ContactListPanel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class NavigationPanel extends JPanel {

	public static String ACTIONS_CARD_NAME = "ACTIONS";
	public static String CONVERSATION_CARD_NAME = "CONVERSATION";
	public static String SEND_FILE_CARD_NAME = "SEND_FILE";
	public static String SEND_VOICE_MESSAGE_CARD_NAME = "SEND_VOICE_MESSAGE";
	public static String SEND_VIDEO_MESSAGE_CARD_NAME = "SEND_VIDEO_MESSAGE";
	
	private Map<String, JPanel> cards = new HashMap<String, JPanel>();
	
	private long id;
	private boolean isConversation;
	private User user;
	private ConversationPanel convPanel;
	
	public NavigationPanel(User user, long id) {
		this.user = user;
		
		setLayout(new CardLayout());
		ActionsPanel actionsPanel = new ActionsPanel(this);
		add(actionsPanel, ACTIONS_CARD_NAME);
		cards.put(ACTIONS_CARD_NAME, actionsPanel);
		
		convPanel = new ConversationPanel(user);

		add(convPanel, CONVERSATION_CARD_NAME);
		cards.put(CONVERSATION_CARD_NAME, convPanel);
		
		AudioRecorderPanel audioRecorderPanel = new AudioRecorderPanel(user);
		add(new AudioRecorderPanel(user), SEND_VOICE_MESSAGE_CARD_NAME);
		cards.put(SEND_VOICE_MESSAGE_CARD_NAME, audioRecorderPanel);
		
		VideoRecorderPanel videoRecorderPanel = new VideoRecorderPanel(user);
		add(new VideoRecorderPanel(user), SEND_VIDEO_MESSAGE_CARD_NAME);
		cards.put(SEND_VIDEO_MESSAGE_CARD_NAME, videoRecorderPanel);
		
		changeCard(ACTIONS_CARD_NAME);
		
		setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
	}
	
	public long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public ConversationPanel getConversationPanel() {
		return convPanel;
	}
	
	public boolean isConversation() {
		return isConversation;
	}
	
	public void changeCard(String cardName) {
		( (CardLayout) getLayout() ).show(this, cardName);
		isConversation = false;
		
		if (cardName == CONVERSATION_CARD_NAME) {
			isConversation = true;
			
			ConversationPanel convPanel = getConversationPanel();
			for ( TextMessage msg : TextMessageQueue.getMessages(user) ) {
				convPanel.addMessage(user, msg);	
			}
			ContactListPanel.getContactPanel( user.getId() ).showNewMessagesNotification(0);
			
		}

	}
	
}
