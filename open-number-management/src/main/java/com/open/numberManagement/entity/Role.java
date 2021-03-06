package com.open.numberManagement.entity;
// Generated Feb 10, 2018 1:15:48 AM by Hibernate Tools 5.0.6.Final

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Roles generated by hbm2java
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Getter
@Setter
@Table(name = "roles", catalog = "openNM")
public class Role implements Serializable {

	private Integer id;
	private String name;
	private String descr;
	@JsonIgnore
	private String rowAddedUser;
	@JsonIgnore
	private Date rowAddedDttm;
	@JsonIgnore
	private String rowUpdatedUser;
	@JsonIgnore
	private Date rowUpdatedDttm;

	@JsonIgnore
    private User user;
    
    private Set<Permission> permissions =  new HashSet<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@OneToOne(mappedBy = "role", cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, optional = true)
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Role() {
	}

	public Role(String name, String descr) {
		this.name = name;
		this.descr = descr;
	}
	
	public Role(String descr, String rowAddedUser, Date rowAddedDttm, String rowUpdatedUser, Date rowUpdatedDttm) {
		this.descr = descr;
		this.rowAddedUser = rowAddedUser;
		this.rowAddedDttm = rowAddedDttm;
		this.rowUpdatedUser = rowUpdatedUser;
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

	public Role(String name, String descr, String rowAddedUser, Date rowAddedDttm, String rowUpdatedUser,
			Date rowUpdatedDttm) {
		this.name = name;
		this.descr = descr;
		this.rowAddedUser = rowAddedUser;
		this.rowAddedDttm = rowAddedDttm;
		this.rowUpdatedUser = rowUpdatedUser;
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "descr", nullable = false, length = 200)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@CreatedBy
	@Column(name = "row_added_user", nullable = false, length = 50)
	public String getRowAddedUser() {
		return this.rowAddedUser;
	}

	public void setRowAddedUser(String rowAddedUser) {
		this.rowAddedUser = rowAddedUser;
	}

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "row_added_dttm", nullable = false, length = 19)
	public Date getRowAddedDttm() {
		return this.rowAddedDttm;
	}

	public void setRowAddedDttm(Date rowAddedDttm) {
		this.rowAddedDttm = rowAddedDttm;
	}

	@LastModifiedBy
	@Column(name = "row_updated_user", nullable = false, length = 50)
	public String getRowUpdatedUser() {
		return this.rowUpdatedUser;
	}

	public void setRowUpdatedUser(String rowUpdatedUser) {
		this.rowUpdatedUser = rowUpdatedUser;
	}

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "row_updated_dttm", nullable = false, length = 19)
	public Date getRowUpdatedDttm() {
		return this.rowUpdatedDttm;
	}

	public void setRowUpdatedDttm(Date rowUpdatedDttm) {
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

}
