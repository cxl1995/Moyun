package com.mywl.app.platform.mapper.user.mywl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mywl.app.platform.module.entity.MywlUserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName MywlUser
 * @Description TODO
 * @Author cxl
 * @Date 2023/6/26 下午1:31
 * @Version 1.0
 */
//@Mapper
public interface MywlUserMapper extends BaseMapper<MywlUserEntity> {

  @Select("select mywl_sys_userid,mywl_sys_username,mywl_sys_belongertype,mywl_sys_belongername,mywl_sys_belongerid,mywl_sys_roleid,mywl_sys_description,mywl_sys_uesrinternalid,mywl_sys_gender from oodm_incr_mywl_sys_user")
  List<MywlUserEntity> selectAllPerson();

  @Insert("insert into oodm_incr_mywl_sys_user"+
      "(mywl_sys_userid,mywl_sys_username,mywl_sys_belongertype,mywl_sys_belongername,mywl_sys_belongerid,mywl_sys_roleid,mywl_sys_description,mywl_sys_uesrinternalid,mywl_sys_gender)"+
      "values(#{userId},#{userName},#{belongerType},#{belongerName},#{belongerId},#{roleId},#{description},#{userInternalId},#{gender})")
  int insert(MywlUserEntity mywlUserEntity);

}

