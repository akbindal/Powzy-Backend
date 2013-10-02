package com.powzy.action;


import static com.powzy.OfyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.powzy.entity.BusinessEntity;
import com.powzy.entity.BusinessEntityLookup;
import com.powzy.entity.Category;
import com.powzy.entity.Users;
import com.powzy.jsonmodel.BusinessView;
import com.powzy.jsonmodel.Signup;
import com.powzy.util.JsonRequestException;
import com.powzy.util.UnauthorizedException;


@Path("/businessInfo")
public class BusinessInfo {
	private static final Logger log = Logger.getLogger(BusinessInfo.class.getName());
	
	HttpServletResponse response;
	
	//post
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BusinessEntity signupBusiness(final Signup newSignup) throws IOException {
		BusinessEntity entity = createEntity(newSignup.getAuthId(), 
				newSignup.getAuthToken());
		//no session cookie for now
		//TODO: set session cookies
		if(entity == null) {
			throw new UnauthorizedException("Business entity already exists");
		} else 
			return entity;
	}
	
	//post
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BusinessEntity loginBusiness(final Signup newSignup) throws IOException {
		BusinessEntity entity = this.getEntityWithAuth(newSignup.getAuthId(), newSignup.getAuthToken());
		if(entity == null) 
			throw new UnauthorizedException("entity not found");
		else 
			return entity;
	}
	
	@POST
	@Path("/category")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public BusinessView[] getBusinessCategory(String categoryList) {
		//String[] items = categoryList.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "").split(",");
		Long[] results;
		log.info(categoryList + " is requested");
		try {
			int lastIndex = categoryList.length();
			categoryList = categoryList.substring(1, lastIndex-1);
			ObjectMapper mapper = new ObjectMapper();
			results = mapper.readValue(categoryList, Long[].class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new JsonRequestException("integer array parsing error");
		}
		List<BusinessEntity> list = new ArrayList<BusinessEntity>();
		if(results ==null || results.length==0) {
			log.info("categories added");
			list = ofy().load().type(BusinessEntity.class).list();
		} else {
			Set<BusinessEntity> entities = new HashSet<BusinessEntity>();
			for(Long cat: results) {
				Ref<Category> ref = Ref.create(Key.create(Category.class, cat));
				entities.addAll(ofy().load().type(BusinessEntity.class).filter("categories", ref).list());
			}
			log.info("categories added list as well");
			list.addAll(entities);
		}
		
		BusinessView[] viewList = new BusinessView[list.size()];
		for(int i = 0 ; i< list.size(); i++) {
			viewList[i] = BusinessView.getLongDescription(list.get(i), 787878787l);
		}
		return viewList;
	}
	
	@POST
	@Path("/put")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String putBusinessDetail(BusinessView entity) {
		BusinessEntity en = this.getEntityWithAuth(entity.getId(), entity.getPassword());
		if(en!=null) {
			en.setBusinessDescription(entity.getBusinessDescription());
			en.setContactDetail(entity.getContactDetail());
			en.setLocation(entity.getLocation());
			en.setOpeningTime(entity.getOpeningTime());
			en.setName(entity.getName());
			en.setImageUrl(entity.getImageUrl());
			en.setLogoUrl(entity.getLogoUrl());
			//TODO: to remove
			en.setId(en.getId());
			//fill categories
			Set<Ref<Category>> categoryList = 
					new HashSet<Ref<Category>>();
			
			for(Long id: entity.getCategories())  {
				Category category = CategoryActivity.getCategory(id);
				categoryList.add(Ref.create(category));
			}
			en.setCategories(categoryList);
			ofy().save().entity(en).now();
			return "true";
		}else 
			return "false";

	}
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public BusinessEntity getBusinessDetail(@QueryParam("id") String stringId) {
		try {
		Long id = Long.valueOf(stringId);
		return ofy().load().entityById(id);
		} catch (NumberFormatException e)  {
			return null;
		}
	}
	
	@GET
	@Path("/getList")
	@Produces(MediaType.APPLICATION_JSON)
	public List<BusinessEntity> getBusinessDetail() {
		return ofy().load().type(BusinessEntity.class).list();
	}
	
	@GET
	@Path("/getList/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<BusinessView> getShortBusinessDetail(@PathParam("userId") Long userId,
			@QueryParam("lovedBrand") boolean isLovedBrands) {
		Iterable<BusinessEntity> list;
		Key<Users> userKey = Users.key(userId);
		if(isLovedBrands) 
			list = ofy().load().type(BusinessEntity.class).filter("lovers", userKey);
		else 
			list = ofy().load().type(BusinessEntity.class);
		List<BusinessView> bv = new ArrayList<BusinessView>();
		for(BusinessEntity ba: list) 
			bv.add(BusinessView.getShortDescription(ba, userKey));
		return bv;
	}
	
	
	
	/**
	 * trasactionally log in the person , creating if necessary
	 * @param entity
	 * @return
	 */
	//TODO: do it later, provide transaction functionality
	BusinessEntity createEntity(final String email, final String password) {
		BusinessEntity entity = ofy().load().entityByEmail(email);
		if(entity!=null) {
			entity = null;
		} else {
			entity = new BusinessEntity(email);
			ofy().save().entity(entity).now();
			ofy().save().entity(
					new BusinessEntityLookup(email, password, entity)
					).now();
			//load id
			entity = ofy().load().entityByEmail(email);
			//entity = fetchEntityByEmail(email);
		}
		return entity;
	}
	
	public BusinessEntity getEntityWithAuth(Long id, String password) {
		//get entity by id
		BusinessEntity result = getEntity(id);
		if (result == null)
			return null;
		else
			return ofy().load().authEntiyByEmail(result.getEmail(), password);
	}
	
	public BusinessEntity getEntityWithAuth(String email, String password) {
		//get entity by id
		if(email==null || email.trim().length() ==0 || password == null|| password.trim().length() == 0)
			return null;
		return ofy().load().authEntiyByEmail(email, password);
		
	}
	
	public BusinessEntity getEntity(Long id) {
		BusinessEntity result = ofy().load().entityById(id);
		return result;
	}
}
