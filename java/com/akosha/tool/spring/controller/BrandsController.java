package com.akosha.tool.spring.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.tags.ParamAware;

import com.akosha.tool.spring.form.BrandStory;
import com.akosha.tool.spring.form.Brands;
import com.akosha.tool.spring.form.Comment;
import com.akosha.tool.spring.form.User;
import com.akosha.tool.spring.service.BrandStoryService;
import com.akosha.tool.spring.service.BrandsService;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;

@Controller
public class BrandsController 
{
	@Autowired
	public BrandStoryService brandsStoryService;
		
	@Autowired
	public BrandsService brandsService;
	
	public void loadAndSaveStories(JsonObject brandStories,String brandId,BrandStory brandStory,FacebookClient facebookClient,List<BrandStory> tempList,List<BrandStory> brandStoryList,Model model,HttpSession session)
	{
		for(int i = 0; i < brandStories.getJsonArray("data").length(); i++) 
		{
			 if(brandStories.getJsonArray("data").getJsonObject(i).has("likes")&&brandStories.getJsonArray("data").getJsonObject(i).has("message")&&brandStories.getJsonArray("data").getJsonObject(i).has("comments"))
			 {	
				brandStory=new BrandStory();
				brandStory.setBrandId(brandId);
				brandStory.setStoryId(brandStories.getJsonArray("data").getJsonObject(i).getString("id"));
				brandStory.setCommentCount(brandStories.getJsonArray("data").getJsonObject(i).getJsonObject("comments").getJsonObject("summary").getInt("total_count"));
				brandStory.setLikeCount(brandStories.getJsonArray("data").getJsonObject(i).getJsonObject("likes").getJsonObject("summary").getInt("total_count"));
				brandStory.setMessage(brandStories.getJsonArray("data").getJsonObject(i).getString("message"));
				if(brandStories.getJsonArray("data").getJsonObject(i).has("from"))
				{
					brandStory.setPostedByUserId(brandStories.getJsonArray("data").getJsonObject(i).getJsonObject("from").getString("id"));
				}
				if(brandStories.getJsonArray("data").getJsonObject(i).has("object_id"))
				{
					JsonObject picObject=facebookClient.fetchObject(brandStories.getJsonArray("data").getJsonObject(i).getString("object_id")+"?fields=images&",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
					brandStory.setPicLink(picObject.getJsonArray("images").getJsonObject(5).getString("source"));
				}
				if(brandStories.getJsonArray("data").getJsonObject(i).has("source"))
				{
					brandStory.setVideoLink(brandStories.getJsonArray("data").getJsonObject(i).getString("source").replace("autoplay=1", "autoplay=0"));
				}
				tempList=brandsStoryService.getBrandStory(brandStory.getStoryId());
				if(tempList.size()!=0)
				{
					if(tempList.get(0).getLikeCount()!=brandStory.getLikeCount()||tempList.get(0).getCommentCount()!=brandStory.getCommentCount())
					{
						brandsStoryService.update(brandStory);
					}
				}
				else
				{
					brandsStoryService.save(brandStory);
				}
				
				brandStoryList.add(brandStory);
			 }
			 
		}
	}
	@RequestMapping(value="getLatestFeed")
	public String getLatestFeed(@RequestParam("brandId") String brandId,Model model,HttpSession session)
	{
		FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));	
		BrandStory brandStory=null;
		List<BrandStory> brandStoryList=new ArrayList<BrandStory>();
		List<BrandStory> tempList=null;
		JsonObject brandStories=facebookClient.fetchObject(brandId+"/posts?fields=likes.limit(1).summary(true),picture,link,from,source,object_id,message,comments.limit(1).summary(true)&limit=10",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
		loadAndSaveStories(brandStories, brandId, brandStory, facebookClient, tempList, brandStoryList, model, session);
		int beginIndex=brandStories.getJsonObject("paging").getString("next").indexOf("until=")+6;
		model.addAttribute("nextLinkIndex", brandStories.getJsonObject("paging").getString("next").substring(beginIndex));
		
		beginIndex=brandStories.getJsonObject("paging").getString("previous").indexOf("since=")+6;
		int endIndex=brandStories.getJsonObject("paging").getString("previous").lastIndexOf("&");
		model.addAttribute("previousLinkIndex", brandStories.getJsonObject("paging").getString("previous").substring(beginIndex,endIndex));
		
		model.addAttribute("brandId", brandId);
		model.addAttribute("brandsList", brandsService.getBrands());
		model.addAttribute("brandStoryList", brandStoryList);
		return "AllStories";
	}
	
