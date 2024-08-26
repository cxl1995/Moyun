package com.mywl.app.platform.mapper.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mywl.app.platform.module.entity.AlertCurrentEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface AlertCurrentMapper extends BaseMapper<AlertCurrentEntity> {

  @Select("select record_id,description,start_datatimestamp,source,source_property_name,source_prop_show_name,alert_type,new_value from supos_dt.supngin_oodm_alert_current where status = 1")
  List<AlertCurrentEntity> selectAllOld(String templateId, String instanceId);

  @Select({
      "<script>"
          + "select record_id,description,start_datatimestamp,source,source_property_name,source_prop_show_name,alert_type,new_value "
          + " from supos_dt.supngin_oodm_alert_current "
          + " where status = 1 "
          + " <if test=\"param1 != null\">AND template_id=#{param1}</if> "
          + " <if test=\"param2 != null\">AND instance_id=#{param2}</if> "
          + " </script>"
  })
  List<AlertCurrentEntity> selectAll(@Param("param1") String templateId, @Param("param2") String instanceId);
}