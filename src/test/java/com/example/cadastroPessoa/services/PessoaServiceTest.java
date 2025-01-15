package com.example.cadastroPessoa.services;

import com.example.cadastroPessoa.entities.Pessoa;
import com.example.cadastroPessoa.repositories.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Teste do método save
    @Test
    @DisplayName("Deve salvar uma pessoa.")
    public void testSave_NovaPessoa_SalvaComSucesso() {

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

//    @Test
//    @DisplayName("Deve lançar erro ao tentar salvar uma pessoa já Cadastrada")
//    public void testSave_PessoaJaExiste_ExcecaoLancada() {
//        // Arrange
//        Pessoa pessoa1 = new Pessoa();
//        pessoa1.setId(1);
//        pessoa1.setNome("Pessoa 1");
//
//        Pessoa pessoa2 = new Pessoa();
//        pessoa2.setId(2);
//        pessoa2.setNome("Pessoa 2");
//
//        when(pessoaRepository.save(pessoa1.getNome(),pessoa2.getNome())).thenReturn();
//
//        //Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            pessoaService.savePessoa(pessoa);
//        });
//
//        assertEquals("CPF já Cadastrado", exception.getMessage());
//        verify(pessoaRepository, never()).save(pessoa);
//    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar um cpf da pessoa já Cadastrada.")
    public void testSave_CpfJaExiste_ExcecaoLancada() {
        // Arrange
        Pessoa pessoa = new Pessoa();
        pessoa.setCpf("12345678901");

        when(pessoaRepository.findByCpf(pessoa.getCpf())).thenReturn(pessoa);

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pessoaService.savePessoa(pessoa);
        });

        assertEquals("CPF já Cadastrado.", exception.getMessage());
        verify(pessoaRepository, never()).save(pessoa);
    }

    //Teste do método findByAll
    @Test
    @DisplayName("Deve obter todas as pessoas Cadastradas.")
    public void testFindAllPessoas_PessoasExistem() {
        // Arrange
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1);
        pessoa1.setNome("Pessoa 1");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2);
        pessoa2.setNome("Pessoa 2");

        List<Pessoa> listaPessoas = Arrays.asList(pessoa1, pessoa2);
        when(pessoaRepository.findAll()).thenReturn(listaPessoas);

        // Act
        List<Pessoa> result = pessoaService.findAllPessoas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pessoa 1", result.get(0).getNome());
        assertEquals("Pessoa 2", result.get(1).getNome());
        verify(pessoaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve lançar erro ao não ter nenhuma pessoa Cadastrada.")
    public void testFindAllPessoas_NenhumaPessoa() {
        // Arrange
        when(pessoaRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pessoaService.findAllPessoas();
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Nenhuma Pessoa encontrada.", exception.getReason());
        verify(pessoaRepository, times(1)).findAll();
    }

    // Teste do método update
    @Test
    @DisplayName("Deve Atualizar Pessoa Existente.")
    public void testUpdate_PessoaExistente() {
        //Arrange
        Integer id = 1;
        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(id);
        pessoaExistente.setNome("João");

        Pessoa pessoaAtualizado = new Pessoa();
        pessoaAtualizado.setNome("João Atualizado");
        pessoaAtualizado.setCpf("12345678901");
        pessoaAtualizado.setEmail("joao@gmail.com");
        pessoaAtualizado.setTelefone("61999998888");
        pessoaAtualizado.setEnderecoCompleto("Novo Endereco");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaAtualizado);

        //Act
        Pessoa result = pessoaService.updatePessoa(id, pessoaAtualizado);

        //Assert
        assertNotNull(result);
        assertEquals("João Atualizado", result.getNome());
        assertEquals("12345678901", result.getCpf());
        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao não encontrar pessoa para Atualização.")
    public void testUpdate_PessoaNaoExiste(){
        //Arrange


        Integer id = 1;
        Pessoa pessoaAtualizado = new Pessoa();
        pessoaAtualizado.setId(id);
        pessoaAtualizado.setNome("João");

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pessoaService.updatePessoa(id, pessoaAtualizado);
        });

        assertEquals("Pessoa não encontrada para Atualização.", exception.getReason());
        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    //Teste do método delete
    @Test
    @DisplayName("Deve Deletar Pessoa Existente.")
    public void testDelete_PessoaExistente() {
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
    @DisplayName("Deve lançar erro ao não encontrar Pessoa Existente para Deletar.")
    public void testDelete_PessoaNaoExistente() {
        // Arrange
        Integer id = 1;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pessoaService.deletePessoa(id);
        });

        assertEquals("Pessoa não encontrada.", exception.getReason()); // Corrigido para corresponder ao código atual
        verify(pessoaRepository, never()).delete(any());
    }


    //Teste do método findById
    @Test
    @DisplayName("Deve obter uma pessoa por Id.")
    public void testFindById_PessoaExiste() {
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
    @DisplayName("Deve lançar erro ao não obter Pessoa Existente por id.")
    public void testFindById_PessoaNaoExistente() {
        // Arrange
        Integer id = 1;

        // Simula que o repositório retorna Optional.empty()
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pessoaService.findPessoaPorId(id);
        });

        assertEquals("Pessoa não encontrada para o Id informado.", exception.getReason());
        verify(pessoaRepository, times(1)).findById(id);
    }


}

