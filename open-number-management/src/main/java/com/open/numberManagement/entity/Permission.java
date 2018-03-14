package com.open.numberManagement.entity;
// Generated Feb 10, 2018 1:15:48 AM by Hibernate Tools 5.0.6.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

//====== DO NOT USE LOMBOK!!! IT CAUSES THAT ManyToMany STOPS WORKING!!!!!!!
/**
 * Permissions generated by hbm2java
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "permissions", catalog = "openNM")
public class Permission implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@NaturalId
	@Column(name = "name", nullable = false, unique = true, length = 50)
	private String name;
	@Column(name = "descr", nullable = false, length = 200)
	private String descr;
	@JsonIgnore
	@CreatedBy
	@Column(name = "row_added_user", nullable = false, length = 50)
	private String rowAddedUser;
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "row_added_dttm", nullable = false, length = 19)
	@JsonIgnore
	private Date rowAddedDttm;
	@JsonIgnore
	@LastModifiedBy
	@Column(name = "row_updated_user", nullable = false, length = 50)
	private String rowUpdatedUser;
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "row_updated_dttm", nullable = false, length = 19)
	@JsonIgnore
	private Date rowUpdatedDttm;

	@JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = { 
            CascadeType.PERSIST, 
            CascadeType.MERGE
        })
        @JoinTable(name = "roles2permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "perm_id")
        )
	private Set<Role> roles = new HashSet<>();
	
    @ManyToMany(fetch = FetchType.EAGER, cascade = { 
            CascadeType.PERSIST, 
            CascadeType.MERGE
        })
        @JoinTable(name = "permissions2resourcetype",
            joinColumns = @JoinColumn(name = "perm_id"),
            inverseJoinColumns = @JoinColumn(name = "res_type_id")
        )
	private Set<ResourceType> resourceTypes =  new HashSet<>();
	
	public Set<ResourceType> getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(Set<ResourceType> resourceTypes) {
		this.resourceTypes = resourceTypes;
	}
	
	public Permission() {
		
	}
	
	public Permission(String name, String descr) {
		this.name = name;
		this.descr = descr;
	}
	
	public Permission(String name, String descr, String rowAddedUser, Date rowAddedDttm, String rowUpdatedUser,
			Date rowUpdatedDttm) {
		this.name = name;
		this.descr = descr;
		this.rowAddedUser = rowAddedUser;
		this.rowAddedDttm = rowAddedDttm;
		this.rowUpdatedUser = rowUpdatedUser;
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

	@JsonIgnore
	@Transient
	@Override
	public String getAuthority() {
		return this.name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getRowAddedUser() {
		return rowAddedUser;
	}

	public void setRowAddedUser(String rowAddedUser) {
		this.rowAddedUser = rowAddedUser;
	}

	public Date getRowAddedDttm() {
		return rowAddedDttm;
	}

	public void setRowAddedDttm(Date rowAddedDttm) {
		this.rowAddedDttm = rowAddedDttm;
	}

	public String getRowUpdatedUser() {
		return rowUpdatedUser;
	}

	public void setRowUpdatedUser(String rowUpdatedUser) {
		this.rowUpdatedUser = rowUpdatedUser;
	}

	public Date getRowUpdatedDttm() {
		return rowUpdatedDttm;
	}

	public void setRowUpdatedDttm(Date rowUpdatedDttm) {
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

}
