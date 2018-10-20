package com.belyabl9.server.repository;

import com.belyabl9.server.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

	User findByName(String name);
	
	User findByNickname(String nickname);
	
	User findByLogin(String login);
}
