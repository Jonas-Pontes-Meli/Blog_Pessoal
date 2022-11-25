package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/tema")
@RestController
@CrossOrigin(origins = "*" ,allowedHeaders = "*")
public class TemaController {
    @Autowired
   private TemaRepository temaRepository;
    @GetMapping
    public ResponseEntity<List<Tema>> getTema()
    {
       return ResponseEntity.ok(temaRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Tema> getTemaById(@PathVariable Long id)
    {
       return temaRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta)).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("descricao/{desc}")
    public ResponseEntity<List<Tema>> getByDescrica (@PathVariable String desc)
    {
        return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(desc));
    }
    @PostMapping
            public ResponseEntity postTema(@Valid @RequestBody Tema tema)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaRepository.save(tema));
    }
    @PutMapping
            public ResponseEntity putTema(@Valid @RequestBody Tema tema)
    {
        return temaRepository.findById(tema.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @DeleteMapping("/{id}")
    public void deleteTema (@PathVariable Long id)
    {
        Optional<Tema> tema = temaRepository.findById(id);
        if(tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        temaRepository.deleteById(id);

    }


}
