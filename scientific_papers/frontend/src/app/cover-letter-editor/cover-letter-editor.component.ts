import { Component, OnInit } from '@angular/core';
import { XonomyCoverLetterService } from '../_service/xonomy-cover-letter.service';
import { CoverLetterService } from '../_service/cover-letter.service';
import { ToastrService } from 'ngx-toastr';

declare const Xonomy: any;

@Component({
  selector: 'app-cover-letter-editor',
  templateUrl: './cover-letter-editor.component.html',
  styleUrls: ['./cover-letter-editor.component.scss']
})
export class CoverLetterEditorComponent implements OnInit {

  coverLetter: string;
  processId: string;

  constructor(private xonomyService: XonomyCoverLetterService,
              private clService: CoverLetterService,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.processId = localStorage.getItem('processId');
    localStorage.removeItem('processId');
    this.setXonomy();

  }

  setXonomy() {
    this.coverLetter = '<cover_letter xmlns="https://github.com/AnaMijailovic/XML-Tim19/cover_letter" ' +
    'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ></cover_letter> ';
    const xonomy = document.getElementById('xonomy-letter-editor');
    Xonomy.render(this.coverLetter, xonomy, this.xonomyService.coverLetterElement);
  }

  addLetterXonomy() {
    // alert(Xonomy.harvest() as string);
    this.coverLetter = Xonomy.harvest() as string;
    this.submitLetter();
  }

  submitLetter() {

    // this.toastr.success('Success', 'About to submit');
    this.clService.addCoverLetter(this.coverLetter, this.processId).subscribe(
      (response => {
        this.toastr.success('Success', 'Cover letter submitted');
        this.processId = response.toString();
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

  loadClTemplate() {
    this.clService.getTemplate().subscribe(
      (response => {
        const xonomy = document.getElementById('xonomy-letter-editor');
        Xonomy.render(response, xonomy, this.xonomyService.coverLetterElement);
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

}
