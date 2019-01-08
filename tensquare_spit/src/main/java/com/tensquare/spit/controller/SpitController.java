package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    //增
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK,"保存吐槽成功");
    }

    //删
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId){
        spitService.delete(spitId);
        return new Result(true,StatusCode.OK,"删除吐槽成功");
    }
    //改
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);
        spitService.updata(spit);
        return new Result(true,StatusCode.OK,"修改吐槽成功");
    }

    //查询所有
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Spit> spitList = spitService.findAll();
        return new Result(true,StatusCode.OK,"查询所有成功",spitList);
    }

    //根据id查询
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        Spit spit = spitService.findById(spitId);
        return new Result(true,StatusCode.OK,"根据id查询成功",spit);
    }

    //根据上级ID查询吐槽列表
    @RequestMapping(value = "/comment/{parentId}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentId(@PathVariable String parentId,@PathVariable int page,@PathVariable int size){
        Page<Spit> spitPage = spitService.findByParentid(parentId, page, size);
        return new Result(true,StatusCode.OK,"据上级ID查询吐槽列表成功",new PageResult<Spit>(spitPage.getTotalElements(),spitPage.getContent()));
    }

    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String spitId){

        spitService.updateThumbup(spitId);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
