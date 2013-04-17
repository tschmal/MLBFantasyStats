package com.schmal.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Position
{
    private final long ID;

    private final long playerID;

    private final String slot;

    private final Date eligibleStart;
}