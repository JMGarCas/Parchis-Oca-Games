package com.ling1.springmvc.friend;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ling1.springmvc.user.User;

@Repository
public interface FriendRepository extends CrudRepository<Friend, Integer>{
    List<Friend> findAll();
    @Query("SELECT friend FROM Friend friend WHERE friend.User1 = ?1 OR friend.User2 = ?1")
    List<Friend> findMyFriends(User user) throws DataAccessException;
    @Query("SELECT friend FROM Friend friend WHERE (friend.User1 = ?1 AND friend.User2 = ?2) OR (friend.User1 = ?2 AND friend.User2 = ?1)")
    Friend findFriendship(User user1, User user2) throws DataAccessException;
}
