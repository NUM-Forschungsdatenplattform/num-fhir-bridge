package org.ehrbase.fhirbridge.fhir.observation;

import org.ehrbase.fhirbridge.comparators.CustomTemporalAcessorComparator;
import org.ehrbase.fhirbridge.ehr.converter.specific.mibikultur.MibiBefundConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.mibikultur.MibiKulturNachweisConverter;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.MikrobiologischerBefundComposition;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.AnatomischeLokalisationCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.AntibiogrammCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.BefundObservation;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.EinsenderstandortCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.EmpfaengerstandortCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.ErregerdetailsCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.FallidentifikationCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.KulturCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.ProAntibiotikumCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.ProErregerCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.ProErregerZugehoerigeLaborprobeDvUri;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.ProbeCluster;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.ProbeZeitpunktDerProbenentnahmeDvDateTime;
import org.ehrbase.fhirbridge.fhir.AbstractMappingTestSetupIT;
import org.ehrbase.fhirbridge.fhir.observation.validator.MibiKulturValidator;
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

public class MikrobiologischerBefundIT  extends AbstractMappingTestSetupIT {

    public MikrobiologischerBefundIT() {
        super("Observation/MikrobiologischerBefund/", Observation.class); //fhir-Resource
    }

    @Test
    void createMibi() throws IOException {
        create("create-mibi.json");
    }

    @Test
    void createMIbiMapping() throws IOException {
        testMapping("create-mibi.json", "paragon-create-mibi.json");
    }

    @Test
    void createMREVREMapping() throws IOException {
        testMapping("create-mibi-mre-MVRE.json", "paragon-create-mibi-mre-MVRE.json");
    }

    @Test
    void createVREMapping() throws IOException {
        testMapping("create-mibi-mre-VRE.json", "paragon-create-mibi-mre-VRE.json");
    }

    @Test
    void createMrgnMapping() throws IOException {
        testMapping("create-mibi-mrgn.json", "paragon-create-mibi-mrgn.json");
    }
    @Test
    void createOhneHemmkonzentrationMapping() throws IOException {
        testMapping("create-mibi-ohneminimal-hemm.json", "paragon-create-mibi-ohneminimal-hemm.json");
    }

    @Test
    void createProbeMapping() throws IOException {
        testMapping("create-mibi-probe.json", "paragon-create-mibi-probe.json");
    }

    @Test
    void createMibiCLSI() throws IOException {
        testMapping("create-mibi-CLSI.json", "paragon-create-mibi-CLSI.json");
    }


    @Test
    void invalidComponentValueMissing() throws IOException {
        Exception exception = executeValidatorException("kultur-component-value-missing.json");
        assertEquals("Kultur component has to include a valueCodeableConcept!", exception.getMessage());
    }
    @Test
    void invalidComponentMissing() throws IOException {
        Exception exception = executeValidatorException("kultur-missing-component.json");
        assertEquals("Kultur has to include a component!", exception.getMessage());
    }

    @Test
    void invalidMultipleComponent() throws IOException {
        Exception exception = executeValidatorException("kultur-multiple-component.json");
        assertEquals("Kultur has to include only one component not multiple", exception.getMessage());
    }

    @Test
    void invalidMissingValuecodable() throws IOException {
        Exception exception = executeValidatorException("missing-valuecodable.json");
        assertEquals("Kultur has to include a ValueCodeableConcept with the Outcome of the Test!", exception.getMessage());
    }


    @Test
    void invalidNotDetectedKultur() throws IOException {
        Exception exception = executeValidatorException("not-detected-kultur.json");
        assertEquals("Only detected Kulturen are allowed!", exception.getMessage());
    }

    @Test
    void invalidStatus() throws IOException {
        Exception exception = executeValidatorException("wrong-status-kultur.json");
        assertEquals("For Status in Kultur only final is allowed", exception.getMessage());
    }

    @Test
    void invalidContainedMissing() throws IOException {
        Exception exception = executeValidatorException("no-contained.json");
        assertEquals("No contained resources provided, MRE/MRGN and Empfindlichkeit are missing.", exception.getMessage());
    }

    @Test
    void invalidMREAndMRGN() throws IOException {
        Exception exception = executeValidatorException("both-MRE-and-MRGN.json");
        assertEquals("Both MRGN and MRE are provided, has to be either one of those not both.", exception.getMessage());
    }

    @Test
    void invalidMREMRGNMissing() throws IOException {
        Exception exception = executeValidatorException("missing-mrgn-mre.json");
        assertEquals("Either MRE or MRGN is missing from the contained of Kultur", exception.getMessage());
    }

    @Test
    void invalidMissingEmpfindlichkeit() throws IOException {
        Exception exception = executeValidatorException("missing-empfindlichkeit.json");
        assertEquals("Empfindlichkeit is missing from the contained of Kultur.", exception.getMessage());
    }

