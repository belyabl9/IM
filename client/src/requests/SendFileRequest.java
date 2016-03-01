package requests;

import java.util.Map;

import model.client.Response;
import model.client.TextMessage;
import model.client.User;
import ui.contacts.ContactListPanel;
import ui.nav.NavigationSide;
import ui.nav.SaveFileDialog;
import utils.FileService;
import client.TextMessageQueue;

public class SendFileRequest {

	public static Response process(Object o) {
		System.out.println("SendFileRequest was processed");
		
		Map<String, Object> content = (Map<String, Object>) o;
		byte[] blob     = (byte[]) content.get("blob");
		String filename = (String) content.get("filename");

		SaveFileDialog dlg = SaveFileDialog.getInstance(filename);
		dlg.setVisible(true);
		if (dlg.getFilename() != null) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					FileService.saveFile(blob, dlg.getFilename());
				}
			}).start();
			return new Response(Response.SUCCESS, true);
		}
		
		return new Response(Response.ERROR, false);
	}
}
