package com.generation.blogpessoal.SERVICE;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> cadastrarUsuario(Usuario usuario)
    {
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            return Optional.empty();

        /**
         * Se o Usuário não existir no Banco de Dados, a senha será criptografada
         * através do Método criptografarSenha.
         */
        usuario.setSenha(criptografarsenha(usuario.getSenha()));

        /**
         * Assim como na Expressão Lambda, o resultado do método save será retornado dentro
         * de um Optional, com o Usuario persistido no Banco de Dados.
         *
         * of​ -> Retorna um Optional com o valor fornecido, mas o valor não pode ser nulo.
         * Se não tiver certeza de que o valor não é nulo use ofNullable.
         */
        return Optional.of(usuarioRepository.save(usuario));

    }
    public Optional<Usuario> atualizarUsuario(Usuario usuario)
    {
        if(usuarioRepository.findById(usuario.getId()).isPresent()) {

            /**
             * Cria um Objeto Optional com o resultado do método findById
             */
            Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());

            /**
             * Se o Usuário existir no Banco de dados e o Id do Usuário encontrado no Banco for
             * diferente do usuário do Id do Usuário enviado na requisição, a Atualização dos
             * dados do Usuário não pode ser realizada.
             */
            if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId()))
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

            /**
             * Se o Usuário existir no Banco de Dados e o Id for o mesmo, a senha será criptografada
             * através do Método criptografarSenha.
             */
            usuario.setSenha(criptografarsenha(usuario.getSenha()));

            /**
             * Assim como na Expressão Lambda, o resultado do método save será retornado dentro
             * de um Optional, com o Usuario persistido no Banco de Dados ou um Optional vazio,
             * caso aconteça algum erro.
             *
             * ofNullable​ -> Se um valor estiver presente, retorna um Optional com o valor,
             * caso contrário, retorna um Optional vazio.
             */
            return Optional.ofNullable(usuarioRepository.save(usuario));

        }

        /**
         * empty -> Retorna uma instância de Optional vazia, caso o usuário não seja encontrado.
         */
        return Optional.empty();

    }

    public  Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin)
    {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
        if(usuario.isPresent())
        {
            if(comparaSenhas(usuarioLogin.get().getSenha(),usuario.get().getSenha()))
            {
                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario(),usuarioLogin.get().getSenha()));
                usuarioLogin.get().setSenha(usuario.get().getSenha());
                return usuarioLogin;
            }

        }
        return Optional.empty();
    }

    private String gerarToken(String usuario, String senha) {
        String token = usuario + ":" + senha;
        byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
        return  "Basic" + new String(tokenBase64);
    }


    private boolean comparaSenhas(String senhaDigitada, String senhaBanco) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaDigitada,senhaBanco);

    }

    private  String criptografarsenha(String senha)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return  encoder.encode(senha);

    }

}
