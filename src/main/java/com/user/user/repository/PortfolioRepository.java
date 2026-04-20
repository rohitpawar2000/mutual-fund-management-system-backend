package com.user.user.repository;

import com.user.user.entity.Fund;
import com.user.user.entity.Portfolio;
import com.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUserAndFund(User user, Fund fund);

    List<Portfolio> findByUser(User user);

}
