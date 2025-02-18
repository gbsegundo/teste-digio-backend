package br.com.compra.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteFiel {
  
    private String nome;
    private int quantidade_compra;
    private double valor_total;

}

