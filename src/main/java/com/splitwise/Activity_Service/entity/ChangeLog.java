package com.splitwise.Activity_Service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name ="change_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_log_id")
    private Long changeLogId;
    @Column(name = "change_message")
    private String changeMessage;
    @ManyToOne()
    @JoinColumn(name = "activity_id")
    @JsonIgnore
    private Activity activity;

}
