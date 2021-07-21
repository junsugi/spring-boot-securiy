package com.subject.genesislab.service;

import com.subject.genesislab.dao.User;
import com.subject.genesislab.dao.UserRepository;
import com.subject.genesislab.dao.UserRole;
import com.subject.genesislab.dao.UserRoleRepository;
import com.subject.genesislab.dto.UserDto;
import com.subject.genesislab.jwt.TokenProvider;
import javassist.bytecode.DuplicateMemberException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder, TokenProvider tokenProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public User loginUser(UserDto userDto){
        Optional<User> userOpt = userRepository.findUserByUserNameAndUserPw(userDto.getUserName(), userDto.getUserPw());

        return userOpt.orElseGet(null);
    }

    @Transactional
    public void signUp(UserDto userDto) throws DuplicateMemberException {
        if(userRepository.findUserByEmail(userDto.getEmail()).isPresent()){
            logger.error("이미 가입한 사용자 입니다. '{}'", userDto.getEmail());
            throw new DuplicateMemberException("이미 가입한 사용자 입니다.");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String signDate = format.format(new Date());

        UserRole userRole = UserRole.builder()
                                    .roleName("ROLE_USER")
                                    .build();

        User user = User.builder()
                        .userPw(passwordEncoder.encode(userDto.getUserPw()))
                        .userName(userDto.getUserName())
                        .phoneNum(userDto.getPhoneNum())
                        .email(userDto.getEmail())
                        .authorities(Collections.singleton(userRole))
                        .signDate(signDate)
                        .build();

        userRepository.save(user);
    }

    @Transactional
    public User findUserByAccessToken(Cookie cookie) {
        String token = cookie.getValue();
        Authentication authentication = tokenProvider.getAuthentication(token);
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findUserByEmail(email);

        return userOpt.orElseGet(null);
    }

    @Transactional
    public void updateProfile(UserDto userDto) throws NoSuchElementException{
        Optional<User> userOpt = userRepository.findUserByEmail(userDto.getEmail());
        if(userOpt.isPresent()){
            User dbUser = userOpt.get();
            User user = User.builder()
                            .userName(userDto.getUserName())
                            .userPw(userDto.getUserPw().equals("") ? dbUser.getUserPw() : passwordEncoder.encode(userDto.getUserPw()))
                            .email(userDto.getEmail())
                            .signDate(dbUser.getSignDate())
                            .phoneNum(userDto.getPhoneNum())
                            .build();

            userRepository.save(user);
        } else {
            throw new NoSuchElementException("존재하는 사용자가 없습니다.");
        }
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteUserByEmail(email);
    }

    @Transactional
    public List<Object[]> findUserByDate(String startDate, String endDate) {
        List<Object[]> userList = userRepository.findUserBySignDateBetween(startDate, endDate);

        return userList;
    }
}
