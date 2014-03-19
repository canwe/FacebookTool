package com.akosha.tool.spring.DAO;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akosha.tool.spring.form.BrandStory;

@Repository
public class BrandStoryDAOImpl implements BrandStoryDAO 
{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(BrandStory brandStory)
	{
		sessionFactory.getCurrentSession().save(brandStory);
	}

	@Override
	public List<BrandStory> getBrandStory(String storyId) 
	{
		return sessionFactory.getCurrentSession().createQuery("from BrandStory where storyId='"+storyId+"'").list();
	}

	@Override
	public void update(BrandStory brandStory) 
	{
		sessionFactory.getCurrentSession().update(brandStory);
	}

}
