package br.com.senai.view.acesso;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.util.properties.Manipulador;

public class ViewSelecionarEnvio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public ViewSelecionarEnvio() {
		setTitle("Configurar notifica\u00E7\u00E3o");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblConfigurarNotificação = new JLabel("Configurar envio de notifica\u00E7\u00E3o");
		lblConfigurarNotificação.setFont(new Font("Dialog", Font.BOLD, 16));
		lblConfigurarNotificação.setBounds(69, 35, 324, 22);
		contentPane.add(lblConfigurarNotificação);

		JLabel lblMetodoAtual = new JLabel("Método de notificação atual: " + getMetodoNotificacao());
		lblMetodoAtual.setFont(new Font("Liberation Sans", Font.PLAIN, 12));
		lblMetodoAtual.setBounds(100, 62, 300, 20);
		contentPane.add(lblMetodoAtual);

		JRadioButton rbSMS = new JRadioButton("SMS");
		rbSMS.setFont(new Font("Dialog", Font.PLAIN, 12));
		rbSMS.setBounds(100, 90, 98, 23);
		contentPane.add(rbSMS);

		JRadioButton rdbEmail = new JRadioButton("Email");
		rdbEmail.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbEmail.setBounds(212, 90, 149, 23);
		contentPane.add(rdbEmail);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbSMS);
		buttonGroup.add(rdbEmail);

		JButton btnConfirmar = new JButton("Confirmar");
		btnConfirmar.setBounds(151, 143, 117, 25);
		contentPane.add(btnConfirmar);
		setLocationRelativeTo(null);
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rbSMS.isSelected()) {
					setMetodoNotificacao("SMS");
					lblMetodoAtual.setText("Método de notificação atual: " + getMetodoNotificacao());
					JOptionPane.showMessageDialog(contentPane, "Metódo de notificação salvo: SMS.");
				} else if (rdbEmail.isSelected()) {
					setMetodoNotificacao("email");
					lblMetodoAtual.setText("Método de notificação atual: " + getMetodoNotificacao());
					JOptionPane.showMessageDialog(contentPane, "Metódo de notificação salvo: E-mail.");
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma opção.");
				}
			}
		});

	}

	private void setMetodoNotificacao(String metodo) {
        try {
            Properties props = Manipulador.getProp();
            props.setProperty("metodoNotificacao", metodo);
            FileOutputStream out = new FileOutputStream("./properties/config.properties");
            props.store(out, null);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

	private String getMetodoNotificacao() {
		Properties props = null;
		try {
			props = Manipulador.getProp();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props.getProperty("metodoNotificacao", "Nenhum método configurado");
	}

}
