package dev.vms.wcb.googlegenai;

import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
    
    @Bean
    MessageWindowChatMemory chatMemory() {
    	return MessageWindowChatMemory.builder()
    		    .maxMessages(100)
    		    .build();
    }
}