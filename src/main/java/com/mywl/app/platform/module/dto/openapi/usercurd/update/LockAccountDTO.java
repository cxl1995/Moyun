package com.mywl.app.platform.module.dto.openapi.usercurd.update;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @ClassName LockAccountDTO
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/8 下午4:14
 * @Version 1.0
 */
@Data
public class LockAccountDTO {

  /**
   * 是否锁定 1-是，2-否
   */
  private Integer hasLock;

  /**
   * 锁定原因类型 1-管理员锁定
   */
  private Integer lockReason;

  /**
   * 锁定时间
   */
  private Timestamp lockTime;

  /**
   * ids
   */
  private List<Long> personIds;
}

