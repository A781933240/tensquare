package com.tensquare.spit.gathering;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import util.IdWorker;
@SpringBootApplication
@EnableCaching
public class gatheringApplication {

	public static void main(String[] args) {
		SpringApplication.run(gatheringApplication.class, args);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}
	
}
