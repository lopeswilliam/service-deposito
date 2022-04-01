package br.com.cadastro.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import br.com.cadastro.entity.Deposito;
import br.com.cadastro.models.DepositoRequest;
import br.com.cadastro.services.DepositoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@CrossOrigin
@Api(value = "Deposito")
@RequestMapping("/deposito/v1")
public class DepositoController {
	

	@Autowired
	private DepositoService depositoService;
	
	private static final Logger logger = LogManager.getLogger(DepositoController.class);

	@ApiOperation(value = "Inclui Deposito")
	@PostMapping(path = "/incluir" , produces = {"application/json"})
	public ResponseEntity<Deposito> inclusao(@RequestBody DepositoRequest depositoRequest, @RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a Inclusao do depositoService");
		return depositoService.inclusao(depositoRequest );
	}

	@ApiOperation(value = "consulta Depositos")
	@GetMapping(path = "/listarDepositos" , produces = {"application/json"})
	public ResponseEntity<List<Deposito>> consultaDepositos(@RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a consulta do depositoService");
		return depositoService.listarDepositos();
	}
	
	@ApiOperation(value = "Atualizar Depositos")
	@PutMapping(path = "/atualizar" , produces = {"application/json"})
	public ResponseEntity<Deposito> atualizar(@RequestBody DepositoRequest depositoRequest, @RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a Inclusao do depositoService");
		return depositoService.atualizar(depositoRequest );
	}
	
	@ApiOperation(value = "Consultar Depositos")
	@GetMapping(path = "/consultar" , produces = {"application/json"})
	public ResponseEntity<List<Deposito>> consulta(@RequestParam("cnpj") String cnpj, @RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a consulta do depositoService");
		return depositoService.consultar(cnpj);
	}
	

}
