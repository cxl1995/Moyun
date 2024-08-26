package com.mywl.app.platform.service;

import com.mywl.app.platform.module.dto.writedata.WriteDataDTO;
import org.springframework.stereotype.Service;

@Service
public interface DataWriteDownService {
  void writeDownData(String metaInstName, WriteDataDTO writeDataDTO);

  int writeDownMessage(String metaInstName, String metaTagName, Double value);
}
