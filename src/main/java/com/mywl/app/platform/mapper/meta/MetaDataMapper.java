package com.mywl.app.platform.mapper.meta;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mywl.app.platform.module.entity.MetaDataEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 联锁投用率参数表 Mapper 接口
 * </p>
 *
 * @author Shirman
 * @since 2020-04-20
 */
@Mapper
public interface MetaDataMapper extends BaseMapper<MetaDataEntity> {

//  @Insert("<script>" +
//      "INSERT INTO meta_data (meta_id, meta_inst_name, meta_tag_name, create_time) VALUES" +
//      "<foreach collection='MetaDataEntityList' item='i'   separator=','> " +
//      "(#{i.metaId},#{i.metaInstName},#{i.metaTagName},#{i.createTime})" +
//      "</foreach> " +
//      "</script>")
//  int insertData(List<MetaDataEntity> MetaDataEntityList);

  @Delete("delete from meta_data where meta_full_name = #{metaFullName}")
  int deleteByFullName(String metaFullName);

  @Select("select * from meta_data where meta_full_name = #{metaFullName}")
  MetaDataEntity queryByMetaFullName(String metaFullName);

  @Select("select * from meta_data where meta_inst_name = #{metaInstName} and meta_tag_name = #{metaTagName}")
  List<MetaDataEntity> queryByMetaInstAndTag(String metaInstName, String metaTagName);

  @Select("select * from meta_data")
  List<MetaDataEntity> queryAll();


  @Update("<script>" +
      "update card_user set deleted = #{timestamp} where card_id = #{cardId} and  deleted = 0" +
      "</script>")
  int logicDelByCardId(String cardId, Long timestamp);


  @Update("<script>" +
      "update card_user set deleted = #{timestamp} where user_id = #{userId} and  deleted = 0" +
      "</script>")
  int logicDelByUserId(String userId, Long timestamp);

  @Update("<script>" +
      "update card_user set deleted = #{timestamp} where platform_user_id = #{platformUserId} and  deleted = 0" +
      "</script>")
  int logicDelByPlatformUserId(String platformUserId, Long timestamp);

}
