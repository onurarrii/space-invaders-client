package com.group14.termproject.client.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderboardEntry {
    private String user;
    private double score;
    private String createdOn;
}