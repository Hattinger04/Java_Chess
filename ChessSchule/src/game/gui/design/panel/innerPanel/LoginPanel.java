/**
 * @author Simon Greiderer 
 * @author Marc Underrain
 * 
 */
package game.gui.design.panel.innerPanel;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import game.data.Client;
import game.gui.GUIUtils;
import game.gui.design.panel.ContentPanel;
import game.gui.design.panel.buttons.subclasses.PanelButtonLogin;


@SuppressWarnings("serial")
public class LoginPanel extends ContentPanel {
	private static JTextField textField;
	private static JPasswordField textField_1;
	private static JTextField textField_2;
	private static JTextField textField_3;
	private static JPasswordField textField_4;


	public LoginPanel(String path) {
		super(path);
		if(!Client.isLoggedIn()) {
			setLayout(null);
			setBounds(0, 0, 500, 400);
			setOpaque(false);
	
			JLabel lblRegister = new JLabel("Register");
			lblRegister.setFont(GUIUtils.font);
			lblRegister.setBounds(180, 178, 150, 34);
			add(lblRegister);
			
			PanelButtonLogin panel_login = new PanelButtonLogin("LoginSendUnpressed.png", "LoginSendPressed.png");
			panel_login.setBounds(10, 121, 180, 46);
			add(panel_login);
	
			PanelButtonLogin panel_register = new PanelButtonLogin("RegisterSendUnpressed.png", "RegisterSendPressed.png");
			panel_register.setBounds(10, 343, 180, 46);
			add(panel_register);
	
			JLabel lblLogin = new JLabel("Login");
			lblLogin.setFont(GUIUtils.font);
			lblLogin.setBounds(180, 10, 150, 34);
			add(lblLogin);
			
			textField = new JTextField();
			textField.setBounds(176, 45, 180, 27);
			add(textField);
			textField.setColumns(10);
			
			textField_1 = new JPasswordField();
			textField_1.setColumns(10);
			textField_1.setBounds(176, 83, 180, 27);
			add(textField_1);
			
			textField_2 = new JTextField();
			textField_2.setColumns(10);
			textField_2.setBounds(176, 221, 180, 27);
			add(textField_2);
			
			textField_3 = new JTextField();
			textField_3.setColumns(10);
			textField_3.setBounds(176, 259, 180, 27);
			add(textField_3);
			
			textField_4 = new JPasswordField();
			textField_4.setColumns(10);
			textField_4.setBounds(176, 297, 180, 27);
			add(textField_4);
			
			JLabel lbl_login_username = new JLabel("Username: ");
			lbl_login_username.setFont(GUIUtils.font);
			lbl_login_username.setBounds(10, 51, 140, 21);
			add(lbl_login_username);
			
			JLabel lbl_login_password = new JLabel("Password: ");
			lbl_login_password.setFont(GUIUtils.font);
			lbl_login_password.setBounds(10, 89, 140, 21);
			add(lbl_login_password);
			
			JLabel lbl_register_email = new JLabel("E-Mail: ");
			lbl_register_email.setFont(GUIUtils.font);
			lbl_register_email.setBounds(10, 227, 140, 21);
			add(lbl_register_email);
			
			JLabel lbl_register_username = new JLabel("Username: ");
			lbl_register_username.setFont(GUIUtils.font);
			lbl_register_username.setBounds(10, 265, 140, 21);
			add(lbl_register_username);
			
			JLabel lbl_register_password = new JLabel("Password:");
			lbl_register_password.setFont(GUIUtils.font);
			lbl_register_password.setBounds(10, 303, 140, 21);
			add(lbl_register_password);
	
			setVisible(true);
		}	
	}
	
	public static JTextField getTextField() {
		return textField;
	}
	
	public static JPasswordField getTextField_1() {
		return textField_1;
	}
	
	public static JTextField getTextField_2() {
		return textField_2;
	}
	
	public static JTextField getTextField_3() {
		return textField_3;
	}
	public static JPasswordField getTextField_4() {
		return textField_4;
	}
}