package com.SJcoder.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.SJcoder.model.applicationUsers;

public interface userRepository extends JpaRepository<applicationUsers,Integer>{
	
	applicationUsers findByUserName(String username);
	
	applicationUsers findByEmailId(String emailId);
	
}
