package com.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cas.delegate.LoginDelegate;
import com.cas.model.User;

@Controller
public class LoginController {
	@Autowired
	private LoginDelegate loginDelegate;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView displayLogin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("loginpage");
		User user = new User();
		model.addObject("user", user);
		return model;
	}

	@RequestMapping(value = "/performlogin", method = RequestMethod.POST)
	public String performLogin(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("user") User user) {
		ModelAndView model = null;
		try {
			User returnedUser = loginDelegate.isValidUser(user.getEmailId(), user.getPassword());
			if (returnedUser != null) {
				System.out.println("User Login Successful");
				request.setAttribute("loggedInUser", returnedUser.getUsername());
				request.getSession().setAttribute("LOGGEDIN_USER", user);
				return "redirect:/welcome";
			} else {
				model = new ModelAndView("loginpage");
				model.addObject("user", user);
				request.setAttribute("message", "Invalid credentials!");
				return "redirect:/login";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";

	}
}
