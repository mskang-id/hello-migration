package com.shopmall.web;

import com.shopmall.common.envelope.ApiResponse;
import com.shopmall.common.error.ErrorCode;
import com.shopmall.facade.MemberFacade;
import com.shopmall.web.dto.member.MemberXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/api/members")
public class MemberController {

    @Autowired private MemberFacade memberFacade;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse detail(@PathVariable("id") long id) {
        Map<String, Object> m = memberFacade.getMember(id);
        if (m == null) {
            return ResponseFactory.fail(ErrorCode.NOT_FOUND, "member not found", null);
        }
        MemberXml xml = new MemberXml();
        xml.setMemberId(((Number) m.get("MEMBER_ID")).longValue());
        xml.setLoginId(String.valueOf(m.get("LOGIN_ID")));
        xml.setName(m.get("NAME") == null ? null : String.valueOf(m.get("NAME")));
        xml.setGrade(String.valueOf(m.get("GRADE")));
        xml.setPoint(((Number) m.get("POINT")).intValue());
        return ResponseFactory.ok(xml);
    }
}
