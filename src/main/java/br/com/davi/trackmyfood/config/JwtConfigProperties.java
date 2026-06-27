package br.com.davi.trackmyfood.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "br.com.davi.trackmyfood.jwt")
public class JwtConfigProperties {

    private String accessSecret;
    private Long accessExpiresIn;
    private String refreshSecret;
    private Long refreshExpiresIn;

}
