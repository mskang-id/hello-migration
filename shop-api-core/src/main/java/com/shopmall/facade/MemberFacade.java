package com.shopmall.facade;

import com.shopmall.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

// Member lookup. Hits MemberDao directly — the member read path predates MemberService.
@Component
public class MemberFacade {

    @Autowired private MemberDao memberDao;

    public Map<String, Object> getMember(long memberId) {
        return memberDao.findById(memberId);
    }
}
