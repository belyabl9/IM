package service;

import java.util.Date;
import java.util.List;

import model.client.StatusTypes;
import model.server.User;
import utils.Digest;
import dao.DaoFactory;
import dao.UsersDAO;

public class UsersService {
	
	public static User createUser(User user) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		return dao.save(user);
	}
	
	public static User findByName(String name) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		return dao.findByName(name);
	}
	
	public static User findByNickname(String nickname) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		return dao.findByNickname(nickname);
	}
	
	public static User findByLogin(String login) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		return dao.findByLogin(login);
	}
	
	public static User findById(long id) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		return dao.findById(id);
	}
	
	public static List<User> getAllUsers() {
		UsersDAO dao = DaoFactory.getUsersDAO();
		List<User> users = dao.getAllUsers();

		return users;
	}

	public static void updateInfo(User user) {
		UsersDAO dao = DaoFactory.getUsersDAO();
		user.setMtime(new Date());
		dao.save(user);
	}
	
	public static boolean authUser(String login, String password) {
		User user = findByLogin(login);
		if (user == null)
			return false;
		String passwordHash = Digest.encrypt(password.getBytes());
		
		return passwordHash.equals(user.getPassword());
	}
	
	public static void processOfflineUsers() {
		for ( User u : getAllUsers() ) {
			double fromLastUpdate = ( new Date().getTime() - u.getMtime().getTime() ) / ( 60 * 1000 );
			if ( fromLastUpdate > 1 ) {
				u.setStatus(StatusTypes.OFFLINE);
				updateInfo(u);
			}
		}
	}
	
	
	public static void main(String[] args) {
		Date d1 = new Date(2015, 3, 15, 10, 0, 0);
		Date d2 = new Date(2015, 3, 15, 20, 0, 0);
	
		System.out.println( ( d2.getTime() - d1.getTime() ) / ( 60 * 1000) );
	}
}
