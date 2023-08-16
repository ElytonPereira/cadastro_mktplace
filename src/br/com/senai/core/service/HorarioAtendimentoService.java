package br.com.senai.core.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.DiaSemana;
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
			this.validarIntervalo(horarioAtendimento);
			
			
			
			boolean horarioInvalido = horarioAtendimento.getHoraFechamento().isBefore(horarioAtendimento.getHoraAbertura());
			
			if (horarioInvalido) {
				throw new IllegalArgumentException("A hora do fechamento não pode ser menor que a abertura");
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
	
	public void validarIntervalo(HorarioAtendimento horarioAtendimento) {
		List<HorarioAtendimento> listaRestaurante = new ArrayList<HorarioAtendimento>();
		LocalTime horAberturaNovo = horarioAtendimento.getHoraAbertura();
		LocalTime horFechamentoNovo = horarioAtendimento.getHoraFechamento();
		DiaSemana diaSemanaNovo = horarioAtendimento.getDia();		
		
		listaRestaurante =  this.dao.listarPor(horarioAtendimento.getRestaurante().getId());
		
		if(!listaRestaurante.isEmpty()) {
			HorarioAtendimento horarioSalvo;
			
			for(HorarioAtendimento horarios : listaRestaurante){
				horarioSalvo = horarios;
				
				if (horarioSalvo.getDia() == diaSemanaNovo) {
					
					if (horFechamentoNovo.isAfter(horarioSalvo.getHoraAbertura()) && horAberturaNovo.isBefore(horarioSalvo.getHoraFechamento())) {
				        throw new IllegalArgumentException("Novo horário conflita com horário existente");
				    }
				    
				    if (diaSemanaNovo == horarioAtendimento.getDia()) {
				        if ((horAberturaNovo.isAfter(horarioSalvo.getHoraAbertura()) && horAberturaNovo.isBefore(horarioSalvo.getHoraFechamento())) ||
				            (horFechamentoNovo.isAfter(horarioSalvo.getHoraAbertura()) && horFechamentoNovo.isBefore(horarioSalvo.getHoraFechamento()))) {
				            throw new IllegalArgumentException("Novo horário conflita com horário existente");
				        }
				        
				        if (horAberturaNovo.isBefore(horarioSalvo.getHoraAbertura()) && horFechamentoNovo.isAfter(horarioSalvo.getHoraFechamento())) {
				            throw new IllegalArgumentException("Novo horário envolve horário existente");
				        }
					
				    }
					
				}
				
				
			}
			
		}
		
		
	}
	
	
}
