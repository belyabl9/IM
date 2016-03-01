package dao;

import java.util.HashMap;
import java.util.List;

import model.server.User;

import org.hibernate.SessionFactory;


public class UsersDAO extends GenericDAO<User> {

	public UsersDAO(SessionFactory factory) {
		super(User.class, factory);
	}
	
	public User findByName(String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		return executeQuery("from User where name = :name", true, map);
	}
	
	public User findByNickname(String nickname) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("nickname", nickname);
		return executeQuery("from User where nickname = :nickname", true, map);
	}
	
	public User findByLogin(String login) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("login", login);
		return executeQuery("from User where login = :login", true, map);
	}
	
	public User findById(long id) {
		return super.findById(id);
	}
	
	public List<User> getAllUsers() {
		return (List<User>) super.findAll();
	}
	
	
}
