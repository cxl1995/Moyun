package com.mywl.app.platform.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.mywl.app.platform.constant.CommonConstant;
import com.mywl.app.platform.mapper.meta.MetaDataMapper;
import com.mywl.app.platform.module.dto.MessageDTO;
import com.mywl.app.platform.module.dto.MetaTagDTO;
import com.mywl.app.platform.module.entity.MetaDataEntity;
import com.mywl.app.platform.protocol.LocationProtos;
import com.mywl.app.platform.service.DataUploadService;
import com.mywl.app.platform.utils.DateUtil;
import com.mywl.app.platform.utils.MqttUtils;
import com.mywl.app.platform.utils.PBSerializerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mywl.app.platform.constant.CommonConstant.supOSmetaTagRetainTopic;
import static com.mywl.app.platform.constant.CommonConstant.supOSrtdvalueReportTopic;


@Slf4j
@Service
public class DataUploadServiceImpl implements DataUploadService {

  @Autowired
  private MetaDataMapper metaDataMapper;

  @Autowired
  RedisTemplate<String, String> redisTemplate;


  @Value("${supOS.iot.authToken}")
  private String authToken;
  @Value("${supOS.iot.endPointId}")
  private String endPointId;
  @Value("${supOS.iot.endPointName}")
  private String endPointName;


  @Override
  public void sendMetaData(MetaTagDTO metaTagDTO) {
    final String topic = supOSmetaTagRetainTopic(authToken, endPointId, endPointName);
    final LocationProtos.MetaTagSequence.Builder builder = LocationProtos.MetaTagSequence.newBuilder();
    LocationProtos.MetaTag metaTag = LocationProtos.MetaTag.newBuilder()
        .setVersion(metaTagDTO.getVersion())
        .setName(metaTagDTO.getName())
        .setShowName(metaTagDTO.getName())
        .setType(LocationProtos.ValueType.Double)
        .setStorage(true)
        .build();
    ;
    builder.addTags(metaTag);
    log.info("上传元数据：，topic={}, data={}", topic, builder.build());
    syncToSupOS(topic, 1, true, builder.build());
  }

  @Override
  public void sendRtdDataSingle(MessageDTO messageDTO) {
    // 对接平台实时数据的topic
    final String supOSTopic = supOSrtdvalueReportTopic(authToken, endPointId, endPointName);
    // 组装数据
    LocationProtos.ValueSequence valueSequence = syncToSupOSRtdValueSingle(messageDTO.getValue(), messageDTO.getName());
//    log.info("test-message，topic={}, data={}", supOSTopic, valueSequence);
    // 同步到supOS
    syncToSupOS(supOSTopic, 1, false, valueSequence);
  }

  @Override
  public void sendRtdDataBatch(List<MessageDTO> messageDTOList) {
    // 对接平台实时数据的topic
    final String supOSTopic = supOSrtdvalueReportTopic(authToken, endPointId, endPointName);
    // 组装数据
    LocationProtos.ValueSequence valueSequence = syncToSupOSRtdValueBatch(messageDTOList);
//    log.info("test-message，topic={}, data={}", supOSTopic, valueSequence);
    // 同步到supOS
    syncToSupOS(supOSTopic, 1, false, valueSequence);
  }

  private void syncToSupOS(String topic, int qos, boolean retained, Message message) {
    MqttUtils.sendMessage(topic, message.toByteArray(), qos, retained, CommonConstant.MQTT_CHANEL_SUPOS);
    log.info("往supOS平台推数据,topic={},data={},", topic, PBSerializerUtil.toJson(message).get());
  }

  private LocationProtos.ValueSequence syncToSupOSRtdValueSingle(String jsonStr, String metaName) {
    LocationProtos.RtdValue rtdValue = LocationProtos.RtdValue.newBuilder()
        .setTimeStamp(DateUtil.dateToTimestamp(LocalDateTime.now()))
        .setQuality(0)
        .setStrVal(jsonStr)
        .build();
    LocationProtos.NamedValue nameValue = LocationProtos.NamedValue.newBuilder()
        .setName(metaName)
        .setValue(rtdValue)
        .build();
    LocationProtos.ValueSequence valueSequence = LocationProtos.ValueSequence.newBuilder()
        .addValues(nameValue)
        .build();
    return valueSequence;
  }

  private LocationProtos.ValueSequence syncToSupOSRtdValueBatch(List<MessageDTO> messageDTOList) {
    ArrayList<LocationProtos.NamedValue> namedValues = new ArrayList<>();
    for (MessageDTO messageDTO : messageDTOList) {
      String metaName = messageDTO.getName();
      String value = messageDTO.getValue();
      LocalDateTime his = LocalDateTime.of(2023, 11, 1, 0, 0, 49, 100000000);
      LocationProtos.RtdValue rtdValue = LocationProtos.RtdValue.newBuilder()
//          .setTimeStamp(DateUtil.dateToTimestamp(LocalDateTime.now()))
          .setTimeStamp(DateUtil.dateToTimestamp(his))
          .setQuality(0)
          .setStrVal(value)
          .build();
      LocationProtos.NamedValue nameValue = LocationProtos.NamedValue.newBuilder()
          .setName(metaName)
          .setValue(rtdValue)
          .build();
      namedValues.add(nameValue);
    }
    LocationProtos.ValueSequence valueSequence = LocationProtos.ValueSequence.newBuilder()
        .addAllValues(namedValues)
        .build();
    return valueSequence;
  }

