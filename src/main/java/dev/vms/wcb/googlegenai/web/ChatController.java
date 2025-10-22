package dev.vms.wcb.googlegenai.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.vms.wcb.googlegenai.AgentProfileService;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

	private final ChatClient chatClient;
	private final TemplateEngine jteTemplateEngine;
	private final AgentProfileService agentProfileService;

	public ChatController(ChatClient.Builder builder, TemplateEngine jteTemplateEngine, AgentProfileService agentProfileService) {
		var chatMemory = MessageWindowChatMemory.builder().build();
		this.chatClient = builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
				.conversationId(UUID.randomUUID().toString()).order(0).build()).build();

		this.jteTemplateEngine = jteTemplateEngine;
		this.agentProfileService = agentProfileService;
	}

	@GetMapping("/chat")
	public String chat(Model model) {
		model.addAttribute("agentName", agentProfileService.getAgentName());
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
		String aiResponse = chatClient.prompt().system(agentProfileService.getSystemPrompt()).user(message).call().content();
		jteTemplateEngine.render("chat/answer.jte", Map.of("answer", aiResponse), output);
		log.debug("chatResponse: " + message);
		return output.toString();
	}
}