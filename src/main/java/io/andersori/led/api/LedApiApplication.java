package io.andersori.led.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import redis.clients.jedis.Jedis;

@SpringBootApplication
public class LedApiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext contex = SpringApplication.run(LedApiApplication.class, args);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			contex.getBean(Jedis.class).disconnect();
		}));	
	}

}
