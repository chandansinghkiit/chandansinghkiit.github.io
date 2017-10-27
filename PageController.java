package com.mystyle.onlineshopping.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mystyle.onlineshoppingbackend.dto.Category;
import com.mystyle.onlineshoppingbackend.dto.Product;
import com.mystyle.onlineshopping.exception.ProductNotFoundException;
import com.mystyle.onlineshoppingbackend.dao.CategoryDAO;
import com.mystyle.onlineshoppingbackend.dao.ProductDAO;

@SuppressWarnings("unused")
@Configuration
@ComponentScan(basePackages={"com.mystyle.onlineshoppingbackend.dto"})
@ComponentScan(basePackages={"com.mystyle.onlineshoppingbackend.daoimpl"})
@Controller
public class PageController {
	
	
	private static final Logger logger=LoggerFactory.getLogger(PageController.class);
	
	@Autowired(required=true)
    private CategoryDAO categoryDAO;
	
	@Autowired(required=true)
    private ProductDAO productDAO;
	

	 

	@RequestMapping(value = { "/", "/index", "/home" })
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "home");
		

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		mv.addObject("categories", categoryDAO.list());
		mv.addObject("userClickHome", true);
		return mv;
	}

	@RequestMapping(value = "/about")
	public ModelAndView about() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "About us");
		mv.addObject("userClickAbout", true);
		return mv;
	}

	@RequestMapping(value = "/contact")
	public ModelAndView contact() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Contact us");
		mv.addObject("userClickContact", true);
		return mv;
	}
	
	
	/*Method to load all product based on category*/
	
	@RequestMapping(value ="/show/all/products")
	public ModelAndView showAllProducts() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "All Products");

		mv.addObject("categories", categoryDAO.list());
		mv.addObject("userClickAllProducts", true);
		return mv;
	}
	
	@RequestMapping(value ="/show/category/{id}/products")
	public ModelAndView showCategoryProducts(@PathVariable("id") int id) {
		ModelAndView mv = new ModelAndView("page");
		
		//categoryDAO to fetch a single Category
		Category  category=null;
		category=categoryDAO.get(id);
		
		mv.addObject("title",category.getName());
//passing the list of categories
		mv.addObject("categories", categoryDAO.list());
		//passing the single category object
		mv.addObject("category",category);
		mv.addObject("userClickCategoryProducts", true);
		return mv;
	}

	
	//viewing a single product
	
	@RequestMapping(value="/show/{id}/product")
	public ModelAndView showSingleProduct(@PathVariable int id)throws ProductNotFoundException
	{
		ModelAndView mv= new ModelAndView("page");
		
		Product product=productDAO.get(id);
		
		if(product==null) throw new ProductNotFoundException();
		
		//view count update
		product.setViews(product.getViews() + 1);
		productDAO.update(product);
		
		////////////////////
		mv.addObject("title",product.getName());
		mv.addObject("product",product);
		mv.addObject("userClickShowProduct",true);
		
		
		
		
		
		return  mv;
	}
	
}
