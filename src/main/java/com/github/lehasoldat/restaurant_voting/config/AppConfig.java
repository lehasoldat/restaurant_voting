package com.github.lehasoldat.restaurant_voting.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.time.LocalDate;

@Configuration
@Slf4j
@EnableCaching
public class AppConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server on localhost:9092");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Bean
    KeyGenerator currentDateKeyGenerator() {
        return (target, method, params) -> LocalDate.now();
    }
}
