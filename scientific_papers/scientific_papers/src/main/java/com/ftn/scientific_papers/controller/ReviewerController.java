package com.ftn.scientific_papers.controller;

import com.ftn.scientific_papers.dto.BasicUserInfoDTO;
import com.ftn.scientific_papers.dto.PublishingProcessDTO;
import com.ftn.scientific_papers.dto.ReviewRequestDTO;
import com.ftn.scientific_papers.mapper.PublishingProcessMapper;
import com.ftn.scientific_papers.mapper.ReviewRequestMapper;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.publishing_process.VersionReview;
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
    private PublishingProcessMapper publishingProcessMapper;

    @Autowired
    private ReviewRequestMapper reviewRequestMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<List<BasicUserInfoDTO>> getAllReviewers() {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        List<TUser> users = userService.findAll();
        List<BasicUserInfoDTO> response = new ArrayList<>();
        for (TUser u: users) {
            if (!u.getUserId().equals(user.getUserId())) {
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
    public ResponseEntity<PublishingProcessDTO> assignReviewer(@PathVariable("processId") String processId, @PathVariable("reviewerId") String reviewerId) {
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

        if (!checkIfReviewer(user.getRoles().getRole())) {
            user.getRoles().getRole().add(new TRole("ROLE_REVIEWER"));
            userService.update(user);
        }

        publishingProcessService.assignReviewer(process, user);

        ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
        PublishingProcessDTO publishingProcessDTO = publishingProcessMapper.toDTO(scientificPaper, process, process.getLatestVersion().intValue()-1);
        return new ResponseEntity(publishingProcessDTO, HttpStatus.OK);
    }


    @GetMapping(value ="/reviewRequests")
    @PreAuthorize("hasRole('ROLE_REVIEWER')")
    public ResponseEntity<ReviewRequestDTO> getPendingReviewRequests() {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        List<ReviewRequestDTO> result = new ArrayList<>();
        List<PublishingProcess> processes = publishingProcessService.getReviewRequestForUser(user.getUserId());

        for (PublishingProcess process: processes) {
            if (!isInOngoingProcess(process))
                continue;

            ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
            ReviewRequestDTO dto = reviewRequestMapper.toDTO(scientificPaper, process);
            result.add(dto);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value ="/assignedReviews")
    @PreAuthorize("hasRole('ROLE_REVIEWER')")
    public ResponseEntity<ReviewRequestDTO> getAssignedReviews() {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        List<ReviewRequestDTO> result = new ArrayList<>();
        List<PublishingProcess> processes = publishingProcessService.getAssignedReviewsForUser(user.getUserId());

        for (PublishingProcess process: processes) {
            if (!isInOngoingProcess(process))
                continue;

            ScientificPaper scientificPaper = scientificPaperService.findOneUnmarshalled(process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getScientificPaperId());
            ReviewRequestDTO dto = reviewRequestMapper.toDTO(scientificPaper, process);
            result.add(dto);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping(value="/accept/{processId}")
    @PreAuthorize("hasRole('ROLE_REVIEWER')")
    public ResponseEntity acceptReview(@PathVariable("processId") String processId) {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);

        if (process == null) {
            return new ResponseEntity("Invalid process id", HttpStatus.NOT_FOUND);
        }

        publishingProcessService.acceptReview(process, user.getUserId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value="/reject/{processId}")
    @PreAuthorize("hasRole('ROLE_REVIEWER')")
    public ResponseEntity rejectReview(@PathVariable("processId") String processId) {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);

        if (process == null) {
            return new ResponseEntity("Invalid process id", HttpStatus.NOT_FOUND);
        }

        publishingProcessService.rejectReview(process, user.getUserId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value="/paper/{processId}")
    @PreAuthorize("hasRole('ROLE_REVIEWER')")
    public ResponseEntity<String> paperForReviewer(@PathVariable("processId") String processId) {
        String username = tokenUtils.getUsernameFromRequest(request);
        TUser user = userService.findByUsername(username);

        PublishingProcess process = publishingProcessService.findOneUnmarshalled(processId);
        if (process == null) {
            return new ResponseEntity("Invalid process id", HttpStatus.NOT_FOUND);
        }

        PublishingProcess.PaperVersion latestVersion = process.getPaperVersion().get(process.getLatestVersion().intValue()-1);

        boolean isReviewerOnPaper = false;
        for (VersionReview review : latestVersion.getVersionReviews().getVersionReview()) {
            if (review.getReviewerId().equals(user.getUserId())) {
                isReviewerOnPaper = true;
            }
        }

        if (!isReviewerOnPaper) {
            return new ResponseEntity<>("No rights to see paper.", HttpStatus.BAD_REQUEST);
        }

        String paperId = latestVersion.getScientificPaperId();

        // TODO: return paper without author
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isInOngoingProcess(PublishingProcess process) {
        String status = process.getStatus();

        if (status.equals("ACCEPTED") || status.equals("REJECTED") || status.equals("WITHDRAWN"))
            return false;
        return true;
    }

    private boolean checkIfReviewer(List<TRole> roles) {
        for (TRole role: roles) {
            if (role.getRole().equals("ROLE_REVIEWER")) {
                return true;
            }
        }
        return false;
    }
}
