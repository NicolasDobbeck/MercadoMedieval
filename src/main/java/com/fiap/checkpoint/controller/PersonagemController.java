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

import com.fiap.checkpoint.model.ClassePersonagem;
import com.fiap.checkpoint.model.Personagem;
import com.fiap.checkpoint.repository.PersonagemRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("personagens")
public class PersonagemController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    PersonagemRepository repository;

    @GetMapping
    @Cacheable("personagens")
    public List<Personagem> index() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "personagens", allEntries = true)
    @Operation(summary = "Cadastrar personagem", description = "Insere um novo personagem no sistema", responses = {
            @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Personagem> create(@RequestBody @Valid Personagem personagem) {
        log.info("Cadastrando personagem: " + personagem.getNome());
        repository.save(personagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(personagem);
    }

    @GetMapping("{id}")
    public Personagem show(@PathVariable Long id) {
        log.info("Buscando personagem com id " + id);
        return getPersonagem(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando personagem com id " + id);
        repository.delete(getPersonagem(id));
    }

    @PutMapping("{id}")
    public Personagem update(@PathVariable Long id, @RequestBody @Valid Personagem personagem) {
        log.info("Atualizando personagem com id " + id);
        getPersonagem(id);
        personagem.setId(id);
        repository.save(personagem);
        return personagem;
    }

    private Personagem getPersonagem(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("buscar-por-nome")
    public List<Personagem> buscarPorNome(@RequestParam String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("buscar-por-classe")
    public List<Personagem> buscarPorClasse(@RequestParam ClassePersonagem classe) {
        return repository.findByClasse(classe);
    }

}
