package br.com.ai.ecomart.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorizador")
public class CategorizadorrDeProdutosController {

    private final ChatClient chatClient;

    public CategorizadorrDeProdutosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public String categorizarProdutos(String produto) {
        var system = "Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado";

        return this.chatClient.prompt()
                .system(system)
                .user(produto)
                .options(ChatOptions.builder()
                        .temperature(0.85)
                        .build())
                .call()
                .content();
    }

}
