package com.open.numberManagement.service.repository;

import com.open.numberManagement.entity.Permissions2resourcetype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import com.open.numberManagement.entity.Permissions2resourcetypeId;

/**
* Generated by Spring Data Generator on 10/02/2018
*/
@Repository
@PreAuthorize("hasAuthority('ADMIN_PERM')")
public interface Permissions2resourcetypeRepository extends JpaRepository<Permissions2resourcetype, Permissions2resourcetypeId>, JpaSpecificationExecutor<Permissions2resourcetype> {

}
