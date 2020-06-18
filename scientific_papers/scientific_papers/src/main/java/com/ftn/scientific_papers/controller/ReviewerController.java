package com.ftn.scientific_papers.controller;

import com.ftn.scientific_papers.dto.BasicUserInfoDTO;
import com.ftn.scientific_papers.dto.PublishingProcessDTO;
import com.ftn.scientific_papers.mapper.PublishingProcessMapper;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.model.user.TRole;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.security.TokenUtils;
import com.ftn.scientific_papers.service.CustomUserDetailsService;
import com.ftn.scientific_papers.service.PublishingProcessService;
import com.ftn.scientific_papers.service.ScientificPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private PublishingProcessService publishingProcessService;

    @Autowired
    private ScientificPaperService scientificPaperService;

    @Autowired
    private PublishingProcessMapper mapper;

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

    @PutMapping(value = "/assign/{processId},{reviewerId}")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<PublishingProcessDTO> AssignReviewer(@PathVariable("processId") String processId, @PathVariable("reviewerId") String reviewerId) {
        PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);

        if (process == null) {
            return new ResponseEntity("Invalid process id", HttpStatus.NOT_FOUND);
        }

        if (!process.getStatus().equals("NEW_SUBMISSION") &&
            !process.getStatus().equals("NEW_REVIEWER_NEEDED") &&
            !process.getStatus().equals("NEW_REVISION")) {
            return new ResponseEntity("Publishing process status is not valid for assigning review", HttpStatus.BAD_REQUEST);
        }

        TUser user = userService.findById(reviewerId);

        if (!user.getRoles().getRole().contains("ROLE_REVIEWER")) {
            user.getRoles().getRole().add(new TRole("ROLE_REVIEWER"));
            userService.update(user);
        }

        publishingProcessService.assignReviewer(process, user);

        ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
        PublishingProcessDTO publishingProcessDTO = mapper.toDTO(scientificPaper, process);
        return new ResponseEntity(publishingProcessDTO, HttpStatus.OK);
    }
}
