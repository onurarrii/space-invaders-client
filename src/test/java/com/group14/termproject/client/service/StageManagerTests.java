package com.group14.termproject.client.service;

import com.group14.termproject.client.controller.game.GameController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StageManagerTests {

    @InjectMocks
    StageManagerImpl stageManager;

    @Mock
    GameController gameController;

    @Test
    public void testOpenGamePageOnce() {
        stageManager.openGamePage();
        verify(gameController, times(1)).open();
    }

    @Test
    public void testOpenGamePageMultipleTimes() {
        // when game page is tried to be opened multiple times, it should work.
        int callCount = 3;
        for (int i = 0; i < 3; i++) {
            stageManager.openGamePage();
        }
        verify(gameController, times(callCount)).open();
    }
}