  public static void main(String[] args) {
    LocalDateTime now = LocalDateTime.now();
    System.out.println(now);
    LocalDateTime his = LocalDateTime.of(2023, 10, 31, 23, 59, 50, 100000000);
    System.out.println(his);
  }

  /* 解析报文 */
  public Map<Object, Float> parseMessage(JSONObject payload) {
    log.info("开始解析报文，payload---{}", payload);
    JSONArray dataArr = payload.getJSONArray("data");
    Map<Object, Float> map = null;
    if (dataArr != null) {
      map = dataArr.stream().filter(Objects::nonNull)
          .collect(Collectors.toMap(
              object -> {
                JSONObject item = (JSONObject) object;
                return item.getString("name");
              },
              object -> {
                JSONObject item = (JSONObject) object;
                return item.getFloat("value");
              }
          ));
      log.info("map---{}", map);
      log.info("key---{}", map.keySet());
    } else {
      log.info("----报文error----");
    }
    return map;
  }

  public void getAllMetaData() {
    List<MetaDataEntity> entityList = metaDataMapper.queryAll();
    log.info("table={}", entityList);
  }

  /*
   * 数据库处理
   * */
  public Integer handleDataSource(String metaFullName) {
    log.info("开始请求数据库---{}", metaFullName);
    int insert = 0;
    try {
      /* 查询数据库是否已经存在记录 */
      MetaDataEntity dataEntity = metaDataMapper.queryByMetaFullName(metaFullName);
      /* 不存在时添加数据 */
      if (Objects.isNull(dataEntity)) {
        Long now = DateUtil.dateToTimestamp(LocalDateTime.now());
        String date = DateUtil.timeStamp2Date(now.toString(), null);
        /* 新增数据库记录 */
        MetaDataEntity metaDataEntity = new MetaDataEntity();
        metaDataEntity.setMetaInstName(metaFullName.split("_")[1]);
        metaDataEntity.setMetaTagName(metaFullName.split("_")[2]);
        metaDataEntity.setMetaFullName(metaFullName);
        metaDataEntity.setCreateTime(date);
        insert = metaDataMapper.insert(metaDataEntity);
        log.info("添加数据成功metaFullName---{},insert==={}", metaFullName, insert);
      } else {
        log.info("数据库已经存在全称={}的数据", metaFullName);
        insert = 2;
      }
    } catch (Exception e) {
      log.error("{}数据库处理失败---{}", this.getClass().getSimpleName(), ExceptionUtil.stacktraceToString(e));
    }

    return insert;
  }

  @Override
  public Integer handleDataRedis(String metaFullName) {
    /* -1 异常 0-不存在，新增失败 1-不存在，已新增   2-已存在   */
    int status = -1;
    try {
      /* 查询redis是否已经存在记录 */
      Boolean isExist = redisTemplate.hasKey(metaFullName);
      /* 不存在时添加数据 */
      if (Boolean.FALSE.equals(isExist)) {
        redisTemplate.opsForValue().set(metaFullName, new Date().toString());
        if (Boolean.TRUE.equals(redisTemplate.hasKey(metaFullName))) {
          status = 1;
          log.info("redis添加数据成功：{}", metaFullName);
        } else {
          status = 0;
          log.info("redis添加数据失败：{}", metaFullName);
        }
      } else {
//        log.info("redis已存在数据：{}", metaFullName);
        status = 2;
      }
    } catch (Exception e) {
      log.info("redis读取数据失败：{}", metaFullName);
    }
    return status;
  }

  @Override
  public Boolean queryByMetaFullNameRedis(String metaFullName) {
    /* 查询redis是否已经存在记录 */
    Boolean isExist = redisTemplate.hasKey(metaFullName);
    return isExist;
  }

  @Override
  public Boolean addRedis(String metaFullName) {
    redisTemplate.opsForValue().set(metaFullName, new Date().toString());
    if (Boolean.TRUE.equals(redisTemplate.hasKey(metaFullName))) {
      log.info("redis添加数据成功：{}", metaFullName);
      return true;
    } else {
      log.error("redis添加数据失败：{}", metaFullName);
      return false;
    }
  }

  @Override
  public Boolean deleteRedis(String metaFullName) {
    Boolean delete = redisTemplate.delete(metaFullName);
    return delete;
  }

  @Override
  public Boolean queryByMetaFullNameDataSource(String metaFullName) {
    MetaDataEntity dataEntity = metaDataMapper.queryByMetaFullName(metaFullName);
    if (null == dataEntity) {
      return false;
    }
    if (dataEntity.toString().equals("")) {
      return false;
    }
    return true;
  }

  @Override
  public Integer queryByMetaInstAndTag(String metaInstName, String metaTagName) {
    List<MetaDataEntity> entityList = metaDataMapper.queryByMetaInstAndTag(metaInstName, metaTagName);
    return entityList.size();
  }

  @Override
  public Boolean deleteMetaData(String metaFullName) {
    int i = metaDataMapper.deleteByFullName(metaFullName);
    return i == 1;
  }


}
