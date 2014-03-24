package com.akosha.tool.spring.service;

import java.util.List;
import com.akosha.tool.spring.form.Brands;

public interface BrandsService 
{
	public List<Brands> getBrands();
	public List<Brands> getBrandsByName(String brandName);
	public void save(Brands Brand);
}
