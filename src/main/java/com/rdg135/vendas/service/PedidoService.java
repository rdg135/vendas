package com.rdg135.vendas.service;

import com.rdg135.vendas.domain.entity.Pedido;
import com.rdg135.vendas.domain.enums.StatusPedido;
import com.rdg135.vendas.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus (Integer id, StatusPedido statusPedido);
}
