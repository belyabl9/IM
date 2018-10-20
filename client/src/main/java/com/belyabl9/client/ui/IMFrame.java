package com.belyabl9.client.ui;

import com.belyabl9.client.Client;
import com.belyabl9.client.Session;
import com.belyabl9.api.User;
import com.belyabl9.client.ui.auth.AuthorizeFrame;
import com.belyabl9.client.ui.contacts.ContactListPanel;
import com.belyabl9.client.ui.contacts.ContactPanel;
import com.belyabl9.client.ui.contacts.StatusPanel;
import com.belyabl9.client.ui.nav.NavigationSide;

import javax.swing.*;
import java.awt.*;


public class IMFrame extends JFrame {
	private static IMFrame FRAME = new IMFrame();

	private static SystemTray systemTray = SystemTray.getSystemTray();
	private static TrayIcon trayIcon;
	
	private JPanel  contactsPanel;
	private JPanel  mainPanel;
	private JPanel  statusPanel;
	

	
	private static boolean isMinimized;
	
	private IMFrame() {
		initComponents();
	}

	public static IMFrame getInstance() {
		return FRAME;
	}
	
	public static void showNavPanel(User user) {
		NavigationSide.getInstance().showNavPanel(user);
		FRAME.pack();
	}
	
	
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contactsPanel = new JPanel();
		JScrollPane contactsPane = new JScrollPane(contactsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   
		contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
		
		mainPanel = new JPanel(new BorderLayout());
		ContactListPanel listPanel = ContactListPanel.getInstance();
		for (User user : Client.getAllUsers()) {
			if (user.getId() != Session.getInstance().getUser().getId() )
				listPanel.addContact(user);
		}
		ContactListPanel.loadUnsentMessages();
		ContactListPanel.updateMessageNotifications();
		
		contactsPanel.add(listPanel);

		startUpdateStatusBackgroundTask();
		
		statusPanel = new StatusPanel(Session.getInstance().getUser());
		mainPanel.add(statusPanel, BorderLayout.SOUTH);
		
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		
		sidePanel.add(contactsPane);
		
		mainPanel.add(sidePanel, BorderLayout.WEST);
		mainPanel.add(NavigationSide.getInstance(), BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		JMenuItem logOutItem = new JMenuItem("Log Out");
		logOutItem.addActionListener(event -> {
            IMFrame.this.setVisible(false);
            AuthorizeFrame.getInstance().setVisible(true);
        });
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(event -> IMFrame.this.setVisible(false));
		menu.add(logOutItem);
		menu.add(exitItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Conversation", mainPanel);
		tabbedPane.addTab("Media MailBox", MediaPanel.getInstance());
		tabbedPane.addChangeListener(event -> IMFrame.this.pack());
		
		if (Session.getInstance().getUser().getLogin().equals("admin")) {
			tabbedPane.addTab("Users", AdminPanel.getInstance());
		}
		
		getContentPane().add(tabbedPane);
		
		setMinimumSize(new Dimension(350, 450));
		setMaximumSize(new Dimension(350, 450));
		
		pack();
	}

	private void startUpdateStatusBackgroundTask() {
		new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10_000);
                    
                    User self = Session.getInstance().getUser();
                    Client.updateStatus(self.getStatus());
                    
                    for ( User user : Client.getAllUsers() ) {
                        ContactPanel contactPanel = ContactListPanel.getContactPanel(user.getId());
                        if ( contactPanel != null )
                            contactPanel.updateContactInfo(user);
                    }
                    
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
	}

	private void removeTrayIcon() {
		isMinimized = false;
		systemTray.remove(trayIcon);
	}
	  
	private void addTrayIcon() {
		try {
		  isMinimized = true;
		  systemTray.add(trayIcon);
		} catch(AWTException e) {
			throw new RuntimeException(e);
		}
	}
	  
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> getInstance().setVisible(true));
	}
}
