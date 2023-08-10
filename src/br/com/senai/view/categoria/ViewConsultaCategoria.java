package br.com.senai.view.categoria;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.util.Utilitaria;
import br.com.senai.view.componentes.table.CategoriaTableModel;

public class ViewConsultaCategoria extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFiltro;
	private CategoriaService service;
	private JTable tableCategoria;

	public ViewConsultaCategoria() {
		this.service = new CategoriaService();
		
		CategoriaTableModel model = new CategoriaTableModel(new ArrayList<Categoria>());
		this.tableCategoria = new JTable(model);
		Utilitaria.configurarTabela(tableCategoria);

		setTitle("Gerenciar Categoria - Listagem");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 521, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroCategoria view = new ViewCadastroCategoria(ViewConsultaCategoria.this);
				setVisible(false);
				Utilitaria.limparCampos(contentPane);
				view.setVisible(true);
				setVisible(true);
			}
		});
		btnNovo.setBounds(376, 12, 117, 25);
		contentPane.add(btnNovo);
		
		txtFiltro = new JTextField();
		txtFiltro.setBounds(100, 60, 264, 25);
		contentPane.add(txtFiltro);
		txtFiltro.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(52, 65, 70, 15);
		contentPane.add(lblNome);
		
		JLabel lblFiltro = new JLabel("Filtros");
		lblFiltro.setBounds(12, 28, 70, 25);
		contentPane.add(lblFiltro);
		
		JButton btnListar = new JButton("Listar");
		btnListar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<Categoria> categoriaEncontrada = service.listarPor(txtFiltro.getText());
					CategoriaTableModel model = new CategoriaTableModel(categoriaEncontrada);
					tableCategoria.setModel(model);
					Utilitaria.configurarTabela(tableCategoria);
					if(categoriaEncontrada.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Não foi"
								+ " encontrado nenhuma categoria com esse nome.");
					} 
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnListar.setBounds(376, 60, 117, 25);
		contentPane.add(btnListar);
		
		JLabel lblCategoriasEncontradas = new JLabel("Categorias Encontradas");
		lblCategoriasEncontradas.setBounds(24, 97, 171, 15);
		contentPane.add(lblCategoriasEncontradas);
		
		JScrollPane scrollPane = new JScrollPane(tableCategoria);
		scrollPane.setBounds(30, 128, 465, 101);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "A\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(248, 238, 245, 63);
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnExcluir = new JButton("Excluir");
		panel.add(btnExcluir);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableCategoria.getSelectedRow();
				CategoriaTableModel model = (CategoriaTableModel) tableCategoria.getModel();
				if(linhaSelecionada >= 0 && !model.isVazio()) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja excluir a categoria?",
							"Remoção", JOptionPane.YES_NO_OPTION);
					if(opcao == 0) {
						Categoria categoriaSelecionada = model.getPor(linhaSelecionada);
						try {
							service.excluirPor(categoriaSelecionada.getId());
							model.removePor(linhaSelecionada);
							JOptionPane.showMessageDialog(contentPane, "Categoria removida.");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
					}
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
				}
			}
		});
		
		JButton btnEditar = new JButton("Editar");
		panel.add(btnEditar);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableCategoria.getSelectedRow();
				if (linhaSelecionada >= 0) {
					CategoriaTableModel model = (CategoriaTableModel) tableCategoria.getModel();
					Categoria categoriaSelecionada = model.getPor(linhaSelecionada);
					ViewCadastroCategoria view = new ViewCadastroCategoria(ViewConsultaCategoria.this);
					view.setCategoria(categoriaSelecionada);
					setVisible(false);
					view.setEdicaoCategoria(true); 
					view.setVisible(true);
					setVisible(true);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
	}
	
}
