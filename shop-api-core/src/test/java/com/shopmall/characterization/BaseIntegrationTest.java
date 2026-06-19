package com.shopmall.characterization;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/test-context.xml")
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    protected SqlMapClient sqlMapClient;

    @Autowired
    protected DataSource dataSource;

    protected int queryInt(String sql) throws Exception {
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        int val = rs.getInt(1);
        rs.close();
        st.close();
        return val;
    }

    protected String queryString(String sql) throws Exception {
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        if (!rs.next()) { rs.close(); st.close(); return null; }
        String val = rs.getString(1);
        rs.close();
        st.close();
        return val;
    }
}
