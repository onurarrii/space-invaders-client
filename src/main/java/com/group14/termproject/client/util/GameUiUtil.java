package com.group14.termproject.client.util;

import com.group14.termproject.client.game.GameObjectDTO;
import com.group14.termproject.client.game.Vector2D;
import com.group14.termproject.client.game.enums.Level;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public final class GameUiUtil {

    private static final double GRID_WIDTH = 800;
    private static final double GRID_HEIGHT = 600;

    private static final Map<Level, String> LEVEL_NAMES = new HashMap<>() {{
        put(Level.LEVEL_1, "LEVEL I");
        put(Level.LEVEL_2, "LEVEL II");
        put(Level.LEVEL_3, "LEVEL III");
        put(Level.LEVEL_4, "LEVEL IV");
        put(Level.LEVEL_5, "LEVEL V");
    }};

    private GameUiUtil() {
    }

    /**
     * Converts a position in game grid to a position in canvas so that resultant position and {@code actualPosition}
     * could be the same relatively.
     *
     * @param actualPosition the position relative to game grid.
     * @param canvas what to relative position calculated for.
     * @return position relative to canvas.
     */
    public static Vector2D getProportionedPosition(Vector2D actualPosition, Canvas canvas) {
        Vector2D proportionRatio = getProportionRatio(canvas);
        return getVectorWithProportion(proportionRatio, actualPosition);

    }

    /**
     * Converts a position in canvas to a position in game grid so that resultant position and {@code actualPosition}
     * could be the same relatively.
     *
     * @param proportionedPosition the position relative to canvas.
     * @param canvas what to relative position calculated for.
     * @return position relative to game grid.
     */
    public static Vector2D getActualPosition(Vector2D proportionedPosition, Canvas canvas) {
        Vector2D proportionRatio = getProportionRatio(canvas);
        // Invert the ratio as reverse calculation should be applied
        proportionRatio.setX(1 / proportionRatio.getX());
        proportionRatio.setY(1 / proportionRatio.getY());
        return getVectorWithProportion(proportionRatio, proportionedPosition);

    }

    public static String getLevelName(Level level) {
        return LEVEL_NAMES.get(level);
    }

    static ImageView generateImageView(Image image, GameObjectDTO gameObject, double hitBoxSize, Canvas canvas) {
        ImageView imageView = new ImageView(image);
        setImagePosition(gameObject, imageView, hitBoxSize, canvas);
        setImageSize(imageView, hitBoxSize, canvas);
        return imageView;
    }


    private static void setImagePosition(GameObjectDTO gameObject, ImageView imageView, double hitBoxSize,
                                         Canvas canvas) {
        double offset = hitBoxSize / 2;

        double calculatedX = gameObject.getPosition().getX() - offset;
        double calculatedY = gameObject.getPosition().getY() - offset;

        Vector2D proportionedPosition = getProportionedPosition(new Vector2D(calculatedX, calculatedY), canvas);

        imageView.setX(proportionedPosition.getX());
        imageView.setY(proportionedPosition.getY());
    }

    private static void setImageSize(ImageView imageView, double size, Canvas canvas) {
        Vector2D proportionedSize = getProportionedPosition(new Vector2D(size, size), canvas);
        imageView.setFitWidth(proportionedSize.getX());
        imageView.setFitHeight(proportionedSize.getY());
    }

    private static Vector2D getProportionRatio(Canvas canvas) {
        return new Vector2D(canvas.getWidth() / GRID_WIDTH, canvas.getHeight() / GRID_HEIGHT);
    }

    private static Vector2D getVectorWithProportion(Vector2D proportionRatio, Vector2D vector) {
        double newX = proportionRatio.getX() * vector.getX();
        double newY = proportionRatio.getY() * vector.getY();
        return new Vector2D(newX, newY);
    }
}
