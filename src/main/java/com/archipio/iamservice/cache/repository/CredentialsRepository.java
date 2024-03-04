package com.archipio.iamservice.cache.repository;

import com.archipio.iamservice.cache.entity.CredentialsCache;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends KeyValueRepository<CredentialsCache, String> {}
