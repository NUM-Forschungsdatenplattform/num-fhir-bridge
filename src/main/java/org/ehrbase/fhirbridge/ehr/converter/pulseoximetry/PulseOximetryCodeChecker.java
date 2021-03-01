package org.ehrbase.fhirbridge.ehr.converter.pulseoximetry;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;

import java.util.List;

import static org.ehrbase.fhirbridge.ehr.converter.pulseoximetry.PulseOxymetryCode.CODING_1;
import static org.ehrbase.fhirbridge.ehr.converter.pulseoximetry.PulseOxymetryCode.CODING_2;
import static org.ehrbase.fhirbridge.ehr.converter.pulseoximetry.PulseOxymetryCode.CODING_3;
import static org.ehrbase.fhirbridge.ehr.converter.pulseoximetry.PulseOxymetryCode.CODING_4;

public class PulseOximetryCodeChecker {

    void checkIfPulseOximetry(Observation observation) {
        checkAmountOfCodes(observation);
    }

    private void checkAmountOfCodes(Observation observation) {
        if (observation.getCode().getCoding().size() == 1) {
            matchCode(observation.getCode().getCoding());
        } else if (observation.getCode().getCoding().size() == 2) {
            matchCodes(observation.getCode().getCoding());
        } else {
            throw new UnprocessableEntityException("Too many Codes, for oxygen saturation a maximum combination of two codes is supported");
        }
    }

    private void matchCode(List<Coding> codes) {
        String codeSystem = codes.get(0).getSystem();
        String code = codes.get(0).getCode();
        if (!(codeSystem.equals(CODING_1.getCode().get(0).get(0)) && code.equals(CODING_1.getCode().get(0).get(1)))
                && !(codeSystem.equals(CODING_2.getCode().get(0).get(0)) && code.equals(CODING_2.getCode().get(0).get(1)))) {
            throw new UnprocessableEntityException("The Code of code.coding not supported for the Fhir-Bridge");
        }
    }

    private void matchCodes(List<Coding> codes) {
        if (!matchCode3(codes) || !matchCode4(codes)) {
            throw new UnprocessableEntityException("The Code of code.coding is not supported for the Fhir-Bridge");
        }

    }

    private boolean matchCode3(List<Coding> codes) {
        for (Coding code : codes) {
            return code.getSystem().equals(CODING_3.getCode().get(0).get(0)) && code.getCode().equals(CODING_3.getCode().get(0).get(1)) ||
                    code.getSystem().equals(CODING_3.getCode().get(1).get(0)) && code.getCode().equals(CODING_3.getCode().get(1).get(1));
        }
        return false;
    }

    private boolean matchCode4(List<Coding> codes) {
        for (Coding code : codes) {
            return code.getSystem().equals(CODING_4.getCode().get(0).get(0)) && code.getCode().equals(CODING_4.getCode().get(0).get(1)) ||
                    code.getSystem().equals(CODING_4.getCode().get(1).get(0)) && code.getCode().equals(CODING_4.getCode().get(1).get(1));
        }
        return false;
    }
}


