package com.schmal.entity;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Result
{
    private final long ID;
    
    private final long teamID;
    
    private final Date date;
    
    private List<Stat> stats;
}