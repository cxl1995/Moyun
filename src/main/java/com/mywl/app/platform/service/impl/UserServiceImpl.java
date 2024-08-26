package com.mywl.app.platform.service.impl;


import com.mywl.app.platform.mapper.user.mywl.MywlUserMapper;
import com.mywl.app.platform.mapper.user.AuthUserMapper;
import com.mywl.app.platform.module.entity.AuthUserEntity;
import com.mywl.app.platform.module.entity.MywlUserEntity;
import com.mywl.app.platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
public class UserServiceImpl implements UserService {

  @Autowired
  private AuthUserMapper authUserMapper;

  @Autowired
  private MywlUserMapper mywlUserMapper;

  @Override
  public List<AuthUserEntity> queryAll() {
    List<AuthUserEntity> authUserEntities = authUserMapper.selectAllUser();
    return authUserEntities;
  }

  @Override
  public Integer updateByPrimaryKey(String personId, String userName) {
    int i = authUserMapper.updateByPrimaryKey(personId, userName);
    return i;
  }

  @Override
  public Integer add(MywlUserEntity mywlUserEntity) {
    int insert = mywlUserMapper.insert(mywlUserEntity);
    return insert;
  }

  @Override
  public Integer lockAccount(Integer has_lock, Integer lock_reason, Timestamp lock_time, List<Long> personIds) {
    int insert = authUserMapper.lockAccount(has_lock, lock_reason, lock_time, personIds);
    return insert;
  }

}

