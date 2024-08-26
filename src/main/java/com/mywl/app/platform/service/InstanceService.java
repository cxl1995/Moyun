package com.mywl.app.platform.service;

import com.mywl.app.platform.module.dto.interapi.instance.AddInstanceDTO;
import com.mywl.app.platform.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface InstanceService {

  ApiResponse add(AddInstanceDTO dto) throws ExecutionException;

  ApiResponse AssignTemplateInstances(List<String> list, String groupId) throws ExecutionException;
}
