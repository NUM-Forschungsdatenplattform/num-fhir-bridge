package org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition;

import java.lang.Boolean;
import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2024-02-20T15:55:44.387667795+01:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.25.0"
)
@OptionFor("DV_BOOLEAN")
public class ProAntibiotikumTestmethodeDvBoolean implements RMEntity, ProAntibiotikumTestmethodeChoice {
  /**
   * Path: Mikrobiologischer Befund/Befund/Jedes Ereignis/Kultur/Pro Erreger/Erregerdetails/Antibiogramm/Pro Antibiotikum/Testmethode/Testmethode
   * Description: Die Beschreibung der Methode, mit der der Test nur für diesen Analyten durchgeführt wurde.
   */
  @Path("|value")
  private Boolean testmethodeValue;

  public void setTestmethodeValue(Boolean testmethodeValue) {
     this.testmethodeValue = testmethodeValue;
  }

  public Boolean isTestmethodeValue() {
     return this.testmethodeValue ;
  }
}