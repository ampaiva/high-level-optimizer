package com.ampaiva.in;

import healthwatcher.data.IAddressRepository;
import healthwatcher.model.address.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lib.exceptions.ExceptionMessages;
import lib.exceptions.ObjectAlreadyInsertedException;
import lib.exceptions.ObjectNotFoundException;
import lib.exceptions.ObjectNotValidException;
import lib.exceptions.PersistenceMechanismException;
import lib.exceptions.RepositoryException;
import lib.persistence.IPersistenceMechanism;




/**
 * Repositorio responsável por realizar a persitência
 * de endereços. Esse persistência é realizada em banco
 * de dados relacional utilizando JDBC
 */
public class AddressRepositoryRDB implements IAddressRepository {

	private IPersistenceMechanism mp;

	private ResultSet resultSet;

	public AddressRepositoryRDB(IPersistenceMechanism mp) {
		this.mp = mp;
	}

	/**
	 * Método para atualização de um endereço.
	 * Na versão atual do sistema disque saúde
	 * essa funcionalidade não está implementada.
	 */
	public void update(Address end) throws RepositoryException, ObjectNotFoundException {
	}

	/**
	 * Método para verificação da existência de um
	 * endereço com código específicado como parâmetro.
	 * Na versão atual do sistema disque saúde essa
	 * funcionalidade não está implementada.
	 */
	public boolean exists(int codigo) throws RepositoryException {
		return false;
	}

	/**
	 * Método para inserção de endereço no repositrório persistente.
	 * Essa implementação faz a persistência através de JDBC
	 * conectando com um banco de dados relacional.
	 */
	public int insert(Address end) throws ObjectAlreadyInsertedException, ObjectNotValidException,
		ObjectNotValidException, RepositoryException {
		
		// teste da validade do objeto a ser inserido
		if (end == null) {
			throw new ObjectNotValidException(ExceptionMessages.EXC_NULO);
		}

		Statement stmt;

		try {

			// Bloco para a recuperação de um código sequencial do
			// banco de dados para ser utilizado como identificador
			// e código da tabela de endereços.
			String consulta = null;
			try {

				//pega id e codigo e seta no objeto primeiro
				consulta = "select * from SCBS_endereco";

				stmt = (Statement) mp.getCommunicationChannel();
				resultSet = stmt.executeQuery(consulta);

				int count = 0;
				while (resultSet.next()) {
					count++;
				}
				end.setCode(count + 1);

				count++;

				resultSet.close();
				stmt.close();

			} catch (SQLException e) {
				System.out.println(consulta);
				throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
			}

			// Consulta na tabela ao código gerado pelo trecho anterior.
			// Caso o código já esteja em uso uma exceção é levantada.
			try {
				stmt = (Statement) mp.getCommunicationChannel();
				String sql = "SELECT * FROM SCBS_endereco WHERE codigo = '" + end.getCode() + "'";
				resultSet = stmt.executeQuery(sql);

				if (resultSet.next()) {
					throw new ObjectAlreadyInsertedException(ExceptionMessages.EXC_JA_EXISTE);
				}
				resultSet.close();
				stmt.close();
			} catch (SQLException e) {
				throw new PersistenceMechanismException(ExceptionMessages.EXC_FALHA_BD);
			}

			// Essa última etapa da inserção insere de fato os valores
			// recebidos no objeto do parâmetro com código e
			// identificador devidamente alterados
			try {
				String sql = "INSERT INTO SCBS_endereco VALUES(";

				//sql += end.getId() + ",";
				sql += "'" + end.getCode() + "',";
				sql += "'" + end.getStreet() + "',";
				sql += "'" + end.getComplement() + "',";
				sql += "'" + end.getZip() + "',";
				sql += "'" + end.getState() + "',";
				sql += "'" + end.getPhone() + "',";
				sql += "'" + end.getCity() + "',";
				sql += "'" + end.getNeighbourhood() + "')";

				stmt = (Statement) this.mp.getCommunicationChannel();
				stmt.executeUpdate(sql);
				stmt.close();

			} catch (SQLException e) {
				throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
			}
		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_INCLUSAO);
		}
		
		// TODO THIS IS WRONG!!!
		return -1;
	}

	/**
	 * Método para recuperação de endereço a partir do código.
	 *
	 * @param codigo Código identificador do endereço a 
	 *				 ser recuperado.
	 * @return endereço montado com os dados recuperados do bd
	 * @exception RepositorioException caso ocorra algum problema
	 *			  relacionado ao acesso aos dados persistentes.
	 * @exception ObjetoInexistenteException caso o endereço
	 *			  não seja encontrada nos dados persistentes
	 */
	public Address search(int code) throws RepositoryException, ObjectNotFoundException {
		Address end = null;
		String sql = null;
		try {
			sql = "select * from SCBS_endereco where " + " codigo = '" + code + "'";

			Statement stmt = (Statement) this.mp.getCommunicationChannel();

			//System.out.println(sql);
			resultSet = stmt.executeQuery(sql);

			if (resultSet.next()) {
				//long id = resultSet.getLong("ID");
				code = (new Integer(resultSet.getString("codigo"))).intValue();

				String rua = resultSet.getString("rua");
				String complemento = resultSet.getString("complemento");
				String cep = resultSet.getString("cep");
				String uf = resultSet.getString("uf");
				String fone = resultSet.getString("fone");
				String cidade = resultSet.getString("cidade");
				String bairro = resultSet.getString("bairro");
				end = new Address(rua, complemento, cep, uf, fone, cidade, bairro);
				end.setCode(code);

			} else {
				throw new ObjectNotFoundException(ExceptionMessages.EXC_FALHA_PROCURA);
			}
			resultSet.close();
			stmt.close();

		} catch (PersistenceMechanismException e) {
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		} catch (SQLException e) {
			System.out.println(sql);
			throw new RepositoryException(ExceptionMessages.EXC_FALHA_BD);
		}

		return end;
	}

	public void remove(int code) throws ObjectNotFoundException, RepositoryException {
	}
}
