import { Component, OnInit } from '@angular/core';
import { ReviewRequest } from '../_model/reviewRequest.model';
import { ReviewService } from '../_service/review.service';

@Component({
  selector: 'app-review-requests',
  templateUrl: './review-requests.component.html',
  styleUrls: ['./review-requests.component.scss']
})
export class ReviewRequestsComponent implements OnInit {
  reviewRequests: ReviewRequest[];

  constructor(private reviewService: ReviewService) { }

  ngOnInit() {
    this.getPendingReviewRequests();
  }

 getPendingReviewRequests() {
   this.reviewRequests = [];

   this.reviewService.getReviewRequests().subscribe(
    (response) => {
      this.reviewRequests = response;
    }
  );
 }

}
