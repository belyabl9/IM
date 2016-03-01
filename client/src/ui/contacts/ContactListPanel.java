package ui.contacts;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import model.client.Message;
import model.client.TextMessage;
import model.client.User;
import client.Client;
import client.TextMessageQueue;
import client.Session;

public class ContactListPanel extends JPanel {
	
	private static List<ContactPanel> contactList = new ArrayList<ContactPanel>();
	
	private static ContactListPanel contactListPanel = new ContactListPanel();
	
	public static ContactListPanel getInstance() {
		if (contactListPanel == null) {
			contactListPanel = new ContactListPanel();
		}
		return contactListPanel;
	}
	
	private ContactListPanel() {	
		setBorder(new LineBorder(java.awt.Color.BLUE));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
	}
	
	public void addContact(User user) {
		ContactPanel contactPanel = new ContactPanel(user);
		contactList.add(contactPanel);
		add(contactPanel);
	}
	
	public static void loadUnsentMessages() {
		for (ContactPanel contactPanel : contactList) {
			User from = contactPanel.getContact();
			List<TextMessage> unsentMessages = Client.getUnsentMessages( from.getId(), 
															         Session.getInstance().getUser().getId() 
															       );
			List<Long> ids = new ArrayList<Long>();
			for (TextMessage msg : unsentMessages)
				ids.add(msg.getId());
			TextMessageQueue.addMessages(from, unsentMessages);
			Client.markAsReadMessages(ids);
		}
	}
	
	public static void updateMessageNotifications() {
		for (ContactPanel contactPanel : contactList) {
			int cnt = TextMessageQueue.getMessagesCount(contactPanel.getContact());
			contactPanel.showNewMessagesNotification(cnt);
		}
	}
	
	public static ContactPanel getContactPanel(long id) {
		for (ContactPanel contactPanel : contactList) {
			if ( contactPanel.getContact().getId() == id ) {
				return contactPanel;
			}
		}
		return null;
	}
	
	public static  User getContact(long id) {
		return getContactPanel(id).getContact();
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ContactListPanel listPanel = new ContactListPanel();
		
		f.getContentPane().add(listPanel);
		f.pack();
		f.setVisible(true);
	}
}
