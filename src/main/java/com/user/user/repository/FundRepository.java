package com.user.user.repository;

import com.user.user.entity.Fund;
import com.user.user.entity.reqdto.fundIdResp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundRepository extends JpaRepository<Fund,Long> {

}
