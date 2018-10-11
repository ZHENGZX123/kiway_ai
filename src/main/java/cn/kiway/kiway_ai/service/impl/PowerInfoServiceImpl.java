package cn.kiway.kiway_ai.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.kiway.kiway_ai.entity.Powerinfo;
import cn.kiway.kiway_ai.mapper.PowerInfoMapper;
import cn.kiway.kiway_ai.service.PowerInfoService;

@Service
public class PowerInfoServiceImpl extends ServiceImpl<PowerInfoMapper,Powerinfo> implements PowerInfoService {

}
