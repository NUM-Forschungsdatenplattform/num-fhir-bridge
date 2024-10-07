package org.ehrbase.fhirbridge.fhir.bundle;

import org.ehrbase.fhirbridge.comparators.CustomTemporalAcessorComparator;
import org.ehrbase.fhirbridge.ehr.converter.specific.mibimolekdiagnostik.MibiMolekDiagnostikConverter;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.VirologischerBefundComposition;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.AnatomischeLokalisationCluster;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.BefundJedesEreignisPointEvent;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.BefundObservation;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.EmpfaengerstandortCluster;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.FallidentifikationCluster;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.LabortestPanelCluster;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.ProAnalytCluster;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.ProAnalytErgebnisStatusDvCodedText;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.ProAnalytErgebnisStatusElement;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.ProAnalytQuantitativesErgebnisDvQuantity;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.ProAnalytQuantitativesErgebnisElement;
import org.ehrbase.fhirbridge.ehr.opt.virologischerbefundcomposition.definition.ProbeCluster;
import org.ehrbase.fhirbridge.fhir.AbstractBundleMappingTestSetupIT;
import org.ehrbase.fhirbridge.fhir.bundle.converter.MolekDiagBundleConverter;
import org.ehrbase.fhirbridge.fhir.bundle.validator.MolekDiagBundleValidator;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.metamodel.clazz.ValueObjectDefinition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests for {@link Bundle Bundle} resource.
 */
class MolekDiagIT extends AbstractBundleMappingTestSetupIT {


    //Just to test Bundle transformation the mapping tests for Observations are located in Observation/Virologischer
    public MolekDiagIT() {
        super("Bundle/MolekDiag/", Bundle.class);
    }


    @Test
    void createMolekDiag() throws IOException {
        create("create-molek-diag.json");
    }

    @Test
    void createMolekDiagMapp() throws IOException {
        testMapping("create-molek-diag.json", "paragon-create-molek-diag.json");
    }

    @Test
    void invalidSpecimenMissing() throws IOException {
        Exception exception = executeValidatorException("invalid-specimen-missing.json");
        assertEquals("Make sure only the for Mibi Molekular Diagnostik supported Profiles are contained in the Bundle these are: Molek Diagn Organismus Nachweis, Specimen", exception.getMessage());

    }

    @Test
    void invalidMolekDiagMissing() throws IOException {
        Exception exception = executeValidatorException("invalid-molekdiag-missing.json");
        assertEquals("Make sure only the for Mibi Molekular Diagnostik supported Profiles are contained in the Bundle these are: Molek Diagn Organismus Nachweis, Specimen", exception.getMessage());

    }


    @Test
    void invalidDuplicate() throws IOException {
        Exception exception = executeValidatorException("invalid-duplicate-molek.json");
        assertEquals("Make sure only the for Mibi Molekular Diagnostik supported Profiles are contained in the Bundle these are: Molek Diagn Organismus Nachweis, Specimen", exception.getMessage());

    }

    @Override
    public Javers getJavers() {
        return JaversBuilder.javers()
                .registerValue(TemporalAccessor.class, new CustomTemporalAcessorComparator())
                .registerValueObject(new ValueObjectDefinition(VirologischerBefundComposition.class, List.of("location", "feederAudit")))
                .registerValueObject(FallidentifikationCluster.class)
                .registerValueObject(BefundObservation.class)
                .registerValueObject(EmpfaengerstandortCluster.class)
                .registerValueObject(new ValueObjectDefinition(BefundJedesEreignisPointEvent.class, List.of("timeValue")))
                .registerValueObject(ProbeCluster.class)
                .registerValueObject(LabortestPanelCluster.class)
                .registerValueObject(ProAnalytCluster.class)
                .registerValueObject(ProAnalytQuantitativesErgebnisElement.class)
                .registerValueObject(ProAnalytErgebnisStatusElement.class)
                .registerValueObject(ProAnalytQuantitativesErgebnisDvQuantity.class)
                .registerValueObject(ProAnalytErgebnisStatusDvCodedText.class)
                .registerValueObject(AnatomischeLokalisationCluster.class)
                .build();
    }


    @Override
    public Exception executeMappingException(String path) throws IOException {
        Bundle bundle = (Bundle) testFileLoader.loadResource(path);
        return assertThrows(Exception.class, () -> {
            new MibiMolekDiagnostikConverter().convert(new MolekDiagBundleConverter().convert(bundle));
        });
    }

    public Exception executeValidatorException(String path) throws IOException {
        Bundle bundle = (Bundle) testFileLoader.loadResource(path);
        return assertThrows(Exception.class, () -> {
            new MolekDiagBundleValidator().validateRequest(bundle, null);
        });
    }

    @Override
    public void testMapping(String resourcePath, String paragonPath) throws IOException {
        Bundle bundle = (Bundle) super.testFileLoader.loadResource(resourcePath);
        MolekDiagBundleConverter molekDiagBundleConverter = new MolekDiagBundleConverter();
        Observation observation = molekDiagBundleConverter.convert(bundle);
        MibiMolekDiagnostikConverter mibiMolekDiagnostikConverter = new MibiMolekDiagnostikConverter();
        VirologischerBefundComposition mapped = mibiMolekDiagnostikConverter.convert(observation);
        Diff diff = compareCompositions(getJavers(), paragonPath, mapped);
        assertEquals(0, diff.getChanges().size());
    }


}

