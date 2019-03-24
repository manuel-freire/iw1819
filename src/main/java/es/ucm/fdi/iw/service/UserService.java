package es.ucm.fdi.iw.service;

import java.util.List;

import es.ucm.fdi.iw.model.User;

public interface UserService {
	
	User create(User user);
	
	User delete(User user);
	
	User save(User user);
	
	List<User> getAll();

	User findByEmail(String email);
	
	User findByNickname(String nickname);
	
	User findById(long id);
}
