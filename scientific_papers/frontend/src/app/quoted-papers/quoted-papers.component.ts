import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ScientificPaper } from '../_model/scientificPaper.model';

@Component({
  selector: 'app-quoted-papers',
  templateUrl: './quoted-papers.component.html',
  styleUrls: ['./quoted-papers.component.scss']
})
export class QuotedPapersComponent implements OnInit {

  papers: ScientificPaper[];

  constructor(private router: Router,
              private spService: ScientificPaperService) { }

  ngOnInit() {
    const splited = this.router.url.split('/');
    const id = splited[splited.length - 1];
    this.getScientificPapers(id);

  }

  getScientificPapers(paperId: string) {
    this.papers = [];

    this.spService.getQuotedBy(paperId).subscribe(
      (response) => {
        this.papers = this.spService.responseToArray(response);
      });

  }

}
