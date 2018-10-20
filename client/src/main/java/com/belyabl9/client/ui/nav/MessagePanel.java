package com.belyabl9.client.ui.nav;

import com.belyabl9.api.TextMessage;
import com.belyabl9.api.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class MessagePanel extends JPanel {
	
	private JPanel createInfoPanel(User user, String date) {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1, 2));
		
		infoPanel.add(new JLabel(String.format("%s %s", user.getName(), user.getSurname())));
		JLabel dateLbl = new JLabel(date);
		
		JPanel datePanel = new JPanel(new BorderLayout());
		datePanel.add(dateLbl, BorderLayout.EAST);
		
		infoPanel.add(datePanel);
		infoPanel.setBorder(new EmptyBorder(10, 10, 0, 10) );
		return infoPanel;
	}
	
	
	private JPanel createMessagePanel(TextMessage msg) {
		JTextArea ta = new JTextArea();
		ta.setText( msg.getContent() );
		ta.setFont(new Font("Arial", Font.PLAIN, 16));
		ta.setBackground(Color.LIGHT_GRAY);
		ta.setMargin(new Insets(10,10,10,10));
		JScrollPane pane = new JScrollPane(ta);
		
		JPanel taPanel = new JPanel(new BorderLayout());
		taPanel.add(pane);
		
		taPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );
		
		return taPanel;
	}
	
	public MessagePanel(User user, TextMessage msg) {
		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		add(createInfoPanel(user, msg.getDate().toString()), BorderLayout.NORTH);
		add(createMessagePanel(msg), BorderLayout.CENTER);
	}
	
}
