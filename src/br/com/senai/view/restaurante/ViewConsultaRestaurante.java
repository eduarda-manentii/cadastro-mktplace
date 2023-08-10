package br.com.senai.view.restaurante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.core.util.Utilitaria;
import br.com.senai.view.componentes.table.RestauranteTableModel;

public class ViewConsultaRestaurante extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JComboBox<Categoria> cbCategorias;
	private JTable tableRestaurante;
	private RestauranteService restauranteService;
	
	public ViewConsultaRestaurante(List<Categoria> categorias) {
		this.restauranteService = new RestauranteService();
		
		RestauranteTableModel model = new RestauranteTableModel();
		this.tableRestaurante = new JTable(model);
		tableRestaurante.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Utilitaria.configurarTabela(tableRestaurante);
		
		setResizable(false);
		setTitle("Gerenciar Restaurante - Listagem");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 610, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ViewCadastroRestaurante view = new ViewCadastroRestaurante(ViewConsultaRestaurante.this);
				setVisible(false);
				Utilitaria.limparCampos(contentPane);
				view.setVisible(true);
				setVisible(true);
			}
		});
		btnNovo.setBounds(452, 12, 117, 25);
		contentPane.add(btnNovo);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableRestaurante.getSelectedRow();
				RestauranteTableModel model = (RestauranteTableModel) tableRestaurante.getModel();
				if(linhaSelecionada >= 0 && !model.isVazio()) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja excluir o restaurante?",
							"Remoção", JOptionPane.YES_NO_OPTION);
					if(opcao == 0) {
						Restaurante restauraneSelecionado = model.getPor(linhaSelecionada);
						try {
							restauranteService.excluirPor(restauraneSelecionado.getId());
							model.removePor(linhaSelecionada);
							JOptionPane.showMessageDialog(contentPane, "Restaurante excluido.");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
					}
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
				}
			}
		});
		btnExcluir.setBounds(452, 299, 117, 25);
		contentPane.add(btnExcluir);
		
		JButton btnListar = new JButton("Listar");

		btnListar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					String filtroInformado = txtNome.getText();
					Categoria categoriaInformada = (Categoria) cbCategorias.getSelectedItem();
					List<Restaurante> restauranteEncontrado = restauranteService.listarPor(filtroInformado, categoriaInformada);
					RestauranteTableModel model = new RestauranteTableModel(restauranteEncontrado);
					tableRestaurante.setModel(model);
					Utilitaria.configurarTabela(tableRestaurante);
					if (restauranteEncontrado.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Não foi encontrado nenhum restaurante "
								+ "com os filtros informados.");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnListar.setBounds(477, 81, 100, 25);
		contentPane.add(btnListar);
		
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableRestaurante.getSelectedRow();
				if(linhaSelecionada >= 0) {
					RestauranteTableModel model = (RestauranteTableModel) tableRestaurante.getModel();
					Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
					ViewCadastroRestaurante view = new ViewCadastroRestaurante(ViewConsultaRestaurante.this);
					view.setRestaurante(restauranteSelecionado);
					setVisible(false);
					view.setEdicaoRestaurante(true); 
					view.setVisible(true);
					setVisible(true);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
		btnEditar.setBounds(321, 299, 117, 25);
		contentPane.add(btnEditar);
		
		txtNome = new JTextField();
		txtNome.setBounds(35, 81, 206, 25);
		contentPane.add(txtNome);
		txtNome.setColumns(10);

		cbCategorias = new JComboBox<Categoria>();
		Categoria placeholder = new Categoria("Selecione...");
		Utilitaria.preencherCombo(cbCategorias, categorias, placeholder);
		cbCategorias.setBounds(252, 81, 210, 25);
		contentPane.add(cbCategorias);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(45, 61, 70, 15);
		contentPane.add(lblNome);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setBounds(262, 61, 70, 15);
		contentPane.add(lblCategoria);
		
		JLabel lblRestaurantes = new JLabel("Restaurantes Encontrados");
		lblRestaurantes.setBounds(22, 128, 261, 15);
		contentPane.add(lblRestaurantes);
		
		JLabel lblFiltros = new JLabel("Filtros");
		lblFiltros.setBounds(12, 34, 70, 15);
		contentPane.add(lblFiltros);
		
		JScrollPane scrollPane = new JScrollPane(tableRestaurante);
		scrollPane.setBounds(22, 143, 561, 127);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "A\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(299, 282, 288, 60);
		contentPane.add(panel);
	}
	
}
