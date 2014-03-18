/**
 * 
 */
package com.akosha.tool.spring.DAO;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akosha.tool.spring.form.Brands;

/**
 * @author hp
 *
 */
@Repository
public class BrandsDAOImpl implements BrandsDAO 
{

	/* (non-Javadoc)
	 * @see com.akosha.tool.spring.DAO.BrandsDAO#getBrands()
	 */
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<Brands> getBrands() 
	{
		return sessionFactory.getCurrentSession().createQuery("from Brands").list();

	}

}
