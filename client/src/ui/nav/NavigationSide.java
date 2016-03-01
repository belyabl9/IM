package ui.nav;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.client.TextMessage;
import model.client.User;
import ui.contacts.ContactListPanel;
import client.TextMessageQueue;

public class NavigationSide extends JPanel {

	private static NavigationSide navSidePanel = new NavigationSide();
	private static List<NavigationPanel> navPanels = new ArrayList<NavigationPanel>();
	
	private static List<Long> navPanelsIDs = new ArrayList<Long>();
	private static long curNavPanelID;
	
	private NavigationSide() {
		setLayout(new CardLayout());
	}
	
	public static NavigationSide getInstance() {
		return navSidePanel;
	}
	
	public void showNavPanel(User user) {
		if ( navPanelsIDs.indexOf(user.getId()) == -1 ) {
			NavigationPanel navPanel = new NavigationPanel(user, user.getId());
			navPanels.add(navPanel);
			add(navPanel, "navPanel_" + user.getId());
			navPanelsIDs.add(user.getId());
		}
		changeNavPanel(user.getId());
	}
	
	public static NavigationPanel getNavigationPanel(User user) {
		for (NavigationPanel navPanel : navPanels) {
			if ( navPanel.getUser().getId() == user.getId() )
				return navPanel;
		}
		return null;
	}
	
	public static ConversationPanel getConversationPanel(User user) {
		NavigationPanel navPanel = getNavigationPanel(user);
		if (navPanel == null)
			return null;
		
		return navPanel.getConversationPanel();
	}
	
	public static boolean addMessage(User from, TextMessage msg) {
		ConversationPanel convPanel = getConversationPanel(from);
		if (convPanel == null)
			return false;
		
		NavigationPanel navPanel = NavigationSide.getInstance().getNavigationPanel(from);
		if ( navPanel.isConversation() ) {
			convPanel.addMessage(from, msg);
			return true;
		}
		
		return false;
	}

	public static NavigationPanel getCurrentNavigationPanel() {
		for ( NavigationPanel navPanel : navPanels ) {
			if ( navPanel.getId() == curNavPanelID )
				return navPanel;
		}
		return null;
	}
	
	public static void changeNavPanel(long id) {
		( (CardLayout) navSidePanel.getLayout() ).show(navSidePanel, "navPanel_" + id);
		curNavPanelID = id;
		
		NavigationPanel navPanel = getCurrentNavigationPanel();
		if (navPanel == null)
			return;
		
		if ( navPanel.isConversation() ) {
			ConversationPanel convPanel = navPanel.getConversationPanel();
			User from = navPanel.getUser();
			for ( TextMessage msg : TextMessageQueue.getMessages(from) ) {
				convPanel.addMessage(from, msg);	
			}
			ContactListPanel.getContactPanel( from.getId() ).showNewMessagesNotification(0);
		}
	}
	
}
