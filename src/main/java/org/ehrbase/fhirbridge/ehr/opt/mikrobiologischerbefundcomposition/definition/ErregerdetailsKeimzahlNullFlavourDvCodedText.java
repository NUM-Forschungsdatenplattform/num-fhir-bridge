package org.ehrbase.fhirbridge.ehr.opt.mikrobiologischerbefundcomposition.definition;

import javax.annotation.processing.Generated;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.OptionFor;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.classgenerator.interfaces.RMEntity;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;

@Entity
@Generated(
    value = "org.ehrbase.client.classgenerator.ClassGenerator",
    date = "2024-02-20T15:55:44.394536321+01:00",
    comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.25.0"
)
@OptionFor("DV_CODED_TEXT")
public class ErregerdetailsKeimzahlNullFlavourDvCodedText implements RMEntity, ErregerdetailsKeimzahlNullFlavourChoice {
  /**
   * Path: Mikrobiologischer Befund/Befund/Event Series/Jedes Ereignis/Tree/Kultur/Pro Erreger/Erregerdetails/Keimzahl/Keimzahl/null_flavour/null_flavour
   * Description: Quantitative Angabe zur Keimzahl, z.B. bei Urinen
   */
  @Path("|defining_code")
  private NullFlavour keimzahlNullFlavourDefiningCode;

  public void setKeimzahlNullFlavourDefiningCode(NullFlavour keimzahlNullFlavourDefiningCode) {
     this.keimzahlNullFlavourDefiningCode = keimzahlNullFlavourDefiningCode;
  }

  public NullFlavour getKeimzahlNullFlavourDefiningCode() {
     return this.keimzahlNullFlavourDefiningCode ;
  }
}
