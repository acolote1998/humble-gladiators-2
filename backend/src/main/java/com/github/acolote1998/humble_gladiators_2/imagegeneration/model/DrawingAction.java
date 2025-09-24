package com.github.acolote1998.humble_gladiators_2.imagegeneration.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawingAction {
    private int drawingMethod;
    private int initialX;
    private int initialY;

    public DrawingAction(int drawingMethod, int initialX, int initialY) {
        this.drawingMethod = drawingMethod;
        this.initialX = initialX;
        this.initialY = initialY;
    }
}
