package io.cotiviti.meredith.worker.shell;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan
@ImportResource("classpath:/io/cotiviti/meredith/shell/config.xml")
public class MeredithWorkerShellConfig {
}
