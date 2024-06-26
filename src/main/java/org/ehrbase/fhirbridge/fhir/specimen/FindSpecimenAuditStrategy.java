/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehrbase.fhirbridge.fhir.specimen;

import org.ehrbase.fhirbridge.fhir.FhirBridgeEventType;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.event.QueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.OperationOutcomeOperations;

/**
 * Implementation of {@link org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy AuditStrategy}
 * for 'Find Observation' transaction.
 *
 * @since 1.0.0
 */
public class FindSpecimenAuditStrategy extends FhirQueryAuditStrategy {

    public FindSpecimenAuditStrategy() {
        super(true, OperationOutcomeOperations.INSTANCE);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirQueryAuditDataset auditDataset) {
        return new QueryInformationBuilder<>(auditContext, auditDataset, FhirBridgeEventType.FIND_SPECIMEN)
                .addPatients(auditDataset.getPatientIds())
                .getMessages();
    }
}
