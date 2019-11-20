package com.niit.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.niit.Dao.CartDao;
import com.niit.Dao.CartItemDao;
import com.niit.Dao.ProductDao;
import com.niit.Dao.UserDao;
import com.niit.model.Cart;
import com.niit.model.CartItem;
import com.niit.model.ProductInfo;
import com.niit.model.UserInfo;

@Controller
public class CartController
{
@Autowired
ProductInfo productInfo;
@Autowired
ProductDao productDao;
@Autowired
UserInfo userInfo;
@Autowired
UserDao userDao;
@Autowired
Cart cart;
@Autowired
CartDao cartDao;
@Autowired
CartItem cartItem;
@Autowired
CartItemDao cartItemDao;
@Autowired
HttpSession session;

@RequestMapping("/addtocart/{cart_id}")
public ModelAndView cart(@PathVariable("cart_id") String id)
{
	Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
	if(!(authentication instanceof AnonymousAuthenticationToken))
	{
		String currusername= authentication.getName();
		UserInfo u =userDao.getUseremail(currusername);
		if(userInfo==null)
		{
			return new ModelAndView("redirect:/");
		
		}
		else
		{
		cart=u.getCart();
		ProductInfo productInfo1= productDao.Getproduct(id);
		CartItem cartItem =new CartItem();
		cartItem.setCart(cart);
		cartItem.setProductInfo(productInfo1);
		cartItem.setPrice(productInfo1.getPrice());
		cartItemDao.saveorupdate(cartItem);
		cart.setGroand_total(cart.getGroand_total()+ productInfo1.getPrice());
		cart.setTotal_item(cart.getTotal_item()+1);
		cartDao.saveorupdate(cart);
		session.setAttribute("items", cart.getTotal_item());
		session.setAttribute("gd", cart.getGroand_total());
		return new ModelAndView("redirect:/");
		
		}
}
	
	else
	{
		return new ModelAndView("redirect:/");
	}
}
@RequestMapping(value="/viewcart")
public String viewcart(Model model,HttpSession session)
{	
	System.out.println(1223);
	Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
	if(!(authentication instanceof AnonymousAuthenticationToken))
	{
		String currusername= authentication.getName();
		UserInfo u =userDao.getUseremail(currusername);
		Cart c= u.getCart();
		List<CartItem> cartItem=cartItemDao.getlist(u.getCart().getCart_id());
		if(cartItem==null || cartItem.isEmpty())
		{
			session.setAttribute("items", 0);
			model.addAttribute("gtotal", 0.0);
			model.addAttribute("msg","no Items is added to cart");
			return "Home";
			
		}
		model.addAttribute("cartItem",cartItem);
		model.addAttribute("gtotal",c.getGroand_total());
		session.setAttribute("items",c.getTotal_item());
		session.setAttribute("cartId",c.getCart_id());
		return "Viewcart";
}
//	else
//	{
		return "redirect:/viewcart";
//	}
}
@RequestMapping(value="/Remove/{carti_id}")
public ModelAndView RemoveFromCart(@PathVariable("carti_id") String id)
{
	ModelAndView obj =new ModelAndView("redirect:/viewcart");
	cartItem =cartItemDao.GetCartItem(id);
	Cart c=cartItem.getCart();
	c.setGroand_total(c.getGroand_total()-cartItem.getPrice());
	c.setTotal_item(c.getTotal_item()-1);
	cartDao.saveorupdate(c);
	
	cartItemDao.delete(cartItem);
	return obj;
}
@RequestMapping(value="/Removeall")
public String RemoveallFromCart(Model model,HttpSession session)
{
	Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
	if(!(authentication instanceof AnonymousAuthenticationToken))
	{
		String currusername= authentication.getName();
		UserInfo u =userDao.getUseremail(currusername);
		Cart c= cartDao.GetCart(u.getCart().getCart_id());
		List<CartItem> cartItem=cartItemDao.getlist(u.getCart().getCart_id());
		for(CartItem g:cartItem)
		{
			cartItemDao.delete(g);
		}
		c.setGroand_total(0.0);
		c.setTotal_item(0);
		cartDao.saveorupdate(c);
		session.setAttribute("items", c.getTotal_item());
		return "redirect:/viewcart";
}
	else
	{
		return "redirect:/";
	}
}
}