package com.mywl.app.platform.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.module.dto.MessageDTO;
import com.mywl.app.platform.module.dto.MetaTagDTO;
import com.mywl.app.platform.service.DataUploadService;
import com.mywl.app.platform.service.DataWriteDownService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mywl.app.platform.constant.CommonConstant.MQTT_CHANEL_ORIGIN_DATA;


@Log4j2
@Component
public class MqttMessageHandler implements MessageHandler {

  @Autowired
  private DataUploadService dataUploadService;

  @Autowired
  private DataWriteDownService dataWriteDownService;

  /**
   * 自动注入规则：${channelName}MqttChannelAdapter
   */
  @Autowired
  private MqttPahoMessageDrivenChannelAdapter channel1MqttChannelAdapter;

//  @PostConstruct
//  private void addFailEvent() {
//    channel1MqttChannelAdapter.setApplicationEventPublisher((event) -> {
//      log.error("失败事件 {}", event);
//    });
//  }

  @ServiceActivator(inputChannel = MQTT_CHANEL_ORIGIN_DATA)
  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    try {
      log.info("{}收到消息---{}", this.getClass().getSimpleName(), message);
      String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
      log.info("channel1 topic==={}", topic);
      JSONObject payload = JSON.parseObject((String) message.getPayload());
      JSONArray dataArr = payload.getJSONArray("data");
      if (dataArr != null && topic.endsWith("MonitorData")) {
        handleAllProcessImprovement(dataArr, topic);
      } else {
        log.info("topic==={},dataArr==={}", topic, dataArr);
      }
    } catch (Exception e) {
      log.error("{}消息处理失败---{}", this.getClass().getSimpleName(), ExceptionUtil.stacktraceToString(e));
    }
  }

  /*
   * 改进 批量上传实时数据
   * 查询数据库判断是否需要上传元数据，数据库不存在则添加数据库记录并上传元数据，反之无需操作，最终上传实时数据
   * */
  public void handleAllProcessImprovement(JSONArray dataArr, String topic) {
    ArrayList<Object> arrayList = new ArrayList<>();

    /* 从topic中获取设备id */
    String instName = getInstName(topic);
    for (Object data : dataArr) {
      JSONObject jsonObject = (JSONObject) JSON.toJSON(data);
      if (!Objects.isNull(instName)) {
        String name = jsonObject.get("name").toString();
        String metaFullName = formatFullName(instName, name);
        String value = jsonObject.get("value").toString();
        /* 查询 redis 是否存在该记录 */
        Boolean isExistRedis = dataUploadService.queryByMetaFullNameRedis(metaFullName);
        /* 已存在 */
        if (isExistRedis) {
//          log.info("redis已存在数据==={}，直接上传实时数据", metaFullName);
          /* 上传实时数据 */
          sendRealTimeData(metaFullName, value, topic);
        } else {
          /* 查询 mariadb 是否存在该记录 */
          Boolean isExistMariadb = dataUploadService.queryByMetaFullNameDataSource(metaFullName);
          if (isExistMariadb) {
            log.info("=====Mariadb存在数据但redis不存在数据==={}，往redis添加，并上传实时数据=====", metaFullName);
            /* mariadb 存在数据,添加到 redis */
            Boolean aBoolean = dataUploadService.addRedis(metaFullName);
            /* 上传实时数据 */
            sendRealTimeData(metaFullName, value, topic);
          } else {
            log.info("=====Mariadb不存在数据=====metaFullName{}，无需上传实时数据", metaFullName);
          }
          /*添加数据库记录 count=1 - 不存在，已新增 */
          Integer count = dataUploadService.handleDataSource(metaFullName);
          if (count == 1) {
            /*上传元数据 */
            try {
              MetaTagDTO metaTagDTO = new MetaTagDTO();
              metaTagDTO.setVersion(1);
              metaTagDTO.setName(metaFullName);
              dataUploadService.sendMetaData(metaTagDTO);
            } catch (Exception e) {
              log.error("上传元数据失败，data:{}，topic：{}，message：{}", data, topic, e.getMessage());
            }
          }
        }
      } else {
        log.error("未知的topic:{},无法找到设备id", topic);
      }
    }
  }

  private void sendRealTimeData(String metaFullName, String value, String topic) {
    List<MessageDTO> messageDTOS = new ArrayList<MessageDTO>();
    MessageDTO messageDTO = new MessageDTO();
    messageDTO.setName(metaFullName);
    messageDTO.setValue(value);
    messageDTOS.add(messageDTO);
    try {
      /* 上传实时数据 */
      dataUploadService.sendRtdDataBatch(messageDTOS);
    } catch (Exception e) {
      log.error("上传实时数据失败，messageDTOS:{}，topic：{}，message：{}", messageDTOS, topic, e.getMessage());
    }
  }


  public String formatFullName(String instName, String name) {
    return String.format("Property_%s_%s", instName, name);
  }

  private String getInstName(String topic) {
    String[] strings = topic.split("/");
    String InstName = null;
    for (String str : strings) {
      Boolean numberCheck = isNumberCheck(str);
      if (numberCheck) {
        InstName = str;
        break;
      }
    }
    return InstName;
  }

  private Boolean isNumberCheck(String str) {
    boolean matches;
    if (str.matches("[0-9]+")) {
      matches = true;
    } else {
      matches = false;
    }
    return matches;
  }

}
