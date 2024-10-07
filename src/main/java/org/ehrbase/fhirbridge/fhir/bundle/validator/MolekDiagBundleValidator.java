package org.ehrbase.fhirbridge.fhir.bundle.validator;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Bundle;

import java.util.Map;

public class MolekDiagBundleValidator extends AbstractBundleValidator {
    private static final String MOLEK_DIAG_URL = "https://www.medizininformatik-initiative.de/fhir/modul-mikrobio/StructureDefinition/mii-pr-mikrobio-molekulare-diagnostik";
    private static final String SPECIMEN_URL = "http://hl7.org/fhir/StructureDefinition/Specimen";

    private int molekContained = 0;

    private int specimenContained = 0;

    @Override
    public void validateRequest(Object bundle, Map<String, Object> parameters) {
        super.validateRequest(bundle, parameters);
        validateMolekDiag((Bundle) bundle);
    }

    private void validateMolekDiag(Bundle bundle) {
        resetAttributes();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            validateProfiles(entry);
        }
        checkForMandatoryProfiles();
    }

    private void resetAttributes() {
        molekContained = 0;
        specimenContained = 0;
    }

    private void validateProfiles(Bundle.BundleEntryComponent entry) {
        try {
            String profileUrl = entry.getResource().getMeta().getProfile().get(0).getValue();
            switch (profileUrl) {
                case MOLEK_DIAG_URL:
                    setMolekDiag();
                    break;
                case SPECIMEN_URL:
                    specimenContained += 1;
                    break;
                default:
                    throw new UnprocessableEntityException("Mibi Molekular Diagnostik bundle needs to contain the specified profiles. Please delete profile " + profileUrl + " from the Bundle.");
            }

        } catch (IndexOutOfBoundsException e) {
            throw new UnprocessableEntityException("Make sure only the for Mibi Molekular Diagnostik supported Profiles are contained in the Bundle tthese are: Molek Diagn Organismus Nachweis, Specimen");
        }
    }


    private void checkForMandatoryProfiles() {
        if (molekContained != 1 || specimenContained != 1) {
            throw new UnprocessableEntityException("Make sure only the for Mibi Molekular Diagnostik supported Profiles are contained in the Bundle these are: Molek Diagn Organismus Nachweis, Specimen");
        }
    }

    private void setMolekDiag() {
        molekContained += 1;
        if (molekContained != 1) {
            throw new UnprocessableEntityException("Molek Diag Organismus Nachweis profile is duplicated within the bundle, please delete one of them");
        }

    }


}
