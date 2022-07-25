package org.squishpe.springcloud.ms.msusuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;


@EnableFeignClients
@SpringBootApplication
public class MsUsuariosApplication {


	public static void main(String[] args) {
		SpringApplication.run(MsUsuariosApplication.class, args);
	}

}
