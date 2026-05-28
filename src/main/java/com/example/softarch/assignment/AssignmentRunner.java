package com.example.softarch.assignment;

import com.example.softarch.assignment.agent.ArchitectureDesignAgent;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AssignmentRunner implements CommandLineRunner {

    private final ArchitectureDesignAgent agent;
    private final Path outputDir;

    public AssignmentRunner(
            ArchitectureDesignAgent agent,
            @Value("${assignment.output-dir:outputs}") String outputDir) {
        this.agent = agent;
        this.outputDir = Path.of(outputDir);
    }

    @Override
    public void run(String... args) throws Exception {
        agent.run(outputDir);
    }
}
