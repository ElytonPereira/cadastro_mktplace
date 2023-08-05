package br.com.senai.view.categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.service.CategoriaService;

public class ViewCadastroCategoria extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField edtNome;
	
	private CategoriaService service = new CategoriaService();
	private Categoria categoria;
	
	public void alterarCategoria(Categoria categoria) {
		this.categoria = categoria;
		this.edtNome.setText(categoria.getNome());
	}

	public ViewCadastroCategoria() {
		setTitle("Gerenciar Categoria - Cadastro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 148);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JLabel lblNewLabel = new JLabel("Nome:");
		lblNewLabel.setBounds(10, 48, 46, 14);
		contentPane.add(lblNewLabel);
		
		edtNome = new JTextField();
		edtNome.setBounds(45, 45, 379, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaCategoria view = new ViewConsultaCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		btnPesquisar.setBounds(323, 11, 101, 23);
		contentPane.add(btnPesquisar);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				try {
					String nome = edtNome.getText();
					
					if (categoria ==null) {
						categoria = new Categoria(nome);
						service.salvar(categoria);
						JOptionPane.showMessageDialog(contentPane, "Categoria inserido com sucesso");
						edtNome.setText("");
																		
						categoria = null;
						
					}else {
						categoria.setNome(nome);
						service.salvar(categoria);
						JOptionPane.showMessageDialog(contentPane, "Categoria alterada com sucesso");
						edtNome.setText("");
						categoria = null;
					}
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(contentPane, e2.getMessage());
					categoria = null;
				}
				
			}
		});
		btnSalvar.setBounds(232, 76, 89, 23);
		contentPane.add(btnSalvar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edtNome.setText("");
			}
		});
		btnCancelar.setBounds(335, 76, 89, 23);
		contentPane.add(btnCancelar);
	}
}
