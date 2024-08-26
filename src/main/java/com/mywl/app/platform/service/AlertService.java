package com.mywl.app.platform.service;

import com.mywl.app.platform.module.entity.AlertCurrentEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlertService {

  List<AlertCurrentEntity> queryAll(String templateId,String instanceId);


}
