package com.pile.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.pile.backend.pojo.po.mapper")
public class PileApplication {

    public static void main(String[] args) {
        SpringApplication.run(PileApplication.class, args);
    }
}
