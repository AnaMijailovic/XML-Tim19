package com.ftn.scientific_papers.controller;

import com.ftn.scientific_papers.dto.BasicUserInfoDTO;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.security.TokenUtils;
import com.ftn.scientific_papers.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/reviewers")
@CrossOrigin()
public class ReviewerController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<List<BasicUserInfoDTO>> GetAllReviewers() {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        List<TUser> users = userService.findAll();
        List<BasicUserInfoDTO> response = new ArrayList<>();
        for (TUser u: users) {
            if (u.getUserId() != user.getUserId()) {
                BasicUserInfoDTO dto = new BasicUserInfoDTO();
                dto.setUserId(u.getUserId());
                dto.setFullName(u.getName() + " " + u.getSurname());
                response.add(dto);
            }
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
