package com.mywl.app.platform.service.impl;


import com.mywl.app.platform.mapper.user.AlertCurrentMapper;
import com.mywl.app.platform.module.entity.AlertCurrentEntity;
import com.mywl.app.platform.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/7 下午3:26
 * @Version 1.0
 */
@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

  @Autowired
  private AlertCurrentMapper alertCurrentMapper;


  @Override
  public List<AlertCurrentEntity> queryAll(String templateId, String instanceId) {
    List<AlertCurrentEntity> alertCurrentEntities = alertCurrentMapper.selectAll(templateId, instanceId);
    return alertCurrentEntities;
  }


}

