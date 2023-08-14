package br.com.senai.view.acesso;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.util.properties.Manipulador;

public class ViewLoginAdministrador extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField pwdAdminsitrador;

	public ViewLoginAdministrador(Window onwer) {
		super(onwer);
		setTitle("Configura\u00E7\u00E3o de Admnistrador");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		
		JLabel lblLogin = new JLabel("Senha do Administrador ");
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 15));
		lblLogin.setBounds(109, 53, 264, 15);
		contentPane.add(lblLogin);
		
		pwdAdminsitrador = new JPasswordField();
		pwdAdminsitrador.setBounds(54, 75, 349, 30);
		contentPane.add(pwdAdminsitrador);
		
		JButton btnAcessar = new JButton("Acessar");
		btnAcessar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String senhaDigitada = new String(pwdAdminsitrador.getPassword());
				Properties prop;
				try {
					prop = Manipulador.getProp();
					String senhaArmazenada = prop.getProperty("senha");
					if (senhaDigitada.equals(senhaArmazenada)) {
					    ViewSelecionarEnvio view = new ViewSelecionarEnvio();
					    view.setVisible(true);
					    dispose();
					} else {
					   JOptionPane.showMessageDialog(contentPane, "Senha incorreta.");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			    
			}
		});
		btnAcessar.setBounds(159, 146, 117, 25);
		contentPane.add(btnAcessar);
	}
}
