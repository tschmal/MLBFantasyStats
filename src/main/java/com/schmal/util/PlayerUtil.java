package com.schmal.util;

import com.schmal.domain.League;
import com.schmal.domain.Player;
import com.schmal.domain.Projection;
import com.schmal.domain.Result;
import java.util.ArrayList;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

@NoArgsConstructor
@Slf4j
public class PlayerUtil
{
    public Player getPlayer(Element playerCell, League league) throws Exception
    {
        Element playerLink = playerCell.select("a").first();
        long playerID = Long.parseLong(playerLink.attr("playerId"));

        Player player = league.getPlayerMap().get(playerID);
        if (player == null)
        {
            player = createPlayer(league, playerCell);
        }

        return player;
    }

    private Player createPlayer(League league, Element playerCell) throws Exception
    {
        Element playerLink = playerCell.select("a").first();

        String name = playerLink.ownText();
        long playerID = Long.parseLong(playerLink.attr("playerId"));
        String eligibility = playerCell.ownText().replaceAll("\u00a0", " ");
        eligibility = eligibility.substring(eligibility.indexOf(" ", 3) + 1).replaceAll(" ", "").trim();

        Player player = new Player(league, playerID, name, eligibility);
        player.setResults(new ArrayList<Result>());
        player.setProjections(new ArrayList<Projection>());
        league.getPlayerMap().put(playerID, player);
        league.getPlayers().add(player);

        return player;
    }
}