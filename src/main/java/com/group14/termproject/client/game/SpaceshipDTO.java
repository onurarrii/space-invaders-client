package com.group14.termproject.client.game;

import com.group14.termproject.client.game.enums.SpaceshipTier;
import com.group14.termproject.client.game.enums.SpaceshipType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceshipDTO extends GameObjectDTO {
    private double maxHealth;
    private double health;
    private SpaceshipTier tier;
    private SpaceshipType type;
}
