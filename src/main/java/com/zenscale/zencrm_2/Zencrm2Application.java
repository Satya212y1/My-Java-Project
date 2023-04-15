package com.zenscale.zencrm_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TreeMap;

@SpringBootApplication
public class Zencrm2Application {

    @Autowired
    DataSource dataSource;




    public static void main(String[] args) {

        SpringApplication.run(Zencrm2Application.class, args);

    }




    @Bean
    JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(dataSource);
    }




}
