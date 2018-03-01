package com.open.numberManagement.controller;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static com.open.numberManagement.util.Constants.ADMINISTRATOR_PERMISSION;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.open.numberManagement.dto.DtoMapper;
import com.open.numberManagement.dto.entity.UserDto;
import com.open.numberManagement.entity.User;
import com.open.numberManagement.service.UserService;
import com.open.numberManagement.util.UriBuilder;
import com.open.numberManagement.exception.*;

@RequestMapping(value = "v1/users")
@RestController
public class UserController {

	private UserService userService;
	private DtoMapper dtoMapper;
	private UriBuilder uriBuilder = new UriBuilder();

	@Autowired
	public UserController(UserService userService, DtoMapper dtoMapper) {
		this.userService = userService;
		this.dtoMapper = dtoMapper;
	}

	@PreAuthorize("hasAuthority('" + ADMINISTRATOR_PERMISSION + "')")
	@RequestMapping(method = RequestMethod.GET)
	public List<UserDto> getUsers(
			@RequestParam(required = false, defaultValue = "0", name = "pageNumber") int pageNumber,
			@RequestParam(required = false, defaultValue = "10", name = "pageSize") int pageSize) {
		List<User> users = userService.getUsers();
		List<UserDto> userDtos = dtoMapper.map(users, UserDto.class);
		return userDtos;
	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserDto getUser(@PathVariable("id") Integer id) {
		User user = userService.getUser(id);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMINISTRATOR_PERMISSION)))
				&& !(user.getLogin().equals(authentication.getName()))) {
			throw new NoAccessToUserException(id);
		}

		return dtoMapper.map(user, UserDto.class);
	}

	@RequestMapping(value = "login/{login}", method = RequestMethod.GET)
	@ResponseBody
	public UserDto getUserByLogin(@PathVariable("login") String login) {
		User user = userService.getUserByLogin(login);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMINISTRATOR_PERMISSION)))
				&& !(user.getLogin().equals(authentication.getName()))) {
			throw new NoAccessToUserException(login);
		}
		
		return dtoMapper.map(user, UserDto.class);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('" + ADMINISTRATOR_PERMISSION + "')")
	public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
		User user = new User();
		URI uri;

		dtoMapper.map(userDto, user);

		user = userService.addUser(user);
		uri = uriBuilder.requestUriWithId(user.getId());

		return created(uri).build();
	}

	@RequestMapping(value = "lock/{id}", method = RequestMethod.PATCH)
	@PreAuthorize("hasAuthority('" + ADMINISTRATOR_PERMISSION + "')")
	@ResponseBody
	public ResponseEntity lockUser(@PathVariable("id") Integer id) {
		User user = userService.getUser(id);
		user.setLocked('Y');
		user = userService.updateUser(user);
		return noContent().build();
	}
}
