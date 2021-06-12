package com.yaskovskyi.task4.repository;

import com.yaskovskyi.task4.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    int changeStatusById(@Param("id") int id, @Param("status") String s);

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    public User findByName(String name);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    int deleteByIds(@Param("id") int id);

    @Modifying
    @Query("UPDATE User u SET u.lastLoginDate = :time WHERE u.email = :user")
    int updateLoginDate(String user, String time);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.email = :email")
    int changeStatusByName(@Param("email") String name, @Param("status") String s);
}
