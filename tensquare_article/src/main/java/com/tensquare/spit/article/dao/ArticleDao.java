package com.tensquare.spit.article.dao;

import com.tensquare.spit.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    @Query(value = "UPDATE tb_article set state = 1 where id = ?",nativeQuery = true)
    @Modifying
    public void updateState(String id );


    @Query(value = "UPDATE tb_article set thumbup=thumbup+1 where id = ?",nativeQuery = true)
    @Modifying
    public void updateThumbup(String id);
}
