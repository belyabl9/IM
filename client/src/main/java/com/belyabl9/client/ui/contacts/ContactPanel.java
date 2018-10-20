package com.belyabl9.client.ui.contacts;

import com.belyabl9.api.StatusType;
import com.belyabl9.api.User;
import com.belyabl9.client.ui.IMFrame;
import lombok.NonNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class ContactPanel extends JPanel {
	
	private final User contact;
	
	private JLabel contactInfoLbl;
	private JPanel onlineLamp;
	private static Color ONLINE_LAMP_COLOR = Color.GREEN;
	private static Color OFFLINE_LAMP_COLOR = Color.GRAY;
	private static Color BUSY_LAMP_COLOR = Color.YELLOW;
	
	private int msgCounter;
	
	public ContactPanel (@NonNull User user) {
		this.contact = user;
		
		Font font = new Font("Verdana", Font.PLAIN, 14);
		
		setBorder(new LineBorder(Color.LIGHT_GRAY));
		
		contactInfoLbl = new JLabel(prepareContactInfo(user)); 
		contactInfoLbl.setAlignmentX(RIGHT_ALIGNMENT);
		contactInfoLbl.setFont(font);	     
	
		onlineLamp = new JPanel();
		onlineLamp.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setStatusLamp(user.getStatus());
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(onlineLamp);
		add(contactInfoLbl);
	     
		setBackground(Color.WHITE);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				ContactPanel contactPanel = (ContactPanel) e.getSource();
				
				for (Component component : contactPanel.getParent().getComponents()) {
					component.setBackground(Color.WHITE);
				}
				contactPanel.setBackground(Color.lightGray);
				
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

		setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
	}
	
	public User getContact() {
		return contact;
	}

	public void updateContactInfo(User user) {
		contactInfoLbl.setText(prepareContactInfo(user));
		setStatusLamp(user.getStatus());
	}
	
	public void showNewMessagesNotification(int delta) {
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
		return user.getStatus() == StatusType.ONLINE ? true : false;
	}
	
	public void setStatusLamp(StatusType status) {
		Color color;
		switch (status) {
			case ONLINE:
				color = ONLINE_LAMP_COLOR;
				break;
			case OFFLINE:
				color = OFFLINE_LAMP_COLOR;
				break;
			case BUSY:
				color = BUSY_LAMP_COLOR;
				break;
			default:
				throw new UnsupportedOperationException();
		}
	    onlineLamp.setBackground(color);
	}
	

}

