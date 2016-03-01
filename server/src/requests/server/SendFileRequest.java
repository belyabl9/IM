package requests.server;

import java.util.Map;

import requests.client.ServerToClient;
import model.server.Response;

public class SendFileRequest {

	public static model.client.Response process(Object o) {
		System.out.println("SendFileRequest was called");
		
		Map<String, Object> content = (Map<String, Object>) o;
		model.client.User from       = (model.client.User) content.get("from");
		model.client.User to         = (model.client.User) content.get("to");
		byte[] blob                 = (byte[]) content.get("blob");
		String filename             = (String) content.get("filename");
		
		ServerToClient serverToClient = new ServerToClient(to.getIP(), to.getPort());
		model.client.Response acceptResp = serverToClient.acceptFile(from, filename);
		if ( (boolean) acceptResp.getContent() ) {
			return serverToClient.sendFile(blob, filename);
		}
		
		return new model.client.Response(Response.ERROR, null);
	}
}
