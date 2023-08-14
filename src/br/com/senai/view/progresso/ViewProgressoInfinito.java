package br.com.senai.view.progresso;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ViewProgressoInfinito {

	private static JDialog progressoInfinito;

	public static void mostraProgresso(Window owner) {

		SwingUtilities.invokeLater(() -> {
			progressoInfinito = new JDialog(owner);
			progressoInfinito.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			progressoInfinito.setSize(200, 100);
			progressoInfinito.setAlwaysOnTop(true);

			JProgressBar progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			progressBar.setString("Carregando...");
			progressBar.setStringPainted(true);
			progressoInfinito.add(progressBar);

			progressoInfinito.setLocationRelativeTo(null);
			progressoInfinito.setUndecorated(true);
			progressoInfinito.setVisible(true);

		});
	}

	public static void fechaProgresso() {
		progressoInfinito.setVisible(false);
	}

}
