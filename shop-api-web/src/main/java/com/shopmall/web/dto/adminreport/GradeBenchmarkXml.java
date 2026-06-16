package com.shopmall.web.dto.adminreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class GradeBenchmarkXml {
    private Long memberId;
    private String name;
    private String grade;
    private Long totalSpend;
    private Long gradeAvgSpend;
    private String benchmark;

    public GradeBenchmarkXml() {}
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Long getTotalSpend() { return totalSpend; }
    public void setTotalSpend(Long totalSpend) { this.totalSpend = totalSpend; }
    public Long getGradeAvgSpend() { return gradeAvgSpend; }
    public void setGradeAvgSpend(Long gradeAvgSpend) { this.gradeAvgSpend = gradeAvgSpend; }
    public String getBenchmark() { return benchmark; }
    public void setBenchmark(String benchmark) { this.benchmark = benchmark; }
}
