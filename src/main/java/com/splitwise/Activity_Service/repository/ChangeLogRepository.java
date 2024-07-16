package com.splitwise.Activity_Service.repository;

import com.splitwise.Activity_Service.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog,Long> {
}
