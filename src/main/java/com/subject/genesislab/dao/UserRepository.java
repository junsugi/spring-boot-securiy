package com.subject.genesislab.dao;

import com.subject.genesislab.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUserNameAndUserPw(String userName, String UserPw);
    void deleteUserByEmail(String email);

    @Query("select u.email, u.userName, u.signDate, u.phoneNum from User u where u.signDate between ?1 and ?2")
    List<Object[]> findUserBySignDateBetween(String startDate, String endDate);
}
