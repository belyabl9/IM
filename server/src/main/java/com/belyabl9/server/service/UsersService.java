package com.belyabl9.server.service;

import com.belyabl9.api.StatusType;
import com.belyabl9.server.model.server.Department;
import com.belyabl9.server.model.server.User;
import com.belyabl9.server.repository.UsersRepository;
import com.belyabl9.server.utils.Digest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository repository;
	
	@Autowired
	private DepartmentsService departmentsService;
	
	@Autowired
    private PlatformTransactionManager platformTransactionManager;

	@PostConstruct
	private void init() {
        TransactionTemplate tmpl = new TransactionTemplate(platformTransactionManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Department department = departmentsService.save(new Department("IT"));
                User user = new User("serhii", "beliablia", "sss", "serhii", Digest.encrypt("123456".getBytes()), "127.0.0.1", 0);
                user.setDepartment(department);
                department.getUsers().add(user);
                save(user);
                user = new User("anzhela", "konenko", "sss","anzh", Digest.encrypt("123456".getBytes()), "127.0.0.1", 0);
                user.setDepartment(department);
                department.getUsers().add(user);
                save(user);
            }
        });
	}

	public User save(User user) {
		return repository.save(user);
	}
	
	public User findByName(String name) {
		return repository.findByName(name);
	}
	
	public User findByNickname(String nickname) {
		return repository.findByNickname(nickname);
	}
	
	public User findByLogin(String login) {
		return repository.findByLogin(login);
	}
	
	public User findById(long id) {
		return repository.findOne(id);
	}
	
	public List<User> findAll() {
		return repository.findAll();
	}

	public void updateWithTimestamp(User user) {
		user.setMtime(new Date());
		repository.save(user);
	}
	
	public boolean authUser(String login, String password) {
		User user = findByLogin(login);
		if (user == null) {
			return false;
		}
		String passwordHash = Digest.encrypt(password.getBytes());
		return passwordHash.equals(user.getPassword());
	}
	
	public void updateOfflineUsers() {
		for (User user : findAll()) {
			double fromLastUpdate = (Instant.now().toEpochMilli() - user.getMtime().getTime() ) / ( 60 * 1000 );
			if (fromLastUpdate >= 1) {
				user.setStatus(StatusType.OFFLINE);
				updateWithTimestamp(user);
			}
		}
	}

	public User createInstanceFromDto(com.belyabl9.api.User clientUser) {
		User user = clientUser.getId() != 0 ? findById(clientUser.getId()) : new User();
		user.setName(clientUser.getName());
		user.setSurname(clientUser.getSurname());
		user.setNickname(clientUser.getNickname());
		user.setLogin(clientUser.getLogin());
		user.setPassword(clientUser.getPassword());
		user.setStatus(clientUser.getStatus());
		user.setPort(clientUser.getPort());
		user.setIp(clientUser.getIp());
		user.setMtime(new Date());

		if ( clientUser.getDepartment() != null ) {
			user.setDepartment( departmentsService.createInstanceFromDto(clientUser.getDepartment()) );
		}

		return user;
	}
	
}
