package com.belyabl9.client.ui.form;

import com.belyabl9.client.Client;
import com.belyabl9.api.Department;

import java.awt.Font;
import java.awt.Panel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DepartmentForm extends JDialog {
	private static final Font FONT = new Font("Sitka Display", Font.PLAIN, 16);
	
	private JPanel contentPane;
	private JTextField nameTextField;
	
	private Department department;
	
	 public DepartmentForm(Department department) {
		 this.department = department;
	     initComponents();
	 }
	 
	 public DepartmentForm() {
	     initComponents();
	 }
	 
	 private void initComponents() {
		 setResizable(false);
		 setBounds(100, 100, 316, 180);
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
		 
			panel.add(createSaveButton());
			panel.add(createCancelButton());
	 }

	private JButton createCancelButton() {
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(event -> dispose());
		btnCancel.setBounds(38, 83, 94, 23);
		btnCancel.setFont(FONT);
		return btnCancel;
	}

	private JButton createSaveButton() {
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(event -> {
			Department newDepartment = new Department();
			newDepartment.setId(department == null ? 0 : department.getId());
			newDepartment.setName(getName());
			newDepartment = saveDepartment(newDepartment);	
			firePropertyChange("Save", null, newDepartment);
			setVisible(false);
		});
		btnSave.setBounds(156, 83, 86, 23);
		btnSave.setFont(FONT);
		return btnSave;
	}

	private JTextField createNameField() {
		JTextField nameTextField = new JTextField();
		nameTextField.setBounds(27, 35, 228, 23);
		nameTextField.setColumns(10);
		nameTextField.setText( department == null ? "" : department.getName() );
		return nameTextField;
	}

	private JLabel createNameLabel() {
		JLabel lblName = new JLabel("Department Name");
		lblName.setFont(FONT);
		lblName.setBounds(84, 22, 125, 14);
		return lblName;
	}

	public void setName(String name){
		 nameTextField.setText(name);
	 }
	 
	 public String getName(){
		return  nameTextField.getText();
	 }
	 
	 private Department saveDepartment(Department department){
		 return Client.createDepartment(department);
	 }
	 
}
