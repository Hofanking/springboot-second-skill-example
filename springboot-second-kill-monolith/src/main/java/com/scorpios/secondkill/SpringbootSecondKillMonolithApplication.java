package com.scorpios.secondkill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.scorpios.secondkill.mapper")
public class SpringbootSecondKillMonolithApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecondKillMonolithApplication.class, args);
    }

}
