package com.game.nonogram;

import com.game.nonogram.game.kernel.NonogramKernel;
import com.game.nonogram.jpa.services.FieldServices;
import com.game.nonogram.jpa.services.PlayerServices;
import com.game.nonogram.jpa.services.RecordServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.game.nonogram.jpa","com.game.nonogram.controllers","com.game.nonogram.game"})
public class NonogramApplicationClient implements CommandLineRunner {

	@Autowired
	private ApplicationContext appContext;

	public static void main(String[] args) {
		new SpringApplicationBuilder(NonogramApplicationClient.class).web(WebApplicationType.NONE).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		NonogramKernel kernel = new NonogramKernel(
				appContext.getBean(PlayerServices.class),
				appContext.getBean(RecordServices.class),
				appContext.getBean(FieldServices.class));
		kernel.loadGame();
	}
}
