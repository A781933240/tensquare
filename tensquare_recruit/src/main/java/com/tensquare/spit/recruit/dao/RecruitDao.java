package com.tensquare.spit.recruit.dao;

import com.tensquare.spit.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    //查询推荐职位，通过state=2，排序创建时间
    public List<Recruit> findByStateOrderByCreatetimeDesc(String state);//推荐职位的state编号是2

    //查询最新职位，通过state!=0，排序创建时间
    public List<Recruit> findByStateNotOrderByCreatetimeDesc(String state);
}