    @Test
    void invalidMissingMRGNValue() throws IOException {
        Exception exception = executeValidatorException("mrgn-value-missing.json");
        assertEquals("Missing valueCodeableConcept in MRGN", exception.getMessage());
    }

    @Test
    void invalidUnsupportedMRECode() throws IOException {
        Exception exception = executeValidatorException("unsupported-mre.json");
        assertEquals("MRE contains coding that is not supported, has to be either SNOMED: 115329001 (Methicillin resistant Staphylococcus aureus ) or 113727004 (Vancomycin resistant Enterococcus ).", exception.getMessage());
    }

    @Test
    void invalidMRECodeMissing() throws IOException {
        Exception exception = executeValidatorException("missing-value-mre.json");
        assertEquals("Missing valueCodeableConcept in MRE", exception.getMessage());
    }

    @Test
    void invalidEmpfindlichkeitStatus() throws IOException {
        Exception exception = executeValidatorException("invalid-empfindlichkeit-status.json");
        assertEquals("For status of Empfindlichkeit only final is allowed", exception.getMessage());
    }

    @Test
    void invalidInterpretationMissingEmpfindlichkeit() throws IOException {
        Exception exception = executeValidatorException("interpretation-missing-empfindlichkeit.json");
        assertEquals("Interpretation missing in Empfindlichkeit", exception.getMessage());
    }

    @Test
    void invalidUnsupportedCLSI() throws IOException {
        Exception exception = executeValidatorException("unsupported-CLSI.json");
        assertEquals("SDD and NS are not supported as codes for EUCAST!", exception.getMessage());
    }

    @Test
    void invalidUnsupportedCLSI2() throws IOException {
        Exception exception = executeValidatorException("unsupported-CLSI2.json");
        assertEquals("SDD and NS are not supported as codes for EUCAST!", exception.getMessage());
    }
    @Test
    void invalidCollectedMissing() throws IOException {
        Exception exception = executeValidatorException("missing-collection.json");
        assertEquals("Specimen is missing collection", exception.getMessage());
    }

    @Test
    void invalidMissingCollected() throws IOException {
        Exception exception = executeValidatorException("missing-collected.json");
        assertEquals("Specimen is missing collection collected time.", exception.getMessage());
    }

    @Test
    void invalidUnsupportedCodingProbe() throws IOException {
        Exception exception = executeValidatorException("probe-unsupported-coding.json");
        assertEquals("This type coding is not supported for specimen, check implementation guide.", exception.getMessage());
    }

    @Test
    void invalidTypeIsMissingProbe() throws IOException {
        Exception exception = executeValidatorException("missing-probe-type.json");
        assertEquals("Type is missing from specimen.", exception.getMessage());
    }


    @Override
    public Javers getJavers() {
        return JaversBuilder.javers()
                .registerValue(TemporalAccessor.class, new CustomTemporalAcessorComparator())
                .registerValueObject(new ValueObjectDefinition(MikrobiologischerBefundComposition.class, List.of("location", "feederAudit")))
                .registerValueObject(FallidentifikationCluster.class)
                .registerValueObject(BefundObservation.class)
                .registerValueObject(EmpfaengerstandortCluster.class)
                .registerValueObject(EinsenderstandortCluster.class)
                .registerValueObject(ProbeCluster.class)
                .registerValueObject(KulturCluster.class)
                .registerValueObject(AnatomischeLokalisationCluster.class)
                .registerValueObject(ProbeZeitpunktDerProbenentnahmeDvDateTime.class)
                .registerValueObject(ProErregerCluster.class)
                .registerValueObject(ErregerdetailsCluster.class)
                .registerValueObject(ProErregerZugehoerigeLaborprobeDvUri.class)
                .registerValueObject(AntibiogrammCluster.class)
                .registerValueObject(ProAntibiotikumCluster.class)
                .build();
    }

    @Override
    public Exception executeMappingException(String path) throws IOException {
        Observation observation = (Observation) testFileLoader.loadResource(path);
        return assertThrows(Exception.class, () ->
                new MibiBefundConverter().convert((observation))
        );
    }


    public Exception executeValidatorException(String path) throws IOException {
        Observation observation = (Observation) testFileLoader.loadResource(path);
        return assertThrows(Exception.class, () -> {
            new MibiKulturValidator().validateRequest(observation, null);
        });
    }

    @Override
    public void testMapping(String resourcePath, String paragonPath) throws IOException {
        Observation observation = (Observation)  super.testFileLoader.loadResource(resourcePath);
        MibiKulturNachweisConverter mibiKulturNachweisConverter = new MibiKulturNachweisConverter();
        MikrobiologischerBefundComposition mapped = mibiKulturNachweisConverter.convert(observation);
        Diff diff = compareCompositions(getJavers(), paragonPath, mapped);
        assertEquals(0, diff.getChanges().size());
    }
}
