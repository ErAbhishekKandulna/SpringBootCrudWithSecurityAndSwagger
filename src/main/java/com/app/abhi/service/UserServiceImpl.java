package com.app.abhi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.abhi.entity.User;
import com.app.abhi.exception.UserNotFoundException;
import com.app.abhi.repo.UserRepository;

@Service
public class UserServiceImpl implements IUserService,UserDetailsService
{
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	//SAVE USER
	@Override
	public Integer saveUser(User user) 
	{
		String pwd = encoder.encode(user.getPassword());
		user.setPassword(pwd);
		return repo.save(user).getId();
	}

	//GET ALL USERS
	@Override
	public List<User> getAllUser() 
	{
		return repo.findAll();
	}

	//GET USER BY ID
	@Override
	public User getOneUser(Integer id) 
	{
		return repo.findById(id).orElseThrow(()-> new UserNotFoundException());
	}

	//UPDATE USER
	@Override
	public void modifyUser(User user) 
	{
		user = this.getOneUser(user.getId());
		if(user.getId()==null || !repo.existsById(user.getId()))
		{
			throw new UserNotFoundException("Student "+user.getId()+" not exist !!");
		}
		else
		{
			repo.save(user);
		}
	}

	//REMOVE USER
	@Override
	public void deleteUser(Integer id) 
	{
		repo.delete(this.getOneUser(id));
	}

	//FIND USER BY USING USERNAME FOR SECURITY
	@Override
	public User findByUsername(String username) 
	{
		Optional<User> opt = repo.findByUsername(username);
		if(opt.isPresent())
		{
			return opt.get();
		}
		return null;
	}

	//LOAD USER BY USING USERNAME UserDetailsService PRE DEFINED METHOD
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User user = findByUsername(username);
		if(null==user)
		{
			throw new UserNotFoundException(username+" not exist !!");
		}
		List<SimpleGrantedAuthority> list = user.getRoles().stream().map(
				role->new SimpleGrantedAuthority(role)
				).collect(Collectors.toList());
		return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
	}
	
}
