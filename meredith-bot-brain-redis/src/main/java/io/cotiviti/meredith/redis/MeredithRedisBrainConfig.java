package io.cotiviti.meredith.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan
@ImportResource("classpath:/io/cotiviti/meredith/redis/config.xml")
public class MeredithRedisBrainConfig {
	@Autowired
	private String id;
}
