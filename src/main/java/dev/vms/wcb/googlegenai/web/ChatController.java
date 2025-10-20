package dev.vms.wcb.googlegenai.web;

import java.io.IOException;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

	private static final String SYSTEM_PROMPT = 
	"""
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
		this.chatClient = builder.build();
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
        return output.toString();
    }

    @GetMapping("/chat/response")
    @ResponseBody
    public String chatResponse(@RequestParam String message) {
        String response = this.chatClient
				.prompt()
				.system(SYSTEM_PROMPT)
				.user(message)
				.call()
				.content();
        StringOutput output = new StringOutput();
        jteTemplateEngine.render("chat/answer.jte", Map.of("answer", response), output);
        return output.toString();
    }
    
	@GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter chatStream(@RequestParam String message) {
		SseEmitter emitter = new SseEmitter(-1L);
		chatClient.prompt()
			.system(SYSTEM_PROMPT)
			.user(message)
			.stream()
			.content()
			.doOnNext(chunk -> {
				if (chunk != null && !chunk.isEmpty()) {
					emitHtmlFragment(emitter, chunk);
				}
			})
			.doOnError((error) -> {
				// Send a completion error that HTMX can detect
				log.error("Error sending completion event", error);
				emitter.completeWithError(error);
			})
			.doOnComplete(() -> {
					// Send a completion event that HTMX can detect
					try {
						emitter.send(SseEmitter.event().name("end").data(""));
					} catch (IOException e) {
						log.error("Error sending completion event", e);
					}
					emitter.complete();
				})
			.subscribe();

		return emitter;
	}

	private void emitHtmlFragment(SseEmitter emitter, String chunk) {
		try {
			StringOutput output = new StringOutput();
			jteTemplateEngine.render("chat/answer.jte", Map.of("answer", chunk), output);

			emitter.send(SseEmitter.event().name("message") // Match this with sse-swap in HTMX
					.data(output.toString()));

		} catch (IOException e) {
			log.error("Error sending SSE event", e);
			emitter.completeWithError(e);
		}
	}    
    
}