package com.tensquare.dao;

import com.tensquare.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface FriendDao extends JpaRepository<Friend,String> {

    /**
     * 根据用户id 和 被关注用户id 查询记录的个数
     */
    @Query(value = "select count(*) from tb_friend  where userid=? and friendid=?",nativeQuery = true)
    public int findByuseridANDfriendid(String userid,String firendid);

    /**
     * 变更是否互相喜欢喜欢
     */
    @Modifying
    @Query(value = "update tb_friend set islike=? where userid=? and friendid=?",nativeQuery = true)
    public void updateLike(String islike,String userid,String friendid);

    /**
     * 删除好友
     */
    @Modifying
    @Query(value = "delete from tb_friend where userid=? and friendid=?",nativeQuery = true)
    public void deleteFriend(String userid,String friendid);


}
