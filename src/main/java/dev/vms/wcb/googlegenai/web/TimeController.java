package dev.vms.wcb.googlegenai.web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@CrossOrigin
public class TimeController {

	@GetMapping("/time")
    public String time() {
        return "time";
    }
    
    @GetMapping(value = "/time/stream")
    public SseEmitter streamTime() {
        SseEmitter emitter = new SseEmitter(60000L);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        
        executor.scheduleAtFixedRate(() -> {
            try {
                String time = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                
                String html = "<div class='time-slot'>" + time + "</div>";
                
                emitter.send(SseEmitter.event()
                    .name("message")
                    .data(html)
                    .build());
                    
            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
        
        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);
        
        return emitter;
    }
}