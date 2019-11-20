package com.niit.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.niit.Dao.BillingDao;
import com.niit.Dao.CartDao;
import com.niit.Dao.CartItemDao;
import com.niit.Dao.OrderDao;
import com.niit.Dao.OrderItemDao;
import com.niit.Dao.PayDao;
import com.niit.Dao.ProductDao;
import com.niit.Dao.ShippingAddressDao;
import com.niit.Dao.UserDao;
import com.niit.model.Billing;
import com.niit.model.Cart;
import com.niit.model.CartItem;
import com.niit.model.Order;
import com.niit.model.OrderItem;
import com.niit.model.Pay;
import com.niit.model.ProductInfo;
import com.niit.model.ShippingAddress;
import com.niit.model.UserInfo;
import com.niit.otp.OtpGenerator;

@Controller
public class OrderController 
{
@Autowired
Cart cart;
@Autowired
CartDao cartDao;
@Autowired
CartItem cartItem;
@Autowired
CartItemDao cartItemDao;
//@Autowired
//Card card;
@Autowired
Billing billing;
@Autowired
BillingDao billingDao;
@Autowired
ShippingAddress shippingAddress;
@Autowired
ShippingAddressDao shippingAddressDao;
@Autowired
Pay pay;
@Autowired
PayDao payDao;
@Autowired
Order order;
@Autowired
OrderDao orderDao;
@Autowired
OrderItem orderItem;
@Autowired
OrderItemDao orderItemDao;
@Autowired
ProductInfo productInfo;
@Autowired
ProductDao productDao;
@Autowired
UserInfo userInfo;
@Autowired
UserDao userDao;
@Autowired
List<CartItem> cartItem1;

@Autowired
private JavaMailSender mailSender;
	
String o;

@RequestMapping("/Buyall")
public String orderall(Model model,HttpSession session)
{
	Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
	if(!(authentication instanceof AnonymousAuthenticationToken))
	{
		String currusername= authentication.getName();
		userInfo = userDao.getUseremail(currusername);
		cart= userInfo.getCart();
		
		session.setAttribute("products", productInfo);
		cartItem=cartItemDao.GetCartItem(cart.getCart_id());
		
		billing=billingDao.GetBilling(userInfo.getuId());
		List<ShippingAddress> shippingAddresies=shippingAddressDao.getaddbyUserInfo(userInfo.getuId());
		
		model.addAttribute("billing",billing);
		model.addAttribute("userInfo",userInfo);
		model.addAttribute("shippingAddresies",shippingAddresies);
		model.addAttribute("shippingAddress",new ShippingAddress());
		session.setAttribute("p",productInfo);
	}
	return "AddressShipping";
}

@RequestMapping("/Buy/{prdid}/{carti_id}")
public String order(@PathVariable("prdid") String id,Model model,HttpSession session)
{
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if(!(authentication instanceof AnonymousAuthenticationToken))
	{
		String currusername= authentication.getName();
		userInfo = userDao.getUseremail(currusername);
		cart= userInfo.getCart();
		cartItem=null;
		productInfo=productDao.Getproduct(id);
		billing=billingDao.GetBilling(userInfo.getuId());
		
	System.out.println(billing.getCountry());
//		for(Billing b: billing)
//		{
//			System.out.println(b.getbId());
//			System.out.println(b.getCountry());
//		}
      List<ShippingAddress> shippingAddresies=shippingAddressDao.getaddbyUserInfo(userInfo.getuId());
		userInfo.setBilling(billing);
		model.addAttribute("billing",billing);
		model.addAttribute("userInfo",userInfo);
		model.addAttribute("shippingAddresies",shippingAddresies);
		model.addAttribute("shippingAddress",new ShippingAddress());
		session.setAttribute("p",productInfo);
		return "AddressShipping";
	}
	else
	{
		return"redirect:/";
	}
}

@RequestMapping("/orderConfirm")
public String payment(@ModelAttribute("shippingAddress")ShippingAddress sh,Model model)
{
//	if(cartItem==null || cartItem.isEmpty())
//	{
//		System.out.println("sorry");
//	}
//	List<ProductInfo> prod=productDao.list();
	sh.setUserInfo(userInfo);
	shippingAddress=sh;
	model.addAttribute("billing", billing);
	model.addAttribute("shippingAddress", shippingAddress);
	model.addAttribute("prot", productInfo);
//	model.addAttribute("prod", prod);
	model.addAttribute("cartItem",cartItem);
	model.addAttribute("cart",cart);
	return "orderconfirm";
}

@RequestMapping("/previous")
public String previous(Model model)
{
	List<ShippingAddress> shippingAddresies=shippingAddressDao.getaddbyUserInfo(userInfo.getuId());
	model.addAttribute("shippingAddresies",shippingAddresies);
	model.addAttribute("billing",billing);
	model.addAttribute("shippingAddress", shippingAddress);
	model.addAttribute("productInfo",productInfo);
	return "addressorder";
}

@RequestMapping("/pay")
public String pay(Model model)
{
//	List<Card> cards=cardDao.getcardbyUserInfo(userInfo.getuId());
//	model.addAttribute("cards",cards);
//	model.addAttribute("card",new Card());

	return "Payment";
}

//@RequestMapping("/Payment")
//public String payment(@RequestParam("otp") String otp,Model model)
//{
//	int a;
//	if(otp==null)
//		a=2;
//	else
//		a=1;
//	switch (a)
//	{
//	case 1:
//		if(otp.equals(o))
//		{
//			pay.setPay_method("COD");
//			pay.setStatus("NO");
//			break;
//		}
//		else
//		{
//			return "redirect:/pay";
//		
//		}
//	case 2:
//		pay.setPay_method("Card");
//		pay.setStatus("yes");
//		payDao.saveorupdate(pay);
//		//cardDao.saveorupdate(car);
//		break;
//		
//	}
//	return "redirect:/orderconformation";
//	
//}

@RequestMapping("/payment")
public String payment(@RequestParam("payb2") String str,Model model)
{
	System.out.println(1324);
//	int a;
	System.out.println(str);
	if(str.equals(o))
	{
		return "redirect:/Thankyou";
	}
	return "redirect:/Thankyou";
	
	
}
@RequestMapping("/orderconfirmation")
public String orderconfirmation(HttpSession session)
{
	System.out.println(32);
	order.setBilling(billing);
	order.setShippingAddress(shippingAddress);
	order.setPay(pay);
	order.setUserInfo(userInfo);
	System.out.println(524);
	if(cartItem == null)
	{
		order.setGroand_total(productInfo.getPrice());
		orderDao.saveorupdate(order);
		orderItem.setOrder(order);
		//orderItem.setProductid(productInfo.getPrdid());
		orderItemDao.saveorupdate(orderItem);
		cart.setGroand_total(cart.getGroand_total() - cartItem.getPrice());
		cart.setTotal_item(cart.getTotal_item()-1);
		session.setAttribute("items", cart.getTotal_item());
		cartDao.saveorupdate(cart);
		//cartItemDao.delete(cartItemDao.getlistall(cart.getCart_id(),productInfo.getPrdid()));
		System.out.println(324);
	}
	else
	{
		System.out.println(656);
		order.setGroand_total(cart.getGroand_total());
		orderDao.saveorupdate(order);
		for(CartItem c: cartItem1)
		{
			System.out.println(3444);
			orderItem.setOrder(order);
			orderItem.setProduct_Item(c.getProductInfo().getPrdid());
			System.out.println(3443);
			orderItemDao.saveorupdate(orderItem);
			cartItemDao.delete(c);
		}
		cart.setGroand_total(0.0);
		cart.setTotal_item(0);
		System.out.println(346);
		session.setAttribute("items", cart.getTotal_item());
		cartDao.saveorupdate(cart);
		
	}
	cartItem=null;
	cartItem1=null;
	productInfo=null;
	order=new Order();
	orderItem=new OrderItem();
	System.out.println(565);
	return "Thankyou";
	
	
}

@RequestMapping(value="/SendMail")
public void SendMail() {
System.out.println(21312);
Authentication authentication =
SecurityContextHolder.getContext().getAuthentication();
if (!(authentication instanceof AnonymousAuthenticationToken)) {
String currusername = authentication.getName();
userInfo = userDao.getUseremail(currusername);
OtpGenerator ot=new OtpGenerator();
// String o=ot.Otpga();
o=ot.Otpga();
String recipientAddress = userInfo.getEmail();
String subject="OTP";
//String subject = request.getParameter("subject");
String message = "your one time password is "+o+" ";

// prints debug info
System.out.println("To:" + recipientAddress);
System.out.println("Subject: " + subject);
System.out.println("Message: " + message);


//System.out.println("OTP:"+ otp);
// creates a simple e-mail object
SimpleMailMessage email = new SimpleMailMessage();
email.setTo(recipientAddress);
email.setSubject(subject);
email.setText(message);
//email.setSubject(otp);
// sends the e-mail
mailSender.send(email);


// forwards to the view named "Result"
//return "Result";
}
}
}


