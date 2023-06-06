package com.progresssoft.ClusteredDataWarehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ClusteredDataWarehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClusteredDataWarehouseApplication.class, args);
	}

}
