package ui.nav;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SaveFileDialog extends JDialog {

	private static JTextField filenameEdt;
	private String filename;
	
	private static SaveFileDialog acceptFileDlg;
	
	private SaveFileDialog(String recommendedFilename) {
		super();
		
		this.filename = recommendedFilename;
		
		setSize(250, 120);
		setModal(true);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		filenameEdt = new JTextField(25);
		filenameEdt.setEnabled(false);
		filenameEdt.setMaximumSize(new Dimension(300, 50));
				
		JPanel btnPanel = new JPanel();
		JButton chooseBtn = new JButton("Choose File");
		JButton acceptBtn = new JButton("Accept");
		acceptBtn.setEnabled(false);
		
		btnPanel.add(chooseBtn);
		chooseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(new File(filename));
				int returnVal = fc.showSaveDialog(SaveFileDialog.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					filename = fc.getSelectedFile().getAbsolutePath();
					filenameEdt.setText(filename);
					acceptBtn.setEnabled(true);
				}
			}
		});
		
		acceptBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptFileDlg.setVisible(false);
			}
		});
		
		btnPanel.add(acceptBtn);
		
		add(filenameEdt);
		add(btnPanel);
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				filename = null;
				filenameEdt.setText("");
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {

			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public static SaveFileDialog getInstance(String filename) {
		if (acceptFileDlg == null) {
			acceptFileDlg = new SaveFileDialog(filename);
		}
		return acceptFileDlg;
	}
	
}
