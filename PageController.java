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
Global error handling 
=============================================================
package com.mystyle.onlineshopping.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handlerNoHandlerFoundException()
	{
		ModelAndView mv= new ModelAndView("error");
		
		mv.addObject("errorTitle","Page not constructed !!!!!");
        mv.addObject("errorDescription","The page you looking not available !!!!!");

		mv.addObject("title"," 404 Error page");
		return mv;
		
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ModelAndView handleProductNotFoundException()
	{
		ModelAndView mv= new ModelAndView("error");
		
		mv.addObject("errorTitle","Product not available !!!!!");
        mv.addObject("errorDescription","The Product you looking not available right now!!!!!");

		mv.addObject("title","Product unavailble");
		return mv;
		
	}
	
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleNotFoundException(Exception ex)
	{
		ModelAndView mv= new ModelAndView("error");
		
		mv.addObject("errorTitle","Contact your Administrator !!!!!");
		
		//testing 
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		
		ex.printStackTrace(pw);
		
        mv.addObject("errorDescription",sw.toString());

		mv.addObject("title","Error");
		return mv;
		
	}
	
	

}



=============================================================
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="css" value="/resources/css" />

<c:set var="contextRoot" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<!-- Bootstrap Core CSS -->
<link href="${css}/bootstrap.min.css" rel="stylesheet">

<!-- Bootstrap Readable Theme -->
<link href="${css}/bootstrap-readable-theme.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${css}/myapp.css" rel="stylesheet">


<title>Online Shopping - ${title}</title>

</head>

<body>

	<div class="wrapper">

	    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	        <div class="container">
	            <!-- Brand and toggle get grouped for better mobile display -->
	            <div class="navbar-header">
	                <a class="navbar-brand" href="${contextRoot}/home">Home</a>
	            </div>
			</div>
		</nav>
			
		
		<div class="content">
		
			<div class="container">
			
				<div class="row">
				
					<div class="col-xs-12">
					
						
						<div class="jumbotron">
						
							<h1>${errorTitle}</h1>
							<hr/>
						
							<blockquote style="word-wrap:break-word">
								
								${errorDescription}
							
							</blockquote>						
						
						</div>
						
											
					</div>					
				
				</div>
			
			</div>
							
		</div>

		
		<%@include file="./shared/footer.jsp" %>

	</div>
		
	
</body>


</html>

======================================================================