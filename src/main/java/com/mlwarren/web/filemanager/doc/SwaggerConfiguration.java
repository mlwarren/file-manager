package com.mlwarren.web.filemanager.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final Contact DEFAULT_CONTACT = new Contact(
            "Mike Warren", "https://github.com/mlwarren", "mlwarren88@gmail.com");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "File Management REST Api",
            "Allows remote users to upload and download files. Allows administrators to set user paramaters and quota.",
            "1.0", "", DEFAULT_CONTACT, "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0");

    public static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(
            Arrays.asList("application/json", "application/xml"));

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }




}
