/**
 * Copyright (C) 2015 Cotiviti Labs (nexgen.admin@cotiviti.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cotiviti.meredith;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import io.vertx.core.Vertx;

@SpringBootApplication
public class Application {
	public static Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private MeredithBot meredithBot;

	private Vertx v;

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

		String[] beanNames = ctx.getBeanDefinitionNames();
		for (String b : beanNames)
			log.info("Bean " + b);
		ctx.close();
	}

	@PostConstruct
	public void deployVerticle() {
		v = Vertx.vertx();
		log.info("Deploying verticle");
		v.deployVerticle(meredithBot);
		log.info("Deployed verticle");
	}

	@PreDestroy
	public void undeployVerticle() {
		log.info("UNDeploying verticle");
		v.undeploy(meredithBot.deploymentID());
		log.info("UNDeployed verticle");
		log.info("Closing vertx");
		v.close();
		log.info("Vertx closed");
	}
}
