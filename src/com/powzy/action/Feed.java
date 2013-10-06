package com.powzy.action;

import static com.powzy.OfyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.powzy.feed.FeedItem;
import com.powzy.jsonmodel.BoolResponse;

@Path("/feed")
public class Feed {
	private static final Logger log =
			Logger.getLogger(Feed.class.getName());
	
	
	@POST
	@Path("/put")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BoolResponse put(final FeedItem feedItem) {
		feedItem.setId(null);
		ofy().save().entity(feedItem);
		return new BoolResponse(true); 
	}
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FeedItem> get(@QueryParam("lastTime") Long lastTime) {
		List<FeedItem> feeds;
		if(lastTime == null || lastTime<= 0) {
			feeds = ofy().load().type(FeedItem.class).list();
		} else 
			feeds = ofy().load().type(FeedItem.class).filter("timeStamp > ", lastTime).list();
		
		return feeds;
	}
	
}
