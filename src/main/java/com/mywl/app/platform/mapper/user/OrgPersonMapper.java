package com.mywl.app.platform.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mywl.app.platform.module.entity.OrgPersonEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//@Mapper
public interface OrgPersonMapper extends BaseMapper<OrgPersonEntity> {
  //  //增加一个Person
//  @Insert("insert into person(id,name,age)values(#{id},#{name},#{age})")
//  int insert(Person person);
  //删除一个Person
  @Delete("delete from org_person where id = #{id}")
  int deleteByPrimaryKey(Long id);

  //更改一个Person
  @Update("update org_person set user_name = #{userName},phone = #{userName} where id = #{personId}")
  int updateByPrimaryKey(String personId, String userName);

  //查询一个Person
//  @Select("select id,name ,age from person where id = #{id}")
//  OrgPersonEntity selectByPrimaryKey(Integer id);
  //查询所有的Person
  @Select("select id,name,code,valid,user_id from org_person")
  List<OrgPersonEntity> selectAllPerson();

  //查询所有的Person
  @Select("select id,name,code,valid,user_id from org_person where id = #{personId}")
  OrgPersonEntity getUserId(String personId);
}