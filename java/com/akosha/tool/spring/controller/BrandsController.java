package com.akosha.tool.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.akosha.tool.spring.form.BrandStory;
import com.akosha.tool.spring.service.BrandStoryService;
import com.akosha.tool.spring.service.BrandsService;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Post;

@Controller
public class BrandsController 
{
	@Autowired
	public BrandStoryService brandsStoryService;
	
	@Autowired
	public BrandsService brandsService;
	
	@RequestMapping(value="getLatestFeed")
	public String getLatestFeed(@RequestParam("brandId") String brandId,@RequestParam("page") Integer page,Model model,HttpSession session)
	{
		int page1=0,page2=0,page3=0;
		FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));	
		BrandStory brandStory=null;
		List<BrandStory> brandStoryList=new ArrayList<BrandStory>();
		List<BrandStory> tempList=null;
		JsonObject brandStories=facebookClient.fetchObject(brandId+"/posts?fields=likes.limit(1).summary(true),picture,link,from,source,object_id,message,comments.limit(1).summary(true)&limit="+page,JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
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
		
		model.addAttribute("brandsList", brandsService.getBrands());
		model.addAttribute("brandStoryList", brandStoryList);
		return "AllStories";
	}
}
