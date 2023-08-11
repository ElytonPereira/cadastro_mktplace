package br.com.senai.core.domain;

import java.time.LocalTime;
import java.util.Objects;

public class HorarioAtendimento {
	private int id;
	private LocalTime horaAbertura;
	private LocalTime horaFechamento;
	private Restaurante restaurante;
	private DiaSemana dia;
	
	
	
	public HorarioAtendimento(LocalTime horaAbertura, LocalTime horaFechamento, Restaurante restaurante, DiaSemana dia) {
		super();
		this.horaAbertura = horaAbertura;
		this.horaFechamento = horaFechamento;
		this.restaurante = restaurante;
		this.dia = dia;
	}
	
	public HorarioAtendimento(int id, LocalTime horaAbertura, LocalTime horaFechamento, Restaurante restaurante,
			DiaSemana dia) {
		this(horaAbertura, horaFechamento, restaurante, dia);
		this.id = id;
		
	}
	
	public int getId() {
		return id;
		
	}

	public void setId(int id) {
		this.id = id;
	}
	public LocalTime getHoraAbertura() {
		return horaAbertura;
	}
	public void setHoraAbertura(LocalTime horaAbertura) {
		this.horaAbertura = horaAbertura;
	}
	public LocalTime getHoraFechamento() {
		return horaFechamento;
	}
	public void setHoraFechamento(LocalTime horaFechamento) {
		this.horaFechamento = horaFechamento;
	}
	public Restaurante getRestaurante() {
		return restaurante;
	}
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
	public DiaSemana getDia() {
		return dia;
	}
	public void setDia(DiaSemana dia) {
		this.dia = dia;
	}
	
	
	
	@Override
	public String toString() {
		return "HorarioAtendimento [id=" + id + ", horaAbertura=" + horaAbertura + ", horaFechamento=" + horaFechamento
				+ ", restaurante=" + restaurante + ", dia=" + dia + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dia);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HorarioAtendimento other = (HorarioAtendimento) obj;
		return dia == other.dia;
	}
	
	public String converterDia (String diaAbreviado) {
		switch (diaAbreviado) {
        case "DOM":
            return "DOMINGO";
        case "SEG":
            return "SEGUNDA";
        case "TER":
            return "TERCA";
        case "QUA":
            return "QUARTA";
        case "QUI":
            return "QUINTA";
        case "SEX":
            return "SEXTA";
        case "SAB":
            return "SABADO";        
        default:
            return "";
    }
}
	public String reconverterDia (String dia) {
		switch (dia) {
		case "DOMINGO":
			return "DOM=";
		case "SEGUNDA":
			return "SEG";
		case "TERCA":
			return "TER";
		case "QUARTA":
			return "QUA";
		case "QUINTA":
			return "QUI";
		case "SEXTA":
			return "SEX";
		case "SABADO":
			return "SAB";        
		default:
			return "";
		}
	}
		
	}
	
