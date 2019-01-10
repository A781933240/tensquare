package com.tensquare.dao;

import com.tensquare.pojo.NotFriend;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotFriendDao extends JpaRepository<NotFriend,String> {

    public NotFriend findByUseridAndFriendid(String userid, String friendid);



}
