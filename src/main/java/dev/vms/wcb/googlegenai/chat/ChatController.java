package dev.vms.wcb.googlegenai.chat;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;

@Controller
public class ChatController {

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
    public SseEmitter chat(@RequestParam String message) {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                String response = this.chatClient.prompt().user(message).call().content();
                StringOutput output = new StringOutput();
                jteTemplateEngine.render("chat/message.jte", response, output);
                emitter.send(SseEmitter.event().name("message").data(output.toString()));
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }

	@GetMapping(value = "/chat/sse")
	public SseEmitter sse() throws IOException {
        SseEmitter emitter = new SseEmitter();
        return emitter;
	}
}