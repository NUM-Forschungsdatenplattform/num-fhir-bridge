package org.ehrbase.fhirbridge.fhir.bundle.validator;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Bundle;

import java.util.Map;

public class MibiKulturBundleValidator extends AbstractBundleValidator {
    private static final String MIBI_KULTUR_URL = "https://www.medizininformatik-initiative.de/fhir/modul-mikrobio/StructureDefinition/mii-pr-mikrobio-kultur-nachweis";
    private static final String EMPFINDLICHKEIT_URL = "https://www.medizininformatik-initiative.de/fhir/modul-mikrobio/StructureDefinition/mii-pr-mikrobio-empfindlichkeit";
    private static final String MRGN_URL = "https://www.medizininformatik-initiative.de/fhir/modul-mikrobio/StructureDefinition/mii-pr-mikrobio-mrgn-klasse";
    private static final String MRE_URL = "https://www.medizininformatik-initiative.de/fhir/modul-mikrobio/StructureDefinition/mii-pr-mikrobio-mre-klasse";
    private static final String SPECIMEN_URL = "http://hl7.org/fhir/StructureDefinition/Specimen";

    private int mibiKulturContained = 0;
    private int empfindlichkeitContained = 0;
    private int mrgnContained = 0;
    private int mreContained = 0;
    private int specimenContained = 0;

    @Override
    public void validateRequest(Object bundle, Map<String, Object> parameters) {
        super.validateRequest(bundle, parameters);
        validateKultur((Bundle) bundle);
    }

    private void validateKultur(Bundle bundle) {
        resetAttributes();
        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            validateProfiles(entry);
        }
        checkForMandatoryProfiles();
    }

    private void resetAttributes() {
        mibiKulturContained = 0;
        empfindlichkeitContained = 0;
        mrgnContained = 0;
        mreContained = 0;
        specimenContained = 0;
    }

    private void validateProfiles(Bundle.BundleEntryComponent entry) {
        try {
            String profileUrl = entry.getResource().getMeta().getProfile().get(0).getValue();
            switch (profileUrl) {
                case MIBI_KULTUR_URL:
                    setMIBIKultur();
                    break;
                case EMPFINDLICHKEIT_URL:
                    empfindlichkeitContained += 1;
                    break;
                case MRGN_URL:
                    mrgnContained += 1;
                    break;
                case MRE_URL:
                    mreContained += 1;
                    break;
                case SPECIMEN_URL:
                    specimenContained += 1;
                    break;
                default:
                    throw new UnprocessableEntityException("MIBI Kultur bundle needs to contain the specified profiles. Please delete profile " + profileUrl + " from the Bundle.");
            }

        } catch (IndexOutOfBoundsException e) {
            throw new UnprocessableEntityException("Make sure the Mibi Kultur supported Profiles are contained in the Bundle these are: Kultur, Empfindlichkeit, MRGN or MRE, Specimen");
        }
    }


    private void checkForMandatoryProfiles() {
        if (mibiKulturContained == 1 && empfindlichkeitContained == 1 && specimenContained == 1) {
            if (mrgnContained == 0 && mreContained == 0) {
                throw new UnprocessableEntityException("Make sure the Mibi Kultur supported Profiles are contained in the Bundle these are: Kultur, Empfindlichkeit, MRGN or MRE, Specimen");
            }
        } else {
            throw new UnprocessableEntityException("Make sure the Mibi Kultur supported Profiles are contained in the Bundle these are: Kultur, Empfindlichkeit, MRGN or MRE, Specimen");
        }
    }

    private void setMIBIKultur() {
        mibiKulturContained += 1;
        if (mibiKulturContained != 1) {
            throw new UnprocessableEntityException("MIBI Kultur profile is duplicated within the bundle, please delete one of them");
        }

    }


}
