package com.fiap.checkpoint.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fiap.checkpoint.model.Item;
import com.fiap.checkpoint.model.Raridade;
import com.fiap.checkpoint.model.TipoItem;
import com.fiap.checkpoint.repository.ItemRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("itens")
public class ItemController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ItemRepository repository;

    @GetMapping
    @Cacheable("itens")
    public List<Item> index() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "itens", allEntries = true)
    @Operation(summary = "Cadastrar item", description = "Insere um novo item", responses = {
            @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Item> create(@RequestBody @Valid Item item) {
        log.info("Cadastrando item: " + item.getNome());
        repository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("{id}")
    public Item show(@PathVariable Long id) {
        log.info("Buscando item com id " + id);
        return getItem(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando item com id " + id);
        repository.delete(getItem(id));
    }

    @PutMapping("{id}")
    public Item update(@PathVariable Long id, @RequestBody @Valid Item item) {
        log.info("Atualizando item com id " + id);
        getItem(id);
        item.setId(id);
        repository.save(item);
        return item;
    }

    private Item getItem(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("buscar-por-nome")
    public List<Item> buscarPorNome(@RequestParam String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("buscar-por-tipo")
    public List<Item> buscarPorTipo(@RequestParam TipoItem tipo) {
        return repository.findByTipo(tipo);
    }

    @GetMapping("buscar-por-preco")
    public List<Item> buscarPorPreco(@RequestParam double min, @RequestParam double max) {
        return repository.findByPrecoBetween(min, max);
    }

    @GetMapping("buscar-por-raridade")
    public List<Item> buscarPorRaridade(@RequestParam Raridade raridade) {
        return repository.findByRaridade(raridade);
    }

}
