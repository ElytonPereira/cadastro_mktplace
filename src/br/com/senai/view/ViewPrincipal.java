package br.com.senai.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ViewPrincipal extends JFrame {

	private JPanel contentPane;

	public ViewPrincipal() {
		setResizable(false);
		setTitle("Tela Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 832, 562);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuCadastro = new JMenu("Cadastros");
		menuBar.add(menuCadastro);
		
		JMenuItem opCategoria = new JMenuItem("Categorias");
		opCategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroCategoria view = new ViewCadastroCategoria();
				view.setVisible(true);
			}
		});
		menuCadastro.add(opCategoria);
		
		JMenuItem opRestaurante = new JMenuItem("Restaurantes");
		opRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroRestaurante view = new ViewCadastroRestaurante();
				view.setVisible(true);
				
			}
		});
		menuCadastro.add(opRestaurante);
		
		JMenu menuConfiguraes = new JMenu("Configurações");
		menuBar.add(menuConfiguraes);
		
		JMenuItem opHorario = new JMenuItem("Horários");
		opHorario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroHorario view = new ViewCadastroHorario();
				view.setVisible(true);		
				
			}
		});
		menuConfiguraes.add(opHorario);
		
		JMenu mnSair = new JMenu("Sair");
		mnSair.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		menuBar.add(mnSair);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
