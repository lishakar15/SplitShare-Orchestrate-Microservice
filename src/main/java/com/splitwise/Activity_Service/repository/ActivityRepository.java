package com.splitwise.Activity_Service.repository;

import com.splitwise.Activity_Service.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByGroupIdIn(List<Long> groupIds);
    List<Activity> findByGroupId(Long groupId);
    List<Activity> findByExpenseId(Long expenseId);
    List<Activity> findBySettlementId(Long settlementId);
    @Query(value = "select group_id, count(*) from activities where group_id in (:groupIds) group by group_id",
            nativeQuery = true)
    List<Object[]> getAllGroupActivitiesCount(List<Long> groupIds);
}