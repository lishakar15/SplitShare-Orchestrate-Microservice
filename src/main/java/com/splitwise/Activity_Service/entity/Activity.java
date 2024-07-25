package com.splitwise.Activity_Service.entity;

import com.splitwise.Activity_Service.enums.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId;
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "settlement_id")
    private Long settlementId;
    @Column(name ="expense_id")
    private Long expenseId;
    @Column(name = "activity_type")
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;
    @Column(name = "activity_message")
    private String message;
    @Column(name = "create_date")
    private Date createDate;
    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL)
    private List<ChangeLog> changeLogs;
}
