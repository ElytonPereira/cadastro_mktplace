package br.com.senai.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;

public class ViewCadastroRestaurante extends JFrame {

	private JPanel contentPane;
	private JTextField edtnome;
	private JTextField edtDescricao;
	private JTextField edtlogradouro;
	private JTextField edtCidade;
	private JTextField edtBairro;
	private JTextField edtComplemento;
	private JComboBox<Categoria> cbCategoria;
	
	private CategoriaService serviceCategoria = new CategoriaService();
	
	private Restaurante restaurante;
	private RestauranteService serviceRestaurante = new RestauranteService(); 

	public void carregarComboCategoria() {
		List<Categoria> categorias = serviceCategoria.listarCategoria();
		for(Categoria f : categorias ) {
			this.cbCategoria.addItem(f);
		}		
	}
	
	
	public ViewCadastroRestaurante() {
		setResizable(false);
		setTitle("Gerenciar Restaurante - Cadastro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 641, 415);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setBounds(495, 11, 100, 23);
		contentPane.add(btnPesquisar);
		
		JLabel lblNewLabel = new JLabel("Nome:");
		lblNewLabel.setBounds(10, 59, 46, 14);
		contentPane.add(lblNewLabel);
		
		edtnome = new JTextField();
		edtnome.setBounds(66, 56, 281, 20);
		contentPane.add(edtnome);
		edtnome.setColumns(10);
		
		cbCategoria = new JComboBox();
		cbCategoria.setBounds(426, 55, 143, 22);
		contentPane.add(cbCategoria);
		
		JLabel lblNewLabel_1 = new JLabel("Categoria:");
		lblNewLabel_1.setBounds(357, 59, 59, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblDescrio = new JLabel("Descrição:");
		lblDescrio.setBounds(10, 84, 69, 14);
		contentPane.add(lblDescrio);
		
		edtDescricao = new JTextField();
		edtDescricao.setBounds(100, 87, 495, 125);
		contentPane.add(edtDescricao);
		edtDescricao.setColumns(10);
		
		JLabel lblLogradouro = new JLabel("Logradouro:");
		lblLogradouro.setBounds(10, 238, 69, 14);
		contentPane.add(lblLogradouro);
		
		edtlogradouro = new JTextField();
		edtlogradouro.setColumns(10);
		edtlogradouro.setBounds(88, 235, 411, 20);
		contentPane.add(edtlogradouro);
		
		edtCidade = new JTextField();
		edtCidade.setColumns(10);
		edtCidade.setBounds(79, 272, 191, 20);
		contentPane.add(edtCidade);
		
		JLabel lblCidade = new JLabel("Cidade:");
		lblCidade.setBounds(10, 278, 56, 14);
		contentPane.add(lblCidade);
		
		JLabel lblBairro = new JLabel("Bairro:");
		lblBairro.setBounds(280, 275, 55, 14);
		contentPane.add(lblBairro);
		
		edtBairro = new JTextField();
		edtBairro.setColumns(10);
		edtBairro.setBounds(345, 272, 191, 20);
		contentPane.add(edtBairro);
		
		JLabel lblComplemento = new JLabel("Complemento:");
		lblComplemento.setBounds(10, 313, 93, 14);
		contentPane.add(lblComplemento);
		
		edtComplemento = new JTextField();
		edtComplemento.setColumns(10);
		edtComplemento.setBounds(113, 310, 471, 20);
		contentPane.add(edtComplemento);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int op = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente cancelar?", "Remoção", JOptionPane.YES_NO_OPTION);
				
				if(op ==0) {
					edtnome.setText("");
					edtDescricao.setText("");
					edtlogradouro.setText("");
					edtCidade.setText("");
					edtBairro.setText("");				
					edtComplemento.setText("");
					JOptionPane.showMessageDialog(contentPane, "Operação cancelada!");
					
				}
				
			}
		});
		btnCancelar.setBounds(515, 342, 100, 23);
		contentPane.add(btnCancelar);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Categoria categoria = (Categoria) cbCategoria.getSelectedItem();
					String nome = edtnome.getText();
					String descricao = edtDescricao.getText();
					String logradouro = edtlogradouro.getText();
					String cidade = edtCidade.getText();
					String bairro = edtBairro.getText();
					String Complemento = edtComplemento.getText();
					
					Endereco endereco = new Endereco(cidade, logradouro, bairro, Complemento);
					
					if(restaurante ==null) {
						restaurante = new Restaurante(nome, descricao, endereco, categoria);
						serviceRestaurante.salvar(restaurante);
						JOptionPane.showMessageDialog(contentPane, "Restaurante inserido com sucesso");
						 edtnome.setText("");
						 edtDescricao.setText("");
						 edtlogradouro.setText("");
						 edtCidade.setText("");
						 edtBairro.setText("");
						 edtComplemento.setText("");
						 
						 restaurante =null;

					}
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
					restaurante = null;
				}
			}
		});
		btnSalvar.setBounds(399, 342, 100, 23);
		contentPane.add(btnSalvar);
		this.carregarComboCategoria();
	}
}
