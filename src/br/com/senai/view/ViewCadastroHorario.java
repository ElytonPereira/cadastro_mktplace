package br.com.senai.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.DiaSemana;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioAtendimentoService;
import br.com.senai.core.service.RestauranteService;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JFormattedTextField;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionEvent;

public class ViewCadastroHorario extends JFrame {

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
	
	public ViewCadastroHorario() {
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
						JOptionPane.showMessageDialog(contentPane, "Horário inserido com sucesso");
						ftfAbertura.setText("");
						ftfFechamento.setText("");
						cbDias.setSelectedIndex(0);
						horarioAtendimento = null;
					}
					
					
					
				}catch (DateTimeParseException dtpe) {
					JOptionPane.showMessageDialog(contentPane, "A hora de abertura e fechamento é obrigatória e deve estar no formato HH:MM");	
				}
				catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
					horarioAtendimento = null;
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
		btnEditar.setBounds(546, 142, 89, 23);
		contentPane.add(btnEditar);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(546, 176, 89, 23);
		contentPane.add(btnExcluir);

		JButton btnCancelar = new JButton("Cancelar");
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
}