	@RequestMapping(value="loadMoreStories")
	public String loadMoreStories(@RequestParam("brandId") String brandId,@RequestParam(required=false,value="nextLinkIndex") String nextLinkIndex,@RequestParam(required=false,value="previousLinkIndex") String previousLinkIndex,Model model,HttpSession session)
	{
		try
		{
			FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));	
			BrandStory brandStory=null;
			List<BrandStory> brandStoryList=new ArrayList<BrandStory>();
			List<BrandStory> tempList=null;
			JsonObject brandStories=null;
			if(nextLinkIndex!=null)
			{
				brandStories=facebookClient.fetchObject(brandId+"/posts?fields=likes.limit(1).summary(true),picture,link,from,source,object_id,message,comments.limit(1).summary(true),created_time&until="+nextLinkIndex+"&limit=10",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
				
			}
			else if(previousLinkIndex!=null)
			{
				brandStories=facebookClient.fetchObject(brandId+"/posts?fields=likes.limit(1).summary(true),picture,link,from,source,object_id,message,comments.limit(1).summary(true),created_time&since="+previousLinkIndex+"&limit=10",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
				
			}
			if(!brandStories.has("paging"))
			{
				return "redirect: getLatestFeed?brandId="+brandId;
			}
			loadAndSaveStories(brandStories, brandId, brandStory, facebookClient, tempList, brandStoryList, model, session);
			
			int beginIndex=brandStories.getJsonObject("paging").getString("next").indexOf("until=")+6;
			model.addAttribute("nextLinkIndex", brandStories.getJsonObject("paging").getString("next").substring(beginIndex));
			
			beginIndex=brandStories.getJsonObject("paging").getString("previous").indexOf("since=")+6;
			int endIndex=brandStories.getJsonObject("paging").getString("previous").lastIndexOf("&");
			
			String newPreviousLinkIndex=brandStories.getJsonObject("paging").getString("previous").substring(beginIndex,endIndex);
			if(newPreviousLinkIndex.contains("&format=json&"))
			{
				endIndex=newPreviousLinkIndex.indexOf("&");
				newPreviousLinkIndex=newPreviousLinkIndex.substring(0,endIndex);
			}
			model.addAttribute("previousLinkIndex",newPreviousLinkIndex);
			model.addAttribute("brandId", brandId);
			model.addAttribute("brandStoryList", brandStoryList);
			
			return "AllStories";
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return "Error";
		}
	}
	
