package com.niit.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.niit.Dao.CategoryDao;
import com.niit.Dao.ProductDao;
import com.niit.model.CategoryInfo;
import com.niit.model.ProductInfo;


@Controller
public class CategoryController 
{
@Autowired
CategoryInfo categoryInfo;
@Autowired
CategoryDao categoryDao;
@Autowired
ProductInfo productInfo;
@Autowired
ProductDao productDao;

@RequestMapping("/Category")
public ModelAndView category()
{
	ModelAndView obj=new ModelAndView("Category");
	obj.addObject("Category",new CategoryInfo());
	List<CategoryInfo> lc=categoryDao.list();
	obj.addObject("Categories",lc);
	return obj;
}
	
@RequestMapping("/addcat")
public ModelAndView addcat(@ModelAttribute("Category") CategoryInfo cat)
{
	ModelAndView obj=new ModelAndView("redirect:/Category");
	if(categoryDao.saveorupdate(cat)==true)
	{
		obj.addObject("Msg1"," Category added Successfully");
	}
	else
	{
		obj.addObject("Msg2"," Category not added");
	}
	return obj;
	}

@RequestMapping("/editcategory/{catid}")
public ModelAndView editcat(@PathVariable("catid")String id)
{
	ModelAndView obj=new ModelAndView("Category");
	categoryInfo=categoryDao.Getcategory(id);
	obj.addObject("Category",categoryInfo);
	List<CategoryInfo> categories=categoryDao.list();
	obj.addObject("Categories",categories);
	return obj;
}

@RequestMapping("/deletecategory/{catid}")
public ModelAndView delete(@PathVariable("catid")String id)
{
	ModelAndView obj=new ModelAndView("redirect:/Category");
	List<ProductInfo> lp =productDao.GetProductByCategory(id);
	if(lp==null || lp.isEmpty())
	{
		categoryDao.delete(categoryInfo);
		obj.addObject("msg1","category deleted Successfully");
		
	}
	else
	{
		for(ProductInfo p:lp)
		{
			productDao.delete(p);
		}
		categoryDao.delete(categoryInfo);
		obj.addObject("msg","Category and list of products deleted");
	}
	return obj;
}

}	

