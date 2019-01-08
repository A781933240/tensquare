package com.tensquare.spit.qa.dao;

import com.tensquare.spit.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query(value = "SELECT * FROM tb_problem , tb_pl where id=problemid and labelid=? ORDER BY replytime DESC",nativeQuery = true)
    public Page<Problem> newlist(String labelId, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem , tb_pl where id=problemid and labelid=? ORDER BY reply DESC",nativeQuery = true)
    public Page<Problem> hostlist(String labelId, Pageable pageable);

    @Query(value = "SELECT * FROM tb_problem , tb_pl where id=problemid and labelid=? and  reply =0 ORDER BY createtime DESC",nativeQuery = true)
    public Page<Problem> waitlist(String labelId, Pageable pageable);
}
