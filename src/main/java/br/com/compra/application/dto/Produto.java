package br.com.compra.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Produto {
  
    private int codigo;
    private String tipo_vinho;
    private double preco;
    private int safra;
    private int ano_compra;
}

