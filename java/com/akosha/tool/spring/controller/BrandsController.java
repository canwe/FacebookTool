package com.akosha.tool.spring.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.akosha.tool.spring.form.BrandStory;
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
				System.out.println("previous link clicked");
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
	public String viewStory(@RequestParam(required=true,value="storyId") String storyId,Model model,HttpSession session)
	{
		try
		{
			Comment commentDetails=null;
			User user =null;
			List <Comment> mainComment=new ArrayList<Comment>();
			List<Comment> subComment=null;
			JsonObject userDetails=null;
			FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
			JsonObject storyDetails=facebookClient.fetchObject(storyId+"?fields=comments.limit(3).fields(from,message,comments.limit(3).fields(from,like_count,message,created_time),like_count,created_time)&",JsonObject.class,Parameter.with("qaccess_token",(String)session.getAttribute("sessionAccessToken")));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
			String masterId=null;
			if(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(0).has("comments"))
			{
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
						
						if(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").has("after"))
						{
							tempComment.setMore(storyDetails.getJsonObject("comments").getJsonArray("data").getJsonObject(j).getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("after"));
						
						}
						subComment.add(tempComment);
					}
					commentDetails.setSubComment(subComment);
					mainComment.add(commentDetails);	
				}
			}
			model.addAttribute("storyDetails", mainComment);
			model.addAttribute("next", storyDetails.getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("after"));
			model.addAttribute("previous", storyDetails.getJsonObject("comments").getJsonObject("paging").getJsonObject("cursors").getString("before"));
			model.addAttribute("message", brandsStoryService.getBrandStory(storyId).get(0).getMessage());
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
			System.out.println("comming to controller part to load more comments");
			
			FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));
			String masterCommentId=request.getParameter("masterPostId");
			String more=request.getParameter("more");
			System.out.println(more+","+masterCommentId);
			Comment tempComment=null;
			User subUser=null;
			JsonObject userDetails=null;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
			
			JsonObject moreCommentDetails=facebookClient.fetchObject(masterCommentId+"/comments?limit=3&fields=from,like_count,message,created_time&after="+more+"&", JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
			System.out.println(moreCommentDetails);
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
			System.out.println(subComment.get(0).getMessage());
			System.out.println("coming to controller part with value");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return subComment;
	}
	
}
