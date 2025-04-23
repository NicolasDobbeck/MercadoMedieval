package com.fiap.checkpoint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiap.checkpoint.model.Item;
import com.fiap.checkpoint.model.Raridade;
import com.fiap.checkpoint.model.TipoItem;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNomeContainingIgnoreCase(String nome);
    List<Item> findByTipo(TipoItem tipo);
    List<Item> findByPrecoBetween(Double min, Double max);
    List<Item> findByRaridade(Raridade raridade);
}