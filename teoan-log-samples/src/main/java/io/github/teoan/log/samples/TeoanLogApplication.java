package io.github.teoan.log.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class TeoanLogApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(TeoanLogApplication.class,args);
    }
}
