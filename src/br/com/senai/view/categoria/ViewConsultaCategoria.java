package br.com.senai.view.categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
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
import br.com.senai.core.service.CategoriaService;
import br.com.senai.view.componentes.CategoriaTableModel;

public class ViewConsultaCategoria extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField edtFiltro;
	private JTable tableCategoria;
	
	private CategoriaService service;
	
	public ViewConsultaCategoria() {
		
		CategoriaTableModel model = new CategoriaTableModel(new ArrayList<Categoria>());
		this.tableCategoria =new JTable(model);
		this.service = new CategoriaService();
		
		this.tableCategoria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setTitle("Gerenciar Categoria - Listagem");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 664, 576);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroCategoria view = new ViewCadastroCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		btnNovo.setBounds(549, 11, 89, 23);
		contentPane.add(btnNovo);
		
		JLabel lblNewLabel = new JLabel("Filtros:");
		lblNewLabel.setBounds(10, 63, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome:");
		lblNewLabel_1.setBounds(53, 88, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		edtFiltro = new JTextField();
		edtFiltro.setBounds(109, 85, 409, 20);
		contentPane.add(edtFiltro);
		edtFiltro.setColumns(10);
		
		JButton btnListar = new JButton("Listar");
		btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String filtro = edtFiltro.getText();
					List<Categoria> categoriaResultado = service.listarPor(filtro);
					CategoriaTableModel model = new CategoriaTableModel(categoriaResultado);
					tableCategoria.setModel(model);
					configurarTabela();
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
				
			}
		});
		btnListar.setBounds(549, 84, 89, 23);
		contentPane.add(btnListar);
		
		JLabel lblNewLabel_2 = new JLabel("Categoria Encontradas: ");
		lblNewLabel_2.setBounds(10, 151, 146, 14);
		contentPane.add(lblNewLabel_2);
		
		JScrollPane spCategoria = new JScrollPane(tableCategoria);
		spCategoria.setBounds(10, 195, 628, 224);
		contentPane.add(spCategoria);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					int linhaSelecionada = tableCategoria.getSelectedRow();
				
					CategoriaTableModel model = (CategoriaTableModel) tableCategoria.getModel();
				
					if (linhaSelecionada >= 0 && !model.isVazio() && !model.isLinhaInvalida(linhaSelecionada)) {
						int op = JOptionPane.showConfirmDialog(contentPane, 
								"Deseja realmente remover?", 
								"Remoção", JOptionPane.YES_NO_OPTION);
						
						if (op == 0) {
							
							Categoria CategoriaSelecionada = model.getPor(linhaSelecionada);
							
							model.removerPor(linhaSelecionada);
							service.removerPor(CategoriaSelecionada.getId());
							
							tableCategoria.updateUI();
							JOptionPane.showMessageDialog(contentPane, "Categoria excluida com sucesso");
						}		
					} else {
						JOptionPane.showMessageDialog(contentPane, "Selecione um registro na tabela para remoção");
					}				
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
				
			}
		});
		btnExcluir.setBounds(549, 458, 89, 23);
		contentPane.add(btnExcluir);
		
		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int linhaSelecionada = tableCategoria.getSelectedRow();
					
					CategoriaTableModel model = (CategoriaTableModel) tableCategoria.getModel();
					
					if (linhaSelecionada >=0 && !model.isVazio() && !model.isLinhaInvalida(linhaSelecionada)) {
						Categoria categoriaSelecionada = model.getPor(linhaSelecionada);
						ViewCadastroCategoria view = new ViewCadastroCategoria();
						view.alterarCategoria(categoriaSelecionada);
						view.setVisible(true);
						dispose();
						
					} else {
						JOptionPane.showMessageDialog(contentPane, "Selecione um registro na tabela para alteração");
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
				}
				
			}
		});
		btnEditar.setBounds(429, 458, 89, 23);
		contentPane.add(btnEditar);
		
		setLocationRelativeTo(null);
	}
	private void configurarColuna(int indice, int largura) {
		this.tableCategoria.getColumnModel().getColumn(indice).setResizable(false); // nao deixa alterar o tamanho
		this.tableCategoria.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_ID =0;
		final int COLUNA_NOME =1;
		this.tableCategoria.getTableHeader().setReorderingAllowed(false); //nao deixa mexer na coluna
		this.tableCategoria.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_ID, 3);
		this.configurarColuna(COLUNA_NOME, 250);
	}
	
}
