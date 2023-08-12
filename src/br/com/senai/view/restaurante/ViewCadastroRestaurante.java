package br.com.senai.view.restaurante;

import java.awt.Window;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.core.util.Utilitaria;

public class ViewCadastroRestaurante extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextArea txtDescricao;
	private JTextField txtLogradouro;
	private JTextField txtComplemento;
	private JTextField txtCidade;
	private JTextField txtBairro;
	private JComboBox<Categoria> cbCategorias;
	
	private Restaurante restaurante;
	private RestauranteService restauranteService;
	private CategoriaService categoriaService;
	private boolean isEdicaoRestaurante;
	

	public ViewCadastroRestaurante(Window owner) {
		super(owner);
		this.categoriaService = new CategoriaService();
		List<Categoria> categorias = categoriaService.listarTodas();
		this.restauranteService = new RestauranteService();
		setResizable(false);
		setTitle("Gerenciar Restaurante - Cadastro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isCamposPreenchidos() == true) {
					int opcao = JOptionPane.showConfirmDialog(
							contentPane, 
							"Tem certeza que deseja descartar as informações?",
							"Confirmação",
							JOptionPane.YES_NO_OPTION);
					if(opcao == 0) {
						dispose();
					}
				} else {
					dispose();
				}
			}
		});
		btnPesquisar.setBounds(356, 12, 117, 25);
		contentPane.add(btnPesquisar);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					String nome = txtNome.getText();
				    String descricao = txtDescricao.getText();
				    String logradouro = txtLogradouro.getText();
				    String cidade = txtCidade.getText();
				    String bairro = txtBairro.getText();
				    String complemento = txtComplemento.getText();
				    Categoria categoria = (Categoria) cbCategorias.getSelectedItem();
					
				    if (!isEdicaoRestaurante) {
				        Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
				        restaurante = new Restaurante(nome, descricao, endereco, categoria);
				        restauranteService.salvar(restaurante);
				        JOptionPane.showMessageDialog(contentPane, "Restaurante salvo.");
				        Utilitaria.limparCampos(contentPane);
				    } else {
				    	Endereco enderecoTemp = new Endereco(cidade, logradouro, bairro, complemento);
				        Restaurante restauranteTemp = new Restaurante(nome, descricao, enderecoTemp, categoria);
				        restauranteService.validar(restauranteTemp);
				        Endereco endereco = restaurante.getEndereco();
			        	endereco.setCidade(cidade);
				        endereco.setLongradouro(logradouro);
				        endereco.setBairro(bairro);
				        endereco.setComplemento(complemento);
				        restaurante.setNome(nome);
				        restaurante.setDescricao(descricao);
				        restaurante.setCategoria(categoria);
				        restauranteService.salvar(restaurante);
				        JOptionPane.showMessageDialog(contentPane, "Restaurante alterado.");
					}
				} catch (Exception ex) {
				    JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnSalvar.setBounds(356, 338, 117, 25);
		contentPane.add(btnSalvar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEdicaoRestaurante) { 
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Tem certeza que deseja"
							+ " cancelar a edição?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
					if(opcao == 0) {
						ViewCadastroRestaurante view = new ViewCadastroRestaurante(owner);
						setVisible(false);
						view.setVisible(true);
						dispose();
					}
				} else {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Tem certeza que deseja"
							+ " cancelar a inclusão?", "CONFIRMAÇÃO", JOptionPane.YES_NO_OPTION);
					if(opcao == 0) {
				        Utilitaria.limparCampos(contentPane);
					}
				}
			}
		});
		btnCancelar.setBounds(229, 338, 117, 25);
		contentPane.add(btnCancelar);
		
		txtNome = new JTextField();
		txtNome.setBounds(12, 57, 205, 25);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		
		cbCategorias = new JComboBox<Categoria>();
		Categoria placeholder = new Categoria("Selecione...");
		Utilitaria.preencherCombo(cbCategorias, categorias, placeholder);
		cbCategorias.setBounds(229, 57, 244, 25);
		contentPane.add(cbCategorias);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(12, 35, 70, 15);
		contentPane.add(lblNome);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setBounds(227, 35, 70, 15);
		contentPane.add(lblCategoria);
		
		txtDescricao = new JTextArea();
		txtDescricao.setBounds(12, 106, 461, 59);
		contentPane.add(txtDescricao);
		txtDescricao.setColumns(10);
		
		JLabel lblDescricao = new JLabel("Descri\u00E7\u00E3o");
		lblDescricao.setBounds(12, 90, 70, 15);
		contentPane.add(lblDescricao);
		
		txtLogradouro = new JTextField();
		txtLogradouro.setBounds(12, 191, 461, 25);
		contentPane.add(txtLogradouro);
		txtLogradouro.setColumns(10);
		
		JLabel lblLogradouro = new JLabel("Logradouro");
		lblLogradouro.setBounds(12, 177, 117, 15);
		contentPane.add(lblLogradouro);
		
		txtComplemento = new JTextField();
		txtComplemento.setBounds(12, 299, 461, 25);
		contentPane.add(txtComplemento);
		txtComplemento.setColumns(10);
		
		txtCidade = new JTextField();
		txtCidade.setBounds(12, 245, 219, 25);
		contentPane.add(txtCidade);
		txtCidade.setColumns(10);
		
		txtBairro = new JTextField();
		txtBairro.setColumns(10);
		txtBairro.setBounds(242, 245, 233, 25);
		contentPane.add(txtBairro);
		
		JLabel lblCidade = new JLabel("Cidade");
		lblCidade.setBounds(12, 228, 70, 15);
		contentPane.add(lblCidade);
		
		JLabel lblBairro = new JLabel("Bairro");
		lblBairro.setBounds(249, 228, 70, 15);
		contentPane.add(lblBairro);
		
		JLabel lblComplemento = new JLabel("Complemento");
		lblComplemento.setBounds(12, 282, 152, 15);
		contentPane.add(lblComplemento);
		
	}
	
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
		this.cbCategorias.setSelectedItem(restaurante.getCategoria());
		this.txtNome.setText(restaurante.getNome());
		this.txtDescricao.setText(restaurante.getDescricao());
		this.txtCidade.setText(restaurante.getEndereco().getCidade());
		this.txtBairro.setText(restaurante.getEndereco().getBairro());
		this.txtComplemento.setText(restaurante.getEndereco().getComplemento());
		this.txtLogradouro.setText(restaurante.getEndereco().getLongradouro());
	}
	
	 public void setEdicaoRestaurante(boolean isEdicaoRestaurante) {
	        this.isEdicaoRestaurante = isEdicaoRestaurante;
	    }
	 
	 
	 private boolean isCamposPreenchidos() {
		String nome = txtNome.getText();
	    String descricao = txtDescricao.getText();
	    String logradouro = txtLogradouro.getText();
	    String cidade = txtCidade.getText();
	    String bairro = txtBairro.getText();
	    String complemento = txtComplemento.getText();
	    Categoria categoria = (Categoria) cbCategorias.getSelectedItem();
	    
	    if(nome.isEmpty() && descricao.isEmpty()
	    		&& logradouro.isEmpty() && cidade.isEmpty()
	    		&& 	bairro.isEmpty() && complemento.isEmpty()
	    		&& categoria.equals(null)) {
	    	return false;
	    } else {
	    	return true;
	    }
	    
	 }
	
}
