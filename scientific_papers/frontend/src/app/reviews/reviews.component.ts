import { Component, OnInit } from '@angular/core';
import { ReviewRequest } from '../_model/reviewRequest.model';
import { ReviewService } from '../_service/review.service';

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.scss']
})
export class ReviewsComponent implements OnInit {
  assignedReviews: ReviewRequest[];

  constructor(private reviewService: ReviewService) { }

  ngOnInit() {
    this.getAssignedReviews();
  }

  getAssignedReviews() {
    this.assignedReviews = [];

    this.reviewService.getAssignedReviews().subscribe(
      (response) => {
        this.assignedReviews = response;
      }
    );
  }
}
