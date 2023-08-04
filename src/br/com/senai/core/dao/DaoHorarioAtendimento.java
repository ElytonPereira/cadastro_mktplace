package br.com.senai.core.dao;

import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public interface DaoHorarioAtendimento {

	public void inserir(HorarioAtendimento horarioAtendimento);
	public void alterar(HorarioAtendimento horarioAtendimento);
	public void excluirPor(int id);
	public Restaurante buscarPor(int id);
	public int contarPor(int idDoRestaurante);
	
}
