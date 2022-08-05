package com.mlzj.component.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mlzj.component.generator.dao")
public class MlzjComponentGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MlzjComponentGeneratorApplication.class, args);
    }

}
