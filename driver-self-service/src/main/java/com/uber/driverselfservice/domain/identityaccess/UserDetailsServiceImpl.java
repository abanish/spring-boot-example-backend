package com.uber.driverselfservice.domain.identityaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.uber.driverselfservice.infrastructure.UserLoginRepository;
/**
 * UserDetailsServiceImpl is a domain service that can load user logins from the UserLoginRepository.
 * */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserLoginRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		UserLoginEntity user = this.userRepository.findByEmail(email);

		if (user == null) {
			return null;
		} else {
			return new UserSecurityDetails(
					user.getId(),
					user.getEmail(),
					user.getPassword(),
					AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
					);
		}
	}
}
