package com.splitwise.Activity_Service.model;

import com.splitwise.Activity_Service.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityRequest {
    Activity activity;
    List<Long> userIdList;
}
