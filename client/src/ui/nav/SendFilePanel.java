package ui.nav;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.client.Response;
import model.client.User;
import ui.IMFrame;
import client.Client;
import client.Session;


public class SendFilePanel extends JPanel {
	
	private User to;
	private JTextField fileEdt;
	private JButton sendBtn;
	private JLabel processingLbl;
	
	public SendFilePanel(User to) {
	
		setLayout(new BorderLayout());
		JButton returnBtn = new JButton("Return");
		returnBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NavigationSide.getNavigationPanel(to).changeCard(NavigationPanel.ACTIONS_CARD_NAME);
			}
		});
		
		JPanel sendFilePanel = new JPanel();
		sendFilePanel.setLayout(new BoxLayout(sendFilePanel, BoxLayout.Y_AXIS));
		
		fileEdt = new JTextField(25);
		fileEdt.setEnabled(false);
		fileEdt.setMaximumSize(new Dimension(300, 50));
				
		JPanel btnPanel = new JPanel();
		JButton chooseBtn = new JButton("Choose File");
		sendBtn = new JButton("Send File");
		sendBtn.setEnabled(false);
		
		btnPanel.add(chooseBtn);
		chooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(SendFilePanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getAbsolutePath();
					fileEdt.setText(path);
					sendBtn.setEnabled(true);
				}
			}
		});
		
		sendBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String filename = fileEdt.getText();
						byte[] blob;
						try {
							processingLbl.setText("File is being sent . . .");
							Path path = Paths.get(filename);
							blob = Files.readAllBytes(path);
							Response resp = Client.sendFile(Session.getInstance().getUser(), Client.findById(to.getId()), blob, path.getFileName().toString() );
							processingLbl.setText("");
							
							JOptionPane.showMessageDialog(IMFrame.getInstance(), 
									String.format("File was%s successfully sent", !resp.isSuccessful() || ! (boolean) resp.getContent() ? "'nt" : ""));
							
							fileEdt.setText("");
							sendBtn.setEnabled(false);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}).start();

			}
		});
		
		btnPanel.add(sendBtn);
		
		processingLbl = new JLabel("");
		
		sendFilePanel.add(fileEdt);
		sendFilePanel.add(btnPanel);
		sendFilePanel.add(processingLbl);
		
		add(returnBtn, BorderLayout.NORTH);
		add(sendFilePanel, BorderLayout.CENTER);
		
		this.to = to;
		
		this.repaint();
	}

}
