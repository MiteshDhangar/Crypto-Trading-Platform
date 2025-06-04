package com.mitesh.TradingPlatform.Repository;

import com.mitesh.TradingPlatform.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
