package com.crossover.techtrial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CrossBlogsApplication {	
  public static void main(String[] args) {
    SpringApplication.run(CrossBlogsApplication.class, args);
  }
}
