package org.ehrbase.fhirbridge.fhir.bundle.converter;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

public class MolekDiagBundleConverter extends AbstractBundleConverter<Observation> {
    @Override
    public Observation convert(@NonNull Bundle bundle) {
        Observation observation = getRoot(bundle, Profile.MIBI_MOLEKULARE_DIAGNOSTIC);
        List<Resource> contains = generateContains(bundle, observation.getHasMember());
        contains.add(addSpecimen(bundle, observation.getSpecimen()));
        observation.setContained(contains);
        return observation;
    }

    private Resource addSpecimen(Bundle bundle, Reference reference) {
        Map<String, Resource> resources = mapResources(bundle);
        Resource resource = resources.get(reference.getReference());
        if (resource == null) {
            throw new UnprocessableEntityException("Resource '" + reference.getReference() + "' is missing");
        }
        resource.setId((String) null);
        reference.setReference(null);
        reference.setResource(resource);
        return resource;
    }


}
