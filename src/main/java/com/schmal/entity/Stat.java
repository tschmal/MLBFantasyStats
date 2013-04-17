package com.schmal.entity;

import lombok.Data;

@Data
public class Stat
{
    private final long ID;

    private final long resultID;

    private final long playerID;

    private final long categoryID;

    private final String slot;

    private final float value;
}