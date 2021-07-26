package com.auth.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth.controller.UnauthorizedException;
import com.auth.dao.UserDAO;
import com.auth.model.AuthResponse;
import com.auth.model.UserData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerDetailsServiceImpl implements CustomerDetailsService {
	@Autowired
	private UserDAO userdao;
	@Autowired
	private JwtUtil jwtutil;

	@Override
	public UserDetails loadUserByUsername(String uid) {
		Optional<UserData> userData = userdao.findById(uid);
		if (userData == null) {
			log.error("Unauthorized exception");
			throw new UnauthorizedException("unauthorized");
		}
		
		UserData user = userData.get();
		ArrayList<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority(user.getUrole()));

		return new User(user.getUserid(), user.getUpassword(), list);
	}
	
	@Override
	public String signup(UserData signupdata) {
		String email = signupdata.getEmail();
		signupdata.setUserid(email);
		userdao.save(signupdata);
		return "you have registered successfully, please login to start investing";
	}

	@Override
	public UserData login(UserData userlogincredentials) {
		final UserDetails userdetails = loadUserByUsername(userlogincredentials.getUserid());
		String uid = "";
		String generateToken = "";
		if (userdetails.getPassword().equals(userlogincredentials.getUpassword())) {
			uid = userlogincredentials.getUserid();
			String role = userdetails.getAuthorities().toArray()[0].toString();
			generateToken = jwtutil.generateToken(userdetails);
			return new UserData(uid, null, null, generateToken,role,null,null,null);
		} else {
			log.error("Unauthorized exception");
			throw new UnauthorizedException("Unauthorized");
		}
	}

	@Override
	public AuthResponse getValidity(String token) {
		AuthResponse res = new AuthResponse();
		if (jwtutil.validateToken(token)) {
			res.setUid(jwtutil.extractUsername(token));
			res.setValid(true);
			res.setName(userdao.findById(jwtutil.extractUsername(token)).get().getUname());
			res.setRole(userdao.findById(jwtutil.extractUsername(token)).get().getUrole());
			res.setEmail(userdao.findById(jwtutil.extractUsername(token)).get().getEmail());
		} else {
			res.setValid(false);
			log.info("At Validity : ");
			log.error("Token Has Expired");
		}
		return res;
	}

	

}
