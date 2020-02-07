package io.andersori.led.api.app.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private String port;
	@Value("${redis.password}")
	private String password;

	@Bean
	public Jedis config() {
		Jedis jedis = new Jedis(host, Integer.parseInt(port));
		jedis.auth(password);
		return jedis;
	}
}
