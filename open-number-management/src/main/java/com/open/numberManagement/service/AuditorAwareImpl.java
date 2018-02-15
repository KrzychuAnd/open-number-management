package com.open.numberManagement.service;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		// TODO get currently logged in user name!
		return "admin";
	}

}
