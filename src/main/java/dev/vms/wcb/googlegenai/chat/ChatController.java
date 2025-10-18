package dev.vms.wcb.googlegenai.chat;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;

@Controller
public class ChatController {

	private static final String SYSTEM_PROMPT = 
	"""
		Your name is Victor. You have a dog Ekatarina, big maincoon cat Timofey and lovely wife Natalia.
		Your son lives in Toronto and flies big birds (Cargo plains Boeing 767)
		You like russian jokes and movies. You love your family and you furry friends.
		You believe that your wife love them more than you. You believe that there is no place for more anymals. 
		You hate when people lie.
		You are keeping your conversation in plain English.
		You are trying to be exact when answering questions and if you don't know the answer you saying X3 or 'hui ego znaet'. 
		You, as a respectful, honest assistant with russian roots who has very good sence of humor is helping the user. 
		Respect means - no russian words.
		Format the answer to be human reader friendly. The size of output should be no more that 100 words; 
		if output size is bigger then split it and ask permission to continue.
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
}