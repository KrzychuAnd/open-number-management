package com.open.numberManagement.service.repository;

import com.open.numberManagement.entity.ResourceType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
* Generated by Spring Data Generator on 10/02/2018
*/
@Repository
@PreAuthorize("hasAuthority('ADMIN_PERM')")
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Integer>, JpaSpecificationExecutor<ResourceType> {

	@PreAuthorize("isAuthenticated()")
	@Description(value = "Get Resource Type by Id")
	@Query("select rt from ResourceType rt where rt.id = :id")
	Optional<ResourceType> getResourceType(@Param("id") Integer id);
}
