package com.rdg135.vendas.service.impl;

import com.rdg135.vendas.domain.entity.Cliente;
import com.rdg135.vendas.domain.entity.ItemPedido;
import com.rdg135.vendas.domain.entity.Pedido;
import com.rdg135.vendas.domain.entity.Produto;
import com.rdg135.vendas.domain.enums.StatusPedido;
import com.rdg135.vendas.domain.repository.ClientesRepository;
import com.rdg135.vendas.domain.repository.ItemsPedidoRepository;
import com.rdg135.vendas.domain.repository.PedidosRepository;
import com.rdg135.vendas.domain.repository.ProdutosRepository;
import com.rdg135.vendas.exception.PedidoNaoEncontradoException;
import com.rdg135.vendas.exception.RegraNegocioException;
import com.rdg135.vendas.rest.dto.ItemPedidoDTO;
import com.rdg135.vendas.rest.dto.PedidoDTO;
import com.rdg135.vendas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidosRepository pedidosRepository;
    private final ClientesRepository clientesRepository;
    private final ProdutosRepository produtosRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {

        // buscando o cliente via dto
        Integer clienteId = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(clienteId)
                .orElseThrow( () -> new RegraNegocioException("Código de cliente inválido!"));

        // criando de fato o pedido e setando com base no dto
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDate.now());

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        pedidosRepository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);

        BigDecimal total = itemsPedido
                .stream()
                .map(ItemPedido::getTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setTotal(total);
        pedido.setStatus(StatusPedido.REALIZADO);
        return pedido;

    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidosRepository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus((statusPedido));
                    return pedidosRepository.save(pedido);
                }).orElseThrow( () -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems (Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioException("Adicione itens ao pedido e tente novamente!");
        }
        return items
                .stream()
                .map( dto -> {
                    Integer produtoId = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(produtoId)
                            .orElseThrow( () ->
                                    new RegraNegocioException("Código de produto inválido: " + produtoId));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    itemPedido.setTotalItem(produto.getPreco()
                            .multiply(BigDecimal.valueOf(itemPedido.getQuantidade())));
                    return itemPedido;
                }).collect(Collectors.toList());

    }
}
