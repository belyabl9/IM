package requests;

import java.util.Map;

import javax.swing.JOptionPane;

import model.client.Response;
import ui.IMFrame;

public class FileAcceptRequest {

	public static Response process(Object o) {
		System.out.println("FileAcceptRequest was processed");
		
		Map<String, Object> content = (Map<String, Object>) o;
		String filename    = (String) content.get("filename");
		model.client.User user = (model.client.User) content.get("user");

		int ans = JOptionPane.showConfirmDialog(IMFrame.getInstance(), 
				String.format("Do you want to accept file '%s' from %s %s", filename, user.getName(), user.getSurname()));
		
		return new Response(Response.SUCCESS, ans == JOptionPane.YES_OPTION);
	}
}
