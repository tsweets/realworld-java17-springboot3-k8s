package org.beer30.realworld.config;

import java.util.List;

import org.beer30.realworld.utils.AppInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;


/**
 * @author tsweets
 * 5/1/23 - 10:57 AM
 */

@Configuration
public class OpenAPIConfig {

    //@Value("${realworld.openapi.dev-url}")
    private final String devUrl = "foo";

    //@Value("${realworld.openapi.prod-url}")
    private final String prodUrl = "bar";
 
    @Bean
    public OpenAPI myOpenAPI() {
     //   String versionNumnber = new AppInfo().getAppVersion();
        String versionNumnber = "fixme";

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("tony.sweets@gmail.com");
        contact.setName("Tony Sweets");
        contact.setUrl("https://www.github.com/tsweets");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("Java 17, Spring Boot 3, and Kubernetes RealWorld Backend")
            .version(versionNumnber)
            .contact(contact)
            .description("Implementation of the RealWorld App Backend")
            .termsOfService("URL")
            .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
 }

 
}
