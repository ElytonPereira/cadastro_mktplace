package br.com.senai.core.service;

import java.util.ArrayList;
import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante dao;
	private DaoHorarioAtendimento daoHorario;
	
	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getDaoRestaurante();
		this.daoHorario = FactoryDao.getInstance().getDaoHorarioAtendimento(); 
	}
	
	public void salvar(Restaurante restaurante) {
		this.validar(restaurante);
		
		boolean isJaInserido = restaurante.getId() >0;
		
		if (isJaInserido) {
			this.dao.alterar(restaurante);
		} else {
			this.dao.inserir(restaurante);
		}
		
	}
	
	public void removerPor(int id) {
		if(id > 0) {
			int qtdeHorarios = daoHorario.contarPor(id);
			boolean isExisteHorarioVinculado = qtdeHorarios > 0;
			
			
			if (isExisteHorarioVinculado) {
				throw new IllegalArgumentException("Não foi possivel excluir o restaurante. Motivo: Existem " + qtdeHorarios + " horários vinculados ao restaurante");
			}
			
			this.dao.excluirPor(id);
			
		}else {
			throw new IllegalArgumentException("O id da categoria deve ser maior que 0");
		}
		
	}
	
	public Restaurante buscarPor(int id) {
		if(id > 0) {
			Restaurante RestauranteEncontrada = this.dao.buscarPor(id);
			if(RestauranteEncontrada == null) {
				throw new IllegalArgumentException("Não foi encontrado restaurante para o código informado");
			}
			return RestauranteEncontrada;
		}else {
			throw new IllegalArgumentException("O id do restaurante deve ser maior que 0");
		}
	}
	
	public List<Restaurante> listarPor(String nome, Categoria categoria){
		
		boolean isCategoriaInformada = categoria != null && categoria.getId() >0;
		boolean isNomeInformado = nome != null && !nome.isBlank();
		
		if(!isCategoriaInformada && !isNomeInformado) {
			throw new IllegalArgumentException("Informe o nome ou a categoria para listagem!");
		}
		
		String filtroNome = "";
		
		if (isCategoriaInformada) {
			filtroNome = nome + "%";
		}else {
			filtroNome = "%" + nome + "%";
		}
		return dao.listarPor(filtroNome, categoria);
		
	}
	
	private void validar(Restaurante restaurante) {
		if (restaurante != null) {			
			if (restaurante.getEndereco() != null) {
				boolean isNomeInvalido = restaurante.getNome() == null || restaurante.getNome().isBlank() || restaurante.getNome().length() > 250;
				boolean isDescInvalido = restaurante.getDescricao() ==null || restaurante.getDescricao().isBlank();
				boolean isCidadeInvalido = restaurante.getEndereco().getCidade() ==null || restaurante.getEndereco().getCidade().isBlank() || restaurante.getEndereco().getCidade().length() > 80;
				boolean isLogradouroInvalido = restaurante.getEndereco().getLogradouro() ==null || restaurante.getEndereco().getLogradouro().isBlank() || restaurante.getEndereco().getLogradouro().length() > 200;
				boolean isBairroInvalido = restaurante.getEndereco().getBairro() ==null || restaurante.getEndereco().getBairro().isBlank() || restaurante.getEndereco().getBairro().length() > 250;
				boolean isCategoriaInvalido = restaurante.getCategoria() == null && restaurante.getCategoria().getId() >0;
								
				if (isNomeInvalido) {
					throw new IllegalArgumentException("O nome do restaurante é obrigatório e não deve possuir mais de 250 caracteres");
				}
				
				if (isDescInvalido) {
					throw new IllegalArgumentException("A descrição do restaurante é obrigatório");
				}
				
				if (isCidadeInvalido) {
					throw new IllegalArgumentException("A cidade do restaurante é obrigatório e não deve possuir mais de 80 caracteres");
				}
				
				if (isLogradouroInvalido) {
					throw new IllegalArgumentException("O logradouro do restaurante é obrigatório e não deve possuir mais de 200 caracteres");
				}
				
				if (isBairroInvalido) {
					throw new IllegalArgumentException("O bairro do restaurante é obrigatório e não deve possuir mais de 250 caracteres");
				}
				
				if (isCategoriaInvalido) {
					throw new IllegalArgumentException("A categoria do restaurante é obrigatório");
				}
			} else {
				throw new NullPointerException("O endereço do restaurante não pode ser nulo!");
			}
			
		} else {
			throw new IllegalArgumentException("O restaurante não pode ser nulo!");
			
		}		
		
	}
	public ArrayList<Restaurante> listarRestaurantes(){
		return dao.listarTodos();
	}
	
}
