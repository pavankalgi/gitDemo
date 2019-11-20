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
import com.niit.Dao.SupplierDao;
import com.niit.Fileinput.Fileinput;
import com.niit.model.CategoryInfo;
import com.niit.model.ProductInfo;
import com.niit.model.SupplierInfo;

@Controller
public class ProductController 
{
	@Autowired
	ProductInfo productInfo;
	@Autowired
	ProductDao productDao;
	@Autowired
	CategoryInfo categoryInfo;
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	SupplierInfo supplierInfo;
	@Autowired
	SupplierDao supplierDao;
String path="C:\\Users\\user\\Desktop\\Eclipse Workspace2\\weddingFront\\src\\main\\webapp\\resources\\image\\";
	@RequestMapping("/Product")
	public ModelAndView product()
	{
		ModelAndView obj=new ModelAndView("Product");
		obj.addObject("Product",new ProductInfo());
		obj.addObject("category", categoryInfo);
		List<CategoryInfo> li=categoryDao.list();
		obj.addObject("Categories",li);
		obj.addObject("supplier", supplierInfo);
		List<SupplierInfo> ls=supplierDao.list();
		obj.addObject("Suppliers",ls);
		List<ProductInfo> lp=productDao.list();
		obj.addObject("Products",lp);
		return obj;
	}
		
	@RequestMapping("/addprd")
	public ModelAndView addprd(@ModelAttribute("Product") ProductInfo prd)
	{
		ModelAndView obj=new ModelAndView("redirect:/Product");
		if(productDao.saveorupdate(prd)==true)
		{
			Fileinput.upload(path,prd.getPimg(),prd.getPrdid()+".jpg");
			obj.addObject("Msg1"," Product added Successfully");
		}
		else
		{
			obj.addObject("Msg2"," Product not added");
		}
		return obj;
		}

	@RequestMapping("/editproduct/{prdid}")
	public ModelAndView editcat(@PathVariable("prdid")String id)
	{
		ModelAndView obj=new ModelAndView("Product");
		productInfo=productDao.Getproduct(id);
		obj.addObject("Product",productInfo);
		List<CategoryInfo> li=categoryDao.list();
		obj.addObject("Categories",li);
		List<SupplierInfo> ls=supplierDao.list();
		obj.addObject("Suppliers",ls);
		List<ProductInfo> products=productDao.list();
		obj.addObject("Products",products);
		return obj;
	}

	@RequestMapping("/deleteproduct/{prdid}")
	public ModelAndView delete(@PathVariable("prdid")String id)
	{
		productInfo=productDao.Getproduct(id);
		ModelAndView obj=new ModelAndView("redirect:/Product");
		productDao.delete(productInfo);
		obj.addObject("msg1","Product deleted Successfully");
		return obj;
	}


}
