package com.app.abhi.service;

import java.util.List;

import com.app.abhi.entity.User;

public interface IUserService 
{
	Integer saveUser(User user);
	List<User> getAllUser();
	User getOneUser(Integer id);
	void modifyUser(User user);
	void deleteUser(Integer id);
	//for security
	public User findByUsername(String username);
}
