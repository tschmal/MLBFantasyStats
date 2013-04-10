package com.schmal.resource;

import com.schmal.service.ESPNScraper;
import com.yammer.metrics.annotation.Timed;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/retrieve")
@Produces(MediaType.APPLICATION_JSON)
public class RetrieveResource
{
    private final String leagueURL;

    public RetrieveResource(String leagueURL)
    {
        this.leagueURL = leagueURL;
    }

    @GET
    @Path("/todo")
    @Timed
    public List<String> getMatchupsToParse()
    {
        return ESPNScraper.getMatchups(this.leagueURL);
    }
}