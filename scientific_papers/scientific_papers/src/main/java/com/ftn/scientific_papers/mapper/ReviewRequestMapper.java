package com.ftn.scientific_papers.mapper;

import com.ftn.scientific_papers.dto.ReviewRequestDTO;
import com.ftn.scientific_papers.model.publishing_process.PublishingProcess;
import com.ftn.scientific_papers.model.scientific_paper.ScientificPaper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewRequestMapper {

    public ReviewRequestDTO toDTO(ScientificPaper scientificPaper, PublishingProcess process) {
        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setProcessId(process.getId());
        reviewRequestDTO.setPaperId(scientificPaper.getId());
        reviewRequestDTO.setPaperTitles(formatTitle(scientificPaper.getHead().getTitle()));
        reviewRequestDTO.setVersion(process.getLatestVersion().toString());
        return  reviewRequestDTO;
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
