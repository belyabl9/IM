package ui.contacts;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ui.IMFrame;
import model.client.StatusTypes;
import model.client.User;

public class ContactPanel extends JPanel {
	
	private User contact;
	
	private JLabel contactInfoLbl;
	private JPanel onlineLamp;
	private static Color ONLINE_LAMP_COLOR = Color.GREEN;
	private static Color OFFLINE_LAMP_COLOR = Color.GRAY;
	private static Color BUSY_LAMP_COLOR = Color.YELLOW;
	
	private int msgCounter;
	
	public ContactPanel (User user) {
		setContact(user);
		
		Font font = new Font("Verdana", Font.PLAIN, 14); 
	 
		setBorder(new LineBorder(java.awt.Color.BLUE));
		
		contactInfoLbl = new JLabel(prepareContactInfo(user)); 
		contactInfoLbl.setAlignmentX(RIGHT_ALIGNMENT);
		contactInfoLbl.setFont(font);	     
	
		onlineLamp = new JPanel();
		onlineLamp.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setStatusLamp(user.getStatus());
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(onlineLamp);
		add(contactInfoLbl);
	     
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				ContactPanel contactPanel = (ContactPanel) e.getSource();
				IMFrame.showNavPanel(contactPanel.getContact());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public User getContact() {
		return contact;
	}
	
	private void setContact(User user) {
		this.contact = user;
	}
	
	public void updateContactInfo(User user) {
		contactInfoLbl.setText(prepareContactInfo(user));
		setStatusLamp(user.getStatus());
	}
	
	public void showNewMessagesNotification(int delta) {
		if (contact == null)
			return;
		msgCounter = delta == 0 ? 0 : msgCounter + delta;
		contactInfoLbl.setText(prepareContactInfo(contact));
	}

	
	private String prepareContactInfo(User user) {
		if ( msgCounter == 0)
			return String.format("<html><body><b>%s %s</b><br>[ %s ]</body></html>", user.getName(), user.getSurname(), user.getStatus());
		else
			return String.format("<html><body><b>%s %s</b><br>[ %s ]<br>%d new messages</body></html>", user.getName(), user.getSurname(), user.getStatus(), msgCounter);
	}
	
	public boolean isOnline(User user) {
		return user.getStatus() == StatusTypes.ONLINE ? true : false;
	}
	
	public void setStatusLamp(StatusTypes status) {
		Color color = null;
		if (status == StatusTypes.ONLINE)
			color = ONLINE_LAMP_COLOR;
		else if (status == StatusTypes.OFFLINE)
			color = OFFLINE_LAMP_COLOR;
		else if (status == StatusTypes.BUSY)
			color = BUSY_LAMP_COLOR;
		
	    onlineLamp.setBackground(color);
	}
	

}

