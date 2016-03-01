package requests;

import java.awt.TrayIcon;
import java.util.Map;

import model.client.Message;
import model.client.Response;
import model.client.TextMessage;
import model.client.User;
import ui.IMFrame;
import ui.contacts.ContactListPanel;
import ui.nav.NavigationPanel;
import ui.nav.NavigationSide;
import client.TextMessageQueue;

public class NewMessageRequest {

	public static Response process(Object o) {
		System.out.println("NewMessageRequest was processed");
		
		Map<String, Object> content = (Map<String, Object>) o;
		TextMessage msg = (TextMessage) content.get("message");

		User from = msg.getFrom();
		if ( ! NavigationSide.addMessage(from, msg) ) {
			TextMessageQueue.addMessage(from, msg);
			ContactListPanel.getContactPanel( from.getId() ).showNewMessagesNotification(1);
		}
		
		return new Response(Response.SUCCESS, true);
	}
}
