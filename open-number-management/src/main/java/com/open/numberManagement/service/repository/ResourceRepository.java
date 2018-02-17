package com.open.numberManagement.service.repository;

import com.open.numberManagement.entity.Resource;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

/**
* Generated by Spring Data Generator on 10/02/2018
*/
@Repository
@RepositoryRestResource(exported = false)
public interface ResourceRepository extends JpaRepository<Resource, Integer>, JpaSpecificationExecutor<Resource>, PagingAndSortingRepository<Resource, Integer> {
	
	@Description(value = "Get Resource by Name")
	@RestResource(path = "byname", rel="resources")
	@Query("select r from Resource r where r.name = :name")
	Resource getResourceByName(@Param("name") String name);
	
	@Description(value = "Get Resources by Resource Type ID")
	@RestResource(path = "byrestypeid", rel="resources")
	@Query("select r from Resource r where r.resTypeId = :resTypeId")
	List<Resource> getResourcesByResTypeId(@Param("resTypeId") Integer resTypeId);
	
	@Description(value = "Get Resources by Resource Type Name")
	@RestResource(path = "byrestypename", rel="resources")
	@Query("select r from Resource r, ResourceType rt where r.resTypeId = rt.id and rt.name = :resTypeName")
	List<Resource> getResourcesByResTypeName(@Param("resTypeName") String resTypeName);	
}
