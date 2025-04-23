package com.fiap.checkpoint.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.checkpoint.model.ClassePersonagem;
import com.fiap.checkpoint.model.Personagem;

public interface PersonagemRepository extends JpaRepository<Personagem, Long> {
    List<Personagem> findByNomeContainingIgnoreCase(String nome);
    List<Personagem> findByClasse(ClassePersonagem classe);
}