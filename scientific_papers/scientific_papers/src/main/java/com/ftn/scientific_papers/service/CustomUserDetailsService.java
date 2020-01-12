package com.ftn.scientific_papers.service;

import java.util.ArrayList;
import java.util.List;

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
import com.ftn.scientific_papers.model.user.User;
import com.ftn.scientific_papers.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
			
		} else {
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			
			if (user.getRole().toLowerCase().equals(Role.AUTHOR.toString().toLowerCase())) {
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_AUTHOR"));
			} else if (user.getRole().toLowerCase().equals(Role.REVIEWER.toString().toLowerCase())) {
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_REVIEWER"));
			} else if (user.getRole().toLowerCase().equals(Role.EDITOR.toString().toLowerCase())){
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EDITOR"));
			}
			
			return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    grantedAuthorities
            );
		}
	}

	public void registerUser(User user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new UsernameTakenException();
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

}
