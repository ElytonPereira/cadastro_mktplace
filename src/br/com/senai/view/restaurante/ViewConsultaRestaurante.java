package br.com.senai.view.restaurante;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.RestauranteTableModel;

public class ViewConsultaRestaurante extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField edtNome;
	private JTable tableRestaurante;
	private JComboBox<Categoria> cbCategoria;
	private CategoriaService categoriaService = new CategoriaService();
	private RestauranteService restauranteService = new RestauranteService(); 
	
	public void carregarComboCategoria() {
		List<Categoria> categorias = categoriaService.listarCategoria();
		for(Categoria f : categorias ) {
			this.cbCategoria.addItem(f);
		}		
	}
	
	public ViewConsultaRestaurante() {
		
		RestauranteTableModel model = new RestauranteTableModel(new ArrayList<Restaurante>());
		this.tableRestaurante = new JTable(model);
		this.restauranteService = new RestauranteService();
		
		this.tableRestaurante.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setTitle("Gerenciar Restaurante - Listagem");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 660, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewCadastroRestaurante view = new ViewCadastroRestaurante();
				view.setVisible(true);
				dispose();
			}
		});
		btnNovo.setBounds(545, 11, 89, 23);
		contentPane.add(btnNovo);
		
		JLabel lblNewLabel = new JLabel("Filtros");
		lblNewLabel.setBounds(10, 42, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome");
		lblNewLabel_1.setBounds(20, 68, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		edtNome = new JTextField();
		edtNome.setBounds(53, 65, 223, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Categoria");
		lblNewLabel_1_1.setBounds(290, 68, 71, 14);
		contentPane.add(lblNewLabel_1_1);
		
		cbCategoria = new JComboBox();
		cbCategoria.setBounds(354, 64, 164, 22);
		contentPane.add(cbCategoria);
		
		JLabel lblRestaurantesEncontrados = new JLabel("Restaurantes Encontrados");
		lblRestaurantesEncontrados.setBounds(10, 129, 223, 14);
		contentPane.add(lblRestaurantesEncontrados);
		
		JScrollPane spRestaurante = new JScrollPane(tableRestaurante);
		spRestaurante.setBounds(20, 171, 592, 159);
		contentPane.add(spRestaurante);
		
		tableRestaurante = new JTable();
		spRestaurante.setViewportView(tableRestaurante);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					int linhaSelecionada = tableRestaurante.getSelectedRow();
					
					RestauranteTableModel model = (RestauranteTableModel) tableRestaurante.getModel();
					
					if(linhaSelecionada >=0 && !model.isVazio() && !model.isLinhaInvalida(linhaSelecionada)) {
						Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
						ViewCadastroRestaurante view = new ViewCadastroRestaurante();
						view.alterarRestaurante(restauranteSelecionado);
						view.setVisible(true);
						dispose();
						
					}
					
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
				
			}
		});
		btnEditar.setBounds(422, 341, 89, 23);
		contentPane.add(btnEditar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					int linhaSelecionada = tableRestaurante.getSelectedRow();
					
					RestauranteTableModel model = (RestauranteTableModel) tableRestaurante.getModel();
					
					if(linhaSelecionada >=0 && !model.isVazio() && !model.isLinhaInvalida(linhaSelecionada)) {
						int op = JOptionPane.showConfirmDialog(contentPane, 
								"Deseja realmente remover?", 
								"Remoção", JOptionPane.YES_NO_OPTION);
						
						if (op == 0) {
							Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
							model.removerPor(linhaSelecionada);
							restauranteService.removerPor(restauranteSelecionado.getId());
							
							tableRestaurante.updateUI();
							JOptionPane.showMessageDialog(contentPane, "Restaurante excluido com sucesso");							
							
						}else {
							JOptionPane.showMessageDialog(contentPane, "Selecione um registro na tabela para remoção");
						}
						
					}
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
			}
		});
		btnExcluir.setBounds(523, 341, 89, 23);
		contentPane.add(btnExcluir);
		
		JButton btnListar = new JButton("Listar");
		btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					String nome = edtNome.getText();
					Categoria categoria = (Categoria) cbCategoria.getSelectedItem();
					List<Restaurante> restauranteResultado = restauranteService.listarPor(nome, categoria);
					RestauranteTableModel model = new RestauranteTableModel(restauranteResultado);
					tableRestaurante.setModel(model);
					configurarTabela();					
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
				
			}
		});
		btnListar.setBounds(545, 64, 89, 23);
		contentPane.add(btnListar);
		this.carregarComboCategoria();
	}
	
	private void configurarColuna(int indice, int largura) {
		this.tableRestaurante.getColumnModel().getColumn(indice).setResizable(false); // nao deixa alterar o tamanho
		this.tableRestaurante.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_DIA =0;
		final int COLUNA_ABERTURA =1;
		final int COLUNA_FECHAMENTO =2;
		this.tableRestaurante.getTableHeader().setReorderingAllowed(false); //nao deixa mexer na coluna
		this.tableRestaurante.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_DIA, 3);
		this.configurarColuna(COLUNA_ABERTURA, 150);
		this.configurarColuna(COLUNA_FECHAMENTO, 150);
	}
	
}
