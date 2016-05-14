package net.devkhan.opencrm.service.impl;

import net.devkhan.opencrm.data.entity.User;
import net.devkhan.opencrm.repository.UserRepository;
import net.devkhan.opencrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultUserService implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> findByGroupCd(String groupCd) {
		return userRepository.findByGroupCd(groupCd);
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) throw new UsernameNotFoundException(username + " was not found");
		return user;
	}
}
