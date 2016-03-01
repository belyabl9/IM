package ui.auth;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.client.User;
import ui.IMFrame;
import utils.Validator;
import client.Client;
import client.Session;


public class AuthorizeFrame extends JFrame {

	private JTextField loginField;
	private JPasswordField passField;
	
	private static AuthorizeFrame authFrame;
	
	private AuthorizeFrame() { 
		this.setSize(300, 200);
		createMenu();
		BoxLayout authPanelLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.getContentPane().setLayout(authPanelLayout);
		this.getContentPane().add(createInputPanel());
		this.getContentPane().add(createControlPanel());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem settingsItm = new JMenuItem("Settings");
		settingsItm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsDialog dlg = SettingsDialog.getInstance();
				dlg.setVisible(true);
				System.out.println(dlg);
			}
		});
		JMenuItem exitItm = new JMenuItem("Exit");
		
		menu.add(settingsItm);
		menu.add(exitItm);
		
		menuBar.add(menu);
		
		this.setJMenuBar(menuBar);
	}
	
	public static AuthorizeFrame getInstance() {
		if (authFrame == null) {
			return new AuthorizeFrame();
		}
		return authFrame;
	}
	
	private JPanel createComponentPanel(Component comp, String layout) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(comp, layout);
		return panel;
	}
	
	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new GridLayout(4, 1)); 
		
		JPanel loginLblPanel = createComponentPanel(new JLabel("Login"), BorderLayout.WEST);
		loginLblPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		
		JPanel passLblPanel = createComponentPanel(new JLabel("Password"), BorderLayout.WEST);
		passLblPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		loginField = new JTextField(25);
		JPanel loginTextPanel = createComponentPanel(loginField, BorderLayout.WEST);
		loginTextPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		passField = new JPasswordField(25);
		JPanel passTextPanel = createComponentPanel(passField, BorderLayout.WEST);
		passTextPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		inputPanel.add(loginLblPanel);
		inputPanel.add(loginTextPanel);
		inputPanel.add(passLblPanel);
		inputPanel.add(passTextPanel);
		
		return inputPanel;
	}
	
	private String getLogin() {
		return loginField.getText();
	}
	
	private String getPassword() {
		return new String(passField.getPassword());
	}
	
	private boolean validateFields() {
		String errorMsg = null;
		
		if ( Session.getInstance().getServerIP() == null || 
			 ! Validator.validatePort(Session.getInstance().getServerPort())
		   ) {
			errorMsg = "Please, fill mandatory fields in Settings menu";
		}
		
		if (getLogin().isEmpty() || getPassword().isEmpty())
			errorMsg = "Please, fill all mandatory fields";
		
		if (errorMsg != null) {
			JOptionPane.showMessageDialog(AuthorizeFrame.this, errorMsg);
			return false;
		}
		return true;
	}
	
	private JPanel createControlPanel() {
		JButton loginBtn = new JButton("Log in");
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(loginBtn, BorderLayout.EAST);
		controlPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
		
		loginBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!validateFields())
					return;
				if (Client.auth(getLogin(), getPassword())) {
					createSession();
					AuthorizeFrame.this.setVisible(false);
					showMainFrame();
				} else
					JOptionPane.showMessageDialog(AuthorizeFrame.this, "Login or password aren't correct");
			}
		});
		
		return controlPanel;
	}
	
	private Session createSession() {
		User user = Client.findByLogin(getLogin());
		Session session = Session.getInstance();
		session.setUser(user);
		
		return session;
	}
	
	private void showMainFrame() {
		IMFrame imFrame = IMFrame.getInstance();
		imFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imFrame.setSize(500, 500);
		imFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		AuthorizeFrame auth = AuthorizeFrame.getInstance();
		auth.setVisible(true);
	}
	
}
