package org.ehrbase.fhirbridge.fhir.bundle.validator;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.List;

class MemberValidator {
    List<String> fullUrlList = new ArrayList<>();
    List<String> hasMembersList = new ArrayList<>();

    void addFullUrls(String fullUrl) {
        addListEntry(this.fullUrlList, fullUrl);
    }

    void deleteFullUrl(String fullUrl) {
        fullUrlList.remove(fullUrl);
    }

    void addListEntry(List<String> list, String entry) {
        if (entry.startsWith("urn:uuid:")) {
            list.add(entry);
        } else {
            throw new UnprocessableEntityException(entry + " is not a valid format. The fullUrl has to be an UUID and start with: urn:uuid:, example urn:uuid:04121321-4af5-424c-a0e1-ed3aab1c349d");
        }
    }

    void setHasMembersList(List<Reference> hasMembersList) {
        if (memberListNotAlreadySet()) {
            for (Reference reference : hasMembersList) {
                addListEntry(this.hasMembersList, reference.getReference());
            }
        } else {
            throw new UnprocessableEntityException("More then one Observation has the hasMember Attribute, this is not supported by the Fhir-bridge");

        }
    }

    void addSpecimenMember(Reference reference) {
        hasMembersList.add(reference.getReference());
    }

    boolean memberListNotAlreadySet() {
        return this.hasMembersList.size() == 0;
    }

    void validateMembers() {
        for (String fullUrl : fullUrlList) {
            if (!hasMembersList.contains(fullUrl) && hasMembersList.size()>0 ) {
                throw new UnprocessableEntityException("The url " + fullUrl + " within the Bundle is not matching the hasMember.references. Make sure that every Url contained in the observation with hasMembers is contained and correct.");
            }
        }
    }
}
