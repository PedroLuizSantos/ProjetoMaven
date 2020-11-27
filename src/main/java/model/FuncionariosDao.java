package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionariosDao{
	
	private Connection connection;
	
	
	public FuncionariosDao() {
		connection = new ConexaBD().getConnection();

	}

	public void cadastrarFuncionario(Funcionarios funcionario) {
		
		String sql = "insert into funcionarios(nome, cpf, cargo, salario) values(?, ?, ?, ?)";
		
		try {
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, funcionario.getNome());
		statement.setString(2, funcionario.getCpf());
		statement.setString(3, funcionario.getCargo());
		statement.setFloat(4, funcionario.getSalario());
		
		statement.execute();
		statement.close();
	
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public void atualizarFuncionario(Funcionarios funcionarios) {
		
		String sql = "update funcionarios set nome = ?, cpf = ?, cargo = ?, salario = ? where id = ?";
		
		try {
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, funcionarios.getNome());
		statement.setString(2, funcionarios.getCpf());
		statement.setString(3, funcionarios.getCargo());
		statement.setFloat(4, funcionarios.getSalario());
		statement.setInt(5, funcionarios.getId());
		
		statement.execute();
		statement.close();
		
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			
	}
	
	public void deletarFuncionario(Integer idFuncionarios) {
		
		String sql = "delete from funcionarios where id = ?";
		
		try {
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setInt(1, idFuncionarios);
		statement.execute();
		statement.close();
		
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
	
	public List<Funcionarios> consultaFuncionario(String nomeFuncionario) {
		
		List<Funcionarios> funcionarios = new ArrayList<>();
		
		String sql = "select * from funcionarios where nome like '%" + nomeFuncionario + "%'";
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next() ) {
				
				Funcionarios funcionario = new Funcionarios();
				
				funcionario.setId(resultSet.getInt("id"));
				funcionario.setNome(resultSet.getString("nome"));
				funcionario.setCpf(resultSet.getString("cpf"));
				funcionario.setCargo(resultSet.getString("cargo"));
				funcionario.setSalario(resultSet.getFloat("salario"));
				
				funcionarios.add(funcionario);
				
			}
			
			resultSet.close();
			statement.close();
			
			
			}catch(SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		
		return funcionarios;
	}
	
}