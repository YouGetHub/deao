package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.Log;
import com.mapper.LogMapper;
import com.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 */
@Service
@Transactional
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService{
}
