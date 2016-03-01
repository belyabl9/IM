package capture;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.nav.NavigationPanel;
import ui.nav.NavigationSide;
import model.client.MediaMessage;
import model.client.MediaMessagePacket;
import model.client.User;
import client.Client;
import client.Session;

public class AudioRecorderPanel extends JPanel {

	  AudioFormat audioFormat;
	  TargetDataLine targetDataLine;
	  
	  final JButton captureBtn = new JButton("Capture");
	  final JButton stopBtn = new JButton("Stop");
	  final JButton resetBtn = new JButton("Reset");
	  final JButton sendBtn = new JButton("Send");
	
	  final JPanel btnPanel = new JPanel();
	  
	  private JTextField subjEdt;
	  private JTextField filenameEdt;
	  
	  private User to;
	
	  public AudioRecorderPanel(User to) {
		this.to = to;
		  
	    captureBtn.setEnabled(true);
	    stopBtn.setEnabled(false);
	    resetBtn.setEnabled(false);
	    sendBtn.setEnabled(false);
	
		JButton returnBtn = new JButton("Return");
		returnBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NavigationSide.getNavigationPanel(to).changeCard(NavigationPanel.ACTIONS_CARD_NAME);
			}
		});
	    
	    captureBtn.addActionListener(
	      new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
				if ( getFilename().isEmpty() || getSubject().isEmpty()) {
					JOptionPane.showMessageDialog(AudioRecorderPanel.this, "Fill all required fields");
					return;
				}
				
			  captureBtn.setText("Recording...");
				
	          captureBtn.setEnabled(false);
	          stopBtn.setEnabled(true);
	          resetBtn.setEnabled(false);
	          sendBtn.setEnabled(false);
	          captureAudio(getFilename() + ".wav");
	        }
	      }
	    );
	
	    stopBtn.addActionListener(
	      new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	          captureBtn.setEnabled(false);
	          stopBtn.setEnabled(false);
	          resetBtn.setEnabled(true);
	          sendBtn.setEnabled(true);
	          
	          captureBtn.setText("Capture");
	          
	          subjEdt.setEditable(false);
	          filenameEdt.setEditable(false);

	          targetDataLine.stop();
	          targetDataLine.close();
	        }
	      }
	    );
	
	    resetBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			    captureBtn.setEnabled(true);
			    sendBtn.setEnabled(false);
			    stopBtn.setEnabled(false);
			    resetBtn.setEnabled(false);
			    
			    captureBtn.setText("Capture");
			    
			    new File(getFilename() + ".wav").delete();
		        subjEdt.setEditable(true);
		        subjEdt.setText("");
		        filenameEdt.setEditable(true);
		        filenameEdt.setText("");
			}
		});
	    

	    sendBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( getFilename().isEmpty() || getSubject().isEmpty()) {
					JOptionPane.showMessageDialog(AudioRecorderPanel.this, "Fill all required fields");
					return;
				}
				
				String filename = getFilename() + ".wav";
				MediaMessage msg = new MediaMessage(Session.getInstance().getUser(), to, new Date(), getSubject(), filename);
				MediaMessagePacket packet = null;
				try {
					packet = new MediaMessagePacket(msg, Files.readAllBytes(Paths.get(filename)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Client.sendMediaMessage(packet);
				
		        resetBtn.doClick();
			}
		});
	    
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    
	    JPanel inpPanel = new JPanel();
	    inpPanel.setLayout(new BoxLayout(inpPanel, BoxLayout.Y_AXIS));
	    
	    JPanel btnPanel = new JPanel();
	    
	    JLabel subjLbl = new JLabel("Subject");
	    subjEdt  = new JTextField(100);
	    subjEdt.setMaximumSize(new Dimension(500, 25));
	    
	    JLabel filenameLbl = new JLabel("Filename");
	    filenameEdt  = new JTextField(100);
		filenameEdt.setMaximumSize(new Dimension(500, 25));
	    
		JPanel wrapperPanel = new JPanel(new BorderLayout());
	    inpPanel.add(subjLbl);
	    inpPanel.add(subjEdt);
	    inpPanel.add(filenameLbl);
	    inpPanel.add(filenameEdt);
	    wrapperPanel.add(inpPanel, BorderLayout.CENTER);
	    
	    btnPanel.add(captureBtn);
	    btnPanel.add(stopBtn);
	    btnPanel.add(resetBtn);
	    btnPanel.add(sendBtn);

	    mainPanel.add(wrapperPanel);
	    mainPanel.add(btnPanel);
	    
	    //Finish the GUI and make visible
	    setLayout(new BorderLayout());
	    add(returnBtn, BorderLayout.NORTH);
	    add(mainPanel, BorderLayout.CENTER);
	  }
	  
	  private String getFilename() {
		  return filenameEdt.getText();
	  }
	  
	  private String getSubject() {
		  return subjEdt.getText();
	  }
	
	  //This method captures audio input from a
	  // microphone and saves it in an audio file.
	  private void captureAudio(String fileneme){
	    try{
	      //Get things set up for capture
	      audioFormat = getAudioFormat();
	      DataLine.Info dataLineInfo =
	                          new DataLine.Info(
	                            TargetDataLine.class,
	                            audioFormat);
	      targetDataLine = (TargetDataLine)
	               AudioSystem.getLine(dataLineInfo);
	
	      new CaptureThread(fileneme).start();
	    }catch (Exception e) {
	      e.printStackTrace();
	      System.exit(0);
	    }//end catch
	  }//end captureAudio method
	

	  private AudioFormat getAudioFormat(){
	    float sampleRate = 8000.0F;
	    //8000,11025,16000,22050,44100
	    int sampleSizeInBits = 16;
	    //8,16
	    int channels = 1;
	    //1,2
	    boolean signed = true;
	    //true,false
	    boolean bigEndian = false;
	    //true,false
	    return new AudioFormat(sampleRate,
	                           sampleSizeInBits,
	                           channels,
	                           signed,
	                           bigEndian);
	  }
	
	class CaptureThread extends Thread{
	  
		private String filename;
		
		public CaptureThread(String filename) {
			this.filename = filename;
		}
		
		public void run(){
		    AudioFileFormat.Type fileType = null;
		    File audioFile = null;
	
		    fileType = AudioFileFormat.Type.WAVE;
	      	audioFile = new File(filename);
		
		    try{
		      targetDataLine.open(audioFormat);
		      targetDataLine.start();
		      AudioSystem.write(
		            new AudioInputStream(targetDataLine),
		            fileType,
		            audioFile);
		    }catch (Exception e){
		      e.printStackTrace();
		    }
	
	  }
		
	}

}