package org.ehrbase.fhirbridge.fhir.bundle.converter;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.ehrbase.fhirbridge.fhir.support.Resources;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An abstract {@link Converter} that provides methods to convert a {@link Bundle} into the corresponding {@link Resource}.
 *
 * @param <T> The expected resource type.
 */
public abstract class AbstractBundleConverter<T> implements Converter<Bundle, T> {

    @SuppressWarnings("unchecked")
    protected T getRoot(Bundle bundle, Profile profile) {
        return (T) bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> Resources.hasProfile(resource, profile))
                .findFirst()
                .orElseThrow(() -> new UnprocessableEntityException("Root resource with profile '" + profile.getUri() + "' is missing"));
    }

    protected Map<String, Resource> mapResources(Bundle bundle) {
        return bundle.getEntry().stream()
                .collect(Collectors.toMap(Bundle.BundleEntryComponent::getFullUrl, Bundle.BundleEntryComponent::getResource));
    }

    protected List<Resource> generateContains(Bundle bundle, List<Reference> hasMembers){ //TODO should be two abstracts one for obs and one for Composition to cover all of it ....
        Map<String, Resource> resources = mapResources(bundle);
        List<Resource> contains = new ArrayList<>();
        for (Reference reference : hasMembers) {
            Resource resource = resources.get(reference.getReference());
            if (resource == null) {
                throw new UnprocessableEntityException("Resource '" + reference.getReference() + "' is missing");
            }
            resource.setId((String) null);
            reference.setReference(null);
            reference.setResource(resource);
            contains.add(resource);
        }
        return contains;
    }
}
