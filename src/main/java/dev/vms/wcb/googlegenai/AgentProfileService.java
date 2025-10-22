package dev.vms.wcb.googlegenai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Service
@Getter
public class AgentProfileService {

    @Value("classpath:ai-agent-profile.txt")
    private Resource agentProfileResource;

    private String agentName;
    private String systemPrompt;

    @PostConstruct
    public void init() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(agentProfileResource.getURI()));
        this.agentName = lines.get(0);
        this.systemPrompt = lines.stream().skip(1).collect(Collectors.joining("\n"));
    }
}
