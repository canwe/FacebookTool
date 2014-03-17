package com.akosha.tool.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login 
{
	@RequestMapping(value="/index")
	public String index()
	{
		System.out.println("coming here");
		return "index";
	}
}
