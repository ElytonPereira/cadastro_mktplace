package br.com.senai.view.horarioAtendimento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.DiaSemana;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioAtendimentoService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.HorarioTableModel;

public class ViewCadastroHorario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tableHorario;
	JFormattedTextField ftfAbertura;
	JFormattedTextField ftfFechamento;		
	
	private JComboBox<Restaurante> cbRestaurante;
	private JComboBox<DiaSemana> cbDias;
	
	private RestauranteService restauranteService =new RestauranteService();
	private HorarioAtendimentoService horarioAtendimentoService = new HorarioAtendimentoService();
	private HorarioAtendimento horarioAtendimento;
	
	public void carregarComboRestaurante() {
		List<Restaurante> restaurantes = restauranteService.listarRestaurantes();
		for(Restaurante f : restaurantes ) {
			this.cbRestaurante.addItem(f);
		}		
	}
	
	public void carregarComboDia() {
		List<DiaSemana> listaDias = new ArrayList<>();
		for (DiaSemana dia : DiaSemana.values()) {
            listaDias.add(dia);
            this.cbDias.addItem(dia);
        }
		
	}
	
	private void alterarHorario(HorarioAtendimento horarioSelecionado) {
		
		this.horarioAtendimento = horarioSelecionado;
		cbRestaurante.setSelectedItem(horarioSelecionado.getRestaurante());
		cbDias.setSelectedItem(horarioSelecionado.getDia());
		ftfAbertura.setText(horarioSelecionado.getHoraAbertura().toString());
		ftfFechamento.setText(horarioSelecionado.getHoraFechamento().toString());
		
	}
	
	public ViewCadastroHorario() {
		
		HorarioTableModel model = new HorarioTableModel(new ArrayList<HorarioAtendimento>());
		this.tableHorario = new JTable(model);
		
		this.tableHorario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setTitle("Gerenciar Horários - Cadastro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 726, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		JLabel lblNewLabel = new JLabel("Restaurante: ");
		lblNewLabel.setBounds(10, 24, 80, 14);
		contentPane.add(lblNewLabel);

		cbRestaurante = new JComboBox();
		cbRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Restaurante restauranteCb = (Restaurante) cbRestaurante.getSelectedItem();
				List<HorarioAtendimento> horarios = horarioAtendimentoService.listarPor(restauranteCb);
				HorarioTableModel model2 = new HorarioTableModel(horarios);
				tableHorario.setModel(model2);
				tableHorario.updateUI();
				configurarTabela();
			}
		});
		cbRestaurante.setBounds(100, 20, 582, 22);
		contentPane.add(cbRestaurante);

		JLabel lblNewLabel_1 = new JLabel("Dia da Semana:");
		lblNewLabel_1.setBounds(10, 72, 97, 14);
		contentPane.add(lblNewLabel_1);

		cbDias = new JComboBox();
		cbDias.setBounds(110, 63, 141, 22);
		contentPane.add(cbDias);

		JLabel lblNewLabel_2 = new JLabel("Abertura: ");
		lblNewLabel_2.setBounds(261, 72, 64, 14);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("Fechamento: ");
		lblNewLabel_2_1.setBounds(417, 72, 91, 14);
		contentPane.add(lblNewLabel_2_1);

		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
					
					Restaurante restaurante = (Restaurante) cbRestaurante.getSelectedItem();
					DiaSemana diaSemana =(DiaSemana) cbDias.getSelectedItem();
					LocalTime horAbertura = LocalTime.from(dtf.parse(ftfAbertura.getText()));
					LocalTime horFechamento = LocalTime.from(dtf.parse(ftfFechamento.getText()));								
					
					if (horarioAtendimento ==null) {
						HorarioAtendimento horarioAtendimento = new HorarioAtendimento(horAbertura, horFechamento, restaurante, diaSemana);
						
						horarioAtendimentoService.salvar(horarioAtendimento);						
						JOptionPane.showMessageDialog(contentPane, "Horário salvo com sucesso");
						
						ftfAbertura.setText("");
						ftfFechamento.setText("");
						cbDias.setSelectedIndex(0);
						horarioAtendimento = null;
						
					}else {
						horarioAtendimento.setDia(diaSemana);
						horarioAtendimento.setHoraAbertura(horAbertura);
						horarioAtendimento.setHoraFechamento(horFechamento);
						horarioAtendimento.setRestaurante(restaurante);
						
						horarioAtendimentoService.salvar(horarioAtendimento);
						
						JOptionPane.showMessageDialog(contentPane, "Horário alterado com sucesso");
						ftfAbertura.setText("");
						ftfFechamento.setText("");
						cbDias.setSelectedIndex(0);						
						
						horarioAtendimento = null;
					}					
					
					Restaurante restauranteCb = (Restaurante) cbRestaurante.getSelectedItem();
					List<HorarioAtendimento> horarios = horarioAtendimentoService.listarPor(restauranteCb);
					HorarioTableModel model2 = new HorarioTableModel(horarios);
					tableHorario.setModel(model2);
					tableHorario.updateUI();
					configurarTabela();
					
				}catch (DateTimeParseException dtpe) {
					JOptionPane.showMessageDialog(contentPane, "A hora de abertura e fechamento é obrigatória e deve estar no formato HH:MM");	
				}
				catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());					
				} 
				
			}
		});
		btnAdicionar.setBounds(611, 68, 89, 23);
		contentPane.add(btnAdicionar);

		JScrollPane spHorario = new JScrollPane();
		spHorario.setBounds(10, 145, 476, 144);
		contentPane.add(spHorario);

		tableHorario = new JTable();
		spHorario.setViewportView(tableHorario);

		JLabel lblNewLabel_3 = new JLabel("Horários: ");
		lblNewLabel_3.setBounds(10, 120, 69, 14);
		contentPane.add(lblNewLabel_3);

		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int linhaSelecionada = tableHorario.getSelectedRow();
				
				HorarioTableModel model = (HorarioTableModel) tableHorario.getModel();
				
				if(linhaSelecionada >=0 && !model.isVazio() && !model.isLinhaInvalida(linhaSelecionada)) {
					HorarioAtendimento horarioSelecionado = model.getPor(linhaSelecionada);
					alterarHorario(horarioSelecionado);
					
				}else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para editar");
				}
				
			}
		});
		btnEditar.setBounds(546, 142, 89, 23);
		contentPane.add(btnEditar);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					int linhaSelecionada = tableHorario.getSelectedRow();
					
					HorarioTableModel model = (HorarioTableModel) tableHorario.getModel();
					
					if (linhaSelecionada >= 0 && !model.isVazio() && !model.isLinhaInvalida(linhaSelecionada)) {
						int op = JOptionPane.showConfirmDialog(contentPane, 
								"Deseja realmente remover?", 
								"Remoção", JOptionPane.YES_NO_OPTION);
						
						if (op == 0) {
							HorarioAtendimento horarioSelecionado =model.getPor(linhaSelecionada);
							
							model.removerPor(linhaSelecionada);
							horarioAtendimentoService.removerPor(horarioSelecionado.getId());
							tableHorario.updateUI();
							JOptionPane.showMessageDialog(contentPane, "Horario excluido com sucesso!");
							
						}
						
					} else {
						JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para excluir!");
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
			}
		});
		btnExcluir.setBounds(546, 176, 89, 23);
		contentPane.add(btnExcluir);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ftfAbertura.setText("");
				ftfFechamento.setText("");
				cbDias.setSelectedIndex(0);
				horarioAtendimento = null;
			}
		});
		btnCancelar.setBounds(588, 308, 89, 23);
		contentPane.add(btnCancelar);
		
		ftfAbertura = new JFormattedTextField();
		ftfAbertura.setBounds(316, 72, 83, 14);
		contentPane.add(ftfAbertura);
		try {
			MaskFormatter mascara = new MaskFormatter("##:##:00");
			mascara.install(ftfAbertura);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ftfFechamento = new JFormattedTextField();
		ftfFechamento.setBounds(503, 72, 83, 14);
		contentPane.add(ftfFechamento);
		try {
			MaskFormatter mascara = new MaskFormatter("##:##:00");
			mascara.install(ftfFechamento);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		this.carregarComboRestaurante();
		
		this.carregarComboDia();
	}
	
	private void configurarColuna(int indice, int largura) {
		this.tableHorario.getColumnModel().getColumn(indice).setResizable(false); // nao deixa alterar o tamanho
		this.tableHorario.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_DIA =0;
		final int COLUNA_ABERTURA =1;
		final int COLUNA_FECHAMENTO =2;
		this.tableHorario.getTableHeader().setReorderingAllowed(false); //nao deixa mexer na coluna
		this.tableHorario.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_DIA, 150);
		this.configurarColuna(COLUNA_ABERTURA, 150);
		this.configurarColuna(COLUNA_FECHAMENTO, 150);
	}
}
