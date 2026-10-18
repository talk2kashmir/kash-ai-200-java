package org.kk.azure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AIController {

	@Autowired
	AIService aiService;

	AIController(AIService aiService){
		this.aiService = aiService;
	}

	@GetMapping(path = "/health")
	public String version() {
		return "version docker1";
	}

	@GetMapping(path = "/create-vector-embeddings/{text}")
	public List<Float> createVectorEmbeddings(@PathVariable String text) {
		List<Float> vectors = aiService.createVectorEmbeddings(text);
		return vectors;
	}

	@GetMapping(path = "/chat/{text}")
	public String chat(@PathVariable String text) {
		String response = aiService.chat(text);
		return response;
	}

}
