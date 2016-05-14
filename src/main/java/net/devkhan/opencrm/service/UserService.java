package net.devkhan.opencrm.service;

import net.devkhan.opencrm.data.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
	User saveUser(User user);
	List<User> findByGroupCd(String groupCd);
}
