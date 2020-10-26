package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.ProviderMapper;
import com.domain.Provider;
import com.service.ProviderService;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements ProviderService {
}
