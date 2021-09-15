package ru.solyanin.elasticsearchfiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ElasticSearchFilesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchFilesApplication.class);
    }
}
