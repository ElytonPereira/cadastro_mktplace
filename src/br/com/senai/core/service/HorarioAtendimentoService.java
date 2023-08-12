package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class HorarioAtendimentoService {

	private DaoHorarioAtendimento dao;
		
	public HorarioAtendimentoService() {
		this.dao = FactoryDao.getInstance().getDaoHorarioAtendimento();
	}
	
	public void salvar(HorarioAtendimento horarioAtendimento) {
		this.validar(horarioAtendimento);
		
		boolean isJaInserido = horarioAtendimento.getId() >0;
		if(isJaInserido) {
			this.dao.alterar(horarioAtendimento);
			
		}else {
			this.dao.inserir(horarioAtendimento);
			
		}
	}
	
	public List<HorarioAtendimento> listarPor(Restaurante restaurante){
		return dao.listarPor(restaurante.getId());
	}
	
	public void removerPor(int id) {
		if(id >0) {
			this.dao.excluirPor(id);
		}else {
			throw new IllegalArgumentException("O id do horario deve ser maior que 0");
		}
	}
		
	public void validar(HorarioAtendimento horarioAtendimento) {
		if(horarioAtendimento != null) {
			
			boolean horarioInvalido = horarioAtendimento.getHoraFechamento().isBefore(horarioAtendimento.getHoraAbertura());
			
			if (horarioInvalido) {
				throw new IllegalArgumentException("A hora do fechamento não pode ser menor que a abertura");
			}
			
			boolean intervaloHorarioInvalido =  (horarioAtendimento.getHoraAbertura().isAfter(horarioAtendimento.getHoraFechamento()) || 
	                horarioAtendimento.getHoraFechamento().isBefore(horarioAtendimento.getHoraAbertura())) && (horarioAtendimento.getDia().equals(horarioAtendimento.getDia()));
			
			if (intervaloHorarioInvalido) {
				throw new IllegalArgumentException("As de abertura e fechamento não podem se chocar no mesmo dia");
			}
			
			boolean isHorarioAberturaInvalido = horarioAtendimento.getHoraAbertura() ==null || horarioAtendimento.getHoraAbertura().equals("  :  :  ") || horarioAtendimento.getHoraAbertura().getHour() >24;
			
			if (isHorarioAberturaInvalido) {
				throw new IllegalArgumentException("A hora de abertura é obrigatória e deve estar no formato HH/MM e deve ser informada até as 23:59");
			}			
			
			boolean isHorarioFechamentoInvalido = horarioAtendimento.getHoraFechamento() ==null || horarioAtendimento.getHoraFechamento().equals("  :  :  ") || horarioAtendimento.getHoraFechamento().getHour() >24 ;
			
			if (isHorarioFechamentoInvalido) {
				throw new IllegalArgumentException("A hora de fechamento é obrigatória e deve estar no formato HH/MM e deve ser informada até as 23:59");
			}
			
			boolean diaInvalido = horarioAtendimento.getDia() ==null;
			
			if (diaInvalido) {
				throw new IllegalArgumentException("O dia esta vazio informe o dia");
			}
			
			
		}else {
			throw new IllegalArgumentException("O horario de atendimento não pode ser nulo");
		}		
		
	}	
	
}
