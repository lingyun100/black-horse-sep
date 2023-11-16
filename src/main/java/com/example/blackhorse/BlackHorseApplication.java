package com.example.blackhorse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author linyun.xie
 */
@SpringBootApplication
@EnableRetry
public class BlackHorseApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlackHorseApplication.class, args);
  }
}
