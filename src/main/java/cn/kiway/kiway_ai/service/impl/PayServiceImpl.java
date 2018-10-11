package cn.kiway.kiway_ai.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.kiway.kiway_ai.entity.Pay;
import cn.kiway.kiway_ai.mapper.PayMapper;
import cn.kiway.kiway_ai.service.PayService;

/**
 * Created by ym on Fri Jul 06 11:17:03 CST 2018
 */
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper,Pay> implements PayService {
	
}
