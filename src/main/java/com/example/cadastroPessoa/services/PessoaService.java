package com.example.cadastroPessoa.services;

import com.example.cadastroPessoa.entities.Pessoa;
import com.example.cadastroPessoa.repositories.PessoaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PessoaService {

  private final PessoaRepository pessoaRepository;

  public PessoaService(PessoaRepository pessoaRepository) {
    this.pessoaRepository = pessoaRepository;
  }

  public Pessoa savePessoa(Pessoa pessoa) {
    Pessoa pessoaExiste = pessoaRepository.findByCpf(pessoa.getCpf());
    if (pessoaExiste != null){
      throw new RuntimeException("CPF já Cadastrado");
    }
    return pessoaRepository.save(pessoa);
  }

  public List<Pessoa> findAllPessoas() {
    List<Pessoa> pessoas = pessoaRepository.findAll();
    if (pessoas.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma Pessoa encontrada.");
    }
    return pessoas;
  }

  public Pessoa updatePessoa(Integer id, Pessoa pessoaAtualizada) {
    return pessoaRepository.findById(id)
      .map(pessoaExiste -> {
        pessoaExiste.setNome(pessoaAtualizada.getNome());
        pessoaExiste.setEmail(pessoaAtualizada.getEmail());
        pessoaExiste.setTelefone(pessoaAtualizada.getTelefone());
        pessoaExiste.setEnderecoCompleto(pessoaAtualizada.getEnderecoCompleto());

        return pessoaRepository.save(pessoaExiste);
      })
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Pessoa não encontrada para Atualização."));
  }

  public void deletePessoa(Integer id) {
    Pessoa pessoa = pessoaRepository.findById(id)
      .orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    pessoaRepository.delete(pessoa);
  }

  public Pessoa findPessoaPorId(Integer id) {
    Optional<Pessoa> pessoa = pessoaRepository.findById(id);
    if (pessoa.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada para o Id informado.");
    }
    return pessoa.orElse(null);
  }
}
