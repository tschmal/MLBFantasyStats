package com.schmal.entity;

import lombok.Data;

@Data
public class Projection
{
    private final long ID;

    private final long playerID;

    private final long categoryID;

    private final float value;
}