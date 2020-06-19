import { Component, OnInit } from '@angular/core';
import { XonomyReviewService } from '../_service/xonomy-review.service';
import { ReviewService } from '../_service/review.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

declare const Xonomy: any;

@Component({
  selector: 'app-review-editor',
  templateUrl: './review-editor.component.html',
  styleUrls: ['./review-editor.component.scss']
})
export class ReviewEditorComponent implements OnInit {

  review: string;
  processId: string;

  constructor(private xonomyService: XonomyReviewService,
              private reviewService: ReviewService,
              private toastr: ToastrService,
              private router: Router) { }

  ngOnInit() {
    this.processId = localStorage.getItem('processId');
    localStorage.removeItem('processId');
    this.setXonomy();
  }

  setXonomy() {
    this.review = '<evaluation_form xmlns="https://github.com/AnaMijailovic/XML-Tim19"' +
                  'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"></evaluation_form>';
    const xonomy = document.getElementById('xonomy-ef-editor');
    Xonomy.render(this.review, xonomy, this.xonomyService.reviewElement);
  }

  loadEFTemplate() {
    this.reviewService.getTemplate().subscribe(
      (response => {
        const xonomy = document.getElementById('xonomy-ef-editor');
        Xonomy.render(response, xonomy, this.xonomyService.reviewElement);
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

  addEFXonomy() {
    this.review = Xonomy.harvest() as string;
    this.submitReview();
  }

  submitReview() {
    this.reviewService.submitEvaluationForm(this.review, this.processId).subscribe(
      (response => {
        this.toastr.success('Success', 'Review submitted');
        this.router.navigate(['/assigned-reviews']);
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
