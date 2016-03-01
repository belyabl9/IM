package capture;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.client.MediaMessage;
import model.client.MediaMessagePacket;
import model.client.StatusTypes;
import model.client.User;

import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import ui.nav.NavigationPanel;
import ui.nav.NavigationSide;
import client.Client;
import client.Session;

public class VideoRecorder extends JPanel {
	
	  private OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
	  private CanvasFrame canvasFrame = new CanvasFrame("Cam");

	  final JButton captureBtn = new JButton("Capture");
	  final JButton stopBtn = new JButton("Stop");
	  final JButton resetBtn = new JButton("Reset");
	  final JButton sendBtn = new JButton("Send");
	
	  final JPanel btnPanel = new JPanel();
	  
	  private JTextField subjEdt;
	  private JTextField filenameEdt;
	  
	  private User to;
	
    public static final String VIDEO_TMP_FILENAME = "video.avi";
    public static final String AUDIO_TMP_FILENAME = "audio.mp3";

    public VideoRecorder(User to) {
    	canvasFrame.setVisible(false);
    	
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
					JOptionPane.showMessageDialog(VideoRecorder.this, "Fill all required fields");
					return;
				}
				canvasFrame.setVisible(true);
			  captureBtn.setText("Recording...");
				
	          captureBtn.setEnabled(false);
	          stopBtn.setEnabled(true);
	          resetBtn.setEnabled(false);
	          sendBtn.setEnabled(false);
	          
	          captureVideo(getFilename() + ".avi");
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

	          try {
				grabber.stop();
			    canvasFrame.dispose();
			} catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
				e1.printStackTrace();
			}
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
			    
			    new File(VIDEO_TMP_FILENAME).delete();
			    new File(AUDIO_TMP_FILENAME).delete();
			    new File(getFilename() + ".avi").delete();
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
					JOptionPane.showMessageDialog(VideoRecorder.this, "Fill all required fields");
					return;
				}
				
				String filename = getFilename() + ".avi";
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
    
    
	public static void sync(String audio, String video, String output) throws org.bytedeco.javacv.FrameGrabber.Exception, org.bytedeco.javacv.FrameRecorder.Exception {
   	    FrameGrabber grabber1 = new FFmpegFrameGrabber(video); 
        FrameGrabber grabber2 = new FFmpegFrameGrabber(audio); 

		grabber1.start();
	    grabber2.start(); 
	
	    FFmpegFrameRecorder recorder3 = new FFmpegFrameRecorder(output, grabber1.getImageWidth(), grabber1.getImageHeight(), 2); 

	    recorder3.setFormat("avi");
	    recorder3.setFrameRate(25); 
	    recorder3.setSampleRate(grabber2.getSampleRate()); 
	
	    recorder3.start(); 
	    
	    Frame frame1, frame2 = null; 
	
	    while ((frame1 = grabber1.grabFrame()) != null || 
	          (frame2 = grabber2.grabFrame()) != null) { 

	    	recorder3.record(frame1); 
	        recorder3.record(frame2); 
	    } 

	    recorder3.stop(); 
	    grabber1.stop(); 
	    grabber2.stop(); 
	}
		  
		  private String getFilename() {
			  return filenameEdt.getText();
		  }
		  
		  private String getSubject() {
			  return subjEdt.getText();
		  }
		

		  
		  private void captureVideo(String fileneme){
		    try{
		      new CaptureThread(fileneme).start();
		    }catch (Exception e) {
		      e.printStackTrace();
		      System.exit(0);
		    }
		  }

		
		
		class CaptureThread extends Thread{
		  
			private String filename;
			
			public CaptureThread(String filename) {
				this.filename = filename;
			}
			
			public void run(){
		        try {
					grabber.start();
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
					e1.printStackTrace();
				}
		   
				File	outputFile = new File(AUDIO_TMP_FILENAME);
				AudioFormat	audioFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					44100.0F, 16, 2, 4, 44100.0F, false);

				DataLine.Info	info = new DataLine.Info(TargetDataLine.class, audioFormat);
				TargetDataLine	targetDataLine = null;
				try
				{
					targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
					targetDataLine.open(audioFormat);
				}
				catch (LineUnavailableException e)
				{
					e.printStackTrace();
					System.exit(1);
				}


				AudioFileFormat.Type	targetType = AudioFileFormat.Type.WAVE;
				SimpleAudioRecorder	recorder = new SimpleAudioRecorder(
					targetDataLine,
					targetType,
					outputFile);

		        IplImage grabbedImage = null;
				try {
					grabbedImage = grabber.grab();
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
					e.printStackTrace();
				}
		        

		        canvasFrame.setCanvasSize(grabbedImage.width(), grabbedImage.height());
		    
		        grabber.setFrameRate(15);
		        
		        FFmpegFrameRecorder recorder_v = new FFmpegFrameRecorder(VIDEO_TMP_FILENAME,  grabber.getImageWidth(),grabber.getImageHeight());
		        
		        recorder_v.setVideoCodec(13);
		        recorder_v.setFormat("avi");
		        recorder_v.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
		        recorder_v.setFrameRate(25);
		        recorder_v.setVideoBitrate(10 * 1024 * 1024);

				try {
					recorder_v.start();
				} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
				}
		        recorder.start();
		        try {
					while (canvasFrame.isVisible() && (grabbedImage = grabber.grab()) != null) {
					    canvasFrame.showImage(grabbedImage);
					    recorder_v.record(grabbedImage);
					}
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
				} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
				}
		        try {
					recorder_v.stop();
					recorder.stopRecording();
				} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
				}

		        try {
					sync(AUDIO_TMP_FILENAME, VIDEO_TMP_FILENAME, getFilename() + ".avi");
				} catch (org.bytedeco.javacv.FrameGrabber.Exception
						| org.bytedeco.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	
    
    public static void main(String[] args) throws Exception {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 500);
        f.getContentPane().add(new VideoRecorder(new User(0, "n", "s", "nn", "l", "p", StatusTypes.OFFLINE, new Date(), "1.1.1.1", 0)));
        f.setVisible(true);
        f.pack();
    }
}