package com.belyabl9.client.ui.form;

import com.belyabl9.client.Client;
import com.belyabl9.api.Department;
import com.belyabl9.api.StatusType;
import com.belyabl9.api.User;
import com.belyabl9.client.utils.Digest;

import java.awt.Font;
import java.awt.Panel;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UserForm extends JDialog {
	private static Font FONT = new Font("Sitka Display", Font.PLAIN, 15);

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

		// name
		panel.add(createNameLabel());
		nameTextField = createNameField();
		panel.add(nameTextField);

		// surname
		panel.add(createSurnameLabel());
		surnameTextField = createSurnameField();
		panel.add(surnameTextField);

		// nickname
		panel.add(createNicknameLabel());
		nicknameTextField = createNicknameField();
		panel.add(nicknameTextField);

		// login
		panel.add(createLoginLabel());
		loginTextField = createLoginField();
		panel.add(loginTextField);
		
		// password
		panel.add(createPasswordLabel());
		passwordTextField = createPasswordField();
		panel.add(passwordTextField);

		panel.add(createSaveButton());
		panel.add(createCancelButton());
		
		if (user != null) {
		   setName(user.getName());
		   setSurname(user.getSurname());
		   setNickname(user.getNickname());
		   setLogin(user.getLogin());
		}
	 }

	private JButton createCancelButton() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(event -> dispose());
		btnCancel.setBounds(38, 278, 94, 23);
		btnCancel.setFont(FONT);
		return btnCancel;
	}

	private JButton createSaveButton() {
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(event -> {				
			long id = (user == null) ? 0 : user.getId();
			String pass = getPassword().isEmpty() ? user.getPassword() : Digest.encrypt(getPassword().getBytes());
			User newUser = new User(id, getName(), getSurname(), getNickname(), getLogin(), pass, StatusType.OFFLINE, new Date(), "", 6666);
			newUser.setDepartment(dep);
			saveUser(newUser);
			firePropertyChange("Save", null, newUser);
			setVisible(false);				
		});
		btnSave.setBounds(158, 278, 86, 23);
		btnSave.setFont(FONT);
		return btnSave;
	}

	private JTextField createPasswordField() {
		JTextField passwordTextField = new JTextField();
		passwordTextField.setBounds(27, 231, 228, 23);
		passwordTextField.setColumns(10);
		return passwordTextField;
	}

	private JLabel createPasswordLabel() {
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(FONT);
		lblPassword.setBounds(106, 215, 71, 14);
		return lblPassword;
	}

	private JTextField createLoginField() {
		JTextField loginTextField = new JTextField();
		loginTextField.setBounds(27, 181, 228, 23);
		loginTextField.setColumns(10);
		return loginTextField;
	}

	private JLabel createLoginLabel() {
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(FONT);
		lblLogin.setBounds(117, 165, 58, 14);
		return lblLogin;
	}

	private JTextField createNicknameField() {
		JTextField nicknameTextField = new JTextField();
		nicknameTextField.setBounds(27, 131, 228, 23);
		nicknameTextField.setColumns(10);
		return nicknameTextField;
	}

	private JLabel createNicknameLabel() {
		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setFont(FONT);
		lblNickname.setBounds(106, 118, 71, 14);
		return lblNickname;
	}

	private JTextField createSurnameField() {
		JTextField surnameTextField = new JTextField();
		surnameTextField.setBounds(27, 84, 228, 23);
		surnameTextField.setColumns(10);
		return surnameTextField;
	}

	private JLabel createNameLabel() {
		JLabel lblName = new JLabel("User Name");
		lblName.setFont(FONT);
		lblName.setBounds(95, 21, 125, 14);
		return lblName;
	}

	private JLabel createSurnameLabel() {
		JLabel lblSurname = new JLabel("Surname");
		lblSurname.setFont(FONT);
		lblSurname.setBounds(106, 69, 76, 14);
		return lblSurname;
	}

	private JTextField createNameField() {
		JTextField nameTextField = new JTextField();
		nameTextField.setBounds(27, 35, 228, 23);
		nameTextField.setColumns(10);
		return nameTextField;
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
