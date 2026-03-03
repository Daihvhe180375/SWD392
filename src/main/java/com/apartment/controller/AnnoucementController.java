package com.apartment.controller;

import com.apartment.dto.AnnoucementDTO;
import com.apartment.model.Annoucement;
import com.apartment.repository.AnnoucementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller

public class AnnoucementController {
    @Autowired
    private AnnoucementRepository annoucementRepository;

    @GetMapping("/create_annoucement")
    public String createAnnoucement(Model model) {
        model.addAttribute("annoucement", new AnnoucementDTO());
        return "home/create_annoucement";
    }

    @PostMapping
    public String createAnnoucement(Model model, AnnoucementDTO annoucementDTO) {
        Annoucement annoucement = new Annoucement();
        annoucement.setTitle(annoucementDTO.getTitle());
        annoucement.setContent(annoucementDTO.getContent());
        annoucement.setCreatedDate(new Date());
        annoucementRepository.save(annoucement);
        return "redirect:/home?success";
    }
}
