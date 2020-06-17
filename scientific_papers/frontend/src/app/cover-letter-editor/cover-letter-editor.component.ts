import { Component, OnInit } from '@angular/core';
import { XonomyService } from '../_service/xonomy.service';
import { Router } from '@angular/router';

declare const Xonomy: any;

@Component({
  selector: 'app-cover-letter-editor',
  templateUrl: './cover-letter-editor.component.html',
  styleUrls: ['./cover-letter-editor.component.scss']
})
export class CoverLetterEditorComponent implements OnInit {

  coverLetter: string;

  constructor(private xonomyService: XonomyService,
              private router: Router) { }

  ngOnInit() {
     // TODO Get template from backend
     this.coverLetter = '<scientific_paper xmlns:jxb="http://java.sun.com/xml/ns/jaxb" ' +
     'xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc" ' +
     'xmlns:pred="https://github.com/AnaMijailovic/XML-Tim19/predicate/" ' +
     'xmlns:rdfa="http://www.w3.org/ns/rdfa#" ' +
     'xmlns="https://github.com/AnaMijailovic/XML-Tim19" ' +
     'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ' +
     'xmlns:xs="http://www.w3.org/2001/XMLSchema#" id="" status="" version=""></scientific_paper>';
     const xonomy = document.getElementById('xonomy-letter-editor');
     Xonomy.render(this.coverLetter, xonomy, this.xonomyService.scientificPaperElement);
  }

  addLetterXonomy() {
    alert(Xonomy.harvest() as string);
    this.coverLetter = Xonomy.harvest() as string;
    this.submitLetter();
  }

  submitLetter() {
    alert('Submit');
  }

  loadClTemplate() {
    alert('Load template');
  }

}
