package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

public class MPlayer extends JDialog {
	
	private static String PLAY_BUTTON_LABEL = "Play";
	private static String PAUSE_BUTTON_LABEL = "Pause";
	private static String STOP_BUTTON_LABEL = "Stop";

	private String curState = STOP_BUTTON_LABEL;
	
	private JButton playBtn;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public MPlayer(String path) {
		super();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
    	
        if (path.contains(new String(".avi"))) 
        	setSize(500, 500);
        else
        	setSize(250, 80);
        
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        playBtn = new JButton(PLAY_BUTTON_LABEL);
        playBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        MediaPlayer mp = mediaPlayerComponent.getMediaPlayer();

		        if ( curState == STOP_BUTTON_LABEL) {
		        	mediaPlayerComponent.setVisible(true);
		        	mp.playMedia(path);
		        	if ( !isVideo(path) ) {
		        		mediaPlayerComponent.setVisible(false);
		        	}
			        playBtn.setText(PAUSE_BUTTON_LABEL);
			        curState = PAUSE_BUTTON_LABEL;
		        }
		        else if ( curState == PLAY_BUTTON_LABEL) {
		        	mp.pause();
		        	playBtn.setText(PAUSE_BUTTON_LABEL);
		        	curState = PAUSE_BUTTON_LABEL;
		        } else if ( curState == PAUSE_BUTTON_LABEL) {
		        	mp.pause();
		        	playBtn.setText(PLAY_BUTTON_LABEL);
		        	curState = PLAY_BUTTON_LABEL;
		        }
			}
		});
        
        JButton stopBtn = new JButton(STOP_BUTTON_LABEL);
        stopBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        MediaPlayer mp = mediaPlayerComponent.getMediaPlayer();
		        mp.stop();
		        playBtn.setText(PLAY_BUTTON_LABEL);
		        curState = STOP_BUTTON_LABEL;
			}
		});
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(playBtn);
        btnPanel.add(stopBtn);
        
        mainPanel.add(mediaPlayerComponent);
        mainPanel.add(btnPanel);
		//mediaPlayerComponent.setVisible(true);
        getContentPane().add(mainPanel);
        
        setVisible(true);

    }
   
	private boolean isVideo(String path) {
		String format = null;
		try {
			format = Files.probeContentType(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return format != null && format.substring(0, format.indexOf('/') ).equals("video");
	}
    
    public static void main(final String[] args) {
    	
//    	NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/usr/lib/vlc");
//        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
           SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                   new MPlayer("Q.av");
               }
           });
       }
}