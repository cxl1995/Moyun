package com.mywl.app.platform.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.mywl.app.platform.service.DataUploadService;
import com.mywl.app.platform.service.DataWriteDownService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class MqttMessageHandler1 implements MessageHandler {

  @Autowired
  private DataUploadService dataUploadService;

  @Autowired
  private DataWriteDownService dataWriteDownService;

  /**
   * 自动注入规则：${channelName}MqttChannelAdapter
   */
  @Autowired
  private MqttPahoMessageDrivenChannelAdapter channel1MqttChannelAdapter;

//  @ServiceActivator(inputChannel = MQTT_CHANEL_SUPOS)
  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    try {
      log.info("{}222222222收到消息---{}", this.getClass().getSimpleName(), message);
      String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
      log.info("channel2222 topic==={}", topic);
//      JSONObject payload = JSON.parseObject((String) message.getPayload());
//      JSONArray dataArr = payload.getJSONArray("data");
    } catch (Exception e) {
      log.error("{}消息处理失败---{}", this.getClass().getSimpleName(), ExceptionUtil.stacktraceToString(e));
    }
  }


}
