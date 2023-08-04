package br.com.senai.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.com.senai.view.categoria.ViewPesquisaCategoria;
import br.com.senai.view.horarioAtendimento.ViewGerenciaHorarioAtendimento;
import br.com.senai.view.restaurante.ViewConsultaRestaurante;
import javax.swing.SwingConstants;

public class ViewInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public ViewInicial() {
		setTitle("Tela Principal");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 537, 450);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 521, 21);
		contentPane.add(menuBar);
		
		JMenu mnCadastros = new JMenu("Cadastros");
		menuBar.add(mnCadastros);
		
		JMenuItem mntCategoria = new JMenuItem("Categorias");
		mntCategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewPesquisaCategoria view = new ViewPesquisaCategoria();
				view.setVisible(true);
			}
		});
		mnCadastros.add(mntCategoria);
		
		JMenuItem mntRestaurante = new JMenuItem("Restaurantes");
		mntRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaRestaurante view = new ViewConsultaRestaurante();
				view.setVisible(true);
			}
		});
		mnCadastros.add(mntRestaurante);
		
		JMenu mnConfigurações = new JMenu("Configura\u00E7\u00F5es");
		menuBar.add(mnConfigurações);
		
		JMenuItem mntHorario = new JMenuItem("Horarios");
		mntHorario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewGerenciaHorarioAtendimento view = new ViewGerenciaHorarioAtendimento();
				view.setVisible(true);
			}
		});
		mnConfigurações.add(mntHorario);
		
		JMenuItem mntSair = new JMenuItem("Sair");
		mntSair.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mntSair);
		mntSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
