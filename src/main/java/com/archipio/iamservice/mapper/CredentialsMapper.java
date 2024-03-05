package com.archipio.iamservice.mapper;

import com.archipio.iamservice.cache.entity.CredentialsCache;
import com.archipio.iamservice.dto.CredentialsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CredentialsMapper {

  @Autowired private BCryptPasswordEncoder passwordEncoder;

  @Mapping(source = "password", target = "password", qualifiedByName = "encode")
  public abstract CredentialsCache toCache(CredentialsDto inputDto);

  @Named("encode")
  String encode(String password) {
    return passwordEncoder.encode(password);
  }
}
