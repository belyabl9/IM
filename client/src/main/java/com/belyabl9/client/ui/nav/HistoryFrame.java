package com.belyabl9.client.ui.nav;

import com.belyabl9.api.TextMessage;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HistoryFrame extends JFrame {
	
	public HistoryFrame(String from, String to, List<TextMessage> history) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Conversation history: " + from + " - " + to);
		setVisible(true);
		
		JPanel historyPanel = new JPanel();
		historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
		
		JScrollPane historyPane = new JScrollPane(historyPanel);
		
		for (TextMessage msg : history) {
			MessagePanel msgPanel = new MessagePanel(msg.getFrom(), msg);
			historyPanel.add(msgPanel);
		}
		getContentPane().add(historyPane);
		pack();
	}
	
}
