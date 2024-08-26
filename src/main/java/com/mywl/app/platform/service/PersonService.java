package com.mywl.app.platform.service;

import com.mywl.app.platform.module.dto.interapi.person.AddPersonUserDTO;
import com.mywl.app.platform.module.entity.OrgPersonEntity;
import com.mywl.app.platform.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface PersonService {

  List<OrgPersonEntity> queryAll();

  Integer updateByPrimaryKey(String personId, String userName);

  ApiResponse add(AddPersonUserDTO dto) throws ExecutionException;

  ApiResponse addMywlUser(AddPersonUserDTO dto, String personId) throws ExecutionException;

  Integer deleteByPrimaryKey(Long id);

  ApiResponse addDataGroup(String personId,String comment) throws ExecutionException;

  ApiResponse configTemplate(String groupId) throws ExecutionException;

  Long getUserId(String personId);

  ApiResponse UserBindingGroup(String groupId, String groupName, String userId) throws ExecutionException;

  ApiResponse getDataGroup(String groupName) throws ExecutionException;

  ApiResponse getToken();


}
