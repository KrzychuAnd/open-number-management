package com.open.numberManagement.entity;
// Generated Feb 10, 2018 1:15:48 AM by Hibernate Tools 5.0.6.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Roles2permissionsId generated by hbm2java
 */
@Embeddable
public class Roles2permissionsId implements java.io.Serializable {

	private int roleId;
	private int permId;

	public Roles2permissionsId() {
	}

	public Roles2permissionsId(int roleId, int permId) {
		this.roleId = roleId;
		this.permId = permId;
	}

	@Column(name = "role_id", nullable = false)
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Column(name = "perm_id", nullable = false)
	public int getPermId() {
		return this.permId;
	}

	public void setPermId(int permId) {
		this.permId = permId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Roles2permissionsId))
			return false;
		Roles2permissionsId castOther = (Roles2permissionsId) other;

		return (this.getRoleId() == castOther.getRoleId()) && (this.getPermId() == castOther.getPermId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRoleId();
		result = 37 * result + this.getPermId();
		return result;
	}

}
