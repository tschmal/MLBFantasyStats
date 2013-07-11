package com.schmal.dao;

import com.schmal.domain.Player;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class PlayerDAO extends AbstractDAO<Player>
{
    public PlayerDAO(SessionFactory factory)
    {
        super(factory);
    }

    public Player save(Player player)
    {
        return persist(player);
    }

    public List<Player> getAllPlayers()
    {
        return list(namedQuery("allPlayers"));
    }
}