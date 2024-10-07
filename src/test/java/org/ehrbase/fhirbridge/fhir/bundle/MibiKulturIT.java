package org.ehrbase.fhirbridge.fhir.bundle;

import org.ehrbase.fhirbridge.comparators.CustomTemporalAcessorComparator;
import org.ehrbase.fhirbridge.ehr.converter.specific.mibikultur.MibiKulturNachweisConverter;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.MikrobiologischerBefundComposition;
import org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition.*;
import org.ehrbase.fhirbridge.fhir.AbstractBundleMappingTestSetupIT;
import org.ehrbase.fhirbridge.fhir.bundle.converter.MibiKulturBundleConverter;
import org.ehrbase.fhirbridge.fhir.bundle.validator.MibiKulturBundleValidator;
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
class MibiKulturIT extends AbstractBundleMappingTestSetupIT {


    //Just to test Bundle transformation the mapping tests for Observations are located in Observation/
    public MibiKulturIT() {
        super("Bundle/MibiKultur/", Bundle.class);
    }

    @Test
    void createMRE() throws IOException {
        create("create-mre.json");
    }

    @Test
    void createMRGN() throws IOException {
        create("create-mrgn.json");
    }

    @Test
    void createMREMapping() throws IOException {
        testMapping("create-mre.json", "paragon-create-mre.json");
    }

    @Test
    void createMRGNMapping() throws IOException {
        testMapping("create-mrgn.json", "paragon-create-mrgn.json");
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
        Bundle bundle = (Bundle) testFileLoader.loadResource(path);
        return assertThrows(Exception.class, () -> {
            new MibiKulturNachweisConverter().convert(new MibiKulturBundleConverter().convert(bundle));
        });
    }

    @Override
    public void testMapping(String resourcePath, String paragonPath) throws IOException {
        Bundle bundle = (Bundle) super.testFileLoader.loadResource(resourcePath);
        MibiKulturBundleConverter mibiKulturBundleConverter = new MibiKulturBundleConverter();
        Observation observation = mibiKulturBundleConverter.convert(bundle);
        MibiKulturNachweisConverter mibiKulturNachweisConverter = new MibiKulturNachweisConverter();
        MikrobiologischerBefundComposition mapped = mibiKulturNachweisConverter.convert(observation);
        Diff diff = compareCompositions(getJavers(), paragonPath, mapped);
        assertEquals(diff.getChanges().size(), 0);
    }

    public Exception executeValidatorException(String path) throws IOException {
        Bundle bundle = (Bundle) testFileLoader.loadResource(path);
        return assertThrows(Exception.class, () -> {
            new MibiKulturBundleValidator().validateRequest(bundle, null);
        });
    }

}

