package br.com.senai.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.core.util.Utilitaria;
import br.com.senai.view.acesso.ViewLoginAdministrador;
import br.com.senai.view.categoria.ViewConsultaCategoria;
import br.com.senai.view.horarioAtendimento.ViewGerenciaHorarioAtendimento;
import br.com.senai.view.progresso.ViewProgressoInfinito;
import br.com.senai.view.restaurante.ViewConsultaRestaurante;

public class ViewInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CategoriaService categoriaService;
	private RestauranteService restauranteService;

	public ViewInicial() {
		this.categoriaService = new CategoriaService();
		this.restauranteService = new RestauranteService();
		setTitle("Tela Principal");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 537, 450);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		System.out.println();

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
				ViewConsultaCategoria view = new ViewConsultaCategoria();
				view.setVisible(true);
			}
		});
		mnCadastros.add(mntCategoria);
		
		JMenuItem mntRestaurante = new JMenuItem("Restaurantes");
		mntRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    ViewProgressoInfinito.mostraProgresso(ViewInicial.this); 
			    Utilitaria.executarAmbos(ViewInicial.this, () -> {
			        return categoriaService.listarTodas();
			    }, (List<Categoria> categorias) -> {
			        ViewProgressoInfinito.fechaProgresso(); 
			        ViewConsultaRestaurante view = new ViewConsultaRestaurante(categorias);
			        view.setVisible(true);
			    });
			}
		});
		mnCadastros.add(mntRestaurante);
		
		JMenu mnConfigurações = new JMenu("Configura\u00E7\u00F5es");
		menuBar.add(mnConfigurações);
		
		JMenuItem mntHorario = new JMenuItem("Horarios");
		mntHorario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    ViewProgressoInfinito.mostraProgresso(ViewInicial.this); 
				Utilitaria.executarAmbos(ViewInicial.this,() -> {
                    return restauranteService.listarTodas();
				}, (List<Restaurante> restaurantes) -> {
			        ViewProgressoInfinito.fechaProgresso(); 
					ViewGerenciaHorarioAtendimento view = new ViewGerenciaHorarioAtendimento(restaurantes);
					view.setVisible(true);
				});
			}
		});
		mnConfigurações.add(mntHorario);
		
		JMenu mnNotificação = new JMenu("Notifica\u00E7\u00E3o");
		menuBar.add(mnNotificação);
		
		JMenuItem mniConfiguração = new JMenuItem("Configurar notifica\u00E7\u00E3o");
		mniConfiguração.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewLoginAdministrador view = new ViewLoginAdministrador(ViewInicial.this);
				view.setVisible(true);
			}
		});
		mnNotificação.add(mniConfiguração);
		
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
