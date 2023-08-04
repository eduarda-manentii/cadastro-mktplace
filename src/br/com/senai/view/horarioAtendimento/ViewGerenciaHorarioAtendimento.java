package br.com.senai.view.horarioAtendimento;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.ComparacaoDeHorario;
import br.com.senai.core.domain.DiasDaSemana;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioAtendimentoService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.core.util.Utilitaria;
import br.com.senai.view.componentes.table.HorarioAtendimentoTableModel;

public class ViewGerenciaHorarioAtendimento extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFormattedTextField txtAbertura;
	private JFormattedTextField txtFechamento;
	JComboBox<Restaurante> cbRestaurantes;
	JComboBox<DiasDaSemana> cbDiasDaSemana;
	private JTable tableHorarioAtendimento;
    private JProgressBar progressBar;
	
	private RestauranteService restauranteService;
	private HorarioAtendimento horarioAtendimento;
	private HorarioAtendimentoService horarioService;
	private boolean isAlteracao = false;
	private List<Component> camposManter = new ArrayList<>();
	
	public ViewGerenciaHorarioAtendimento() {
		this.restauranteService = new RestauranteService();
		this.horarioService = new HorarioAtendimentoService();
		
		HorarioAtendimentoTableModel model = new HorarioAtendimentoTableModel(new ArrayList<HorarioAtendimento>());
		this.tableHorarioAtendimento = new JTable(model);
		tableHorarioAtendimento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setTitle("Gerenciar horarios - Cadastro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 780, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel lblHorario = new JLabel("Hor\u00E1rios");
		lblHorario.setBounds(22, 124, 70, 24);
		contentPane.add(lblHorario);
		
		JLabel lblRestaurante = new JLabel("Restaurante");
		lblRestaurante.setBounds(42, 26, 110, 15);
		contentPane.add(lblRestaurante);
		
		cbRestaurantes = new JComboBox<Restaurante>();
		List<Restaurante> restaurantes = restauranteService.listarTodas();
		Restaurante placeholder = new Restaurante("Selecione...");
		Utilitaria.preencherCombo(cbRestaurantes, restaurantes, placeholder);
		cbRestaurantes.setBounds(147, 21, 601, 24);
		contentPane.add(cbRestaurantes);
		
		progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);
		
		JLabel lblDiaSemana = new JLabel("Dia de Semana");
		lblDiaSemana.setBounds(12, 71, 128, 15);
		contentPane.add(lblDiaSemana);
		
		DiasDaSemana[] valoresOpcoes = DiasDaSemana.values();
		cbDiasDaSemana = new JComboBox<>(valoresOpcoes);
		cbDiasDaSemana.setSelectedIndex(0);
		cbDiasDaSemana.insertItemAt(null, 0);
		cbDiasDaSemana.setSelectedIndex(0);
		cbDiasDaSemana.setBounds(126, 66, 128, 24);
		contentPane.add(cbDiasDaSemana);
		
		JLabel lblAbertura = new JLabel("Abertura");
		lblAbertura.setBounds(260, 71, 101, 15);
		contentPane.add(lblAbertura);
		
		txtAbertura = new JFormattedTextField();
		txtAbertura.setBounds(341, 67, 101, 24);
		contentPane.add(txtAbertura);
		
		try {
			MaskFormatter mascaraHora = new MaskFormatter("##:##");
	        mascaraHora.install(txtAbertura);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(contentPane, ex.getMessage());
		}		
        
		JLabel lblFechamento = new JLabel("Fechamento");
		lblFechamento.setBounds(455, 71, 101, 15);
		contentPane.add(lblFechamento);
		
		txtFechamento = new JFormattedTextField();
		txtFechamento.setBounds(548, 67, 93, 24);
		contentPane.add(txtFechamento);
		
		try {
			MaskFormatter mascaraHora = new MaskFormatter("##:##");
	        mascaraHora.install(txtFechamento);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(contentPane, ex.getMessage());
		}
		
		cbRestaurantes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					Restaurante restaurante = (Restaurante) cbRestaurantes.getSelectedItem();
					atualizaTabelaHorarios(restaurante);
					if(isAlteracao == true) {
						camposManter.add(cbRestaurantes);
						Utilitaria.limparCampos(contentPane, camposManter);
					}
				}
				
			}
		});
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            Restaurante restaurante = (Restaurante) cbRestaurantes.getSelectedItem();
		            DiasDaSemana diasDaSemana = (DiasDaSemana) cbDiasDaSemana.getSelectedItem();
		            LocalTime horaAbertura = extrair(txtAbertura);
		            LocalTime horaFechamento = extrair(txtFechamento);

	            	if (isAlteracao == false) {
                        horarioAtendimento = new HorarioAtendimento(diasDaSemana, horaAbertura, horaFechamento, restaurante);
                        horarioService.salvar(horarioAtendimento);
                        JOptionPane.showMessageDialog(contentPane, "Horario de atendimento salvo.");
                        Restaurante restaurante2 = (Restaurante) cbRestaurantes.getSelectedItem();
                        atualizaTabelaHorarios(restaurante2);
                        camposManter.add(cbRestaurantes);
                        Utilitaria.limparCampos(contentPane, camposManter);
	            	} else {
                        horarioAtendimento.setRestaurante(restaurante);
                        horarioAtendimento.setDiaSemana(diasDaSemana);
                        horarioAtendimento.setHoraAbertura(horaAbertura);
                        horarioAtendimento.setHoraFechamento(horaFechamento);
                        horarioService.salvar(horarioAtendimento);
                        JOptionPane.showMessageDialog(contentPane, "Horario de atendimento alterada com sucesso!");
                        isAlteracao = false;
                        camposManter.add(cbRestaurantes);
                        Utilitaria.limparCampos(contentPane, camposManter);
                        atualizaTabelaHorarios(restaurante);
		        } 
	            	} catch (DateTimeException ex) {
		            JOptionPane.showMessageDialog(contentPane, "Digite um valor para a hora válido.");
				}catch (Exception ex) {
		            JOptionPane.showMessageDialog(contentPane, ex.getMessage());
		        }
		    }
		});

		btnSalvar.setBounds(653, 66, 101, 25);
		contentPane.add(btnSalvar);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        int linhaSelecionada = tableHorarioAtendimento.getSelectedRow();
		        HorarioAtendimentoTableModel model = (HorarioAtendimentoTableModel) tableHorarioAtendimento.getModel();
		        if (!model.isVazio() && linhaSelecionada >= 0) {
		            HorarioAtendimento horarioAtendimentoSelecionado = model.getPor(linhaSelecionada);
		            if (horarioAtendimentoSelecionado != null) {
		            	isAlteracao = true;
		                setHorarioAtendimento(horarioAtendimentoSelecionado);
		            } else {
		                JOptionPane.showMessageDialog(contentPane, "Nenhum item selecionado para edição.");
		            }
		        } else {
		            JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
		        }
		    }
		});


		btnEditar.setBounds(536, 181, 183, 25);
		contentPane.add(btnEditar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        int linhaSelecionada = tableHorarioAtendimento.getSelectedRow();
		        HorarioAtendimentoTableModel model = (HorarioAtendimentoTableModel) tableHorarioAtendimento.getModel();

		        if (linhaSelecionada >= 0 && !model.isVazio()) {
		            int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente excluir?", "Remoção", JOptionPane.YES_NO_OPTION);
		            if (opcao == 0) {
		                HorarioAtendimento horarioAtendimentoSelecionado = model.getPor(linhaSelecionada);
		                try {
		                    horarioService.excluirPor(horarioAtendimentoSelecionado.getId());
		                    model.removePor(linhaSelecionada);
		                    JOptionPane.showMessageDialog(contentPane, "Horário de atendimento excluído.");
		                } catch (Exception ex) {
		                    JOptionPane.showMessageDialog(contentPane, ex.getMessage());
		                }
		            }
		        } else {
		            JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
		        }
		    }
		});

		btnExcluir.setBounds(536, 230, 183, 25);
		contentPane.add(btnExcluir);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(631, 313, 117, 25);
		btnCancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Utilitaria.limparCampos(contentPane);
				
			}
		});
		contentPane.add(btnCancelar);
		
		JScrollPane scrollPane = new JScrollPane(tableHorarioAtendimento);
		scrollPane.setBounds(20, 160, 443, 191);
		contentPane.add(scrollPane);
		
		this.restauranteService = new RestauranteService();
		this.horarioService = new HorarioAtendimentoService();
	}
	
	void atualizaTabelaHorarios(Restaurante restaurante) {
		List<HorarioAtendimento> horarioEncontrado = horarioService.listarPor(restaurante);
		Collections.sort(horarioEncontrado, new ComparacaoDeHorario());

		HorarioAtendimentoTableModel model = new HorarioAtendimentoTableModel(horarioEncontrado);
		tableHorarioAtendimento.setModel(model);

		if(horarioEncontrado == null) {
			JOptionPane.showMessageDialog(contentPane, "Não foi encontrado nenhum horario de atendimento "
					+ "com os filtros informados.");
		} 
	}
	
	public void setHorarioAtendimento(HorarioAtendimento horarioAtendimento) {
	    this.horarioAtendimento = horarioAtendimento;

	    if (horarioAtendimento != null) {
	        this.cbDiasDaSemana.setSelectedItem(horarioAtendimento.getDiaSemana());
	        this.cbRestaurantes.setSelectedItem(horarioAtendimento.getRestaurante());

	        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

	        LocalTime horaAbertura = horarioAtendimento.getHoraAbertura();
	        this.txtAbertura.setText(horaAbertura.format(dtf));

	        LocalTime horaFechamento = horarioAtendimento.getHoraFechamento();
	        this.txtFechamento.setText(horaFechamento.format(dtf));
	    } else {
	    	Utilitaria.limparCampos(contentPane);
	    }
	}
	
	
	public LocalTime extrair(JFormattedTextField hora) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		String horario = hora.getText();
		try {
			return LocalTime.from(dtf.parse(horario));
		} catch (Exception e) {
			return null;
		}
	}
	

}