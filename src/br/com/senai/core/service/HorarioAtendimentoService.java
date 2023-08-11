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
		this.dao.inserir(horarioAtendimento);
	}
	
	public List<HorarioAtendimento> listarPor(Restaurante restaurante){
		return dao.listarPor(restaurante.getId());
	}
	
	public void validar(HorarioAtendimento horarioAtendimento) {
		if(horarioAtendimento != null) {
			
			boolean isHorarioAberturaInvalido = horarioAtendimento.getHoraAbertura() ==null || horarioAtendimento.getHoraAbertura().equals("  :  :  ") || horarioAtendimento.getHoraAbertura().getHour() >24;
			
			if (isHorarioAberturaInvalido) {
				throw new IllegalArgumentException("A hora de abertura é obrigatória e deve estar no formato HH/MM e deve ser informada até as 23:59");
			}
			
			boolean isHorarioFechamentoInvalido = horarioAtendimento.getHoraFechamento() ==null || horarioAtendimento.getHoraFechamento().equals("  :  :  ") || horarioAtendimento.getHoraFechamento().getHour() >24 ;
			
			if (isHorarioFechamentoInvalido) {
				throw new IllegalArgumentException("A hora de fechamento é obrigatória e deve estar no formato HH/MM e deve ser informada até as 23:59");
			}
			
			
		}else {
			throw new IllegalArgumentException("O horario de atendimento não pode ser nulo");
		}
		
		
	}
	
	
	
}
