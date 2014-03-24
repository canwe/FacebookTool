package com.akosha.tool.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akosha.tool.spring.DAO.BrandStoryDAO;
import com.akosha.tool.spring.form.BrandStory;
import com.akosha.tool.spring.form.Brands;


@Service
public class BrandStoryServiceImpl implements BrandStoryService {

	@Autowired
	public BrandStoryDAO brandStoryDAO;
	
	@Override
	@Transactional
	public void save(BrandStory brandStory) 
	{
		brandStoryDAO.save(brandStory);

	}

	@Override
	@Transactional
	public List<BrandStory> getBrandStory(String storyId) 
	{
		return brandStoryDAO.getBrandStory(storyId);
	}

	@Override
	@Transactional
	public void update(BrandStory brandStory)
	{
		brandStoryDAO.update(brandStory);
		
	}
	
	

}
	