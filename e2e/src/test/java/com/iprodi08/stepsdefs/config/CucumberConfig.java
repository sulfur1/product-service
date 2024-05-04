package com.iprodi08.stepsdefs.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberConfig.class)
public class CucumberConfig {
}
