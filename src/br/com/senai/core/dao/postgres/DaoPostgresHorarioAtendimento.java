package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.DiaSemana;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class DaoPostgresHorarioAtendimento implements DaoHorarioAtendimento{
	
	
	
	private final String INSERT = "INSERT INTO horarios_atendimento (dia_semana, hora_abertura, hora_fechamento, id_restaurante) values (?,?,?,?) ";
	private final String UPDATE = "UPDATE horarios_atendimento SET dia_semana= ?, hora_abertura= ?, hora_fechamento= ?, id_restaurante= ? WHERE id= ? ";
	private final String SELECT_BY_ID = "SELECT h.id id_horario, h.dia_semana, h.hora_abertura, h.hora_fechamento, r.id id_restaurante, r.nome nome_restaurante, r.descricao, r.cidade, r.logradouro, r.bairro, r.complemento, c.id id_categoria, c.nome nome_categoria FROM horarios_atendimento h inner join restaurantes r on h.id_restaurante = r.id inner join categorias c on r.id_categoria = c.id  where h.id =? ";
	private final String SELECT_HORARIO = "SELECT h.id id_horario, h.dia_semana, h.hora_abertura, h.hora_fechamento, r.id id_restaurante, r.nome nome_restaurante, r.descricao, r.cidade, r.logradouro, r.bairro, r.complemento, c.id id_categoria, c.nome nome_categoria FROM horarios_atendimento h inner join restaurantes r on h.id_restaurante = r.id inner join categorias c on r.id_categoria = c.id  WHERE h.id_restaurante =?  order by CASE h.dia_semana WHEN 'DOMINGO' THEN 1 WHEN 'SEGUNDA' THEN 2 WHEN 'TERCA' THEN 3 WHEN 'QUARTA' THEN 4 WHEN 'QUINTA' THEN 5 WHEN 'SEXTA' THEN 6 WHEN 'SABADO' THEN 7 END;";
	private final String DELETE = "DELETE FROM horarios_atendimento WHERE id = ?";
	private final String COUNT_BY_REST = "SELECT count(*) qtde FROM horarios_atendimento h WHERE h.id_restaurante = ? ";
	
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
	public HorarioAtendimento buscarPor(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conexao.prepareStatement(SELECT_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return extrairDo(rs);
				
			} else {
				return null;
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao buscar por id do horario. Motivo: " + e.getMessage());
			
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
			
		}
	}
	
	public List<HorarioAtendimento> listarPor(int idRestaurante) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<HorarioAtendimento> horarios = new ArrayList<HorarioAtendimento>();
		
		try {
			ps = conexao.prepareStatement(SELECT_HORARIO);
			ps.setInt(1, idRestaurante);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				horarios.add(extrairDo(rs));
				
			}
			
			return horarios;		
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao buscar por id do restaurante. Motivo: " + e.getMessage());
			
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
			
		}
	}
	
	
	
	private HorarioAtendimento extrairDo(ResultSet rs) {
		try {
			int idHorario = rs.getInt("id_horario");
			String diaSemana = rs.getString("dia_semana");
			String horaAbertura = rs.getString("hora_abertura");
			String horaFechamento = rs.getString("hora_fechamento");
						
			int idDoRestaurante = rs.getInt("id_restaurante");
			String nomeDoRestaurante = rs.getString("nome_restaurante");
			String descricao = rs.getString("descricao");
			String cidade = rs.getString("cidade");
			String logradouro = rs.getString("logradouro");
			String bairro = rs.getString("bairro");
			String complemento = rs.getString("complemento");
			
			int idDaCategoria = rs.getInt("id_categoria");
			String nomeDaCategoria = rs.getString("nome_categoria");
			
			Endereco endereco = new Endereco(cidade, logradouro, bairro, complemento);
			Categoria categoria = new Categoria(idDaCategoria, nomeDaCategoria);
			
			Restaurante restaurante = new Restaurante(idDoRestaurante, nomeDoRestaurante, descricao, endereco, categoria);
			
			LocalTime hAbertura = LocalTime.parse(horaAbertura);
			LocalTime hFechamento = LocalTime.parse(horaFechamento);
			
			
			
			
			String diaConvertido = reconverterDia(diaSemana);
			DiaSemana diaSemana2 = DiaSemana.valueOf(diaConvertido);
			
			return new HorarioAtendimento(idHorario, hAbertura, hFechamento, restaurante, diaSemana2);
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao extrair os horarios. Motivo: " + e.getMessage());
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
	
	public String reconverterDia (String dia) {
		switch (dia) {
		case "DOMINGO":
			return "DOM";
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
