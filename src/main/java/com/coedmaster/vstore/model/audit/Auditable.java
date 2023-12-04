package com.coedmaster.vstore.model.audit;

public interface Auditable {
	
	AuditSection getAuditSection();

	void setAuditSection(AuditSection audit);
	
}
