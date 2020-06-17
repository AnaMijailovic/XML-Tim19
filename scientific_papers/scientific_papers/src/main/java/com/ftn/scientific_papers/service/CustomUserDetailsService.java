package com.ftn.scientific_papers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ftn.scientific_papers.exceptions.UsernameTakenException;
import com.ftn.scientific_papers.model.user.TRole;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		TUser user = userRepository.findByUsername(username);
		System.out.println("User: " + user);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
			
		} else {
			List<GrantedAuthority> authorities = new ArrayList<>();

			if (user.getRoles() != null) {
				for (String role : user.getRoles().getRole().stream().map(TRole::getRole).distinct()
						.collect(Collectors.toList())) {
					authorities.add(new SimpleGrantedAuthority(role));
				}
			}

			return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
		}
	}

	public TUser findById(String userId) {
		return userRepository.findById(userId);
	}

	public TUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public void registerUser(TUser user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new UsernameTakenException();
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}


}
