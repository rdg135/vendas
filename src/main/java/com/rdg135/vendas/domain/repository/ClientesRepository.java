package com.rdg135.vendas.domain.repository;

import com.rdg135.vendas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ClientesRepository extends JpaRepository<Cliente, Integer> {
    @Query(value = "select * from cliente c where c.nome like '%:nome%'", nativeQuery = true)
    List<Cliente> encontrarPorNome (@Param ("nome") String nome);

    @Modifying
    @Query(value = "delete from Cliente c where c.nome =:nome")
    void deleteByNome (String nome);

    boolean existsByNome(String nome);

    @Query("select c from Cliente c left join fetch c.pedidos where c.id =:id")
    Cliente findClienteFetchPedidos ( @Param("id") Integer id);
}
