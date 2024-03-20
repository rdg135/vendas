package com.rdg135.vendas.rest.dto;

import com.rdg135.vendas.domain.entity.ItemPedido;
import com.rdg135.vendas.validation.NotEmptyList;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
    private Integer cliente;

//    private BigDecimal total;

    @NotEmptyList(message = "Adicione itens ao pedido.", payLoad={})
    private List<ItemPedidoDTO> items;
}
