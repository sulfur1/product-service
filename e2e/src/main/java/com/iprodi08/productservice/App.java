package com.iprodi08.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class App {
    private App() {
    }

    /**
     * Test application for check cucumber.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
