package com.apartment.controller;

import com.apartment.dto.AnnoucementDTO;
import com.apartment.model.Announcement;
import com.apartment.repository.AnnouncementRepository;
import com.apartment.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller

public class AnnouncementController {
    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/create_announcement")
    public String createAnnouncement(Model model) {

        model.addAttribute("announcement", new AnnoucementDTO());
        return "home/create_announcement";
    }

    @PostMapping("/create_announcement")
    public String createAnnouncement(Model model, AnnoucementDTO annoucementDTO) {
        Announcement announcement = new Announcement();
        announcement.setTitle(annoucementDTO.getTitle());
        announcement.setContent(annoucementDTO.getContent());
        announcement.setCreatedDate(new Date());
        announcement.setPriority(annoucementDTO.getPriority());
        announcementRepository.save(announcement);
        return "redirect:/";
    }
}
