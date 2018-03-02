package com.open.numberManagement.service.repository;

import com.open.numberManagement.entity.Resource;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
* Generated by Spring Data Generator on 10/02/2018
*/
@Repository
@RepositoryRestResource(exported = false)
public interface ResourceRepository extends JpaRepository<Resource, Integer>, JpaSpecificationExecutor<Resource>, PagingAndSortingRepository<Resource, Integer> {
	
	@Description(value = "Get Resource by id")
	@Query("select r from Resource r where r.id = :id")
	Optional<Resource> getResource(@Param("id") Integer id);
	
	@Description(value = "Get Resource by Name")
	@Query("select r from Resource r where r.name = :name")
	Optional<Resource> getResourceByName(@Param("name") String name);
	
	@Description(value = "Get Resources by Resource Type ID")
	@Query("select r from Resource r where r.resTypeId = :resTypeId")
	Optional<List<Resource>> getResourcesByResTypeId(@Param("resTypeId") Integer resTypeId);
	
	@Description(value = "Get Resources by Resource Type Name")
	@Query("select r from Resource r, ResourceType rt where r.resTypeId = rt.id and rt.name = :resTypeName")
	Optional<List<Resource>> getResourcesByResTypeName(@Param("resTypeName") String resTypeName);	

	@Description(value = "Get Max Resource number by Resource Type ID")
	@Query("select max(cast(r.name as long)) from Resource r where r.resTypeId = :resTypeId")
	Optional<Long> getMaxResourceNumberByResTypeId(@Param("resTypeId") Integer resTypeId);
}
