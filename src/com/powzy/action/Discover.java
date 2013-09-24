package com.powzy.action;

import static com.powzy.OfyService.ofy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;


import com.googlecode.objectify.Key;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.Category;
import com.powzy.entity.Location;
import com.powzy.entity.Users;
import com.powzy.jsonmodel.BusinessView;
import com.powzy.util.UnauthorizedException;

@Path("/discover")
public class Discover {
	
	@POST
	@Path("/map/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<BusinessView> getShortBusinessDetail( @PathParam("userId") Long userId, Location location) {
		Iterable<BusinessEntity> list;
		Key<Users> userKey = Users.key(userId);
		list = ofy().load().type(BusinessEntity.class);
		List<BusinessView> bv = new ArrayList<BusinessView>();
		for(BusinessEntity ba: list) 
			bv.add(BusinessView.getShortDescription(ba, userKey));
		return bv;
	}
	
	@POST
	@Path("/category/{userId}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getBusinessSet(@PathParam("userId")Long uid, String jsonCategories) {
		//get keys
		if(jsonCategories == null || jsonCategories.trim().length()==0)
			return null;
		
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);		List<Long> categories;
		try {
			categories = objMapper.readValue(jsonCategories, 
				new TypeReference<List<Long>>(){});
		} catch (Exception e) {
			throw new UnauthorizedException("json parse error");
		}
		
		Key<Users> userKey = Users.key(uid);
		Set<Key<Category>> cateKeys = new HashSet<Key<Category>>();
		for(Long id: categories) {
			Key<Category> key = Key.create(Category.class, id);
			cateKeys.add(key);
		}
		Map<Key<Category>, Category> mapper = ofy().load().keys(cateKeys);
		
		Iterable<BusinessEntity> list;
		list = ofy().load().type(BusinessEntity.class).filter("categories in", mapper.keySet()).list();
		Set<BusinessView> bv = new HashSet<BusinessView>();
		for(BusinessEntity ba: list) 
			bv.add(BusinessView.getShortDescription(ba, userKey));
		
		ByteArrayOutputStream sos = new ByteArrayOutputStream();
		try {
			objMapper.writeValue(sos, bv);
		} catch (Exception e) {
			throw new UnauthorizedException("object parser error");
		}
		return sos.toString();
	}
}
