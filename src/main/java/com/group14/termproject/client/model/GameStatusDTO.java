package com.group14.termproject.client.model;

import com.group14.termproject.client.game.BulletDTO;
import com.group14.termproject.client.game.SpaceshipDTO;
import com.group14.termproject.client.game.enums.Level;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameStatusDTO {
    private Level level;
    private boolean gameCompleted;
    private boolean gameStarted;

    private double score;

    private List<SpaceshipDTO> spaceships = new ArrayList<>();
    private List<BulletDTO> bullets = new ArrayList<>();
}
