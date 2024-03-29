package ecse321.group12.tamas.view;

import javax.swing.JFrame;
import javax.swing.JTextField;

import ecse321.group12.tamas.controller.TamasController;
import ecse321.group12.tamas.controller.InvalidInputException;
import ecse321.group12.tamas.model.ResourceManager;

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class RegisterInstructorPage extends JFrame{
	
	private static final long serialVersionUID = -1254811647258555349L;

	private JTextField idTextField;
	private JTextField nameTextField;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JButton registerInstructorButton;
	private JButton backButton;
	
	private ResourceManager rm;
	
	private String error = null;
	private JLabel errorMessage;
	
	/** Creates new form RegisterDepartmentPage */
	public RegisterInstructorPage(ResourceManager rm) {
	    this.rm = rm;
	    initComponents();
	}
	
	private void initComponents() {
	    // elements for registering a department
		idTextField = new JTextField();
		nameTextField = new JTextField();
		idLabel = new JLabel("Id:");
		nameLabel = new JLabel("Name:");
		registerInstructorButton = new JButton("Register");
		backButton = new JButton("Back");
		
	    // elements for error message
	    errorMessage = new JLabel();
	    errorMessage.setForeground(Color.RED);

	    // global settings and listeners
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setTitle("TAMAS REGISTER INSTRUCTOR");

	    // layout
	    GroupLayout layout = new GroupLayout(getContentPane());
	    getContentPane().setLayout(layout);
	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	    

	    layout.setHorizontalGroup(
	    	layout.createParallelGroup()
	        .addComponent(errorMessage)
	        .addGroup(layout.createSequentialGroup()
	        	.addComponent(nameLabel)
	        	.addComponent(nameTextField, 200, 200, 400))
	        .addGroup(layout.createSequentialGroup()
	        	.addComponent(idLabel)
	        	.addComponent(idTextField, 200, 200, 400))
	        .addGroup(layout.createSequentialGroup()
	        	.addComponent(backButton)
	        	.addComponent(registerInstructorButton))
	        );

	    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {registerInstructorButton, idTextField, nameTextField});
	    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {backButton, idLabel, nameLabel});

	    
	    layout.setVerticalGroup(
	    	layout.createSequentialGroup()
		    .addComponent(errorMessage)
		    .addGroup(layout.createParallelGroup()
		        .addComponent(nameLabel)
		        .addComponent(nameTextField))
		    .addGroup(layout.createParallelGroup()
		        .addComponent(idLabel)
		        .addComponent(idTextField))
		    .addGroup(layout.createParallelGroup()
		       	.addComponent(backButton)
		        .addComponent(registerInstructorButton))
		    );

	    this.setLocationRelativeTo(null);
	    pack();
	    
	    backButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            backButtonActionPerformed();
	        }
	    });
	    registerInstructorButton.addActionListener(new java.awt.event.ActionListener() {
	        public void actionPerformed(java.awt.event.ActionEvent evt) {
	            registerInstructorButtonActionPerformed();
	        }
	    });
	}
	
	protected void registerInstructorButtonActionPerformed() {
		// create and call the controller
		TamasController tc = new TamasController(rm);
		error = null;
		try {
			tc.registerInstructor(nameTextField.getText(), idTextField.getText());
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		//update visuals
		refreshData();
	}
	
	protected void backButtonActionPerformed() {
		LogInPage lip = new LogInPage(rm);
		this.dispose();
		lip.setVisible(true);
	}
	
	private void refreshData() {
		// error
	    errorMessage.setText(error);
	    if (error == null || error.length() == 0) {
	        nameTextField.setText("");
	        idTextField.setText("");
	    }
	    pack();
	}
}

