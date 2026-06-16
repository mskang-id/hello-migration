package com.shopmall.common.envelope;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"resultCode", "resultMessage", "serverDateTime"})
public class ApiHeader {
    private String resultCode;
    private String resultMessage;
    private String serverDateTime; // yyyyMMddHHmmss (VARCHAR-style timestamp, legacy)

    public ApiHeader() {}
    public ApiHeader(String resultCode, String resultMessage, String serverDateTime) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.serverDateTime = serverDateTime;
    }
    public String getResultCode() { return resultCode; }
    public void setResultCode(String resultCode) { this.resultCode = resultCode; }
    public String getResultMessage() { return resultMessage; }
    public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }
    public String getServerDateTime() { return serverDateTime; }
    public void setServerDateTime(String serverDateTime) { this.serverDateTime = serverDateTime; }
}
