package com.group14.termproject.client.util;

import com.group14.termproject.client.game.Vector2D;
import javafx.scene.canvas.Canvas;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GameUiUtilTests {

    private static double GRID_WIDTH;
    private static double GRID_HEIGHT;

    @BeforeClass
    public static void setup() {
        GRID_WIDTH = (double) ReflectionTestUtils.getField(GameUiUtil.class, "GRID_WIDTH");
        GRID_HEIGHT = (double) ReflectionTestUtils.getField(GameUiUtil.class, "GRID_HEIGHT");
    }

    private Vector2D generatePoint() {
        Random ran = new Random();
        return new Vector2D(ran.nextInt((int) GRID_WIDTH), ran.nextInt((int) GRID_HEIGHT));
    }

    private void testProportionedPosition(Vector2D point, double canvasWidth, double canvasHeight, Vector2D expected) {
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        Vector2D result = GameUiUtil.getProportionedPosition(point, canvas);
        Assert.assertEquals(expected, result);
    }

    /**
     * @param point      point to be tested
     * @param scaleRatio shows ratio between canvas's and grid's size
     */
    private void testActualPosition(Vector2D point, double scaleRatio) {
        Vector2D expected = new Vector2D(point.getX() / scaleRatio, point.getY() / scaleRatio);
        Canvas canvas = new Canvas(GRID_WIDTH * scaleRatio, GRID_HEIGHT * scaleRatio);
        Vector2D result = GameUiUtil.getActualPosition(point, canvas);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetProportionedPositionWithSameSize() {
        Vector2D point = new Vector2D(GRID_WIDTH / 2, GRID_HEIGHT / 2);
        testProportionedPosition(point, GRID_WIDTH, GRID_HEIGHT, point);
    }

    @Test
    public void testGetProportionedPositionWithBiggerCanvas() {
        Vector2D point = generatePoint();
        // When canvas's size is 2 times greater than grid.
        Vector2D expected = new Vector2D(point.getX() * 2, point.getY() * 2);
        testProportionedPosition(point, GRID_WIDTH * 2, GRID_HEIGHT * 2, expected);
    }

    @Test
    public void testGetProportionedPositionWithSmallerCanvas() {
        Vector2D point = generatePoint();
        // When canvas's size is half of the grid.
        Vector2D expected = new Vector2D(point.getX() / 2, point.getY() / 2);
        testProportionedPosition(point, GRID_WIDTH / 2, GRID_HEIGHT / 2, expected);
    }

    @Test
    public void testActualPositionWithSameSize() {
        testActualPosition(generatePoint(), 1);
    }

    @Test
    public void testActualPositionWithBiggerCanvas() {
        testActualPosition(generatePoint(), 2);
    }

    @Test
    public void testActualPositionWithSmallerCanvas() {
        testActualPosition(generatePoint(), 0.5);
    }

}
