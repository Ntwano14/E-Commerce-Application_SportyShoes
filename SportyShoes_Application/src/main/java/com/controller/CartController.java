package com.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.global.GlobalData;
import com.model.Product;
import com.model.Purchase;
import com.model.User;
import com.repository.PurchaseRepository;
import com.repository.UserRepository;
import com.service.ProductService;

@Controller
public class CartController {
		
	@Autowired
	ProductService productService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PurchaseRepository purchaseRepository;
	
	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable int id) {
		GlobalData.cart.add(productService.getProductById(id).get());
		return "redirect:/shop";
	}
	
	@GetMapping("/cart")
	public String cartGet(Model model) {
		model.addAttribute("cartCount" , GlobalData.cart.size());
		model.addAttribute("total" , GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		model.addAttribute("cart" , GlobalData.cart);
		return "cart";
		
		
	}
	
	@GetMapping("/cart/removeItem/{index}")
	public String cartItemRemove(@PathVariable int index) {
		GlobalData.cart.remove(index);
		return "redirect:/cart";
	}
	
	@GetMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("total" , GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		return "checkout";
	}
	
	@PostMapping("/payNow")
	public String orderConfirmation(Model model) {
		model.addAttribute("total" , GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
		//me Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//me String currentPrincipalName = auth.getName();
		
		List<Purchase>purchaseList = new ArrayList<Purchase>();
		//System.out.println(currentPrincipalName);
		//me User user = userRepository.findUserByEmail(currentPrincipalName).get();
		for(Product product: GlobalData.cart) {
			Purchase purchase = new Purchase();
			//System.out.println(product.getId() + " " + product.getName());
			purchase.setProductId(product.getId());
			//me purchase.setUserId(user.getId());
			purchase.setOrderDate(LocalDate.now());
			purchaseList.add(purchase);
		}
		
		int n = 10000 + new Random().nextInt(90000);
		model.addAttribute("Reciept" , n);
		//System.out.println(purchaseList.toString());
		model.addAttribute("products" , GlobalData.cart);
		purchaseRepository.saveAll(purchaseList);
		
		return "orderPlaced";
	}
	
	
}

