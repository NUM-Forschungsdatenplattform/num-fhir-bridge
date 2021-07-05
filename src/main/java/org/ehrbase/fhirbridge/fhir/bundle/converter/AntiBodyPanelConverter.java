package org.ehrbase.fhirbridge.fhir.bundle.converter;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AntiBodyPanelConverter extends AbstractBundleConverter<Observation> {

    @Override
    public Observation convert(@NonNull Bundle bundle) {
        Observation observation = getRoot(bundle, Profile.ANTI_BODY_PANEL);
        Map<String, Resource> resources = mapResources(bundle);

        List<Resource> contains = new ArrayList<>();
        for (Reference reference : observation.getHasMember()) {
            Resource resource = resources.get(reference.getReference());
            if (resource == null) {
                throw new UnprocessableEntityException("Resource '" + reference.getReference() + "' is missing");
            }

            resource.setId((String) null);
            reference.setReference(null);
            reference.setResource(resource);
            contains.add(resource);
        }

        observation.setContained(contains);
        return observation;
    }
}
