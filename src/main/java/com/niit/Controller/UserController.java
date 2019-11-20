package com.niit.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.niit.Dao.BillingDao;
import com.niit.Dao.UserDao;
import com.niit.model.Billing;
import com.niit.model.Cart;
import com.niit.model.UserInfo;

@Controller
public class UserController 
{
@Autowired
UserInfo userInfo;
@Autowired
UserDao userDao;
@Autowired
Billing billing;
@Autowired
BillingDao billingDao;
 @RequestMapping("/User")
 public ModelAndView userform()
 {
	 List<UserInfo> users=userDao.list();
	 ModelAndView obj=new ModelAndView("User");
	 userInfo.setBilling(billing);
	 obj.addObject("user",new UserInfo());
	 obj.addObject("Users",users);
	 return obj;
 }
 @RequestMapping("/addusr")
	public ModelAndView addusr(@ModelAttribute("user") UserInfo usr)
	{
		ModelAndView obj=new ModelAndView("redirect:/User");
		Cart cart=new Cart();
		usr.setCart(cart);
		if(userDao.saveorupdate(usr)==true)
		{
			obj.addObject("Msg1"," User added Successfully");
		}
		else
		{
			obj.addObject("Msg2"," User not added");
		}
		return obj;
		}
 

}

