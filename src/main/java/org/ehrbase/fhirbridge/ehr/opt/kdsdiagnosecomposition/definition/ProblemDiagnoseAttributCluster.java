package org.ehrbase.fhirbridge.ehr.opt.kdsdiagnosecomposition.definition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import java.util.List;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.LocatableEntity;

@Entity
@Archetype("openEHR-EHR-CLUSTER.problem_qualifier.v2")
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2023-11-20T16:17:09.055523209+01:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.18.0"
)
public class ProblemDiagnoseAttributCluster implements LocatableEntity {
  /**
   * Path: Diagnose/context/Problem/Diagnose Attribut/Diagnoserolle
   * Description: Die Kategorie des Problems oder der Diagnose innerhalb eines spezifischen Behandlungszeitraumes und/oder eines lokalen Behandlungskontextes.
   * Comment: Dieses Datenelement enthält eine Gruppe von Werten, welche üblicherweise in der diagnostischen Kategorisierung benutzt werden. Es ist üblich, dass im episodischen Versorgungskontext (üblicherweise in der Sekundärversorgung) die Diagnosen während der aktuellen Versorgungsepisode nach ihrer Beziehung zur Hauptdiagnose zu kategorisieren/ zu organisieren sind. Diese Kategorien können ebenfalls für die klinische Kodierungs-, Berichts- und Abrechnungszwecke verwendet werden. In manchen Ländern kann die diagnostische Kategorisierung als DRG (diagnosebezogene Fallgruppierungen) bezeichnet werden.
   * Falls es nötig sein sollte, erlaubt das Freitextfeld die Benutzung von lokalen Werten.
   */
  @Path("/items[at0063 and name/value='Diagnoserolle']")
  private List<ProblemDiagnoseAttributDiagnoserolleElement> diagnoserolle;

  /**
   * Path: Diagnose/context/Problem/Diagnose Attribut/feeder_audit
   */
  @Path("/feeder_audit")
  private FeederAudit feederAudit;

  public void setDiagnoserolle(List<ProblemDiagnoseAttributDiagnoserolleElement> diagnoserolle) {
     this.diagnoserolle = diagnoserolle;
  }

  public List<ProblemDiagnoseAttributDiagnoserolleElement> getDiagnoserolle() {
     return this.diagnoserolle ;
  }

  public void setFeederAudit(FeederAudit feederAudit) {
     this.feederAudit = feederAudit;
  }

  public FeederAudit getFeederAudit() {
     return this.feederAudit ;
  }
}
