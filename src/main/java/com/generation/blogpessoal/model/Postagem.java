package com.generation.blogpessoal.model;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Entity
@Table( name ="tb_postagens")
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long  id ;
    @NotBlank(message = "O Atributo Titulo não pode ser nulo ou vazio!")
    @Size(min = 5, max = 100, message = " O Atributo Titulo deve conter no minimo 5 e no máximo 100 caracteres")
    private String titulo;
    @NotBlank(message = "O atributo texto é Obrigatório!")
    @Size(min = 10 , max = 100 , message =  " O Atributo Texto deve conter no minimo 5 e no máximo 100 caracteres")
    private String texto;
    @UpdateTimestamp
    private LocalDate data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
