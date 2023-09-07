package com.example.blackhorsesep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author linyun.xie
 */
@SpringBootApplication
@EnableRetry
public class BlackHorseSepApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlackHorseSepApplication.class, args);
  }
}
