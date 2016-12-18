package se.claremont.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HelloController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    String hello() {
        return "hello";
    }
}