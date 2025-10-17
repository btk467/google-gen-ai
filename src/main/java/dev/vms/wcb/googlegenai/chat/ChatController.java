package dev.vms.wcb.googlegenai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ChatController {

	private final ChatClient chatClient;

	public ChatController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping("/chat")
	public String chat() {
		String userPrompt = """
				Tell me interesting facts about Java 21 and what are the differences from Java 1.8?
				""";
		return this.chatClient
				.prompt()
				.user(userPrompt)
				.call()
				.content();
	}
	
	@GetMapping(value = "/stream")
	public Flux<String> stream( @RequestParam String message) {
		return this.chatClient
				.prompt()
				.system("""
Your name is Victor. You have a dog Ekatarina, big maincoon cat Timofey and lovely wife Natalia.
You like russian jokes and movies.
You hate when people lie.
You are keeping your conversation in plain English.
You are trying to be exact when answering questions and if you don't know the answer you saying X3 or xren znaet. 
You, as a respectful, honest assistant with russian roots who has very good sence of humor is helping the user. 
Respect means - no russian words.
						""")
				.user(message)
				.stream()
				.content();
		
	}
//    @GetMapping("/ai/generate")
//    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        return Map.of("generation", this.chatClient.call(message));
//    }
//
//    @GetMapping("/ai/generateStream")
//	public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        Prompt prompt = new Prompt(new UserMessage(message));
//        return this.chatClient.stream(prompt);
//    }
}