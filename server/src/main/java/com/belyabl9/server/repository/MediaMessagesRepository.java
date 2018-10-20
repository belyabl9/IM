package com.belyabl9.server.repository;

import com.belyabl9.server.model.server.MediaMessage;
import com.belyabl9.server.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MediaMessagesRepository extends JpaRepository<MediaMessage, Long> {

	List<MediaMessage> findByToOrderByDate(User userTo);

	List<MediaMessage> findByToAndSentIsFalse(User to);

	// TODO why dateFrom is needed ?
	List<MediaMessage> findByToAndDateGreaterThanOrderByDate(User userTo, Date dateFrom);
	
	
}
