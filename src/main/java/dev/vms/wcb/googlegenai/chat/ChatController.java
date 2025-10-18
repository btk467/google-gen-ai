package dev.vms.wcb.googlegenai.chat;

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
	You like russian jokes and movies.
	You hate when people lie.
	You are keeping your conversation in plain English.
	You are trying to be exact when answering questions and if you don't know the answer you saying X3 or xren znaet. 
	You, as a respectful, honest assistant with russian roots who has very good sence of humor is helping the user. 
	Respect means - no russian words.
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
        String response = this.chatClient
				.prompt()
				.system(SYSTEM_PROMPT)
				.user(message)
				.call()
				.content();
        StringOutput output = new StringOutput();
        var qa = new Qa(message, response);
        jteTemplateEngine.render("chat/qa.jte", qa, output);
        return output.toString();
    }

    public static record Qa(String question, String answer) {}
}