//package com.arranger.eurekaclient.service.impl;
//
//import com.arranger.eurekaclient.entity.Server;
//import com.arranger.eurekaclient.repository.ServerRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class ServerConfig {
//
//    @Value("${eureka.instance.instance-id}")
//    private String instanceId;
//
//    @Bean
//    CommandLineRunner commandLineRunner(
//            ServerRepository serverRepository) {
//
//        Server server = new Server();
//        server.setId(instanceId);
//        Integer taskNumber = 2;
//        server.setTaskNumber(taskNumber);
//
//        return args -> {
//            serverRepository.save(server);
//        };
//    }
//}
