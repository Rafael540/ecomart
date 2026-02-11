package br.com.ai.ecomart.controller;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorizador")
public class CategorizadorrDeProdutosController {

    private final ChatClient chatClient;

    public CategorizadorrDeProdutosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions.builder()
                .model("gpt-4o-mimi")
            .build())
        .build();
    }

    @GetMapping
    public String categorizarProdutos(String produto) {
        var system = """
                Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado
                
                Escolha uma categoria dentro a lista abaixo:
                
                1. Higiene pessoal
                2. Eletronicos
                3. Esportes
                4. Outros
                
                ##### exemplo de uso:
                
                Pergunta: Bola de futebol
                Resposta: Esportes 
                """;

        var tokens = contarTokens(system, produto);
        System.out.println("QTD de tokens: " + tokens);

        //logica de escolha de modelo baseada no modelo no número de tokens


            return this.chatClient.prompt()
                    .system(system)
                    .user(produto)
                    .options(ChatOptions.builder()
                            .temperature(0.85)
                            .build())
                    .advisors(new SimpleLoggerAdvisor())
                    .call()
                    .content();
        }


    private int contarTokens(String system, String user){
       var registry = Encodings.newDefaultEncodingRegistry();
       var enc = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
       return enc.countTokens(system + user);
    }

}
