package cn.kiway.kiway_ai.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.kiway.kiway_ai.entity.App;
import cn.kiway.kiway_ai.mapper.AppMapper;
import cn.kiway.kiway_ai.service.AppService;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper,App> implements AppService {
	
}
