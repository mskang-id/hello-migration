package com.shopmall.dao.impl;

import com.shopmall.dao.MemberDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MemberDaoImpl extends SqlMapClientDaoSupport implements MemberDao {

    @Autowired
    public void init(SqlMapClient sqlMapClient) {
        super.setSqlMapClient(sqlMapClient);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findById(long id) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Member.findById", id);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findByLoginId(String loginId) {
        return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("Member.findByLoginId", loginId);
    }

    public void deductPoint(Map<String, Object> param) {
        getSqlMapClientTemplate().update("Member.deductPoint", param);
    }

    public void earnPoint(Map<String, Object> param) {
        getSqlMapClientTemplate().update("Member.earnPoint", param);
    }

    // selectKey type="post" writes the generated member_id back into the map under "memberId"
    public void insert(Map<String, Object> param) {
        getSqlMapClientTemplate().insert("Member.insert", param);
    }
}
