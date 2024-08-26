package com.mywl.app.platform.service;

import com.mywl.app.platform.module.entity.AuthUserEntity;
import com.mywl.app.platform.module.entity.MywlUserEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface UserService {

  List<AuthUserEntity> queryAll();

  Integer updateByPrimaryKey(String id, String userName);

  Integer add(MywlUserEntity mywlUserEntity);

  Integer lockAccount(Integer has_lock, Integer lock_reason, Timestamp lock_time, List<Long> personIds);

}
