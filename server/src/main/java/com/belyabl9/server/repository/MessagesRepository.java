package com.belyabl9.server.repository;

import com.belyabl9.server.model.server.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {

}
