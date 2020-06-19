import { Component, OnInit, Input } from '@angular/core';
import { ReviewRequest } from '../_model/reviewRequest.model';
import { ReviewService } from '../_service/review.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-review-request-card',
  templateUrl: './review-request-card.component.html',
  styleUrls: ['./review-request-card.component.scss']
})
export class ReviewRequestCardComponent implements OnInit {

  @Input()
  reviewRequest: ReviewRequest;

  constructor(private reviewService: ReviewService,
              private toastr: ToastrService,
              private router: Router) { }

  ngOnInit() {
  }

  accept() {
    this.reviewService.acceptReview(this.reviewRequest.processId).subscribe(
      ((response: any) => {
        this.toastr.success('Success', 'Successfully accepted review');
        this.router.navigate(['/pending-reviews']);
      }), (error: any) => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      }
    );
  }

  reject() {
    this.reviewService.rejectReview(this.reviewRequest.processId).subscribe(
      ((response: any) => {
        this.toastr.success('Success', 'Successfully rejected review');
        location.reload();
      }), (error: any) => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      }
    );
  }
}
