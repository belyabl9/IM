package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import utils.FileService;
import client.Client;
import model.client.MediaMessage;
import model.client.MediaMessagePacket;
import model.client.User;

public class MediaPanel extends JPanel {

	private JTable table;
	private Map<Integer, MediaMessage> messages = new HashMap<Integer, MediaMessage>();
	
	private static MediaPanel mediaFrame = new MediaPanel();
	private static List<MediaMessagePacket> messagePackets;
	
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
		playBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MediaMessage msg = getMediaMessage();
				if (msg == null)
					return;
				
				new MPlayer(msg.getPath());
			}
		});
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshMessages();
			}
		});
		JButton removeBtn = new JButton("Remove");
		removeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MediaMessage msg = getMediaMessage();
				if ( msg != null ) {
					Client.removeMediaMessage(msg.getId());
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.removeRow(table.getSelectedRow());
				}
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
		if ( messagePackets != null ) {
			List<MediaMessagePacket> packets = new ArrayList<MediaMessagePacket>();
			if ( messagePackets.size() <= 0 )
				return;
			MediaMessagePacket last = messagePackets.get(messagePackets.size() - 1);
			packets = Client.getMediaMessages(last.msg.getDate());

			for ( MediaMessagePacket msg :  packets ) {
				FileService.saveFile(msg.blob, msg.msg.getPath());
				messagePackets.add(msg);
				addMediaMessage(msg.msg);
			}
		} else {
			messagePackets = Client.getMediaMessages(null);
			for ( MediaMessagePacket msg :  messagePackets ) {
				FileService.saveFile(msg.blob, msg.msg.getPath());
				addMediaMessage(msg.msg);
			}
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
