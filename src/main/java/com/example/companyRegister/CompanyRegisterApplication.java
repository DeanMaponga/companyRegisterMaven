package com.example.companyRegister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

@SpringBootApplication
public class CompanyRegisterApplication implements WebMvcConfigurer{

	public static void main(String[] args) {

        // Generate 32 random bytes (256 bits)
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);

        // Convert bytes to BigInteger
        BigInteger bigInt = new BigInteger(1, randomBytes);

        // Convert BigInteger to hexadecimal string
        String hexKey = bigInt.toString(16);

        System.out.println(hexKey);
		SpringApplication.run(CompanyRegisterApplication.class, args);
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    /*@Bean
    public CorsFilter corsFilter() {
        System.out.println("COORS");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8443"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8443","https://apidev2.codevirtus.com","https://apidev2.codevirtus.com:8443")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new ApiTokenFilter());
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }*/
}
