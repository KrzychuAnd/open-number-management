package com.open.numberManagement.service.repository;

import com.open.numberManagement.entity.ResourceStatus;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
* Generated by Spring Data Generator on 10/02/2018
*/
@Repository
@PreAuthorize("hasAuthority('ADMIN_PERM')")
public interface ResourceStatusRepository extends JpaRepository<ResourceStatus, Integer>, JpaSpecificationExecutor<ResourceStatus> {

	@Description(value = "Get Resource Status by Name")
	@RestResource(path = "byname", rel="resourceStatus")
	@Query("select rs from ResourceStatus rs where rs.name = :name")
	ResourceStatus getResourceStatusByName(@Param("name") String name);
	
	@PreAuthorize("isAuthenticated()")
	@Description(value = "Get Resource Status by Resource Status Id")
	@Query("select rs from ResourceStatus rs where rs.id = :id")
	Optional<ResourceStatus> getResourceStatusById(@Param("id") Integer id);
}
