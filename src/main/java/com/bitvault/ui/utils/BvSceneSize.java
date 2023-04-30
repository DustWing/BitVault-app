package com.bitvault.ui.utils;

public record BvSceneSize(
        double width,
        double height
) {

    public static BvSceneSize Default = new BvSceneSize(840,600);

}
