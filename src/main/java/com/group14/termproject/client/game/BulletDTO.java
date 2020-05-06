package com.group14.termproject.client.game;

import com.group14.termproject.client.game.enums.BulletType;
import com.group14.termproject.client.game.enums.SpaceshipType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulletDTO extends GameObjectDTO {
    private BulletType type;
    private SpaceshipType ownerType;
}
