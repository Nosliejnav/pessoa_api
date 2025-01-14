package com.example.cadastroPessoa.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.cadastroPessoa.entities.Pessoa;
import com.example.cadastroPessoa.repositories.PessoaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
class PessoaServiceTest {

  @InjectMocks
  private PessoaService pessoaService;

  @Mock
  private PessoaRepository pessoaRepository;

  @BeforeEach
  public void setup(){
    MockitoAnnotations.openMocks(this);
  }

  //Teste do método save
  @Test
  public void testSave_NovaPessoa_SalvaComSucesso(){

    // Preparação
    Pessoa pessoa = new Pessoa();
    pessoa.setNome("Van Man");
    pessoa.setCpf("12345678901");

    when(pessoaRepository.save(pessoa)).thenReturn(pessoa);
    when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(null);

    // Execução
    Pessoa result = pessoaService.savePessoa(pessoa);

    // Verificação
    assertNotNull(result);
    assertEquals(pessoa.getNome(), result.getNome());
    verify(pessoaRepository, times(1)).save(pessoa);
  }

  @Test
  public void testSave_PessoaJaExiste_ExcecaoLancada(){
    // Arrange
    Pessoa pessoa = new Pessoa();
    pessoa.setCpf("12345678901");

    when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(pessoa);

    //Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () ->{
      pessoaService.savePessoa(pessoa);
    });

    assertEquals("Pessoa já existe!", exception.getMessage());
    verify(pessoaRepository, never()).save(pessoa);
  }

  //Teste do método findById
  @Test
  public void testFindById_PessoaExiste(){
    //Arrange
    Integer id = 1;
    Pessoa pessoa = new Pessoa();
    pessoa.setId(id);
    pessoa.setNome("João Silva");

    when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

    //Act
    Pessoa result = pessoaService.findPessoaPorId(id);

    //Assert
    assertNotNull(result);
    assertEquals(id, result.getId());
    verify(pessoaRepository, times(1)).findById(id);

  }

  @Test
  public void testFindById_PessoaNaoExistente(){
    //Arrange
    Integer id = 1;

    when(pessoaRepository.findById(id)).thenReturn(null);

    //Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      pessoaService.findPessoaPorId(id);
    });

    assertEquals("Pessoa não encontrado para o Id informado.", exception.getMessage());
    verify(pessoaRepository, times(1)).findById(id);
  }

  //Teste do método delete
  @Test
  public void testDelete_PessoaExistente(){
    // Arrange
    Integer id = 1;
    Pessoa pessoa = new Pessoa();
    pessoa.setId(id);
    
    when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
    
    // Act
    pessoaService.deletePessoa(id);
    
    //Assert
    verify(pessoaRepository, times(1)).delete(pessoa);
  }

  @Test
  public void testDelete_PessoaNaoExistente(){
    // Arrange
    Integer id = 1;

    when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      pessoaService.deletePessoa(id);
    });

    assertEquals("Pessoa não encontrada", exception.getReason());
    verify(pessoaRepository, never()).delete(any());



  }
  
  
}

