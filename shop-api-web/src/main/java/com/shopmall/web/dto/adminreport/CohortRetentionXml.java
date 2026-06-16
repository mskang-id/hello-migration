package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class CohortRetentionXml {
    private String cohortMonth;
    private Long cohortSize;
    private Long retainedCnt;
    private Long retentionPct;

    public CohortRetentionXml() {}
    public String getCohortMonth() { return cohortMonth; }
    public void setCohortMonth(String cohortMonth) { this.cohortMonth = cohortMonth; }
    public Long getCohortSize() { return cohortSize; }
    public void setCohortSize(Long cohortSize) { this.cohortSize = cohortSize; }
    public Long getRetainedCnt() { return retainedCnt; }
    public void setRetainedCnt(Long retainedCnt) { this.retainedCnt = retainedCnt; }
    public Long getRetentionPct() { return retentionPct; }
    public void setRetentionPct(Long retentionPct) { this.retentionPct = retentionPct; }
}
