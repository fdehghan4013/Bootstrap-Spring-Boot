/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.domain.Agent;
import com.example.demo.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author rbabaei
 */
@Controller
@Slf4j
public class HomeController {
    private final AgentService agentService;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    public HomeController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("agents", agentService.getAllDecryptedAgentsWithKeys());

        log.debug("Home Page");
        return "home";
    }

    @GetMapping("/newAgent")
    public String getAddingNewAgentPage(Agent agent) {
        return "add-agent";
    }


    @PostMapping("/addAgent")
    public String addUser(@Validated Agent agent, BindingResult result) {
        String response = "redirect:/";

        if (result.hasErrors()) {
            log.error(result.toString());
            return response;
        }

        agentService.generateNewAgent(agent);

        log.debug("Save ok.");
        return response;
    }

    @GetMapping("/newKey/{agentId}")
    public String newKey(@PathVariable(value="agentId") Long agentId) {
        String response = "redirect:/";
        try {
            agentService.generateNewKeyForAgent(agentId);
        } catch (IllegalArgumentException ex) {
            return response;
        }

        log.debug("Save ok.");
        return response;
    }

}
