package com.belyabl9.server.repository;

import com.belyabl9.server.model.server.TextMessage;
import com.belyabl9.server.model.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextMessagesRepository extends JpaRepository<TextMessage, Long> {
	List<TextMessage> findByFromAndToAndSentIsFalse(User from, User to);
	
	List<TextMessage> findByToAndSentIsFalse(User to);

	@Query("select msg from TextMessage msg where msg.from = :idFrom and msg.to = :idTo or msg.from = :idTo and msg.to = :idFrom order by date")
	List<TextMessage> findByFromAndToOrToAndFromOrderByDate(long idFrom, long idTo);
}
