package com.github.vfyjxf.justenoughgraphs.utils;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * From {@link mezz.jei.library.gui.ingredients.CycleTimer}
 */
public class CycleTimer {

    public static final CycleTimer SHARD = new CycleTimer(0);

    /* the amount of time in ms to display one thing before cycling to the next one */
    private static final int cycleTime = 1000;
    private long startTime;
    private long drawTime;
    private long pausedDuration = 0;

    public CycleTimer(int offset) {
        long time = System.currentTimeMillis();
        this.startTime = time - ((long) offset * cycleTime);
        this.drawTime = time;
    }

    public <T> T getCycledValue(List<T> values) {
        long time = System.currentTimeMillis();
        long timeSinceStart = time - startTime;
        int index = (int) ((timeSinceStart / cycleTime) % values.size());
        return values.get(index);
    }

    public void onDraw() {
        if (!Screen.hasShiftDown()) {
            if (pausedDuration > 0) {
                startTime += pausedDuration;
                pausedDuration = 0;
            }
            drawTime = System.currentTimeMillis();
        } else {
            pausedDuration = System.currentTimeMillis() - drawTime;
        }
    }
}
