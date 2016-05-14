package net.devkhan.opencrm.repository;

import net.devkhan.opencrm.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface UserRepository
		extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
	User findByUsername(String username);
	List<User> findByGroupCd(String groupCd);
}
