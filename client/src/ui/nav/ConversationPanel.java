package ui.nav;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import model.client.TextMessage;
import model.client.User;
import ui.IMFrame;
import client.Client;
import client.Session;

public class ConversationPanel extends JPanel {
	
	private JPanel messagesPanel;
	private JTextArea sendMsgArea;
	private User to;
	
	public ConversationPanel(User to) {
		setLayout(new BorderLayout());
		
		JButton returnBtn = new JButton("Return");
		returnBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NavigationSide.getNavigationPanel(to).changeCard(NavigationPanel.ACTIONS_CARD_NAME);
			}
		});
		
		JPanel convPanel = new JPanel();
		BoxLayout messagesLayout = new BoxLayout(convPanel, BoxLayout.Y_AXIS);
		convPanel.setLayout(messagesLayout);	
		
		JScrollPane messagesPanel = createMessagesPanel();
		
		sendMsgArea = new JTextArea(10, 0);
		sendMsgArea.setLineWrap(true);
		JScrollPane textPane = new JScrollPane(sendMsgArea);
		textPane.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		
		convPanel.add(messagesPanel);
		convPanel.add(textPane);
		convPanel.add(createControlPanel());
		
		add(returnBtn, BorderLayout.NORTH);
		add(convPanel, BorderLayout.CENTER);
		
		this.to = to;
		
		this.repaint();
		
	}
	
	private JPanel getMessagesPanel() {
		return messagesPanel;
	}
	
	private void setMessagesPanel(JPanel messagesPanel) {
		this.messagesPanel = messagesPanel;
	}
	
	
	private TextMessage getMessage() {
		String content = sendMsgArea.getText();
		Session session = Session.getInstance();
		
		User from = session.getUser();
		to = Client.findById(to.getId());
		return new TextMessage(from, to, new Date(), content);
	}
	
	public JPanel createControlPanel() {
		JPanel controlPanel = new JPanel(new BorderLayout());
		JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TextMessage msg = getMessage();
				Client.sendTextMessage(msg);
				ConversationPanel.this.addMessage(msg.getFrom(), msg);
				sendMsgArea.setText(null);
				ConversationPanel.this.doLayout();
			}
		});
		
		JButton historyBtn = new JButton("History");
		historyBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HistoryFrame historyFrame = new HistoryFrame(Session.getInstance().getUser().getName(), to.getName(), Client.conversationHistory( Session.getInstance().getUser().getId(), to.getId()));
			}
		});
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(historyBtn);
		buttonsPanel.add(sendBtn);
		
		controlPanel.add(buttonsPanel, BorderLayout.EAST);
		
		controlPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		
		return controlPanel;
	}
	
	public JScrollPane createMessagesPanel() {
		JPanel messagesPanel = new JPanel();
		JScrollPane pane = new JScrollPane(messagesPanel);
		BoxLayout messagesPanelLayout = new BoxLayout(messagesPanel, BoxLayout.Y_AXIS);
		messagesPanel.setLayout(messagesPanelLayout);
		setMessagesPanel(messagesPanel);
		return pane;
	}
	
	public void addMessage(User user, TextMessage msg) {
		getMessagesPanel().add(new MessagePanel(user, msg));
		IMFrame.getInstance().pack();
	}
	
}
