package com.pigmyy.cookingmod.block.custom;

import net.minecraft.util.StringRepresentable;

public enum SoupType implements StringRepresentable {
    WATER("water"),
    MUSHROOM("mushroom"),
    RABBIT("rabbit"),
    BEETROOT("beetroot"),
    FISH("fish"),
    PORK("pork"),
    BEEF("beef"),
    ROTTEN("rotten"),
    CHICKEN("chicken"),
    PUMPKIN("pumpkin"),
    VEGETABLE("vegetable");


    private final String name;
    SoupType(String name) { this.name = name; }

    @Override
    public String getSerializedName() { return this.name; }
}
