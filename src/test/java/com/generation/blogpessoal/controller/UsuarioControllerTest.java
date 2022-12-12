package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.SERVICE.UsuarioService;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
    @Autowired
    private TestRestTemplate  testRestTemplate;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start()
    {
        usuarioRepository.deleteAll();
        usuarioService.cadastrarUsuario(new Usuario(0L,"root","root@.com","rootroot","www.foto.com"));
    }

    @Test
    @DisplayName("Cadastrar um Usuário")
    public void deveCriarUmUsuario() {

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<>(new Usuario(0L, "fulano", "fulano@email.com2", "123456789", " "));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .withBasicAuth("root@.com","rootroot")
                .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta3 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta4 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta5 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta6 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta7 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta8 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
        ResponseEntity<Usuario> corpoResposta9 = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());

    }
    @Test
    @DisplayName("não permite duplicar Usuário")
    public void naoDuplicarUsuario() {
        usuarioService.cadastrarUsuario(new Usuario(0L,"Fulano", "fulano@email.com", "12345678", " "));

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,"Fulano", "fulano@email.com", "12345678", " "));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }
    @Test
    @DisplayName("Listar Todos")
    public void deveMostraListaUsuarios()
    {
        usuarioService.cadastrarUsuario(new Usuario(0l,"root2","root@.com2","rootroot2","www.foto.com"));
        usuarioService.cadastrarUsuario(new Usuario(0l,"root3","root@.com3","rootroot3","www.foto.com"));

        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth("root@.com","rootroot")
                .exchange("/usuarios/all",HttpMethod.GET,null,String.class);


    }
    @Test
    @DisplayName("Listar por id")
    public void deveMostraListaUsuarioPorId()
    {
        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L,"root2","root@.com2","rootroot2","www.foto.com"));
        HttpEntity<Long> id = new HttpEntity<Long>(1l,null);

        ResponseEntity<Usuario> resposta = testRestTemplate
                .withBasicAuth("root@.com","rootroot")
                .exchange("/usuarios/buscarporid",HttpMethod.GET,id,Usuario.class);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(id, resposta.getBody().getId());



    }
    @Test
    @DisplayName("Atualizar Usuario")
    public void atualizarUsuario()
    {
        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L,"root2","root@.com2","rootroot2","www.foto.com"));
        Usuario usuario = new Usuario(usuarioCadastrado.get().getId(),"root3","root@.com2","rootroot2","www.foto.com" );
        HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuario);

        ResponseEntity<Usuario> response = testRestTemplate
                .withBasicAuth("root@.com","rootroot")
                .exchange("/usuarios/atualizar",HttpMethod.PUT,request,Usuario.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request.getBody().getNome(), response.getBody().getNome());
        assertEquals(request.getBody().getUsuario(), response.getBody().getUsuario());


    }

}
