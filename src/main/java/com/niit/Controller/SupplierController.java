package com.niit.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.niit.Dao.ProductDao;
import com.niit.Dao.SupplierDao;
import com.niit.model.ProductInfo;
import com.niit.model.SupplierInfo;

@Controller
public class SupplierController 
{
	
	@Autowired
	SupplierInfo supplierInfo;
	@Autowired
	SupplierDao supplierDao;
	@Autowired
	ProductInfo productInfo;
	@Autowired
	ProductDao productDao;

	@RequestMapping("/Supplier")
	public ModelAndView supplier()
	{
		List<SupplierInfo> suppliers=supplierDao.list();
		ModelAndView obj=new ModelAndView("Supplier");
		obj.addObject("supplier",new SupplierInfo());
		obj.addObject("suppliers",suppliers);
		return obj;
	}
		
	@RequestMapping("/addsup")
	public ModelAndView addsup(@ModelAttribute("supplier") SupplierInfo sup)
	{
		ModelAndView obj=new ModelAndView("redirect:/Supplier");
		if(supplierDao.saveorupdate(sup)==true)
		{
			obj.addObject("Msg1"," Supplier added Successfully");
		}
		else
		{
			obj.addObject("Msg2"," Supplier not added");
		}
		return obj;
		}

	@RequestMapping("/editsupplier/{supId}")
	public ModelAndView editspl(@PathVariable("supId")String id)
	{
		supplierInfo = supplierDao.Getsupplier(id);
		ModelAndView obj=new ModelAndView("Supplier");
		obj.addObject("supplier",supplierInfo);
		List<SupplierInfo> suppliers=supplierDao.list();
		obj.addObject("Suppliers",suppliers);
		return obj;
	}

	@RequestMapping("/deletesupplier/{supId}")
	public ModelAndView delete(@PathVariable("supId")String id)
	{
		supplierInfo=supplierDao.Getsupplier(id);
		ModelAndView obj=new ModelAndView("redirect:/Supplier");
		List<ProductInfo> lp =productDao.GetProductByCategory(id);
		if(lp==null || lp.isEmpty())
		{
			supplierDao.delete(supplierInfo);
			obj.addObject("msg1","supplier deleted Successfully");
			
		}
		else
		{
			for(ProductInfo p:lp)
			{
				productDao.delete(p);
			}
			supplierDao.delete(supplierInfo);
			obj.addObject("msg","Supplier and list of products deleted");
		}
		return obj;
	}


	
}
