package com.lucas.os.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucas.os.domain.Cliente;
import com.lucas.os.domain.OS;
import com.lucas.os.domain.Tecnico;
import com.lucas.os.domain.enuns.Prioridade;
import com.lucas.os.domain.enuns.Status;
import com.lucas.os.repositories.ClienteRepository;
import com.lucas.os.repositories.OSRepository;
import com.lucas.os.repositories.TecnicoRepository;

@Service 
public class DBService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private OSRepository osRepository;

	public void instanciaDB() {
		
		Tecnico t1 = new Tecnico(null, "Lucas Fonseca", "772.639.500-14", "(61)99999-9999");
		Cliente c1 = new Cliente(null, "Pedro José Santos", "241.732.040-19", "(61)98888-8888");
		OS os1 = new OS(null, Prioridade.ALTA, "Teste create OS", Status.ANDAMENTO, t1, c1);

		t1.getList().add(os1);
		c1.getList().add(os1);

		tecnicoRepository.saveAll(Arrays.asList(t1));
		clienteRepository.saveAll(Arrays.asList(c1));
		osRepository.saveAll(Arrays.asList(os1));
	}
}
