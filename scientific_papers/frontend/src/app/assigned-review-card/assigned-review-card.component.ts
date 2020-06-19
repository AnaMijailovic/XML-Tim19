import { Component, OnInit, Input } from '@angular/core';
import { ReviewRequest } from '../_model/reviewRequest.model';
import { ReviewService } from '../_service/review.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-assigned-review-card',
  templateUrl: './assigned-review-card.component.html',
  styleUrls: ['./assigned-review-card.component.scss']
})
export class AssignedReviewCardComponent implements OnInit {
  form: FormGroup;
  reviewXml: string;

  @Input()
  reviewInfo: ReviewRequest;

  constructor(private formBuilder: FormBuilder,
              private reviewService: ReviewService,
              private toastr: ToastrService,
              private router: Router) {
  }

  createForm() {
    this.form = this.formBuilder.group({
      review: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.createForm();
  }

  openEditor() {
    localStorage.setItem('processId', this.reviewInfo.processId);
    this.router.navigate(['/review-editor']);
  }

  openReviewInput() {
    document.getElementById('review').click();
  }

  reviewFileChange(files: any, change: any) {
    const reader = new FileReader();
    reader.readAsText(files[0], 'UTF-8');
    reader.onload  = () => {
        this.reviewXml = reader.result.toString();
        this.toastr.success('Success', 'File added');
    };
    reader.onerror = () => {
      this.toastr.error('Error', 'Failed to read file');
    };
  }

  submitReview() {
    if (this.reviewXml === '' || this.reviewXml === undefined || this.reviewXml === null ) {
      this.toastr.error('Error', 'You must choose review paper');
      return;
    }

    this.reviewService.submitEvaluationForm(this.reviewXml, this.reviewInfo.processId).subscribe(
      (response => {
        this.toastr.success('Success', 'Review submitted');
        location.reload();
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
