package com.arranger.eurekaclient.service;

import com.arranger.eurekaclient.dto.UserDTO;
import com.arranger.eurekaclient.entity.User;


public interface UserService {
    String signUpUser(UserDTO userDTO);

    void enableUser(String email);

    User findUserByEmail(String email);

    UserDTO getUser(User user);

}