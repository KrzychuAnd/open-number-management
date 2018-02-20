package com.open.numberManagement.service;

import org.springframework.transaction.annotation.Transactional;

import com.open.numberManagement.entity.User;
import com.open.numberManagement.service.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShaPasswordEncoder shaPasswordEncoder;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> getUsers(){
		return this.userRepository.findAll();
	}
	
	public User getUser(Integer id) {
		return this.userRepository.getOne(id);
	}
	
	public User getUserByName(String name) {
		return this.userRepository.getUserByName(name);
	}
	
	public User addUser(User user) {
		user.setPassword(shaPasswordEncoder.encodePassword(user.getPassword(), null));
		return this.userRepository.saveAndFlush(user);
	}
	
	public User updateUser(User user) {
		return this.userRepository.saveAndFlush(user);
	}
}
