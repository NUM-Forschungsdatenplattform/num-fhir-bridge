package org.ehrbase.fhirbridge.config;

import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.openehealth.ipf.boot.fhir.IpfFhirAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@AutoConfigureBefore({SearchParamConfig.class, IpfFhirAutoConfiguration.class})
@Configuration
public class SearchParamRegistryConfig {
  @AutoConfigureBefore({SearchParamConfig.class, IpfFhirAutoConfiguration.class})
  @Configuration
  public static class CustomSearchParamRegistryConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CustomSearchParamRegistryConfig.class);

    @Bean
    @Primary
    public ISearchParamRegistry searchParamRegistry() {
      LOG.info("CREATE SEARCH_PARAM_REGISTRY ....");
      return new SearchParamRegistryImpl();
    }
  }
}
