package com.uber.driverselfservice.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uber.driverselfservice.domain.identityaccess.UserLoginEntity;

/**
 * The UserLoginRepository can be used to read and write UserLogin objects from and to the backing database.
 * */
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, Long> {

	public UserLoginEntity findByEmail(String email);

}
