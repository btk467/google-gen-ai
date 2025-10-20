package dev.vms.wcb.googlegenai.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

	private static final String SYSTEM_PROMPT = """
		You are Solution Architect, using Jav/Spring as a main language at WCB of Manitoba. The website is https://www.wcb.mb.ca.
		Your experience is broad. It covers DDD, REST, GRAPHQL, Onion Architecture.
		You like russian jokes and movies. You love your family and you furry friends.
		You hate when people lie. Sometimes, you are sarcastic but still respectful.
		You are keeping your conversation in plain English.
		You are trying to be exact when answering questions and if you don't know the answer you saying 'Hren ego znaet' in Russin or I honestly don't know.
		You, as a respectful, honest AI assistant who has very good russian style sence of humor.
		Respect means - no russian words. Use markdow as a output format when it is needed.
	""";

	private final ChatClient chatClient;
	private final TemplateEngine jteTemplateEngine;

	public ChatController(ChatClient.Builder builder, TemplateEngine jteTemplateEngine) {
		var chatMemory = MessageWindowChatMemory.builder().build();
		this.chatClient = builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
				.conversationId(UUID.randomUUID().toString()).order(0).build()).build();

		this.jteTemplateEngine = jteTemplateEngine;
	}

	@GetMapping("/chat")
	public String chat() {
		return "chat/index";
	}

	@PostMapping("/chat")
	@ResponseBody
	public String chat(@RequestParam String message) {
		StringOutput output = new StringOutput();
		jteTemplateEngine.render("chat/question.jte", Map.of("question", message), output);
		log.debug("chat: " + message);
		return output.toString();
	}

	@GetMapping("/chat/response")
	@ResponseBody
	public String chatResponse(@RequestParam String message) {
		StringOutput output = new StringOutput();
		String aiResponse = chatClient.prompt().system(SYSTEM_PROMPT).user(message).call().content();
		jteTemplateEngine.render("chat/answer.jte", Map.of("answer", aiResponse), output);
		log.debug("chatResponse: " + message);
		return output.toString();
	}
}