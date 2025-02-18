package br.com.compra.application.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.compra.application.dto.Cliente;
import br.com.compra.application.dto.ClienteFiel;
import br.com.compra.application.dto.Compra;
import br.com.compra.application.dto.Produto;
import br.com.compra.application.dto.TipoVinho;
import br.com.compra.application.output.ComprasDTO;

@Service
public class CompraService {

	private static final String LISTA_PRODUTOS = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";
	
	private static final String LISTA_CLIENTE_COMPRA = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";
	
	  
	public Map<String, Object> listaCompras() {
		
		List<Produto> listProduto = this.carregaListaProdutos();
		List<Cliente> listCliente = this.carregaClientes();
		
		List<ComprasDTO> listCompras = this.retornaCompras(listProduto, listCliente);
		
		java.util.stream.Stream<ComprasDTO> sortedCompras = listCompras.stream()
                .sorted(Comparator.comparing(ComprasDTO::getValor_total));
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("compras", sortedCompras);
		return response;	
		
	}
	
	public Map<String, Object> listaClientesFieis() {
		
		List<Produto> listProduto = this.carregaListaProdutos();
		List<Cliente> listCliente = this.carregaClientes();
		List<ClienteFiel> listClienteFiel = new ArrayList<ClienteFiel>();
		
		List<ComprasDTO> listCompras = this.retornaCompras(listProduto, listCliente);
		
		HashMap<String, String> mapValores = new HashMap<String, String>();
		listCompras.forEach(compra -> {
		    if(mapValores.containsKey(compra.getNome_cliente())) {
		    	String[] quantidadeValor = mapValores.get(compra.getNome_cliente()).split(";");
		    	int iQuantidade = Integer.parseInt(quantidadeValor[0]) + compra.getQuantidade_compra();
		    	double dValor = Double.parseDouble(quantidadeValor[1]) + compra.getValor_total();
		    	mapValores.put(compra.getNome_cliente(), iQuantidade+";"+dValor);
		    }else {
		    	mapValores.put(compra.getNome_cliente(), compra.getQuantidade_compra() +";" +compra.getValor_total());
		    }
		});
		
		Iterator<String> it = mapValores.keySet().iterator();
		while(it.hasNext()) {
			ClienteFiel clienteFiel = new ClienteFiel();
			String nome = it.next();
			String[] aQuantideValor = mapValores.get(nome).split(";");
			clienteFiel.setNome(nome);
			clienteFiel.setQuantidade_compra(Integer.parseInt(aQuantideValor[0]));
			clienteFiel.setValor_total(Double.parseDouble(aQuantideValor[1]));
			listClienteFiel.add(clienteFiel);
		}
		
		java.util.stream.Stream<ClienteFiel> sortedClientes  = listClienteFiel.stream()
                .sorted(Comparator.comparing(ClienteFiel::getQuantidade_compra,  Comparator.reverseOrder())).limit(3);
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("clientes", sortedClientes);
		return response;	
	}
	
