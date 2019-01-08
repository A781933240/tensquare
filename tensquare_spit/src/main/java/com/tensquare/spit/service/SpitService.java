package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    //增
    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //如果存在上级id
        if(spit.getParentid()!=null && !"".equals(spit.getParentid())){
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update=new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }

    //删
    public void delete(String id){
        spitDao.deleteById(id);
    }

    //改
    public void updata(Spit spit){
        spitDao.save(spit);
    }

    //查询所有
    public List<Spit> findAll(){
        return spitDao.findAll();
    }


    //根据id查询
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 根据上级ID查询吐槽列表
     * @param parentId
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentId,int page , int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentId,pageable);
    }

    public void updateThumbup(String id){
        //方式一，两次操作数据库，效率不高
        //Spit spit = spitDao.findById(id).get();
        //spit.setThumbup((spit.getThumbup()==null ? 0 : spit.getThumbup())+1);
        //spitDao.save(spit);

        //方式二,使用mongoBD原生的命令自增
        Query quert=new Query();
        quert.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thump",1);
        mongoTemplate.updateFirst(quert,update,"spit");

    }

}
