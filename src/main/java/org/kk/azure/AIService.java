package org.kk.azure;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIService {

    @Value("${open.ai.endpoint}")
    private String aiEndpoint;
    @Value("${open.ai.key}")
    private String apiKey;
    @Value("${chat.model}")
    private String chatModel;
    @Value("${vector.model}")
    private String vectorModel;

    public String chat (String chat){
        // Read Azure configuration from environment variables
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .baseUrl(aiEndpoint.replaceAll("/+$", "") + "/openai/v1/")
                .apiKey(apiKey)
                .build();

        // Use your Azure model deployment name here
        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(chatModel)
                        .addDeveloperMessage(
                                "You are a helpful AI assistant."
                        )
                        .addUserMessage(
                                chat
                        )
                        .maxCompletionTokens(300)
                        .build();

        ChatCompletion response =
                client.chat().completions().create(params);

        String responseText = response.choices().stream()
                .flatMap(choice ->
                        choice.message().content().stream())
                .collect(Collectors.joining());

        client.close();
        return responseText;
    }

    public List<Float> createVectorEmbeddings (String text){

        OpenAIClient client = OpenAIOkHttpClient.builder()
                .baseUrl(aiEndpoint.replaceAll("/+$", "") + "/openai/v1/")
                .apiKey(apiKey)
                .build();

        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .model(vectorModel)
                .input(text)
                .build();
        CreateEmbeddingResponse response = client.embeddings().create(params);
        List<Float> vector = response.data().get(0).embedding();

        System.out.println("Embedding dimensions: " + vector.size());
        System.out.println("Embedding vector: " + vector);
        return vector;
    }
}
