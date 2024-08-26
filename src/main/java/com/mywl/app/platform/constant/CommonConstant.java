package com.mywl.app.platform.constant;

/**
 * @author cxl
 * @date 2023/3/25 18:35
 */
public class CommonConstant {

  public static final String TOPIC_MONITOR_DATA = "Topic/flexem/fbox/301322070904/system/MonitorData";

  public static final String DEFAULT_USER_SYSTEM = "system";

  /**
   * 原始数据MQTT，具体值和 application.yml配置中mqtt.config下节点保持一致
   */
  public static final String MQTT_CHANEL_ORIGIN_DATA = "channel1";

  /**
   * supOS网关MQTT
   */
  public static final String MQTT_CHANEL_SUPOS = "channel2";


  public static String supOSmetaTagRetainTopic(String authToken, String endPointId, String endPointName) {
    return String.format("/%s/%s/%s/metatag/retain", authToken, endPointId, endPointName);
  }

  public static String supOSrtdvalueReportTopic(String authToken, String endPointId, String endPointName) {
    return String.format("/%s/%s/%s/rtdvalue/report", authToken, endPointId, endPointName);
  }

  public static String writeDataTopic(String metaInstName) {
    return String.format("Topic/%s/system/WriteData", metaInstName);
  }


  /**
   * supos地址
   */
  public static final String SUPOS_ADDRESS = "http://124.71.186.8:8080";
  public static final String AUTHORIZATION = "Authorization";
  public static final String AUTH_LOGIN = "/inter-api/auth/login";
  public static final String ACCESS_TOKEN = "Bearer ticket";

  /**
   * java查询所有人员信息
   */
  public static final String PERSON_QUERY_ALL = "/apps/mywl-equipment/mqtt/person/queryAll";

  /**
   * open-api查询所有人员信息
   */
  public static final String PERSON_LIST = "/open-api/organization/v2/persons";

  /**
   * 批量新增、修改、删除人员信息
   */
  public static final String PERSON_CURD = "/open-api/organization/v2/persons/bulk";

  /**
   * 批量新增、修改、删除人员信息
   */
  public static final String PERSON_ADD = "/inter-api/organization/v1/person";

  /**
   * 新增魔云用户表的信息
   */
  public static final String USER_ADD = "/inter-api/oodm-gateway/runtime/mywl_sys/template/user/service/system/AddDataTableEntry/debug";

  /**
   * 新增数据分组
   * {
   * "displayName": "aaa",
   * "comment": ""
   * }
   */
  public static final String ADD_DATA_GROUP = "/inter-api/oodm-gateway/auth/permissonDataSet/dataset";

  /**
   * 给分组配置模板
   * [
   * {
   * "templateId": 11208,
   * "templateInstanceType": "PART"
   * }
   * ]
   */
  public static final String CONFIG_TEMPLATE = "/inter-api/oodm-gateway/auth/permissonDataSet/groupId/templates";

  /**
   * 给分组配置对象实例
   * groupId分组id
   * templateId模板id
   * {
   * "instanceIds":
   * [
   * "14184",
   * "14195",
   * "14264"
   * ]
   * }
   */
  public static final String ASSIGN_INSTANCES = "/inter-api/oodm-gateway/auth/permissonDataSet/groupId/template/templateId/instance/config";

  /**
   * 给用户绑定数据分组，权限设置
   * userId  用户id
   * {
   * "controlled": true,
   * "dataResouceVOS": [
   * {
   * "resourceCode": "17958",   分组id
   * "resourceName": "ttttt",   分组名称
   * "resourceType": null
   * }
   * ]
   * }
   */
  public static final String USER_BINDING_GROUP = "inter-api/rbac/v1/user/userId/data/resource/oodm-data-group-permission";


  /**
   * 给用户绑定数据分组，权限设置
   * groupName  分组名称
   * get
   */
  public static final String GET_DATA_GROUP = "/inter-api/oodm-gateway/auth/permissonDataSet/dataset?pageIndex=1&pageSize=20&keyword=groupName";


  /**
   * 新增对象实例
   * groupName  分组名称
   * get
   */
  public static final String ADD_INSTANCE = "/inter-api/oodm-gateway/template/entity/11208/instance";


}
