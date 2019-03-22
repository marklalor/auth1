package org.auth1.auth1.config;

import java.io.FileInputStream;
import java.io.IOException;
import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.DatabaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"org.auth1.auth1"})
public class IntegrationTestConfig extends WebMvcConfigurerAdapter {

  @Bean
  public DatabaseConfiguration databaseConfiguration(@Value("${config.databaseConfigurationPath}")
      String databaseConfigurationPath) {
    try {
      return
          DatabaseConfiguration
              .fromConfigurationFile(new FileInputStream(databaseConfigurationPath));
    } catch (IOException | org.auth1.auth1.model.Configuration.InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }

  @Bean
  public Auth1Configuration auth1Configuration(@Value("${config.auth1ConfigurationPath}")
      String auth1ConfigurationPath) {
    try {
      return
          Auth1Configuration.fromConfigurationFile(new FileInputStream(auth1ConfigurationPath));
    } catch (IOException | org.auth1.auth1.model.Configuration.InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
}
