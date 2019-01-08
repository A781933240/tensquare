package com.tensquare.spit.base.controller;

import com.tensquare.spit.base.pojo.Label;
import com.tensquare.spit.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/label")
@CrossOrigin //处理跨域问题的注解
public class LabelController {

    @Autowired
    private LabelService labelService;

    //增
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK,"标签保存成功");
    }

    //删
    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"标签删除成功");
    }

    //改
    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String labelId,@RequestBody Label label){
        labelService.updateById(labelId,label);
        return new Result(true, StatusCode.OK,"标签更新成功");
    }

    //查询所有
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK,"查询所有标签成功",labelList);
    }

    //根据id查询
    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId){
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK,"根据id查询标签成功",label);
    }

    //根据条件查询
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findByCondition(@RequestBody Label label){
        List<Label> labelList =labelService.findByCondition(label);
        return new Result(true,StatusCode.OK,"条件查询标签成功",labelList);
    }

    //根据条件分页查询
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findPageByCondition(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pageTable =labelService.findPageByCondition(label,page,size);
        return new Result(true,StatusCode.OK,"条件查询标签成功",new PageResult<Label>(pageTable.getTotalElements(),pageTable.getContent()));
    }
}
