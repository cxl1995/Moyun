package com.mywl.app.platform.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mywl.app.platform.module.entity.AuthUserEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

//@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUserEntity> {
  //  //增加一个Person
//  @Insert("insert into person(id,name,age)values(#{id},#{name},#{age})")
//  int insert(Person person);
//  //删除一个Person
//  @Delete("delete from person where id = #{id}")
//  int deleteByPrimaryKey(Integer id);
//  //更改一个Person
//  @Update("update person set name =#{name},age=#{age} where id=#{id}")
//  int updateByPrimaryKey(Integer id);
  //查询一个Person
//  @Select("select id,name ,age from person where id = #{id}")
//  OrgPersonEntity selectByPrimaryKey(Integer id);
//  //查询所有的Person

  @Update("update auth_user set user_name =#{userName} where person_id=#{personId}")
  int updateByPrimaryKey(String personId, String userName);

  @Select("select id,user_name,person_id,person_code,person_name valid from auth_user")
  List<AuthUserEntity> selectAllUser();

//  @Update("update supos_dt.auth_user set has_lock = 1,lock_reason = 1,lock_time=#{lock_time} where person_id in ('3437759838968928','3449201935244384');")
//  int lockAccount(String personId, String userName);


  @Update({"<script>",
      "update supos_dt.auth_user set has_lock = #{has_lock},lock_reason = #{lock_reason},lock_time=#{lock_time} where person_id in",
      "<foreach item='item' index='index' collection='personIds'",
      "open='(' separator=',' close=')'>",
      "#{item}",
      "</foreach>",
      "</script>"})
  int lockAccount(Integer has_lock, Integer lock_reason, Timestamp lock_time, List<Long> personIds);
}