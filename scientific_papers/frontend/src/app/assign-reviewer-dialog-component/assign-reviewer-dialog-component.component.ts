import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { PublishingProcessCardComponent } from '../publishing-process-card/publishing-process-card.component';
import { BasicUserInfo } from '../_model/basicUserInfo.model';
import { PublishingProcessService } from '../_service/publishing-process.service';

@Component({
  selector: 'app-assign-reviewer-dialog-component',
  templateUrl: './assign-reviewer-dialog-component.component.html',
  styleUrls: ['./assign-reviewer-dialog-component.component.scss']
})
export class AssignReviewerDialogComponentComponent implements OnInit {

  recommendedReviewers: BasicUserInfo[];
  allReviewers: BasicUserInfo[];
  choosenReviewerId: string;

  constructor(private dialogRef: MatDialogRef<PublishingProcessCardComponent>,
              private publishingProcessService: PublishingProcessService) {
  }

  ngOnInit() {
    this.recommendedReviewers = [];

    this.publishingProcessService.getAllReviewers().subscribe(
      ((response: BasicUserInfo[]) => {
        console.log(response);
        this.allReviewers = response;
      }), ((error: any) => {
        console.log(JSON.stringify(error));
      })
    );
  }

  save() {
    if (this.choosenReviewerId != null) {
      this.dialogRef.close(this.choosenReviewerId);
      return;
    }
    this.dialogRef.close();
  }

  close() {
    this.dialogRef.close();
  }
}
