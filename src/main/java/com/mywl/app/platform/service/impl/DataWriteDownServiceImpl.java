package com.mywl.app.platform.service.impl;

import cn.hutool.json.JSONUtil;
import com.mywl.app.platform.constant.CommonConstant;
import com.mywl.app.platform.module.dto.writedata.Data;
import com.mywl.app.platform.module.dto.writedata.WriteDataDTO;
import com.mywl.app.platform.service.DataUploadService;
import com.mywl.app.platform.service.DataWriteDownService;
import com.mywl.app.platform.utils.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.mywl.app.platform.constant.CommonConstant.writeDataTopic;


@Slf4j
@Service
public class DataWriteDownServiceImpl implements DataWriteDownService {

  @Autowired
  DataUploadService dataUploadService;

  @Override
  public void writeDownData(String metaInstName, WriteDataDTO writeDataDTO) {
    //下写实时数据的topic
    final String topic = writeDataTopic(metaInstName);
    log.info("下写实时数据，topic={}, data={}", topic, writeDataDTO);
    MqttUtils.sendMessage(topic, JSONUtil.toJsonStr(writeDataDTO), 1, false, CommonConstant.MQTT_CHANEL_ORIGIN_DATA);
  }

  /*
   * 下写数据
   * */
  public int writeDownMessage(String metaInstName, String metaTagName, Double value) {
    Integer length = dataUploadService.queryByMetaInstAndTag(metaInstName, metaTagName);
    /* 未查询到已经上传的点位，无法执行下写操作 */
    if (length == 0) {
      log.error("未查询到已经上传的点位，无法执行下写操作,metaInstName={},metaTagName={}", metaInstName, metaTagName);
    } else {
      WriteDataDTO writeDataDTO = new WriteDataDTO();
      writeDataDTO.setVersion(10);
      Data data = new Data();
      data.setName(metaTagName);
      data.setValue(value);
      ArrayList<Data> list = new ArrayList<>();
      list.add(data);
      writeDataDTO.setData(list);
      writeDownData(metaInstName, writeDataDTO);
    }
    return length;
  }


}
