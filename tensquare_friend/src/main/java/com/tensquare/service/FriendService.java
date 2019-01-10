package com.tensquare.service;

import com.tensquare.dao.FriendDao;
import com.tensquare.dao.NotFriendDao;
import com.tensquare.pojo.Friend;
import com.tensquare.pojo.NotFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NotFriendDao notFriendDao;

    /**
     * 添加好友
     * @param userid 用户id
     * @param friendid 被关注用户id
     * @return
     */
    public int addFriend(String userid,String friendid){
        //已关注
        //如果已经关注了对方用户,即记录数大于0
        if(friendDao.findByuseridANDfriendid(userid,friendid)>0){
            return 0;
        }
        //未关注

        //没有关注的话,就可以直接添加
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        //先判断对方是否也已经关注了,记录数大于0，即关注了
       if(friendDao.findByuseridANDfriendid(friendid,userid)>0){
           friendDao.updateLike("1",friendid,userid);
           friend.setIslike("1");
       }
       friendDao.save(friend);
       return 1;//添加好友成功

    }

    /**
     * 添加非好友
     */
    public int addNotFriend(String userid,String friendid){

        NotFriend notFriend = notFriendDao.findByUseridAndFriendid(userid, friendid);
        if(notFriend!=null){
            return 0;
        }
        notFriend.setUserid(userid);
        notFriend.setFriendid(friendid);
        notFriendDao.save(notFriend);
        return 1;
    }

    /**
     * 删除好友
     */
    public void deleteFriend(String userid,String friendid){
        //删除好友
        friendDao.deleteFriend(userid,friendid);
        //不管对方有没有关注,都可以设为0
        //如果关注了,那么sql语句有效   update tb_friend set islike= ? where userid=? and friendid=?
        //如果没有关注，那么sql语句无效
        friendDao.updateLike(friendid,userid,"0");
        addNotFriend(userid,friendid);
    }
}
