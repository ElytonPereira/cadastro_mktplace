package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.ManagerDb;
import br.com.senai.core.domain.Categoria;

public class DaoPostgresCategoria implements DaoCategoria {

	private final String INSERT = "INSERT INTO categorias (nome) VALUES (?)";
	private final String UPDATE = "UPDATE categorias SET nome = ? WHERE id = ?";
	private final String DELETE = "DELETE FROM categorias WHERE id = ?";
	private final String SELECT_BY_ID = "SELECT id, nome FROM categorias WHERE id = ?";
	private final String SELECT_BY_NOME = "SELECT id, nome FROM categorias WHERE Upper(nome) LIKE Upper(?) order by nome";
	private String SELECT_ALL_CATEGORIAS ="SELECT id, nome FROM categorias ORDER BY nome";
	private final String SELECT_ID_EXISTENTE = "SELECT count(r.id_categoria) as qtde FROM restaurantes r WHERE r.id_categoria = ? ";
	
	private Connection conexao;
	
	public DaoPostgresCategoria () {
		this.conexao = ManagerDb.getInstance().getConexao();
		System.out.println("Otimização na classe");
	}
	
	@Override
	public void inserir(Categoria categoria) {
		PreparedStatement ps = null;
		
		try {
			ps = conexao.prepareStatement(INSERT);
			ps.setString(1, categoria.getNome());
			ps.execute();
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao inserir a categoria. Motivo" + e.getMessage());
			
		} finally {
			ManagerDb.getInstance().fechar(ps);
			
		}
	}

	@Override
	public void alterar(Categoria categoria) {
		PreparedStatement ps= null;
		
		try {
			ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
			ps = conexao.prepareStatement(UPDATE);			
			ps.setString(1, categoria.getNome());
			ps.setInt(2, categoria.getId());
			boolean isAlteracaoOk = ps.executeUpdate() == 1;
			
			if(isAlteracaoOk) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao alterar a categoria. Motivo: " + e.getMessage());
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
			
			if(isExclusaoOk) {
				this.conexao.commit();
			} else {
				this.conexao.rollback();
			}
			ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao excluir a categoria. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
		}

	}

	@Override
	public Categoria buscarPor(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conexao.prepareStatement(SELECT_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				return extrairDo(rs);				
			} else {
				return null;
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao buscar a categoria. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}		
		
	}

	@Override
	public List<Categoria> listarPor(String nome) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List <Categoria> categorias = new ArrayList<Categoria>();
		
		try {
			ps = conexao.prepareStatement(SELECT_BY_NOME);
			ps.setString(1, nome);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				categorias.add(extrairDo(rs));
			}
			
			return categorias;			
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao listar categoria. Motivo: " + e.getMessage());
			
		} finally {
			
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}		

	}
	
	private Categoria extrairDo(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			String nome = rs.getString("nome");
			
			return new Categoria(id, nome);
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao Extrair a Categoria. Motivo: " + e.getMessage());
			
		}
	}

	@Override
	public ArrayList<Categoria> listarTodos() {
		ArrayList<Categoria> listarCategorias = new ArrayList<Categoria>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conexao.prepareStatement(SELECT_ALL_CATEGORIAS);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				
				listarCategorias.add(extrairDo(rs));
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao listar as categorias. Motivo: " + e.getMessage());
		} finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
			
		}
		return listarCategorias;
	}

	@Override
	public boolean validarRemocao(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conexao.prepareStatement(SELECT_ID_EXISTENTE);
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			boolean isValidadoOK = false;
			if (rs.next()) {
				isValidadoOK = rs.getInt("qtde") > 0;
			}
			
			return isValidadoOK;
			
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro na validação do id para remoção. Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}
	
	
}
