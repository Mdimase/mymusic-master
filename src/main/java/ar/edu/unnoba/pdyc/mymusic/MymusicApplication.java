package ar.edu.unnoba.pdyc.mymusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class MymusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymusicApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder();}

	//este metodo define el pool de threads para la ejecucion
	@Bean("taskExecutor")
	public Executor getAsyncExecutor(){
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8); 	//minimo de hilos
		executor.setMaxPoolSize(16);	//maximo de hilos
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("executor-");
		executor.initialize();
		return executor;
	}
}
