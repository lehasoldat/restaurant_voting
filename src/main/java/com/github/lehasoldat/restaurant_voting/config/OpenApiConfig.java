package com.github.lehasoldat.restaurant_voting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        <a href='http://github.com/lehasoldat/restaurant_voting'>Restaurant voting app</a> 
                        (graduation project at <a href='https://javaops.ru/view/topjava'>course TopJava</a>)
                        <p><b>Test credentials:</b><br>
                        - user@mail.ru / user<br>
                        - admin@mail.ru / admin</p>
                        """,
                contact = @Contact(
                        name = "Aleksey Saldakeev",
                        email = "saldakeev@gmail.com"
                )

        )
        , security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {
}
