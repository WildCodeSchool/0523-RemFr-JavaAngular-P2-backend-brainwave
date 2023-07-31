package com.templateproject.api.controller;

import com.templateproject.api.entity.Contact;
import com.templateproject.api.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private EmailSenderService emailSenderService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.put("contact", new Contact());
        return "/contact";
    }

    @PostMapping("")
    public String send(@RequestBody Contact contact) {
        try {
            String content = "Nom: " + contact.getName() + "<br/>" + "Adresse: " + contact.getAddress() + "<br/>" + "Message: " + contact.getContent();
            emailSenderService.sendEmail(contact.getName(), contact.getAddress(), "antoine.montauban@gmail.com", content);
            return "Votre message a bien été envoyé !";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
