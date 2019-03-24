package es.ucm.fdi.iw.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.integration.UserRepository;
import es.ucm.fdi.iw.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired PasswordEncoder passwordEncoder;
	
	@Transactional
    @Override
    public User create(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setActive(true);
		return userRepo.save(user);
	}
	
	@Transactional
    @Override
    public User delete(User user) {
		user.setActive(false);
		return userRepo.save(user);
	}
	
	@Override
	public User save(User user) {
        if(user != null)
        	user = userRepo.save(user);
        return user;
	}
	
	@Override
	public List<User> getAll() {
		return userRepo.findAll();
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	@Override
	public User findByNickname(String nickname) {
		return userRepo.findByNickname(nickname);
	}

	@Override
	public User findById(long id) {
		return userRepo.findById(id);
	}
	
}
