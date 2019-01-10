package com.tensquare.controller;

import com.tensquare.client.UserClient;
import com.tensquare.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private UserClient userClient;

    /**
     *  添加好友
     * @param friendid 对方用户ID
     * @param type  1：喜欢 0：不喜欢
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //先确定是否已经登录了,用户才需要添加好友,管理员不需要
        Claims claims = (Claims) request.getAttribute("user_claims");
        if(claims==null){
            //未登录或者权限不足
            return new Result(false, StatusCode.LOGINERROR,"无权访问");
        }
        //如果是喜欢,想要添加好友的话
        String userId = claims.getId();
        if("1".equals(type)){
            int i = friendService.addFriend(userId, friendid);
            if(i==0){
                return new Result(false,StatusCode.ERROR,"已经添加过此好友");
            }
            userClient.updatefanscountandfollowcount(userId,friendid,1);
        }else {
            //不喜欢
            int i = friendService.addNotFriend(userId, friendid);
            if(i==0){
                return new Result(false,StatusCode.ERROR,"已经添加此非好友");
            }
        }
        return new Result(true,StatusCode.OK,"操作成功");
    }

    /**
     * 删除好友
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String friendid){
        //先确定是否已经登录了,用户才需要添加好友,管理员不需要
        Claims claims = (Claims) request.getAttribute("user_claims");
        if(claims==null){
            //未登录或者权限不足
            return new Result(false, StatusCode.LOGINERROR,"无权访问");
        }
        String userid = claims.getId();
        friendService.deleteFriend(userid,friendid);
        userClient.updatefanscountandfollowcount(userid, friendid, -1);
        return new Result(true, StatusCode.OK, "删除成功");
    }
}
