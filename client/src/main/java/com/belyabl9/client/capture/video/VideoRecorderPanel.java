package com.belyabl9.client.capture.video;

import com.belyabl9.client.Client;
import com.belyabl9.client.Session;
import com.belyabl9.api.MediaMessage;
import com.belyabl9.api.User;
import com.belyabl9.client.capture.audio.SimpleAudioRecorder;
import com.belyabl9.client.ui.nav.NavigationPanel;
import com.belyabl9.client.ui.nav.NavigationSide;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class VideoRecorderPanel extends JPanel {
	public static final String VIDEO_TMP_FILENAME = "video.avi.tmp";
	public static final String AUDIO_TMP_FILENAME = "audio.mp3.tmp";
	
	private final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
	private final CanvasFrame canvasFrame = new CanvasFrame("Cam");

	private final JButton captureBtn;
	private final JButton stopBtn;
	private final JButton resetBtn;
	private final JButton sendBtn;
	
	private final JPanel btnPanel = new JPanel();
	
	private JTextField subjEdt;
	private JTextField filenameEdt;
	  
	private User to;

    public VideoRecorderPanel(User to) {
    	canvasFrame.setVisible(false);
    	
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

	private JButton createSendButton(User to) {
		JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(event -> {
            if (getFilename().isEmpty() || getSubject().isEmpty()) {
                JOptionPane.showMessageDialog(VideoRecorderPanel.this, "Please, Fill all the required fields.");
                return;
            }

            String filename = getFilename() + ".avi";
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
            
            new File(VIDEO_TMP_FILENAME).delete();
            new File(AUDIO_TMP_FILENAME).delete();
            new File(getFilename() + ".avi").delete();

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

				try {
					grabber.stop();
					canvasFrame.dispose();
				} catch (FrameGrabber.Exception e) {
					throw new RuntimeException(e);
				}
			}
        );
		return stopBtn;
	}

	private JButton createCaptureButton() {
		JButton captureBtn = new JButton("Capture");
		captureBtn.addActionListener(
                event -> {
                    if ( getFilename().isEmpty() || getSubject().isEmpty()) {
                        JOptionPane.showMessageDialog(VideoRecorderPanel.this, "Please, fill all the required fields.");
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
        );
		return captureBtn;
	}

	private JButton createReturnButton(User to) {
		JButton returnBtn = new JButton("Return");
		returnBtn.addActionListener(event -> NavigationSide.getNavigationPanel(to).changeCard(NavigationPanel.ACTIONS_CARD_NAME));
		return returnBtn;
	}


	private void sync(String audio, String video, String output) {
   	    FrameGrabber videoGrabber = new FFmpegFrameGrabber(video); 
        FrameGrabber audioGrabber = new FFmpegFrameGrabber(audio);

		try {
			videoGrabber.start();
			audioGrabber.start();
		} catch (FrameGrabber.Exception e) {
			throw new RuntimeException(e);
		}
	
	    FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(output, videoGrabber.getImageWidth(), videoGrabber.getImageHeight(), 2); 

	    recorder.setFormat("avi");
	    recorder.setFrameRate(25); 
	    recorder.setSampleRate(audioGrabber.getSampleRate());

		Frame frame1, frame2 = null;
		try {
			recorder.start();
			while ((frame1 = videoGrabber.grabFrame()) != null ||
					(frame2 = audioGrabber.grabFrame()) != null) {
				recorder.record(frame1);
				recorder.record(frame2);
			}
			recorder.stop();
			videoGrabber.stop();
			audioGrabber.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
	    }
	}
		  
    private String getFilename() {
      return filenameEdt.getText();
    }
    
    private String getSubject() {
      return subjEdt.getText();
    }
    
    private void captureVideo(String filename){
        try{
            new CaptureThread(filename).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
		
    class CaptureThread extends Thread {
        private final String filename;
        
        public CaptureThread(String filename) {
            this.filename = filename;
        }
        
        public void run() {
            try {
                grabber.start();
            } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
       
            File outputFile = new File(AUDIO_TMP_FILENAME);
            AudioFormat	audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100.0F, 16, 2, 4, 44100.0F, false
            );

            DataLine.Info	info = new DataLine.Info(TargetDataLine.class, audioFormat);
            TargetDataLine	targetDataLine;
            try {
                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                targetDataLine.open(audioFormat);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
            SimpleAudioRecorder audioRecorder = new SimpleAudioRecorder(targetDataLine, targetType, outputFile);

            Frame grabbedImage;
            try {
                grabbedImage = grabber.grab();
            } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }

            canvasFrame.setCanvasSize(grabbedImage.imageWidth, grabbedImage.imageHeight);
        
            grabber.setFrameRate(15);
            
            FFmpegFrameRecorder videoRecorder = new FFmpegFrameRecorder(VIDEO_TMP_FILENAME,  grabber.getImageWidth(),grabber.getImageHeight());
            
            videoRecorder.setVideoCodec(13);
            videoRecorder.setFormat("avi");
            videoRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            videoRecorder.setFrameRate(25);
            videoRecorder.setVideoBitrate(10 * 1024 * 1024);

            try {
                videoRecorder.start();
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                throw new RuntimeException(e);
            }
            audioRecorder.start();
            try {
                while (canvasFrame.isVisible() && (grabbedImage = grabber.grab()) != null) {
                    canvasFrame.showImage(grabbedImage);
                    videoRecorder.record(grabbedImage);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                videoRecorder.stop();
                audioRecorder.stopRecording();
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                throw new RuntimeException(e);
            }

            try {
                sync(AUDIO_TMP_FILENAME, VIDEO_TMP_FILENAME, getFilename() + ".avi");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}