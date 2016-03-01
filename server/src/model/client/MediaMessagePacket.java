package model.client;

import java.io.Serializable;

public class MediaMessagePacket implements Serializable {

	public MediaMessage msg;
	public byte[] blob;
	
	public MediaMessagePacket(MediaMessage msg, byte[] blob) {
		this.msg  = msg;
		this.blob = blob;
	}
	
}
