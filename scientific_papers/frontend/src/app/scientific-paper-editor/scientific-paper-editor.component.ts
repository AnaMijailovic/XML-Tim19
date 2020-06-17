import { Component, OnInit } from '@angular/core';
import { XonomyService } from '../_service/xonomy.service';
import { Router } from '@angular/router';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ToastrService } from 'ngx-toastr';

declare const Xonomy: any;

@Component({
  selector: 'app-scientific-paper-editor',
  templateUrl: './scientific-paper-editor.component.html',
  styleUrls: ['./scientific-paper-editor.component.scss']
})
export class ScientificPaperEditorComponent implements OnInit {

  scientificPaper: string;
  processId: string;

  revisionProcessId: string;
  revisionPaperTitle: string;


  constructor(private xonomyService: XonomyService,
              private spService: ScientificPaperService,
              private router: Router,
              private toastr: ToastrService) { }

  ngOnInit() {
    const revisionData =  JSON.parse(localStorage.getItem('revisionData'));
    if (revisionData) {
      this.revisionPaperTitle = revisionData.paperTitle;
      this.revisionProcessId = revisionData.processId;
      localStorage.removeItem('revisionData');
    }

    this.setXonomy();

  }

  setXonomy() {
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
    // alert(Xonomy.harvest() as string);
    this.scientificPaper = Xonomy.harvest() as string;
    this.submitPaper();
  }

  loadSpTemplate() {
    this.spService.getTemplate().subscribe(
      (response => {
        const xonomy = document.getElementById('xonomy-editor');
        Xonomy.render(response, xonomy, this.xonomyService.scientificPaperElement);
      }), (error => {
        if (error.error.exception) {
          this.toastr.error('Error', error.error.exception);
        } else {
          this.toastr.error('Error', 'Some error happend');
          console.log(JSON.stringify(error));
        }
      })
    );
  }

  submitPaper() {

    let postReq: any;
    if (this.revisionPaperTitle ) {
      postReq = this.spService.addPaperReview(this.scientificPaper, this.revisionProcessId);
    } else {
      postReq = this.spService.addScientificPaper(this.scientificPaper);
    }

    postReq.subscribe(
      (response => {
        this.toastr.success('Success', 'Scientific paper submitted');
        this.processId = response.toString();
        localStorage.setItem('processId', this.processId);
      }), (error => {
        if (error.error.text !== undefined && error.error.text.startsWith('process')) {
          this.toastr.success('Success', 'Scientific paper submitted');
          this.processId = error.error.text;
          localStorage.setItem('processId', this.processId);
        } else {
          if (error.error.exception) {
            this.toastr.error('Error', error.error.exception);
          } else {
            this.toastr.error('Error', 'Some error happend');
            console.log(JSON.stringify(error));
          }
        }
      })
    );
  }

  openClEditor() {
    if (this.processId === '' || this.processId === undefined || this.processId === null ) {
      this.toastr.error('Error', 'You must submit scientific paper first');
      return;
    }

    this.router.navigate(['/add-cover-letter-editor']);
  }

}
