package com.app.abhi.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.abhi.entity.User;
import com.app.abhi.exception.UserNotFoundException;
import com.app.abhi.playload.UserRequest;
import com.app.abhi.playload.UserResponse;
import com.app.abhi.service.IUserService;
import com.app.abhi.util.JwtUtil;

@RestController
@RequestMapping("/v1/user")
public class UserRestController 
{
	@Autowired
	private IUserService service;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//1.SAVE USER IN TO DB
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user)
	{
		Integer id = service.saveUser(user);
		return new ResponseEntity<String>("User with ID:"+id+" created !!",HttpStatus.CREATED);
	}
	
	//2.GET ALL USER
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers()
	{
		List<User> list = service.getAllUser();
		return ResponseEntity.ok(list);
	}
	
	//3.GET ONE USER BY ID
	@GetMapping("/find/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id")Integer id)
	{
		ResponseEntity<User> response = null;
		try 
		{
			User user = service.getOneUser(id);
			response = ResponseEntity.ok(user);
		}
		catch (UserNotFoundException unfe) 
		{
			unfe.printStackTrace();
			throw unfe;
		}
		return response;
	}
	
	//4.UPDATE USER
	@PutMapping("/modify")
	public ResponseEntity<String> updateUser(@RequestBody User user)
	{
		ResponseEntity<String> response = null;
		try 
		{
			service.modifyUser(user);
			response = ResponseEntity.ok("Student "+user.getId()+" Updated Successfully !!");
		}
		catch (UserNotFoundException unfe) 
		{
			unfe.printStackTrace();
			throw unfe;
		}
		return response;
	}
	
	//5.REMOVE USER
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id")Integer id)
	{
		ResponseEntity<String> response = null;
		try 
		{	
			service.deleteUser(id);
			response = ResponseEntity.ok("Student "+id+" Deleted !!");
		} 
		catch (UserNotFoundException unfe)
		{
			unfe.printStackTrace();
			throw unfe;
		}
		return response;
	}
	
	//to generate token hit /login
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest)
	{
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						userRequest.getUsername(),
						userRequest.getPassword())
				);
		
		String token = jwtUtil.generateToken(userRequest.getUsername());
		
		return ResponseEntity.ok(new UserResponse(token,"Granted by Mr.Abhi"));
	}
	
	//to get the logged in user
	@PostMapping("/welcome")
	public ResponseEntity<String> getLoggedInUser(Principal p)
	{
		return ResponseEntity.ok("Hello : "+p.getName());
	}
}
