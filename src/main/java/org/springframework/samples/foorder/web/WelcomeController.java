package org.springframework.samples.foorder.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WelcomeController {
	
	 @GetMapping("")
	  public String welcome(Map<String, Object> model) {	    
	    return "welcome";
	  }
	  
	  @GetMapping("/login")
      public String login() {
          return "login";
      }
	  
	  @GetMapping("/home")
      public String home() {
          return "welcome";
      }
	  
	  @PostMapping("/logout")
      public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          if (auth != null){
              new SecurityContextLogoutHandler().logout(request, response, auth);
          }
          return "redirect:/login";
      }
}
