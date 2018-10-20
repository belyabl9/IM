package com.belyabl9.client.capture.audio;

import com.belyabl9.client.Client;
import com.belyabl9.client.Session;
import com.belyabl9.api.MediaMessage;
import com.belyabl9.api.User;
import com.belyabl9.client.ui.nav.NavigationPanel;
import com.belyabl9.client.ui.nav.NavigationSide;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.*;

public class AudioRecorderPanel extends JPanel {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
      
    private final JButton captureBtn;
	private final JButton stopBtn;
	private final JButton resetBtn;
	private final JButton sendBtn;
    
	private JTextField subjEdt;
	private JTextField filenameEdt;
      
	private User to;
    
	public AudioRecorderPanel(User to) {
        this.to = to;

        JButton returnBtn = createReturnButton(to);

        captureBtn = createCaptureButton();
        stopBtn = createStopButton();
        resetBtn = createResetButton();
        sendBtn = createSendButton(to);

        captureBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        resetBtn.setEnabled(false);
        sendBtn.setEnabled(false);
        
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
        inpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inpPanel.add(subjLbl);
        inpPanel.add(subjEdt);
        inpPanel.add(Box.createRigidArea(new Dimension(0, 10)));
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

    private JButton createReturnButton(User to) {
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(event -> NavigationSide.getNavigationPanel(to).changeCard(NavigationPanel.ACTIONS_CARD_NAME));
        return returnBtn;
    }

    private JButton createSendButton(User to) {
        JButton sendBtn = new JButton("Send");
        sendBtn.addActionListener(event -> {
            if ( getFilename().isEmpty() || getSubject().isEmpty()) {
                JOptionPane.showMessageDialog(AudioRecorderPanel.this, "Please, fill all the required fields.");
                return;
            }

            String filename = getFilename() + ".wav";
            try {
                byte[] blob = Files.readAllBytes(Paths.get(filename));
                MediaMessage msg = new MediaMessage(Session.getInstance().getUser(), to, new Date(), getSubject(), filename, blob);
                Client.sendMediaMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            resetBtn.doClick();
        });
        return sendBtn;
    }

    private JButton createResetButton() {
        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(event -> {
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
        });
        return resetBtn;
    }

    private JButton createStopButton() {
        JButton stopBtn = new JButton("Stop");
        stopBtn.addActionListener(
				event -> {
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
		);
        return stopBtn;
    }

    private JButton createCaptureButton() {
        JButton captureBtn = new JButton("Capture");
        captureBtn.addActionListener(
        		event -> {
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
		);
        return captureBtn;
    }

    private String getFilename() {
          return filenameEdt.getText();
      }
      
    private String getSubject() {
          return subjEdt.getText();
      }
    
    // This method captures audio input from a
    // microphone and saves it in an audio file.
    private void captureAudio(String fileneme) {
	    try {
            //Get things set up for capture
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine)
            AudioSystem.getLine(dataLineInfo);

            new CaptureThread(fileneme).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

      private AudioFormat getAudioFormat(){
          //8000,11025,16000,22050,44100  
          float sampleRate = 8000.0F;
          //8,16
          int sampleSizeInBits = 16;
          //1,2
          int channels = 1;
          //true,false
          boolean signed = true;
          //true,false
          boolean bigEndian = false;

          return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
      }
    
    class CaptureThread extends Thread {
      
        private String filename;
        
        CaptureThread(String filename) {
            this.filename = filename;
        }
        
        public void run() {
            AudioFileFormat.Type fileType;
            File audioFile;
    
            fileType = AudioFileFormat.Type.WAVE;
            audioFile = new File(filename);
        
            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(
                    new AudioInputStream(targetDataLine),
                    fileType,
                    audioFile
                );
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

}