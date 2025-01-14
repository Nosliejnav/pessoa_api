package com.example.cadastroPessoa.controlers;

import com.example.cadastroPessoa.entities.Pessoa;
import com.example.cadastroPessoa.services.PessoaService;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
//import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pessoas")
public class PessoaControler {

  private final PessoaService pessoaService;

  public PessoaControler(PessoaService pessoaService) {
    this.pessoaService = pessoaService;
  }

  @PostMapping
  public Pessoa savePessoa(@RequestBody @Valid Pessoa pessoa){
      return pessoaService.savePessoa(pessoa);
  }

  @GetMapping
  public ResponseEntity<List<Pessoa>> findAllPessoas (){
      List<Pessoa> pessoas = pessoaService.findAllPessoas();
    return ResponseEntity.ok(pessoas);
  }

  @PutMapping("/{id}")
  public void updatePessoa(@PathVariable @Valid Integer id, @RequestBody Pessoa pessoa){
    pessoaService.updatePessoa(id, pessoa);
  }

  @DeleteMapping("/{id}")
  public void deletePessoa(@PathVariable Integer id){
    pessoaService.deletePessoa(id);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Pessoa> findPessoaPorId(@PathVariable Integer id){
    Pessoa pessoa = pessoaService.findPessoaPorId(id);
    return ResponseEntity.ok(pessoa);
  }
}
