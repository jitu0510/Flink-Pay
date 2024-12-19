package com.rmgYantra.loginapp.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rmgYantra.loginapp.security.JwtUtil;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {
	
	@Autowired
	public JwtUtil jwtUtil;
	
	@GetMapping(path = "/login")
	public ResponseEntity authenticate() {
		Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails ud = ((UserDetails)userPrincipal);
		
		String un =ud.getUsername();
		Collection<? extends GrantedAuthority> role = ud.getAuthorities();
		ArrayList al = new ArrayList(role);
		String jwtToken = jwtUtil.generateToken(ud);
		LinkedHashMap<String, String> map= new LinkedHashMap<>();
		map.put("msg","Successfully Logged In");
		map.put("username",un);
		map.put("role",String.valueOf(al.get(0)));
		map.put("jwtToken",jwtToken);
		return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
	}
}
