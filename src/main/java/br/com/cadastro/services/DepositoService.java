package br.com.cadastro.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.cadastro.entity.Deposito;
import br.com.cadastro.entity.Endereco;
import br.com.cadastro.models.DepositoRequest;
import br.com.cadastro.repositories.DepositoRepository;
import br.com.cadastro.repositories.EnderecoRepository;

@Service
public class DepositoService {

	@Autowired
	private DepositoRepository depositoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	private static final Logger logger = LogManager.getLogger(DepositoService.class);


	/**
	 * @param prodRequest
	 * @return
	 */
	public ResponseEntity<Deposito> inclusao(DepositoRequest prodRequest) {
		logger.info("Iniciando a chamada da Inclusao de DepositoRequest");

		Deposito deposito = new Deposito();
		deposito = addPedido(prodRequest, deposito);
		try {
			deposito = depositoRepository.save(deposito);
		} catch (Exception e) {
			logger.error("Ocorreu um erro na persistencia de Dados");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deposito);
		}

		if(deposito != null) {
			Endereco endereco = new Endereco();
			endereco = addEndereco(prodRequest, endereco,deposito);
			endereco = enderecoRepository.save(endereco);
			deposito.setEndereco(endereco);
			return ResponseEntity.ok().body(deposito);
		}

		return ResponseEntity.notFound().build();

	}

	private Deposito addPedido(DepositoRequest depRequest, Deposito deposito) {

		if(depRequest.getId() != null) {
			deposito.setId(Long.valueOf(depRequest.getId()));;
		}

		deposito.setFornecedorid(depRequest.getFornecedorid());
		deposito.setNome(depRequest.getNome());;
		deposito.setNomeFantasia(depRequest.getNomeFantasia());;
		deposito.setEmail(depRequest.getEmail());
		deposito.setCnpj(depRequest.getCnpj());
		deposito.setTipoEmpresa(depRequest.getTipoEmpresa());

		return deposito;
	}

	private Deposito atualizarDeposito(DepositoRequest depRequest, Deposito deposito) {

		if(depRequest.getId() != null) {
			deposito.setId(Long.valueOf(deposito.getId()));;
		}

		deposito.setFornecedorid(depRequest.getFornecedorid());
		deposito.setNome(depRequest.getNome());;
		deposito.setNomeFantasia(depRequest.getNomeFantasia());;
		deposito.setEmail(depRequest.getEmail());
		deposito.setCnpj(depRequest.getCnpj());
		deposito.setTipoEmpresa(depRequest.getTipoEmpresa());

		return deposito;
	}



	/**
	 * @param codigo
	 * @return
	 */
	public ResponseEntity<List<Deposito>> listarDepositos() {
		logger.info("Iniciando a chamada da Consulta de Deposito");
		List<Deposito> depositoList = new ArrayList<>();
		try {

			List<Deposito> findAll = this.depositoRepository.findAll();

			if(findAll.size() > 0 ) {
				for (Deposito deposito : findAll) {

					Optional<Endereco> findById = Optional.ofNullable(enderecoRepository.findByDepositoid(deposito.getId()));

					if(findById.isPresent()) {
						Endereco endereco = new Endereco();
						endereco = findById.orElse(new Endereco());
						deposito.setEndereco(endereco);
						depositoList.add(deposito);
					}
				}
				return ResponseEntity.ok().body(depositoList);
			} 

		} catch (Exception e) {
			logger.error("Iniciando a chamada da consulta de Deposito");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(depositoList);
		}

		logger.info("Fim da chamada da consulta de Deposito");
		return ResponseEntity.notFound().build();

	}

	public ResponseEntity<Deposito> atualizar(DepositoRequest depoRequest) {
		logger.info("Iniciando a chamada da atualizar de Deposito");

		List<Deposito> findByCnpj = depositoRepository.findByCnpj(depoRequest.getCnpj());

		if(findByCnpj.size() > 0) {

			for (Deposito depo : findByCnpj) {

				Deposito deposito = new Deposito();
				deposito = atualizarDeposito(depoRequest, depo);
				try {
					deposito = depositoRepository.saveAndFlush(deposito);
				} catch (Exception e) {
					logger.error("Ocorreu um erro na persistencia de Dados");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deposito);
				}

				if(deposito != null) {
					Endereco endereco = new Endereco();
					endereco = enderecoRepository.findByDepositoid(deposito.getId());
					endereco = addEndereco(depoRequest, endereco,deposito);
					endereco = enderecoRepository.saveAndFlush(endereco);
					deposito.setEndereco(endereco);
					return ResponseEntity.ok().body(deposito);
				}


			}
		}
		
		return ResponseEntity.notFound().build();

	}

	private Endereco addEndereco(DepositoRequest enderecoRequest,Endereco ender, Deposito deposito) {
		Endereco endereco = new Endereco();
		endereco.setId(ender.getId());
		endereco.setClienteId(new Long(0));
		endereco.setFornecedorid(new Long(0));
		endereco.setDepositoid(Long.valueOf(deposito.getId()));
		endereco.setRua(enderecoRequest.getEndereco().getRua());
		endereco.setNumero(enderecoRequest.getEndereco().getNumero());
		endereco.setComplemento(enderecoRequest.getEndereco().getComplemento());
		endereco.setBairro(enderecoRequest.getEndereco().getBairro());
		endereco.setCep(enderecoRequest.getEndereco().getCep());
		endereco.setCidade(enderecoRequest.getEndereco().getCidade());
		endereco.setEstado(enderecoRequest.getEndereco().getEstado());
		return endereco;
	}

	/**
	 * @param codigo
	 * @return
	 */
	public ResponseEntity<List<Deposito>> consultar(String cnpj) {
		logger.info("Iniciando a chamada da Consulta de Deposito");
		List<Deposito> depositoList = new ArrayList<>();
		try {

			List<Deposito> findAll = this.depositoRepository.findByCnpj(cnpj);

			if(findAll.size() > 0 ) {
				for (Deposito deposito : findAll) {

					Optional<Endereco> findById = Optional.ofNullable(enderecoRepository.findByClienteId(deposito.getId()));

					if(findById.isPresent()) {
						Endereco endereco = new Endereco();
						endereco = findById.orElse(new Endereco());
						deposito.setEndereco(endereco);
						depositoList.add(deposito);
					}

				}

				return ResponseEntity.ok().body(depositoList);

			} 

		} catch (Exception e) {
			logger.error("Iniciando a chamada da consulta de Deposito");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(depositoList);
		}

		logger.info("Fim da chamada da consulta de Deposito");
		return ResponseEntity.notFound().build();

	}

}
