package com.shopmall.common.envelope;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiResponse {

    @XmlElement(name = "header")
    private ApiHeader header;

    // lax=true: JAXB resolves the body element to whatever @XmlRootElement DTO the
    // single JAXBContext knows (the web domain DTOs), without a compile-time reference
    // from common -> web. This keeps the module dependency single-directional.
    @XmlAnyElement(lax = true)
    private Object body;

    public ApiResponse() {}
    public ApiResponse(ApiHeader header, Object body) {
        this.header = header;
        this.body = body;
    }
    public ApiHeader getHeader() { return header; }
    public void setHeader(ApiHeader header) { this.header = header; }
    public Object getBody() { return body; }
    public void setBody(Object body) { this.body = body; }
}
