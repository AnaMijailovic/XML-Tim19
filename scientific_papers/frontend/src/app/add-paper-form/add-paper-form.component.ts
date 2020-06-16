import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../_service/authentication.service';
import { Router } from '@angular/router';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { CoverLetterService } from '../_service/cover-letter.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-paper-form',
  templateUrl: './add-paper-form.component.html',
  styleUrls: ['./add-paper-form.component.scss']
})
export class AddPaperFormComponent implements OnInit {
  form: FormGroup;
  paper: string;
  letter: string;
  processId: string;

  revisionProcessId: string;
  revisionPaperTitle: string;

  constructor(private formBuilder: FormBuilder,
              private spService: ScientificPaperService,
              private cvService: CoverLetterService,
              private toastr: ToastrService,
              private router: Router) { }

  createForm() {
    this.form = this.formBuilder.group({
      scientificPaper: ['', Validators.required],
      coverLetter: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.createForm();
    const revisionData =  JSON.parse(localStorage.getItem('revisionData'));
    if (revisionData) {
      this.revisionPaperTitle = revisionData.paperTitle;
      this.revisionProcessId = revisionData.processId;
      localStorage.removeItem('revisionData');
    }
  }

  submitPaper() {
    if (this.paper === '' || this.paper === undefined || this.paper === null ) {
      this.toastr.error('Error', 'You must choose scientific paper');
      return;
    }

    let postReq: any;
    if (this.revisionPaperTitle ) {
      postReq = this.spService.addPaperReview(this.paper, this.revisionProcessId);
    } else {
      postReq = this.spService.addScientificPaper(this.paper);
    }

    postReq.subscribe(
      (response => {
        this.toastr.success('Success', 'Scientific paper submitted');
        this.processId = response.toString();
      }), (error => {
        if (error.error.text !== undefined && error.error.text.startsWith('process')) {
          this.toastr.success('Success', 'Scientific paper submitted');
          this.processId = error.error.text;
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

  submitLetter() {
    if (this.processId === '' || this.processId === undefined || this.processId === null ) {
      this.toastr.error('Error', 'You must submit scientific paper first');
      return;
    }

    if (this.paper === '' || this.paper === undefined || this.paper === null ) {
      this.toastr.error('Error', 'You must choose cover letter');
      return;
    }

    // this.toastr.success('Success', 'About to submit');
    this.cvService.addCoverLetter(this.letter, this.processId).subscribe(
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

  openPaperInput() {
    document.getElementById('scientificPaper').click();
  }

  openLetterInput() {
    document.getElementById('coverLetter').click();
  }

  paperFileChange(files: any, change: any) {
    const reader = new FileReader();
    reader.readAsText(files[0], 'UTF-8');
    reader.onload  = () => {
        this.paper = reader.result.toString();
        this.toastr.success('Success', 'File added');
    };
    reader.onerror = () => {
      this.toastr.error('Error', 'Failed to read file');
    };
  }

  letterFileChange(files: any, change: any) {
    const reader = new FileReader();
    reader.readAsText(files[0], 'UTF-8');
    reader.onload  = () => {
        this.letter = reader.result.toString();
        this.toastr.success('Success', 'File added');
    };
    reader.onerror = () => {
      this.toastr.error('Error', 'Failed to read file');
    };
  }

}