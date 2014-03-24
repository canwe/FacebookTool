package com.akosha.tool.spring.DAO;

import java.util.List;

import com.akosha.tool.spring.form.Brands;

public interface BrandsDAO 
{
	public List<Brands> getBrands();
	public List<Brands> getBrandsByName(String brandName);
	public void save(Brands Brand);
}
