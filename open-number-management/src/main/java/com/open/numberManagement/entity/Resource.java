package com.open.numberManagement.entity;
// Generated Feb 10, 2018 1:15:48 AM by Hibernate Tools 5.0.6.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Resource generated by hbm2java
 */
@Entity
@Table(name = "resource", catalog = "openNM", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Resource implements java.io.Serializable {

	private Integer id;
	private String name;
	private int resTypeId;
	private int resStatusId;
	private String descr;
	private Integer relResId;
	private String rowAddedUser;
	private Date rowAddedDttm;
	private String rowUpdatedUser;
	private Date rowUpdatedDttm;

	public Resource() {
	}

	public Resource(String name, int resTypeId, int resStatusId, String rowAddedUser, Date rowAddedDttm,
			String rowUpdatedUser, Date rowUpdatedDttm) {
		this.name = name;
		this.resTypeId = resTypeId;
		this.resStatusId = resStatusId;
		this.rowAddedUser = rowAddedUser;
		this.rowAddedDttm = rowAddedDttm;
		this.rowUpdatedUser = rowUpdatedUser;
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

	public Resource(String name, int resTypeId, int resStatusId, String descr, Integer relResId, String rowAddedUser,
			Date rowAddedDttm, String rowUpdatedUser, Date rowUpdatedDttm) {
		this.name = name;
		this.resTypeId = resTypeId;
		this.resStatusId = resStatusId;
		this.descr = descr;
		this.relResId = relResId;
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

	@Column(name = "res_type_id", nullable = false)
	public int getResTypeId() {
		return this.resTypeId;
	}

	public void setResTypeId(int resTypeId) {
		this.resTypeId = resTypeId;
	}

	@Column(name = "res_status_id", nullable = false)
	public int getResStatusId() {
		return this.resStatusId;
	}

	public void setResStatusId(int resStatusId) {
		this.resStatusId = resStatusId;
	}

	@Column(name = "descr", length = 200)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "rel_res_id")
	public Integer getRelResId() {
		return this.relResId;
	}

	public void setRelResId(Integer relResId) {
		this.relResId = relResId;
	}

	@Column(name = "row_added_user", nullable = false, length = 50)
	public String getRowAddedUser() {
		return this.rowAddedUser;
	}

	public void setRowAddedUser(String rowAddedUser) {
		this.rowAddedUser = rowAddedUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "row_added_dttm", nullable = false, length = 19)
	public Date getRowAddedDttm() {
		return this.rowAddedDttm;
	}

	public void setRowAddedDttm(Date rowAddedDttm) {
		this.rowAddedDttm = rowAddedDttm;
	}

	@Column(name = "row_updated_user", nullable = false, length = 50)
	public String getRowUpdatedUser() {
		return this.rowUpdatedUser;
	}

	public void setRowUpdatedUser(String rowUpdatedUser) {
		this.rowUpdatedUser = rowUpdatedUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "row_updated_dttm", nullable = false, length = 19)
	public Date getRowUpdatedDttm() {
		return this.rowUpdatedDttm;
	}

	public void setRowUpdatedDttm(Date rowUpdatedDttm) {
		this.rowUpdatedDttm = rowUpdatedDttm;
	}

}
