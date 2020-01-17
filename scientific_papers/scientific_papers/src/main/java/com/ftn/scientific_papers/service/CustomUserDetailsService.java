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
import com.ftn.scientific_papers.model.user.Role;
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
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
			
		} else {
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			if (user.isIsEditor()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_EDITOR"));
			} 
			
			if (user.getRoles() != null) {
				for (String role : user.getRoles().getRole().stream().map(TRole::getRole).distinct()
						.collect(Collectors.toList())) {
					if (role.equalsIgnoreCase(Role.AUTHOR.toString())) {
						authorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR"));
					} else if (role.equalsIgnoreCase(Role.EDITOR.toString())) {
						authorities.add(new SimpleGrantedAuthority("ROLE_EDITOR"));
					}
				}
			}
			
			return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
		}
	}

	public void registerUser(TUser user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new UsernameTakenException();
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

}
