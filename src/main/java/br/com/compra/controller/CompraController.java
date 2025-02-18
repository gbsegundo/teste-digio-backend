package br.com.compra.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.compra.application.service.CompraService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1")
public class CompraController {
	
	@Autowired
	private CompraService service;

	
	@GetMapping(value = "/compras")
	public ResponseEntity<Map<String, Object>> listaCompras()  {
		return new ResponseEntity<>(service.listaCompras(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/maior-compra/{ano}")
	public Object buscaMaiorCompra(@PathVariable(name="ano") Integer ano)  {
		return service.buscaMaiorCompra(ano);
	}
	
	@GetMapping(value = "/clientes-fies")
	public ResponseEntity<Map<String, Object>> listaClientesFieis()  {
		return new ResponseEntity<>(service.listaClientesFieis(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/recomendacao/cliente/tipo")
	public Object buscaRecomendacaoTipo()  {
		return service.buscaRecomendacaoTipo();
	}
	
	
}