    public Object buscaRecomendacaoTipo() {
		
		List<Produto> listProduto = this.carregaListaProdutos();
		List<Cliente> listCliente = this.carregaClientes();
		List<TipoVinho> listTipoVinho = new ArrayList<TipoVinho>();
		
		List<ComprasDTO> listCompras = this.retornaCompras(listProduto, listCliente);
		
		HashMap<String, Integer> mapValores = new HashMap<String, Integer>();
		listCompras.forEach(compra -> {
		    if(mapValores.containsKey(compra.getTipo_vinho())) {
		    	int iQuantidade = mapValores.get(compra.getTipo_vinho());
		    	iQuantidade = iQuantidade + compra.getQuantidade_compra();
		    	mapValores.put(compra.getTipo_vinho(), iQuantidade);
		    }else {
		    	mapValores.put(compra.getTipo_vinho(), compra.getQuantidade_compra());
		    }
		});
		
		Iterator<String> it = mapValores.keySet().iterator();
		while(it.hasNext()) {
			TipoVinho tipoVinho = new TipoVinho();
			String sTipo = it.next();
			int quantidade = mapValores.get(sTipo);
			tipoVinho.setTipoVinho(sTipo);
			tipoVinho.setQuantidade_compra(quantidade);
			listTipoVinho.add(tipoVinho);
		}
		
		Optional<TipoVinho> optTipo = listTipoVinho.stream()
                .sorted(Comparator.comparing(TipoVinho::getQuantidade_compra,  Comparator.reverseOrder())).findFirst();
		
		if(optTipo.isPresent()) {
			return optTipo.get();
		}
		else {
			return new TipoVinho();	
		}
    }
	
	
	public Object buscaMaiorCompra(int ano) {

		List<Produto> listProduto = this.carregaListaProdutos();
		List<Cliente> listCliente = this.carregaClientes();
		
		List<ComprasDTO> listCompras = this.retornaCompras(listProduto, listCliente);
		
		Optional<ComprasDTO> optCompra = listCompras.stream()
                .sorted(Comparator.comparing(ComprasDTO::getValor_total,  Comparator.reverseOrder())).findFirst();
		
		if(optCompra.isPresent()) {
			return optCompra.get();
		}
		else {
			return new ComprasDTO();	
		}
		
	}
	
	
	public List<ComprasDTO> retornaCompras(List<Produto> listProduto, List<Cliente> listCliente) {
		List<ComprasDTO> listCompras = new ArrayList<ComprasDTO>();
		
		listCliente.stream().forEach((c) -> {
			List<Compra> listCompra = c.getCompras();
			listCompra.stream().forEach((l) -> {
				ComprasDTO comprasDTO = new ComprasDTO();
				comprasDTO.setNome_cliente(c.getNome());
				comprasDTO.setCpj_cliente(c.getCpf());
				int codigo = l.getCodigo();
				int quantidade = l.getQuantidade();
				Produto produto =listProduto.stream().filter(cod -> cod.getCodigo() == codigo).findFirst().get();
				comprasDTO.setAno_compra(produto.getAno_compra());
				comprasDTO.setCodigo_produto(produto.getCodigo());
				comprasDTO.setPreco(produto.getPreco());
				comprasDTO.setQuantidade_compra(quantidade);
				comprasDTO.setSafra(produto.getSafra());
				comprasDTO.setTipo_vinho(produto.getTipo_vinho());
				comprasDTO.setValor_total(produto.getPreco() * quantidade);
				listCompras.add(comprasDTO);
			});
			
		});
		
		return listCompras;
	}
	
	
	public List<Produto> carregaListaProdutos() {
		List<Produto> listProduto = new ArrayList<Produto>();
		ModelMapper modelMapper = new ModelMapper();
		try {
			
			 URL url = new URL(LISTA_PRODUTOS);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         conn.connect();
			
	         String inline = "";
             Scanner scanner = new Scanner(url.openStream());
             while (scanner.hasNext()) {
                 inline += scanner.nextLine();
             }
             scanner.close();
             
             JSONParser parse = new JSONParser();
             JSONArray data_obj = (JSONArray) parse.parse(inline);

             listProduto = Arrays.asList(modelMapper.map(data_obj, Produto[].class));
		
	    } catch (Exception e) {
	        System.out.println("Erro ao carregar os produtos: "+e.getMessage());
	    }
		
		return listProduto;
		
	}
	
	
	public List<Cliente> carregaClientes() {
		List<Cliente> listCliente = new ArrayList<Cliente>();
		ModelMapper modelMapper = new ModelMapper();
		try {
			
			 URL url = new URL(LISTA_CLIENTE_COMPRA);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         conn.connect();
			
	         String inline = "";
             Scanner scanner = new Scanner(url.openStream());
             while (scanner.hasNext()) {
                 inline += scanner.nextLine();
             }
             scanner.close();
             
             JSONParser parse = new JSONParser();
             JSONArray data_obj = (JSONArray) parse.parse(inline);

             listCliente = Arrays.asList(modelMapper.map(data_obj, Cliente[].class));
		
	    } catch (Exception e) {
	        System.out.println("Erro ao carregar os clientes: "+e.getMessage());
	    }
		
		return listCliente;
		
	}
	



}