	@RequestMapping(value="viewStory")
	public String viewStory(@RequestParam(required=true,value="storyId") String storyId,@RequestParam(value="redirectCommentId",required=false)String redirectCommentId,Model model,HttpSession session)
	{
		try
		{
			Comment commentDetails=null;
			User user =null;
			List <Comment> mainComment=new ArrayList<Comment>();
			List<Comment> subComment=null;
			JsonObject userDetails=null;
			String more=null;
			FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
			JsonObject storyDetails=facebookClient.fetchObject(storyId+"?fields=comments.limit(3).fields(from,message,comments.limit(1).fields(from,like_count,message,created_time),like_count,created_time)&",JsonObject.class,Parameter.with("qaccess_token",(String)session.getAttribute("sessionAccessToken")));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
			String masterId=null;
				for(int j=0;j<storyDetails.getJsonObject("comments").getJsonArray("data").length();j++)
				{
					user=new User();
					commentDetails=new Comment();
					userDetails=facebookClient.fetchObject(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("from").getString("id")+"?fields=picture,name&",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
					user.setUserName(userDetails.get("name").toString());
					user.setUserPicLink(userDetails.getJsonObject("picture").getJsonObject("data").getString("url"));
					user.getUserId(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("from").getString("id"));
					masterId=storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getString("id");
					commentDetails.setCommentId(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getString("id"));
					commentDetails.setCommentPostedDate(sdf.parse(storyDetails.get("created_time").toString().substring(0,10)));
					commentDetails.setLikeCount(Integer.parseInt(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).get("like_count").toString()));
					commentDetails.setMessage(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getString("message").toString());
					commentDetails.setPostId(storyId);
					commentDetails.setSubPostId(storyId);
					commentDetails.setUser(user);
					Comment tempComment=null;
					subComment=new ArrayList<Comment>();
					User subUser=null;
					if(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).has("comments"))
					{
						for(int i=0;i<storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").length();i++)
						{
							tempComment=new Comment();
							subUser=new User();
							userDetails=facebookClient.fetchObject(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).getJsonObject("from").getString("id")+"?fields=picture,name&",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
							subUser.setUserName(userDetails.getString("name"));
							subUser.setUserPicLink(userDetails.getJsonObject("picture").getJsonObject("data").getString("url"));
							
							tempComment.setCommentId(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("id").toString());
							tempComment.setCommentPostedDate(sdf.parse(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("created_time").toString().substring(0,10)));
							tempComment.setLikeCount(Integer.parseInt(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("like_count").toString()));
							tempComment.setMessage(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("message").toString());
							tempComment.setPostId(storyId);
							tempComment.setSubPostId(masterId);
							tempComment.setUser(subUser);
								
							subComment.add(tempComment);
						}
					}
					commentDetails.setSubComment(subComment);
					if(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).has("comments"))
					{	
						commentDetails.setMore(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("after"));
						if(redirectCommentId!=null&&redirectCommentId.equals(commentDetails.getCommentId()))
						{
							more=commentDetails.getMore();
						}
					}
					mainComment.add(commentDetails);	
				}
			
			model.addAttribute("storyId", storyId);
			model.addAttribute("storyDetails", mainComment);
			model.addAttribute("next", storyDetails.getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("after"));
			model.addAttribute("previous", storyDetails.getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("before"));
			model.addAttribute("storyId", storyId);
			model.addAttribute("message", brandsStoryService.getBrandStory(storyId).get(0).getMessage());
			model.addAttribute("comment", new Comment());
			if(redirectCommentId!=null)
			{
				model.addAttribute("more",more);
				model.addAttribute("redirectCommentId",redirectCommentId);
			}
			return "ViewStory";
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "Error";
		}
	}
	
	@RequestMapping(value="/loadMoreComments")
	public @ResponseBody List<Comment> loadMoreComments(Model model,HttpServletRequest request,HttpSession session)
	{
		List<Comment> subComment=new ArrayList<Comment>();
		try
		{
			FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
			String masterCommentId=request.getParameter("masterPostId");
			String more=request.getParameter("more");
			Comment tempComment=null;
			User subUser=null;
			JsonObject userDetails=null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
			
			JsonObject moreCommentDetails=facebookClient.fetchObject(masterCommentId+"/comments?fields=from,like_count,message,created_time&after="+more+"&", JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
			for(int i=0;i<moreCommentDetails.getJsonArray("data").length();i++)
			{
				tempComment=new Comment();
				subUser=new User();
				userDetails=facebookClient.fetchObject(moreCommentDetails.getJsonArray("data").getJsonObject(i).getJsonObject("from").getString("id")+"?fields=picture,name&",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
				subUser.setUserName(userDetails.getString("name"));
				subUser.setUserPicLink(userDetails.getJsonObject("picture").getJsonObject("data").getString("url"));
				tempComment.setCommentId(moreCommentDetails.getJsonArray("data").getJsonObject(i).get("id").toString());
				tempComment.setCommentPostedDate(sdf.parse(moreCommentDetails.getJsonArray("data").getJsonObject(i).get("created_time").toString().substring(0,10)));
				tempComment.setLikeCount(Integer.parseInt(moreCommentDetails.getJsonArray("data").getJsonObject(i).get("like_count").toString()));
				tempComment.setMessage(moreCommentDetails.getJsonArray("data").getJsonObject(i).get("message").toString());
				tempComment.setSubPostId(masterCommentId);
				tempComment.setUser(subUser);
				if(moreCommentDetails.getJsonObject("paging").has("after"))
				{
					tempComment.setMore(moreCommentDetails.getJsonObject("paging").getString("after"));
				}
				subComment.add(tempComment);
			}
			model.addAttribute("comment", new Comment());
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return subComment;
	}
	
	@RequestMapping(value="viewStoryPagination")
	public String viewStoryPagination(@RequestParam(value="storyId",required=true)String storyId,@RequestParam(value="after",required=false)String after,@RequestParam(value="before",required=false)String before,Model model, HttpSession session) 
	{
		try
		{
			Comment commentDetails=null;
			User user =null;
			List <Comment> mainComment=new ArrayList<Comment>();
			List<Comment> subComment=null;
			JsonObject userDetails=null;
			JsonObject storyDetails=null;
			String more=null;
			FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
			if(after!=null)
			{
				storyDetails=facebookClient.fetchObject(storyId+"/comments?fields=from,message,comments.limit(1).fields(from,like_count,message,created_time,comments),like_count,created_time&limit=3&after="+after+"&",JsonObject.class,Parameter.with("qaccess_token",(String)session.getAttribute("sessionAccessToken")));
			}
			else
			{
				storyDetails=facebookClient.fetchObject(storyId+"/comments?fields=from,message,comments.limit(1).fields(from,like_count,message,created_time,comments),like_count,created_time&limit=3&before="+before+"&",JsonObject.class,Parameter.with("qaccess_token",(String)session.getAttribute("sessionAccessToken")));
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
			String masterId=null;
			if(storyDetails.getJsonArray("data").getJsonObject(0).has("comments"))
			{
				for(int j=0;j<storyDetails.getJsonArray("data").length();j++)
				{
					user=new User();
					commentDetails=new Comment();
					userDetails=facebookClient.fetchObject(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("from").getString("id")+"?fields=picture,name&",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
					user.setUserName(userDetails.get("name").toString());
					user.setUserPicLink(userDetails.getJsonObject("picture").getJsonObject("data").getString("url"));
					user.getUserId(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("from").getString("id"));
					masterId=storyDetails.getJsonArray("data").getJsonObject(j).getString("id");
					commentDetails.setCommentId(storyDetails.getJsonArray("data").getJsonObject(j).getString("id"));
					commentDetails.setCommentPostedDate(sdf.parse(storyDetails.getJsonArray("data").getJsonObject(0).get("created_time").toString().substring(0,10)));
					commentDetails.setLikeCount(Integer.parseInt(storyDetails.getJsonArray("data").getJsonObject(j).get("like_count").toString()));
					commentDetails.setMessage(storyDetails.getJsonArray("data").getJsonObject(j).getString("message").toString());
					commentDetails.setPostId(storyId);
					commentDetails.setSubPostId(storyId);
					commentDetails.setUser(user);
					Comment tempComment=null;
					subComment=new ArrayList<Comment>();
					User subUser=null;
					if(storyDetails.getJsonArray("data").getJsonObject(j).has("comments"))
					{
						for(int i=0;i<storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").length();i++)
						{
							tempComment=new Comment();
							subUser=new User();
							userDetails=facebookClient.fetchObject(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).getJsonObject("from").getString("id")+"?fields=picture,name&",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
							subUser.setUserName(userDetails.getString("name"));
							subUser.setUserPicLink(userDetails.getJsonObject("picture").getJsonObject("data").getString("url"));
							
							tempComment.setCommentId(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("id").toString());
							tempComment.setCommentPostedDate(sdf.parse(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("created_time").toString().substring(0,10)));
							tempComment.setLikeCount(Integer.parseInt(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("like_count").toString()));
							tempComment.setMessage(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonArray("data").getJsonObject(i).get("message").toString());
							tempComment.setPostId(storyId);
							tempComment.setSubPostId(masterId);
							tempComment.setUser(subUser);
							subComment.add(tempComment);
						}
					}
					commentDetails.setSubComment(subComment);
					if(storyDetails.getJsonArray("data").getJsonObject(j).has("comments"))
					{
						commentDetails.setMore(storyDetails.getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("after"));
						
					}
					mainComment.add(commentDetails);	
				}
			}
			
			model.addAttribute("storyDetails", mainComment);
			model.addAttribute("next", storyDetails.getJsonObject("paging").getJsonObject("cursors").getString("after"));
			model.addAttribute("previous", storyDetails.getJsonObject("paging").getJsonObject("cursors").getString("before"));
			model.addAttribute("storyId", storyId);
			model.addAttribute("message", brandsStoryService.getBrandStory(storyId).get(0).getMessage());
			model.addAttribute("comment", new Comment());
			return "ViewStory";
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "Error";
		}
	}
	
	@RequestMapping(value="addComment",method=RequestMethod.POST)
	public String addComment(@ModelAttribute("comment")Comment comment,Model model,HttpSession session,HttpServletRequest request)
	{
		FacebookClient facebookClient=new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
		if(request.getParameter("commentId")==null)
		{
			facebookClient.publish(request.getParameter("storyId")+"/comments", String.class, Parameter.with("message", comment.getMessage()));
		}
		else
		{
			facebookClient.publish(request.getParameter("commentId")+"/comments", String.class, Parameter.with("message", comment.getMessage()));
		}
	
		return "redirect:/viewStory?storyId="+request.getParameter("storyId");
		
	}
	
	
	@RequestMapping(value="searchBrand")
	public String searchBrand(@ModelAttribute("brand") Brands brand,Model model,HttpServletRequest request,HttpSession session)
	{
		try
		{
			String searchParam=request.getParameter("searchParam");
			List <Brands> searchBrandList=null;
			FacebookClient facebookClient= null;
			if(searchParam!=null && searchParam.equals("New"))
			{
				
				facebookClient=new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
				JsonObject brandDetails=facebookClient.fetchObject("search?fields=likes,name,picture,about&q="+URLEncoder.encode(brand.getBrandName(), "UTF-8")+"&type=page&limit=10&", JsonObject.class	,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
				Brands tempBrands=null;
				if(brandDetails.has("data"))
				{
					searchBrandList=new ArrayList<Brands>();
					for(int i=0;i<brandDetails.getJsonArray("data").length();i++)
					{
						tempBrands=new Brands();
						tempBrands.setBrandName(brandDetails.getJsonArray("data").getJsonObject(i).getString("name"));
						tempBrands.setBrandId(brandDetails.getJsonArray("data").getJsonObject(i).getString("id"));
						tempBrands.setLikes(brandDetails.getJsonArray("data").getJsonObject(i).getInt("likes"));
						if(brandDetails.getJsonArray("data").getJsonObject(i).has("about"))
						{
							tempBrands.setAbout(brandDetails.getJsonArray("data").getJsonObject(i).getString("about"));
						}
						tempBrands.setPictureLink(brandDetails.getJsonArray("data").getJsonObject(i).getJsonObject("picture").getJsonObject("data").getString("url"));
						searchBrandList.add(tempBrands);
					}
					model.addAttribute("searchBrandList", searchBrandList);
				}
				else
				{
					model.addAttribute("message", "No page found!!");
				}
			}
			else if(searchParam!=null && searchParam.equals("Existing"))
			{
				searchBrandList=brandsService.getBrandsByName(brand.getBrandName());
				model.addAttribute("searchBrandList",searchBrandList);
			}
			model.addAttribute("brand",brand);
			return "Home";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Error";
		}
		
	}
	
	@RequestMapping(value="addBrand")
	public String addBrand(@ModelAttribute("brand") Brands brand,Model model)
	{
		try
		{
			System.out.println("brand luke:"+brand.getLikes());
			brandsService.save(brand);
			model.addAttribute("message", "Brand added..");
			
		}
		catch(Exception e)
		{
			model.addAttribute("message", "Brand is already added..");
			model.addAttribute("brandsList", brandsService.getBrands());
			
		}
		model.addAttribute("brand", new Brands());
		return "Home";
		
		
	}
}

