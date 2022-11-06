package com.arranger.eurekaclient.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ServerConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            ServerRepository serverRepository) {

        return args -> serverRepository.deleteAll();
    }
}
