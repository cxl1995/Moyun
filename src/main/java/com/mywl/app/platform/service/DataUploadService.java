package com.mywl.app.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.mywl.app.platform.module.dto.MessageDTO;
import com.mywl.app.platform.module.dto.MetaTagDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DataUploadService {
  void sendMetaData(MetaTagDTO metaTagDTO);

  void sendRtdDataSingle(MessageDTO messageDTO);

  void sendRtdDataBatch(List<MessageDTO> messageDTOList);

  Map<Object, Float> parseMessage(JSONObject jsonObject);

  void getAllMetaData();

  Integer handleDataSource(String metaFullName);

  Integer handleDataRedis(String metaFullName);

  Boolean queryByMetaFullNameRedis(String metaFullName);

  Boolean addRedis(String metaFullName);

  Boolean deleteRedis(String metaFullName);

  Boolean queryByMetaFullNameDataSource(String metaFullName);

  Integer queryByMetaInstAndTag(String metaInstName, String metaTagName);

  Boolean deleteMetaData(String metaFullName);


}
