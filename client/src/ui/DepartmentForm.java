package ui;

import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.client.Department;
import client.Client;

public class DepartmentForm extends JDialog {
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
			
			Font font = new Font("Sitka Display", Font.PLAIN, 15); 
			JLabel lblName = new JLabel("Department Name");
			lblName.setFont(font);
			lblName.setBounds(84, 22, 125, 14);
			panel.add(lblName);
			
			nameTextField = new JTextField();
			nameTextField.setBounds(27, 35, 228, 23);
			panel.add(nameTextField);
			nameTextField.setColumns(10);
			nameTextField.setText( department == null ? "" : department.getName() );
			
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Department newDepartment = new Department();
					newDepartment.setId(department == null ? 0 : department.getId());
					newDepartment.setName(getName());
					saveDepartment(newDepartment);	
				    firePropertyChange("Save", null, newDepartment);
					setVisible(false);
				}
			});
			btnSave.setBounds(156, 83, 86, 23);
			btnSave.setFont(new Font("Sitka Display", Font.PLAIN, 16));
			panel.add(btnSave);
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				dispose();	 		
				}
			});
			btnCancel.setBounds(38, 83, 94, 23);
			btnCancel.setFont(new Font("Sitka Display", Font.PLAIN, 16));
			panel.add(btnCancel);
	     
	 }
	 
	 public void setName(String name){
		 nameTextField.setText(name);
	 }
	 
	 public String getName(){
		return  nameTextField.getText();
	 }
	 
	 private void saveDepartment(Department department){
		 Client.createDepartment(department);
	 }
	 
}
