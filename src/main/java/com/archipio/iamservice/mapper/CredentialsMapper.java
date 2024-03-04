package com.archipio.iamservice.mapper;

import com.archipio.iamservice.cache.entity.CredentialsCache;
import com.archipio.iamservice.dto.CredentialsInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface CredentialsMapper {

  CredentialsCache toCache(CredentialsInputDto inputDto);
}
