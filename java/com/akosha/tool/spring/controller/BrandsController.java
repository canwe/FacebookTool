package com.akosha.tool.spring.controller;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public BrandsService brandsService;
	
	@RequestMapping(value="getLatestFeed")
	public String getLatestFeed(@RequestParam("brandId") String brandId,Model model,HttpSession session)
	{
		FacebookClient facebookClient= new DefaultFacebookClient((String)session.getAttribute("sessionAccessToken"));	

		System.out.println(brandId);
		JsonObject brandStories=facebookClient.fetchObject(brandId+"/posts?fields=likes.limit(1).summary(true),message,comments.limit(1).summary(true)&limit=20",JsonObject.class,Parameter.with("qaccess_token", (String)session.getAttribute("sessionAccessToken")));
		JsonObject newObj=brandStories;
		System.out.println(newObj);
		for(int i = 0; i < newObj.getJsonArray("data").length(); i++) 
		{
			 System.out.println(i);
			 if(newObj.getJsonArray("data").getJsonObject(i).has("likes")&&newObj.getJsonArray("data").getJsonObject(i).has("message")&&newObj.getJsonArray("data").getJsonObject(i).has("comments"))
			 {	
				//System.out.println("Object:"+newObj.getJsonArray("data").getJsonObject(i));
				System.out.println("Message:"+newObj.getJsonArray("data").getJsonObject(i).getString("message"));
				System.out.println("Comment:"+newObj.getJsonArray("data").getJsonObject(i).getJsonObject("comments").getJsonObject("summary").getInt("total_count"));
				System.out.println("like:"+newObj.getJsonArray("data").getJsonObject(i).getJsonObject("likes").getJsonObject("summary").getInt("total_count"));
			 }
			 
		}
		System.out.println("returning");
		model.addAttribute("brandsList", brandsService.getBrands());
		return "Home";
	}
}
