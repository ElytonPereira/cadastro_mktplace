package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

import javax.naming.spi.DirStateFactory.Result;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresHorarioAtendimento implements DaoHorarioAtendimento{

	private final String INSERT = "INSERT INTO horarios_atendimento (dia_semana, hora_abertura, hora_fechamento, id_restaurante) values (?,?,?,?) ";
	private final String UPDATE = "UPDATE horarios_atendimento SET dia_semana= ?, hora_abertura= ?, hora_fechamento= ?, id_restaurante= ? WHERE id= ? ";
	private final String SELECT_BY_ID = "SELECT h.id id_horario, h.dia_semana, h.hora_abertura, h.hora_fechamento, r.id id_restaurante FROM horarios_atendimento h inner join restaurantes r on h.id_restaurante = r.id where h.id =?";
	private final String DELETE = "DELETE FROM horarios_atendimento WHERE id = ?";
	private final String COUNT_BY_REST = "SELECT count(*) qtde FROM horarios_atendimento h WHERE h.id_restaurante = ?";
	
	private Connection conexao;
	
	public DaoPostgresHorarioAtendimento() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}
	
	@Override
	public void inserir(HorarioAtendimento horarioAtendimento) {
		PreparedStatement ps = null;
		
		try {
			ps = conexao.prepareStatement(INSERT);
			String diaConvertido = horarioAtendimento.converterDia(horarioAtendimento.getDia().name());
			
			ps.setString(1, diaConvertido);
			ps.setTime(2, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			
			ps.execute();
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro na inserção do horario de atendimento. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public void alterar(HorarioAtendimento horarioAtendimento) {
		PreparedStatement ps = null;
		
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			
			ps = conexao.prepareStatement(UPDATE);
			String diaConvertido = horarioAtendimento.converterDia(horarioAtendimento.getDia().name());
			
			ps.setString(1, diaConvertido);
			ps.setTime(2, Time.valueOf(horarioAtendimento.getHoraAbertura()));
			ps.setTime(3, Time.valueOf(horarioAtendimento.getHoraFechamento()));
			ps.setInt(4, horarioAtendimento.getRestaurante().getId());
			ps.setInt(5, horarioAtendimento.getId());
			
			boolean isAlteracaoOk = ps.executeUpdate() == 1;
			if (isAlteracaoOk) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro na alteração do horario. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public void excluirPor(int id) {
		PreparedStatement ps = null;
		
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			
			ps = conexao.prepareStatement(DELETE);		
			
			ps.setInt(1, id);
			
			boolean isExclusaoOk = ps.executeUpdate() == 1;
			if (isExclusaoOk) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro na exclusão do horario. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}
		
	}

	@Override
	public Restaurante buscarPor(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void extrairDo(Result rs) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	@Override
	public int contarPor(int idDoRestaurante) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conexao.prepareStatement(COUNT_BY_REST);
			ps.setInt(1, idDoRestaurante);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return rs.getInt("qtde");
			}
			return 0;
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao contatr os horarios. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
		
		
	}

	
	
}
