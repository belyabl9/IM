package ui;

import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.client.Department;
import model.client.StatusTypes;
import model.client.User;
import utils.Digest;
import client.Client;

public class UserForm extends JDialog {
	private JPanel contentPane;
	private JTextField nameTextField;
	private JTextField surnameTextField;
	private JTextField nicknameTextField;
	private JTextField loginTextField;
	private JTextField passwordTextField;
	
	private User user;
	private Department dep;
	 
	public UserForm(User user, Department dep) {
		 this.user = user;
		 this.dep = dep;
	     initComponents();
	}
	
	public UserForm(Department dep) {
	    this.dep = dep; 
		initComponents();
	}
	
	private void initComponents() {
		 setResizable(false);
		 setBounds(100, 100, 316, 367);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
			
			Panel panel = new Panel();
			contentPane.add(panel);
			panel.setLayout(null);
			
			Font font = new Font("Sitka Display", Font.PLAIN, 15); 
			JLabel lblName = new JLabel("User Name");
			lblName.setFont(font);
			lblName.setBounds(95, 21, 125, 14);
			panel.add(lblName);
			
			nameTextField = new JTextField();
			nameTextField.setBounds(27, 35, 228, 23);
			panel.add(nameTextField);
			nameTextField.setColumns(10);
			
			JLabel lblSurname = new JLabel("Surname");
			lblSurname.setFont(font);
			lblSurname.setBounds(106, 69, 76, 14);
			panel.add(lblSurname);
			
			surnameTextField = new JTextField();
			surnameTextField.setBounds(27, 84, 228, 23);
			panel.add(surnameTextField);
			surnameTextField.setColumns(10);
			
			JLabel lblNickname = new JLabel("Nickname");
			lblNickname.setFont(font);
			lblNickname.setBounds(106, 118, 71, 14);
			panel.add(lblNickname);
			
			nicknameTextField = new JTextField();
			nicknameTextField.setBounds(27, 131, 228, 23);
			panel.add(nicknameTextField);
			nicknameTextField.setColumns(10);
			
			JLabel lblLogin = new JLabel("Login");
			lblLogin.setFont(font);
			lblLogin.setBounds(117, 165, 58, 14);
			panel.add(lblLogin);
			
			loginTextField = new JTextField();
			loginTextField.setBounds(27, 181, 228, 23);
			panel.add(loginTextField);
			loginTextField.setColumns(10);
			
			JLabel lblPassword = new JLabel("Password");
			lblPassword.setFont(font);
			lblPassword.setBounds(106, 215, 71, 14);
			panel.add(lblPassword);
			
			passwordTextField = new JTextField();
			passwordTextField.setBounds(27, 231, 228, 23);
			panel.add(passwordTextField);
			passwordTextField.setColumns(10);
			
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {				
					long id = user == null ? 0 : user.getId();
					String pass = getPassword().isEmpty() ? user.getPassword() : Digest.encrypt(getPassword().getBytes());
					User newUser = new User(id, getName(), getSurname(), getNickname(), 
							getLogin(), pass, StatusTypes.OFFLINE, new Date(), "", 6666);
					newUser.setDepartment(dep);
					saveUser(newUser);
					firePropertyChange("Save", null, newUser);
					setVisible(false);				
				}
			});
			btnSave.setBounds(158, 278, 86, 23);
			btnSave.setFont(new Font("Sitka Display", Font.PLAIN, 16));
			panel.add(btnSave);
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				dispose();	 		
				}
			});
			btnCancel.setBounds(38, 278, 94, 23);
			btnCancel.setFont(new Font("Sitka Display", Font.PLAIN, 16));
			panel.add(btnCancel);
	     
			if ( user != null ) {
	    	   setName(user.getName());
	    	   setSurname(user.getSurname());
	    	   setNickname(user.getNickname());
	    	   setLogin(user.getLogin());
			}
	 }
	 
	 public void setName(String name){
		 nameTextField.setText(name);
	 }
	 
	 public String getName(){
		return  nameTextField.getText();
	 }
	 
	 public void setSurname(String surname){
		 surnameTextField.setText(surname);
	 }
	 
	 public String getSurname(){
		return  surnameTextField.getText();
	 }
	 
	 public void setNickname(String nickname){
		 nicknameTextField.setText(nickname);
	 }
	 
	 public String getNickname(){
		return  nicknameTextField.getText();
	 }
	 
	 public void setLogin(String login){
		 loginTextField.setText(login);
	 }
	 
	 public String getLogin(){
		return  loginTextField.getText();
	 }
	 
	 public void setPassword(String password){
		 passwordTextField.setText(password);
	 }
	 
	 public String getPassword(){
		return  passwordTextField.getText();
	 }
	 
	 private void saveUser(User user){
		 Client.createUser(user);
	 }
	 
}
