package br.com.compra.application.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComprasDTO {
  
    private String nome_cliente;
    private String cpj_cliente;
    private int codigo_produto;
    private String tipo_vinho;
    private double preco;
    private int safra;
    private int ano_compra;
    private int quantidade_compra;
    private double valor_total;
}
