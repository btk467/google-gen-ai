package dev.vms.wcb.googlegenai.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import dev.vms.wcb.googlegenai.AgentProfileService;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

	private static final String USER_NOT_TELLING_HIS_NAME_GENERATE_ONE_FUNNY_NAME = """
		Attention!!! If the user does not introduce himself then you should respond with short funny but respectful greeting and ask him
		for permission to call him by the name you make. That name should be funny but respectful. 
		Otherwise, continue conversation.
	""";

	private final ChatClient chatClient;
	private final TemplateEngine jteTemplateEngine;
	private final AgentProfileService agentProfileService;

	public ChatController(ChatClient.Builder builder, TemplateEngine jteTemplateEngine,
			AgentProfileService agentProfileService) {
		var chatMemory = MessageWindowChatMemory.builder().build();
		this.chatClient = builder
			.defaultAdvisors(MessageChatMemoryAdvisor
					.builder(chatMemory)
					.conversationId(UUID.randomUUID().toString())
					.order(0)
					.build())
			.build();

		this.jteTemplateEngine = jteTemplateEngine;
		this.agentProfileService = agentProfileService;
	}

	@GetMapping("/chat")
	public String chat(Model model) {
		log.debug("Agent: " + agentProfileService.getAgentName());
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
	public String chatResponse(@RequestParam String message, HttpSession session) {
		StringOutput output = new StringOutput();
		String aiResponse = this.chatClient
				.prompt()
				.system(USER_NOT_TELLING_HIS_NAME_GENERATE_ONE_FUNNY_NAME 
						+ agentProfileService.getSystemPrompt())
				.advisors(getChatMemoryAdvisor(session))
				.user(message).call()
				.content();
		jteTemplateEngine.render("chat/answer.jte", Map.of("answer", aiResponse), output);
		return output.toString();
	}

	private BaseAdvisor getChatMemoryAdvisor(HttpSession session) {
		var userChat =  (BaseAdvisor) session.getAttribute("userChatSession");
		if (userChat == null) {
			var messageWindowChatMemory = MessageWindowChatMemory.builder().build();
			userChat = MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
			.conversationId(UUID.randomUUID().toString())
			.order(0)
			.build();					
			session.setAttribute("userChatSession", userChat);
		}
			
		return userChat;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String handleError(Exception ex, WebRequest request) {
		log.error("Error during chat response", ex);
		String message = request.getParameter("message");
		StringOutput output = new StringOutput();
		jteTemplateEngine.render("chat/error.jte", Map.of("question", message), output);
		return output.toString();
	}
}