package ui;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ui.auth.AuthorizeFrame;
import ui.contacts.ContactListPanel;
import ui.contacts.ContactPanel;
import ui.contacts.StatusPanel;
import ui.nav.NavigationSide;
import model.client.MediaMessage;
import model.client.MediaMessagePacket;
import model.client.User;
import client.Client;
import client.RequestListener;
import client.Session;


	public class IMFrame extends JFrame {

	    private JPanel  contactsPanel;
	    private JPanel  mainPanel;
	    private JPanel  statusPanel;
		
	    private static IMFrame imFrame = new IMFrame();
		private static SystemTray systemTray = SystemTray.getSystemTray();
		private static TrayIcon trayIcon;
	    
		private static boolean isMinimized;
		
		private IMFrame() {
	        initComponents();
	    }

		public static IMFrame getInstance() {
			return imFrame;
		}
		
		public static void showNavPanel(User user) {
			NavigationSide.getInstance().showNavPanel(user);
			imFrame.pack();
		}
		
		
	    private void initComponents() {
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        contactsPanel = new JPanel();
	        JScrollPane contactsPane = new JScrollPane(contactsPanel,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	       
	        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
		    
		    mainPanel = new JPanel(new BorderLayout());
			ContactListPanel listPanel = ContactListPanel.getInstance();
			for (User u : Client.getAllUsers()) {
				if (u.getId() != Session.getInstance().getUser().getId() )
					listPanel.addContact(u);
			}
			
			ContactListPanel.loadUnsentMessages();
			ContactListPanel.updateMessageNotifications();
			
			contactsPanel.add(listPanel);
	    
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(10000);
							
							User self = Session.getInstance().getUser();
							Client.updateStatus(self.getStatus());
							
							for ( User user : Client.getAllUsers() ) {
								ContactPanel contactPanel = ContactListPanel.getContactPanel(user.getId());
								if ( contactPanel != null )
									contactPanel.updateContactInfo(user);
							}
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
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
			logOutItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					IMFrame.this.setVisible(false);
					AuthorizeFrame.getInstance().setVisible(true);
				}
			});
			JMenuItem exitItem = new JMenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					IMFrame.this.setVisible(false);
				}
			});
			menu.add(logOutItem);
			menu.add(exitItem);
			menuBar.add(menu);
			setJMenuBar(menuBar);
			


			new Thread(new RequestListener(0)).start();
			
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Conversation", mainPanel);
			tabbedPane.addTab("Media MailBox", MediaPanel.getInstance());
			tabbedPane.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					IMFrame.this.pack();
				}
			});
			
			if ( Session.getInstance().getUser().getLogin().equals("admin") ) {
				tabbedPane.addTab("Users", AdminPanel.getInstance());
			}
			
			getContentPane().add(tabbedPane);
			
			pack();
	    }
	    
		  private void removeTrayIcon()
		  {
			isMinimized = false;
		    systemTray.remove(trayIcon);
		  }
		  
		  private void addTrayIcon()
		  {
		    try
		    {
		      isMinimized = true;
		      systemTray.add(trayIcon);
		    }
		    catch(AWTException ex)
		    {
		      ex.printStackTrace();
		    }
		  }
		  
	    public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {

	            public void run() {
	                getInstance().setVisible(true);
	            } 
	        });
	    }
	}
