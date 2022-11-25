package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Postagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.generation.blogpessoal.repository.PostagemRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class PostagemController {
    @Autowired
    private PostagemRepository postagemRepository;
    @GetMapping
    public ResponseEntity<List<Postagem>> getAll()
    {
        return  ResponseEntity.ok(postagemRepository.findAll());
    }
    @GetMapping("/{id}")
    public  ResponseEntity<Postagem> postPostagem(@PathVariable Long id)
    {
        return postagemRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
    @GetMapping("titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo (@PathVariable String titulo)
        {
            return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
        }
        @PostMapping
    public ResponseEntity postPostagem (@Valid @RequestBody Postagem postagem)
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));

        }
        @PutMapping
    public ResponseEntity putPostagem (@Valid @RequestBody Postagem postagem)
        {
            return postagemRepository.findById(postagem.getId())
                    .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        }
        @DeleteMapping("/{id}")
    public void deletePostagem (@PathVariable Long id)
        {
            Optional<Postagem> postagem = postagemRepository.findById(id);
            if(postagem.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            postagemRepository.deleteById(id);

        }

}
