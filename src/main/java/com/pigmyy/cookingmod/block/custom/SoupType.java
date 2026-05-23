package com.pigmyy.cookingmod.block.custom;

import net.minecraft.util.StringRepresentable;

public enum SoupType implements StringRepresentable {
    WATER("water"),
    CARROT("carrot"),
    POTATO("potato");

    private final String name;
    SoupType(String name) { this.name = name; }

    @Override
    public String getSerializedName() { return this.name; }
}
