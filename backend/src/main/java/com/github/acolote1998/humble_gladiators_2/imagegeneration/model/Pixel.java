package com.github.acolote1998.humble_gladiators_2.imagegeneration.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pixel {
    private int x;
    private int y;
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public Pixel(int x, int y, int red, int green, int blue, int alpha) {
        this.x = x;
        this.y = y;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
}
