package com.generation.blogpessoal.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springBlogPessoalOpenApi(){
        return  new OpenAPI()
                .info(new Info()
                        .title("Projeto Blog Pessoal")
                        .description("Blog Pessoal Neocamp Generation Brasil")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Jonas Pontes")
                                .url("https://www.linkedin.com/in/jonas-pontes-queiroz-9079a4252/"))
                        .contact(new Contact()
                                .name("Jonas Pontes")
                                .url("https://github.com/Jonas-Pontes-Meli")
                                .email("jonaspqueiroz060@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Jonas-Pontes-Meli"));
    }
    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser()
    {
        return  openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses apiResponses = operation.getResponses();
                apiResponses.addApiResponse("200",createApiResponse("Sucesso!"));
                apiResponses.addApiResponse("201",createApiResponse("Objeto Persistido!"));
                apiResponses.addApiResponse("204",createApiResponse("Objeto Excluido!"));
                apiResponses.addApiResponse("200",createApiResponse("Erro na Requisição!"));
                apiResponses.addApiResponse("200",createApiResponse("Acesso Não Autorizado!"));
                apiResponses.addApiResponse("200",createApiResponse("Objeto Não Encontrado!"));
                apiResponses.addApiResponse("200",createApiResponse("Erro na Aplicação"));
            }) );
        };
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }
}
