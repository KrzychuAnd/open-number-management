package com.open.numberManagement.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.UserDto;
import com.open.numberManagement.service.UserService;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserService userService;
	@Autowired
	private DtoMapper dtoMapper;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		com.open.numberManagement.entity.User onmUser = userService.getUserByLogin(user.getUsername());
		final Map<String, Object> additionalInfo = new HashMap<>();
		
		UserDto onmUserDto = dtoMapper.map(onmUser, UserDto.class);
		
		additionalInfo.put("user", onmUserDto);

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
	}

}