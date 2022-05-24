package com.computer.parts.shop.Router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RouteController {
    @RequestMapping(value = "/user/**", method = RequestMethod.GET)
    public String redirect() {
        return "forward:/";
    }
    @RequestMapping(value = "/admin/**", method = RequestMethod.GET)
    public String redirectAdmin() {
        return "forward:/";
    }
    @RequestMapping(value = "/p/**", method = RequestMethod.GET)
    public String redirectProduct() {
        return "forward:/";
    }
    @RequestMapping(value = "/products/**", method = RequestMethod.GET)
    public String redirectProductList() {
        return "forward:/";
    }
}
