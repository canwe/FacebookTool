package com.akosha.tool.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akosha.tool.spring.DAO.BrandsDAO;
import com.akosha.tool.spring.form.Brands;

@Service
public class BrandsServiceImpl implements BrandsService
{
	@Autowired
	public BrandsDAO  brandsDAO;
	
	@Override
	@Transactional
	public List<Brands> getBrands() 
	{
		return brandsDAO.getBrands();
		
	}

	@Override
	@Transactional
	public List<Brands> getBrandsByName(String brandName) 
	{
	
		return brandsDAO.getBrandsByName(brandName);
	}

	@Override
	@Transactional
	public void save(Brands Brand) 
	{
		brandsDAO.save(Brand);
		
	}

}
