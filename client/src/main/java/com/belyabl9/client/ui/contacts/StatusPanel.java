package com.belyabl9.client.ui.contacts;

import com.belyabl9.api.StatusType;
import com.belyabl9.api.User;
import com.belyabl9.client.Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusPanel extends JPanel  {
	
	private JComboBox statusCombo;
	private List<StatusType> statuses = new ArrayList<>();
	
	public StatusPanel(User user) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Collections.addAll(statuses, StatusType.values());
		
		Font font = new Font("Verdana", Font.PLAIN, 12);
	
	    statusCombo = new JComboBox(statuses.toArray());
	    statusCombo.setFont(font);  
	    statusCombo.setAlignmentX(LEFT_ALIGNMENT);
	    statusCombo.addActionListener(event -> {
            JComboBox box = (JComboBox)event.getSource();
            StatusType item = (StatusType) box.getSelectedItem();
            Client.updateStatus(item);
        });
	
	     add(new JLabel( user.getName() + " " + user.getSurname() ));
	     add(statusCombo);
	     
	     setVisible(true);
	}

}