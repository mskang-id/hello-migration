package com.shopmall.dao;

import java.util.Map;

public interface MemberDao {
    Map<String, Object> findById(long id);
    Map<String, Object> findByLoginId(String loginId);
    void deductPoint(Map<String, Object> param);
    void earnPoint(Map<String, Object> param);
    void insert(Map<String, Object> param);
}
