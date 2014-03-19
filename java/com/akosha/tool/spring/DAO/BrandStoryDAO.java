package com.akosha.tool.spring.DAO;

import java.util.List;

import com.akosha.tool.spring.form.BrandStory;

public interface BrandStoryDAO 
{
	public void save(BrandStory brandStory);
	public List<BrandStory> getBrandStory(String storyId);
	public void update(BrandStory brandStory);
}
