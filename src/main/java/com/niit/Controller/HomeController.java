package com.niit.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.niit.Dao.ProductDao;
import com.niit.model.ProductInfo;

@Controller
public class HomeController
{
	 @Autowired
	 ProductDao productDao;
	@RequestMapping("/")
	public String home()
	{
		return "Home";
	}
//    @RequestMapping("/Category")
//    public String category()
//    {
//    	return "Category";
//    }
//    @RequestMapping("/Product")
//    public String product()
//    {
//    	return "Product";
//    }
//    @RequestMapping("/Supplier")
//    public String supplier()
//    {
//    	return "Supplier";
//    }
    @RequestMapping("/Aboutus")
    public String aboutus()
    {
    	return "Aboutus";
    }
    @RequestMapping("/Contactus")
    public String contactus()
    {
    	return "Contactus";
    }
//    @RequestMapping("/signup")
//    public String signup()
//    {
//    	return "signup";
//    }
//    @RequestMapping("/login")
//    public String login()
//    {
//    	return "login";
//    }
    @RequestMapping("/Forgotpwd")
    public String forgotpwd()
    {
    	return "Forgotpwd";
    }
    @RequestMapping("/Saree")
    public ModelAndView saree()
    {
        ModelAndView obj=new ModelAndView("Saree");
        List<ProductInfo> lp=productDao.list();
        obj.addObject("products",lp);
    	return obj;
    }
   @RequestMapping("/Bridal")
    public ModelAndView bridal()
    {
	   ModelAndView obj=new ModelAndView("Bridal");
	   List<ProductInfo> lp =productDao.list();
	   obj.addObject("products",lp);
    	return obj;
    }
    @RequestMapping("/Rings")
    public ModelAndView rings()
    {
    	ModelAndView obj=new ModelAndView("Rings");
    	List<ProductInfo> lp =productDao.list();
 	    obj.addObject("products",lp);
    	return obj;
    }
    @RequestMapping("/Viewcart")
    public String viewcart()
    {
    	return "Viewcart";
    }
   
    @RequestMapping("/Thankyou")
    public String thank()
    {
    	return "Thankyou";
    }
    
}
