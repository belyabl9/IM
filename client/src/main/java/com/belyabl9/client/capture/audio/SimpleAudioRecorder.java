package com.belyabl9.client.capture.audio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.IOException;



/**	<titleabbrev>SimpleAudioRecorder</titleabbrev>
	<title>Recording to an audio file (simple version)</title>

	<formalpara><title>Purpose</title>
	<para>Records audio data and stores it in a file. The data is
	recorded in CD quality (44.1 kHz, 16 bit linear, stereo) and
	stored in a <filename>.wav</filename> file.</para></formalpara>

	<formalpara><title>Usage</title>
	<para>
	<cmdsynopsis>
	<command>java SimpleAudioRecorder</command>
	<arg choice="plain"><option>-h</option></arg>
	</cmdsynopsis>
	<cmdsynopsis>
	<command>java SimpleAudioRecorder</command>
	<arg choice="plain"><replaceable>audiofile</replaceable></arg>
	</cmdsynopsis>
	</para></formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><option>-h</option></term>
	<listitem><para>print usage information, then exit</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><option><replaceable>audiofile</replaceable></option></term>
	<listitem><para>the file name of the
	audio file that should be produced from the recorded data</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>
	<para>
	You cannot select audio formats and the audio file type
	on the command line. See
	AudioRecorder for a version that has more advanced options.
	Due to a bug in the Sun jdk1.3/1.4, this program does not work
	with it.
	</para></formalpara>

	<formalpara><title>Source code</title>
	<para>
	<ulink url="SimpleAudioRecorder.java.html">SimpleAudioRecorder.java</ulink>
	</para>
	</formalpara>

*/
public class SimpleAudioRecorder extends Thread {
	private TargetDataLine		m_line;
	private AudioFileFormat.Type	m_targetType;
	private AudioInputStream	m_audioInputStream;
	private File			m_outputFile;

	public SimpleAudioRecorder(TargetDataLine line, AudioFileFormat.Type targetType, File file) {
		m_line = line;
		m_audioInputStream = new AudioInputStream(line);
		m_targetType = targetType;
		m_outputFile = file;
	}

	/** Starts the recording.
	    To accomplish this, (i) the line is started and (ii) the
	    thread is started.
	*/
	public void start() {
		/* Starting the TargetDataLine. It tells the line that
		   we now want to read data from it. If this method
		   isn't called, we won't
		   be able to read data from the line at all.
		*/
		m_line.start();

		/* Starting the thread. This call results in the
		   method 'run()' (see below) being called. There, the
		   data is actually read from the line.
		*/
		super.start();
	}


	/** Stops the recording.

	    Note that stopping the thread explicitly is not necessary. Once
	    no more data can be read from the TargetDataLine, no more data
	    be read from our AudioInputStream. And if there is no more
	    data from the AudioInputStream, the method 'AudioSystem.write()'
	    (called in 'run()' returns. Returning from 'AudioSystem.write()'
	    is followed by returning from 'run()', and thus, the thread
	    is terminated automatically.

	    It's not a good idea to call this method just 'stop()'
	    because stop() is a (deprecated) method of the class 'Thread'.
	    And we don't want to override this method.
	*/
	public void stopRecording() {
		m_line.stop();
		m_line.close();
	}




	/** Main working method.
	    You may be surprised that here, just 'AudioSystem.write()' is
	    called. But internally, it works like this: AudioSystem.write()
	    contains a loop that is trying to read from the passed
	    AudioInputStream. Since we have a special AudioInputStream
	    that gets its data from a TargetDataLine, reading from the
	    AudioInputStream leads to reading from the TargetDataLine. The
	    data read this way is then written to the passed File. Before
	    writing of audio data starts, a header is written according
	    to the desired audio file type. Reading continues untill no
	    more data can be read from the AudioInputStream. In our case,
	    this happens if no more data can be read from the TargetDataLine.
	    This, in turn, happens if the TargetDataLine is stopped or closed
	    (which implies stopping). (Also see the comment above.) Then,
	    the file is closed and 'AudioSystem.write()' returns.
	*/
	public void run() {
		try {
			AudioSystem.write(
				m_audioInputStream,
				m_targetType,
				m_outputFile
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

