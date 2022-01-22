package com.scorpios.secondkill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis((input)->{
                    Class<?> declaringClass = input.declaringClass();
                    if(declaringClass.isAnnotationPresent(RestController.class)){
                        return true;
                    }
                    if(input.isAnnotatedWith(ResponseBody.class)){
                        return true;
                    }
                    return false;
                })
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("秒杀测试")
                .contact(new Contact("scorpios","","1435513775@qq.com"))
                .description("秒杀测试")
                .version("1.0.0")
                .build();
    }

}
