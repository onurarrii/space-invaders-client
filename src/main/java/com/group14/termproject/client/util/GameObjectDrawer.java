package com.group14.termproject.client.util;

import com.group14.termproject.client.game.BulletDTO;
import com.group14.termproject.client.game.SpaceshipDTO;
import com.group14.termproject.client.game.enums.BulletType;
import com.group14.termproject.client.game.enums.SpaceshipTier;
import com.group14.termproject.client.game.enums.SpaceshipType;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

import static com.group14.termproject.client.util.GameUiUtil.generateImageView;

public final class GameObjectDrawer {

    private static final double BULLET_HIT_BOX_SIZE = 25;
    private static final double SPACESHIP_HIT_BOX_SIZE = 75;

    private static final Map<BulletType, Image> ENEMY_BULLET_SPRITES = new HashMap<>() {{
        put(BulletType.BASIC, createImage("/static/bullet/enemy_basic.png"));
        put(BulletType.ENHANCED, createImage("/static/bullet/enemy_enhanced.png"));
    }};

    private static final Map<BulletType, Image> PLAYER_BULLET_SPRITES = new HashMap<>() {{
        put(BulletType.BASIC, createImage("/static/bullet/player_basic.png"));
        put(BulletType.ENHANCED, createImage("/static/bullet/player_enhanced.png"));
    }};

    private static final Map<SpaceshipTier, Image> ENEMY_SPACESHIP_SPRITES = new HashMap<>() {{
        put(SpaceshipTier.TIER1, createImage("/static/spaceship/enemy_tier1.png"));
        put(SpaceshipTier.TIER2, createImage("/static/spaceship/enemy_tier2.png"));
        put(SpaceshipTier.TIER3, createImage("/static/spaceship/enemy_tier3.png"));
        put(SpaceshipTier.TIER4, createImage("/static/spaceship/enemy_tier4.png"));
    }};

    private static final Map<SpaceshipTier, Image> PLAYER_SPACESHIP_SPRITES = new HashMap<>() {{
        put(SpaceshipTier.TIER1, createImage("/static/spaceship/player_tier1.png"));
        put(SpaceshipTier.TIER2, createImage("/static/spaceship/player_tier2.png"));
        put(SpaceshipTier.TIER3, createImage("/static/spaceship/player_tier3.png"));
        put(SpaceshipTier.TIER4, createImage("/static/spaceship/player_tier4.png"));
    }};

    private GameObjectDrawer() {
    }

    /**
     * Creates a sprite which is determined by {@link BulletType}. Note that, sprite's size is adjusted according to
     * canvas's size.
     *
     * @param canvas where the bullet will be drawn.
     * @param bullet bullet to be drawn.
     * @return sprite in {@link ImageView} form.
     */
    public static ImageView getSprite(Canvas canvas, BulletDTO bullet) {
        var source = bullet.getOwnerType() == SpaceshipType.ENEMY ? ENEMY_BULLET_SPRITES : PLAYER_BULLET_SPRITES;
        return generateImageView(source.get(bullet.getType()), bullet, BULLET_HIT_BOX_SIZE, canvas);
    }

    /**
     * Creates a sprite which is determined by {@link SpaceshipType} and {@link SpaceshipTier}.
     *
     * @param canvas    where the bullet will be drawn.
     * @param spaceship spaceship to be drawn.
     * @return sprite in {@link ImageView} form.
     */
    public static ImageView getSprite(Canvas canvas, SpaceshipDTO spaceship) {
        var source = spaceship.getType() == SpaceshipType.ENEMY ? ENEMY_SPACESHIP_SPRITES : PLAYER_SPACESHIP_SPRITES;
        return generateImageView(source.get(spaceship.getTier()), spaceship, SPACESHIP_HIT_BOX_SIZE, canvas);

    }

    private static Image createImage(String spriteURL) {
        return new Image(GameObjectDrawer.class.getResourceAsStream(spriteURL));
    }

}
