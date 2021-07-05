package com.lucas.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.os.domain.Pessoa;
import com.lucas.os.domain.Cliente;
import com.lucas.os.dtos.ClienteDTO;
import com.lucas.os.repositories.PessoaRepository;
import com.lucas.os.repositories.ClienteRepository;
import com.lucas.os.services.exceptions.DataIntegratyViolationException;
import com.lucas.os.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! " + "Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public List<Cliente> findAll() { // Método para retornar a lista de técnicos depois de uma busca na base de dados
		return repository.findAll();
	}

	//  Verifica se há conflito de valores de CPF
	public Cliente create(ClienteDTO objDTO) { // Método que instancia um novo objeto e o salva na base de dados,
		if (findByCPF(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}
		return repository.save(new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone()));
	}

	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		Cliente oldObj = findById(id);

		if (findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(oldObj.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		return repository.save(oldObj);
	}

	public void delete(Integer id) {
		Cliente obj = findById(id);
		
			if(obj.getList().size() > 0 ) { 
				/*Estrutura condicional p/ detectar	se o técnico posssui 
				 * OS em aberto. Caso sim, não poderá ser excluído da
				 *  tabela até que a OS seja fechada. */ 		
				throw new DataIntegratyViolationException(
						"Esta pessoa possui Ordens de Serviço em andamento."
						+ " Portanto, não pode ser deletado!");			
			}
		repository.deleteById(id);		
	}	

	private Pessoa findByCPF(ClienteDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());
		if (obj != null) {
			return obj;
		}
		return null;
	}

}
