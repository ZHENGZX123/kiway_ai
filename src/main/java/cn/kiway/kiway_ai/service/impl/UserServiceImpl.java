package cn.kiway.kiway_ai.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.kiway.kiway_ai.entity.User;
import cn.kiway.kiway_ai.mapper.UserMapper;
import cn.kiway.kiway_ai.service.UserService;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
	
}
