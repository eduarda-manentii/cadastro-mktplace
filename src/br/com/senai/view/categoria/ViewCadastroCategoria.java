package br.com.senai.view.categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.util.Utilitaria;

public class ViewCadastroCategoria extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCategoria;
	private JButton btnSalvar;
	
	private Categoria categoria;
	private CategoriaService service;
	private boolean isEdicaoCategoria;

	public ViewCadastroCategoria() {
		this.service = new CategoriaService();
		setResizable(false);
		setTitle("Gerenciar Categoria - Cadastro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		txtCategoria = new JTextField();
		txtCategoria.setBounds(24, 96, 383, 31);
		contentPane.add(txtCategoria);
		txtCategoria.setColumns(10);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(26, 69, 70, 26);
		lblNome.setVerticalAlignment(SwingConstants.BOTTOM);
		contentPane.add(lblNome);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewPesquisaCategoria view = new ViewPesquisaCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		
		btnPesquisar.setBounds(307, 27, 117, 25);
		contentPane.add(btnPesquisar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCategoria.setText(null);
			}
		});
		btnCancelar.setBounds(178, 226, 117, 25);
		contentPane.add(btnCancelar);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = txtCategoria.getText();
			        
			        if (!isEdicaoCategoria) {
			            categoria = new Categoria(nome);
				        service.salvar(categoria);
			            JOptionPane.showMessageDialog(contentPane, "Categoria salva.");
			            categoria = null;
			        	Utilitaria.limparCampos(contentPane);
			        } else {
				        service.salvar(categoria);
			            JOptionPane.showMessageDialog(contentPane, "Categoria alterada.");
			            categoria.setNome(nome);
			        }
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnSalvar.setBounds(307, 226, 117, 25);
		contentPane.add(btnSalvar);
		
	}
		
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
		this.txtCategoria.setText(categoria.getNome());
	}
	

	public void setEdicaoCategoria(boolean isEdicaoCategoria) {
        this.isEdicaoCategoria = isEdicaoCategoria;
    }

		
}
