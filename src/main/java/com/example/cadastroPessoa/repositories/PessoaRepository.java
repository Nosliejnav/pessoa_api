package com.example.cadastroPessoa.repositories;

import com.example.cadastroPessoa.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

//  Pessoa findByNome(String nome);

  Pessoa findByCpf(String cpf);


}
