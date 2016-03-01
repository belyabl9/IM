package ui.contacts;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.client.StatusTypes;
import model.client.User;
import client.Client;

public class StatusPanel extends JPanel  {
	
	private JComboBox statusCombo;
	private ArrayList<StatusTypes> statuses = new ArrayList<StatusTypes>();
	
	public StatusPanel(User user) {
	
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		for (StatusTypes status : StatusTypes.values()) {
			statuses.add(status);
		}
		
		 Font font = new Font("Verdana", Font.PLAIN, 12);
	
	     statusCombo = new JComboBox(statuses.toArray());
	     statusCombo.setFont(font);  
	     statusCombo.setAlignmentX(LEFT_ALIGNMENT);
	     statusCombo.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             JComboBox box = (JComboBox)e.getSource();
	             StatusTypes item = (StatusTypes) box.getSelectedItem();
	             Client.updateStatus(item);
	         }
	     });
	
	     add(new JLabel( user.getName() + " " + user.getSurname() ));
	     add(statusCombo);
	     
	     setVisible(true);
	}

}