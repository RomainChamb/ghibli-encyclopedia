package fr.barbebroux.ghibliencyclopedia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import fr.barbebroux.ghibliencyclopedia.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@SpringBootApplication
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class GhibliEncyclopediaApp {

  private static final Logger log = LoggerFactory.getLogger(GhibliEncyclopediaApp.class);

  public static void main(String[] args) {
    Environment env = SpringApplication.run(GhibliEncyclopediaApp.class, args).getEnvironment();

    if (log.isInfoEnabled()) {
      log.info(ApplicationStartupTraces.of(env));
    }
  }
}
