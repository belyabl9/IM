package com.belyabl9.client.ui.auth;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.belyabl9.client.Client;
import com.belyabl9.client.Session;
import com.belyabl9.client.utils.Validator;

public class SettingsDialog extends JDialog {

	private static JTextField serverIPEdt;
	private static JTextField serverPortEdt;
	
	private static SettingsDialog settingsDlg;
	
	private SettingsDialog() {
		super();
		
		setSize(250, 120);
		setModal(true);
		
		serverIPEdt = new JTextField(15);
		serverPortEdt = new JTextField(4);
		
		serverIPEdt.setText("127.0.0.1");
		serverPortEdt.setText("6666");
		
		JLabel ipLbl = new JLabel("Server IP");
		JLabel portLbl = new JLabel("Server Port");
		
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		panel.add(ipLbl);
		panel.add(serverIPEdt);
		panel.add(portLbl);
		panel.add(serverPortEdt);
		
		JPanel savePanel = new JPanel(new BorderLayout());
		JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (updateSession()) {
					Client.init(Session.getInstance().getServerIp(), Session.getInstance().getServerPort());
					SettingsDialog.settingsDlg.setVisible(false);
				}
			}
		});
		savePanel.add(saveBtn, BorderLayout.EAST);
		
		panel.add(savePanel);
		getContentPane().add(panel);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private boolean updateSession() {
		
		Session session = Session.getInstance();
		
		String serverIP = SettingsDialog.serverIPEdt.getText();
		String prevServerIP = session.getServerIp();
		
		int serverPort;
		try {
			serverPort = Integer.parseInt(SettingsDialog.serverPortEdt.getText());	
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(SettingsDialog.this, "Port number isn't valid");
			return false;
		}
		
		int prevServerPort = session.getServerPort();
		
		boolean validation = Validator.validateIP(serverIP) && Validator.validatePort(serverPort);
		if (!validation) {
			JOptionPane.showMessageDialog(SettingsDialog.this, "IP address or port aren't valid");
			return false;
		}
		
		if ( validation && !serverIP.equals(prevServerIP) ) {
			session.setServerIp(serverIP);
		}
		if ( validation && serverPort != prevServerPort ) {
			session.setServerPort(serverPort);
		}
		return true;
	}
	
	public static SettingsDialog getInstance() {
		if (settingsDlg == null) {
			settingsDlg = new SettingsDialog();
		}
		return settingsDlg;
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(500, 500);
		SettingsDialog.getInstance().setVisible(true);
	}
	
	
}
