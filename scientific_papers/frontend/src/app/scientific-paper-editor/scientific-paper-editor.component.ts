import { Component, OnInit } from '@angular/core';
import { XonomyService } from '../_service/xonomy.service';
import { Router } from '@angular/router';

declare const Xonomy: any;

@Component({
  selector: 'app-scientific-paper-editor',
  templateUrl: './scientific-paper-editor.component.html',
  styleUrls: ['./scientific-paper-editor.component.scss']
})
export class ScientificPaperEditorComponent implements OnInit {

  scientificPaper: string;

  constructor(private xonomyService: XonomyService,
              private router: Router) { }

  ngOnInit() {
     // TODO Get template from backend
     this.scientificPaper = '<scientific_paper xmlns:jxb="http://java.sun.com/xml/ns/jaxb" ' +
     'xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc" ' +
     'xmlns:pred="https://github.com/AnaMijailovic/XML-Tim19/predicate/" ' +
     'xmlns:rdfa="http://www.w3.org/ns/rdfa#" ' +
     'xmlns="https://github.com/AnaMijailovic/XML-Tim19" ' +
     'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ' +
     'xmlns:xs="http://www.w3.org/2001/XMLSchema#" id="" status="" version=""></scientific_paper>';
     const xonomy = document.getElementById('xonomy-editor');
     Xonomy.render(this.scientificPaper, xonomy, this.xonomyService.scientificPaperElement);
  }

  addPaperXonomy() {
    alert(Xonomy.harvest() as string);
    this.scientificPaper = Xonomy.harvest() as string;
    this.submitPaper();
  }

  submitPaper() {
    alert('Submit');
  }

  loadSpTemplate() {
    alert('Load template');
  }

  openClEditor() {
    alert('openClEditor');
    this.router.navigate(['/add-cover-letter-editor']);
  }

}
