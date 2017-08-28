package com.rest.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@ImportResource(
        locations = {"classpath*:/rest_config.xml", "classpath*:/security_config.xml"}
)
@ComponentScan(basePackages = "com.rest")
@PropertySource({
        "classpath*:/rest.properties",
        "classpath*:/web.properties"
})
public class AppConfig {
  @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
    return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
