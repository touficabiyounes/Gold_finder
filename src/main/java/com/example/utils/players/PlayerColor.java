package com.example.utils.players;

import javafx.scene.paint.Color;

import java.util.Random;

public enum PlayerColor {
    ORANGE(0, Color.ORANGE),
    RED (1, Color.RED),
    YELLOW (2, Color.YELLOW),
    GREEN (3, Color.GREEN),
    PINK(4, Color.PINK),
    BLUE(5, Color.BLUE),
    PURPLE(6, Color.PURPLE),
    TEAL(7, Color.TEAL),
    AQUA(8, Color.AQUA),
    BLACK(9, Color.BLACK),
    CRIMSON(10, Color.CRIMSON),
    DARKBLUE(11, Color.DARKBLUE),
    DARKGOLDENROD(12, Color.DARKGOLDENROD),
    DARKGREEN(13, Color.DARKGREEN),
    DARKORANGE(14, Color.DARKORANGE),
    DARk_red(15, Color.DARKRED),
    DARKSALMON(16, Color.DARKSALMON),
    DARKTURQUOISE(17, Color.DARKTURQUOISE),
    FUCHSIA(18, Color.FUCHSIA),
    GOLD(19, Color.GOLD),
    GRAY(20, Color.GRAY),
    HOTPINK(21, Color.HOTPINK),
    INDIGO(22, Color.INDIGO),
    LIGHTBLUE(23, Color.LIGHTBLUE),
    LIGHTCORAL(24, Color.LIGHTCORAL),
    LIGHTGREEN(25, Color.LIGHTGREEN),
    LIGHTSEAGREEN(26, Color.LIGHTSEAGREEN),
    LIME(27, Color.LIME),
    MAGENTA(28, Color.MAGENTA),
    MAROON(29, Color.MAROON),
    NAVY(30, Color.NAVY),
    OLIVE(31, Color.OLIVE),
    SILVER(32, Color.SILVER);

    private final int id;
    private final Color color;

    PlayerColor(int i, Color color) {
        this.id = i;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
