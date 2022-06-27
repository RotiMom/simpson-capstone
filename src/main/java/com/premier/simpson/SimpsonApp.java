package com.premier.simpson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;

@SpringBootApplication
public class SimpsonApp {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(SimpsonApp.class, args);
    }

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.1.14:3306/simpson_capstone");
        dataSource.setUsername("simpsoncs");
        dataSource.setPassword("simpsoncspass");
        return dataSource;
    }

}
