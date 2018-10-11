package cn.kiway.kiway_ai.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.kiway.kiway_ai.entity.Robot;
import cn.kiway.kiway_ai.mapper.RobotMapper;
import cn.kiway.kiway_ai.service.RobotService;
@Service
public class RobotServiceImpl extends ServiceImpl<RobotMapper,Robot> implements RobotService{

}
