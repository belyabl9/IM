package com.belyabl9.client.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.belyabl9.client.Client;
import com.belyabl9.api.MediaMessage;
import com.belyabl9.api.User;
import com.belyabl9.client.utils.FileService;

public class MediaPanel extends JPanel {

	private JTable table;
	private Map<Integer, MediaMessage> messages = new HashMap<>();
	
	private static MediaPanel mediaFrame = new MediaPanel();
	private static List<MediaMessage> mediaMessages;
	
	public static MediaPanel getInstance() {
		return mediaFrame;
	}
	
	private MediaPanel() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		DefaultTableModel model = new DefaultTableModel(null, new String[] { "Subject", "From", "Date" } ) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
			}
		};
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel btnPanel = new JPanel();
		JButton playBtn = new JButton("Play");
		playBtn.addActionListener(event -> {
            MediaMessage msg = getMediaMessage();
            if (msg == null) {
				return;
			}
            new MPlayer("media/" + msg.getPath());
        });
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(event -> refreshMessages());
		JButton removeBtn = new JButton("Remove");
		removeBtn.addActionListener(event -> {
            MediaMessage msg = getMediaMessage();
            if (msg != null) {
                Client.removeMediaMessage(msg.getId());
                DefaultTableModel model1 = (DefaultTableModel) table.getModel();
                model1.removeRow(table.getSelectedRow());
            }
        });
		
		btnPanel.add(playBtn);
		btnPanel.add(refreshBtn);
		btnPanel.add(removeBtn);
		
		add(new JScrollPane(table));
		add(btnPanel);
		
		refreshMessages();
		
	}
	
	public void refreshMessages() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		//Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		mediaMessages = Client.getMediaMessages(null);
		for (MediaMessage msg : mediaMessages) {
			addMediaMessage(msg);
		}
	}
	
	private void addMediaMessage(MediaMessage msg) {
		User userFrom = msg.getFrom();
		String date = msg.getDate().toString();
		String subject = msg.getSubject();
		
		String from = String.format("%s %s (%s)", userFrom.getName(), userFrom.getSurname(), userFrom.getNickname());
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		messages.put(table.getRowCount(), msg);
		model.addRow(new Object[] { subject, from, date } );
	}
	
	private MediaMessage getMediaMessage() {
		int row = table.getSelectedRow();
		if ( row == -1)
			return null;
		
		return messages.get(row);
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new MediaPanel());
		f.setVisible(true);
	}
}
