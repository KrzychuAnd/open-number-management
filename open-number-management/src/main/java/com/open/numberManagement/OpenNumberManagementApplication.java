package com.open.numberManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cmeza.sdgenerator.annotation.SDGenerator;

@SDGenerator(
        entityPackage = "com.open.numberManagement.entity",
        repositoryPackage = "com.open.numberManagement.service.repository",
        //managerPackage = "com.open.numberManagement.managers",
        repositoryPostfix = "Repository",
        //managerPostfix = "Manager",
        onlyAnnotations = false,
        debug = true,
        overwrite = false
)
@SpringBootApplication
public class OpenNumberManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenNumberManagementApplication.class, args);
	}
}
