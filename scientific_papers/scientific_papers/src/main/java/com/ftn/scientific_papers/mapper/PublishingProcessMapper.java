package com.ftn.scientific_papers.mapper;

import com.ftn.scientific_papers.dto.PublishingProcessDTO;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.scientific_paper.Author;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import com.ftn.scientific_papers.model.user.TUser;
import com.ftn.scientific_papers.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublishingProcessMapper {

    @Autowired
    private CustomUserDetailsService userService;

    public PublishingProcessDTO toDTO(ScientificPaper scientificPaper, PublishingProcess process) {
        PublishingProcessDTO publishingProcessDTO = new PublishingProcessDTO();
        publishingProcessDTO.setProcessId(process.getId());

        publishingProcessDTO.setPaperTitles(formatTitle(scientificPaper.getHead().getTitle()));
        publishingProcessDTO.setAuthors(formatAuthors(scientificPaper.getHead().getAuthor()));
        publishingProcessDTO.setStatus(process.getStatus());

        publishingProcessDTO.setVersion(process.getLatestVersion().toString());

        PublishingProcess.PaperVersion.VersionReviews reviews = process.getPaperVersion().get(process.getLatestVersion().intValue()-1).getVersionReviews();
        if (reviews == null) {
            publishingProcessDTO.setReviewers(new ArrayList<>());
        } else {
            publishingProcessDTO.setReviewers(formatReviewers(reviews));
        }

        if (process.getEditorId().equals("")) {
            publishingProcessDTO.setEditorUsername(null);
            publishingProcessDTO.setEditorName("");
        } else {
            TUser editor= userService.findById(process.getEditorId());
            publishingProcessDTO.setEditorUsername(editor.getUsername());
            publishingProcessDTO.setEditorName(editor.getName() + " "  + editor.getSurname());
        }

        return  publishingProcessDTO;
    }

    private List<String> formatReviewers(PublishingProcess.PaperVersion.VersionReviews versionReviews) {
        List<String> reviewers = new ArrayList<>();

        for (int i = 0; i < versionReviews.getVersionReview().size(); i++) {
            TUser author = userService.findById(versionReviews.getVersionReview().get(i).getReviewerId());
            reviewers.add(author.getName() + " "  + author.getSurname());
        }

        return reviewers;
    }


    private List<String> formatAuthors(List<Author> authors) {
        List<String> authorsStr = new ArrayList<>();
        for (int i = 0; i < authors.size(); i++) {
            String authorName = authors.get(i).getFirstName() + " " + authors.get(i).getLastName();
            authorsStr.add((authorName));
        }
        return authorsStr;
    }

    private List<String> formatTitle(List<ScientificPaper.Head.Title> titles) {
        List<String> titlesStr = new ArrayList<>();

        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i).getValue();
            titlesStr.add(title);
        }

        return titlesStr;
    }
}
