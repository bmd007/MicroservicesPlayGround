package ir.tiroon.microservices

import ir.tiroon.microservices.service.OauthClientDetailServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.security.Principal

@RestController
class HttpController {

    @GetMapping(path = '/admin')
    ResponseEntity admin(Principal principal) {
        new ResponseEntity("Admin is " + principal.getName(), HttpStatus.OK)
    }

    @GetMapping(path = '/user')
    ResponseEntity user(Principal principal) {
        new ResponseEntity("User is " + principal.getName(), HttpStatus.OK)
    }

    @GetMapping(path = '/accessDenied')
    ResponseEntity accessDenied() {
        new ResponseEntity("Access Denied ", HttpStatus.OK)
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth)
            SecurityContextHolder.getContext().setAuthentication(null)
        }
    }


    @Autowired
    OauthClientDetailServices clientDetailServices

    @GetMapping(path = '/new/client/{id}/{secret}')
    ResponseEntity register(@PathVariable("id") String id, @PathVariable("secret") String secret) {
        clientDetailServices.persist(id, secret)
        new ResponseEntity(HttpStatus.OK)
    }


}