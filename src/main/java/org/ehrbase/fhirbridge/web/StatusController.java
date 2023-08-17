package org.ehrbase.fhirbridge.web;

import org.ehrbase.client.exception.ClientException;
import org.ehrbase.client.openehrclient.OpenEhrClient;
import org.ehrbase.fhirbridge.core.repository.PatientEhrRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class StatusController implements HealthIndicator {
    private final HealthEndpoint healthEndpoint;
    private final PatientEhrRepository patientEhrRepository;
    private final OpenEhrClient openEhrClient;

    public StatusController(HealthEndpoint healthEndpoint, PatientEhrRepository patientEhrRepository, OpenEhrClient openEhrClient) {
        this.healthEndpoint = healthEndpoint;
        this.patientEhrRepository = patientEhrRepository;
        this.openEhrClient = openEhrClient;
    }

    @GetMapping(path = "/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("UP");
    }

    @GetMapping(path = "/health")
    @Override
    public Health health() {
        try {
            if (checkEhrBaseConnection()) {
                return Health.outOfService().build();
            }
            if (patientEhrRepository.count() >= 0) {
                return Health
                        .status(healthEndpoint.health().getStatus().getCode())
                        .build();
            } else {
                return Health.down().build();
            }
        }catch (Exception ex){
            return Health.down(ex).build();
        }
    }

    private boolean checkEhrBaseConnection() {
        try{
            openEhrClient.templateEndpoint().findAllTemplates();
        } catch (ClientException ex){
            return true;
        }
        return false;
    }
}
