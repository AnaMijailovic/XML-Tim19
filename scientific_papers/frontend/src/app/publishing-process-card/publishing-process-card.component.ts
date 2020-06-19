import { Component, OnInit, Input } from '@angular/core';
import { PublishingProcess } from '../_model/publishingProcess.model';
import { UtilService } from '../_service/util.service';
import { ToastrService } from 'ngx-toastr';
import { PublishingProcessService } from '../_service/publishing-process.service';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { AssignReviewerDialogComponentComponent } from '../assign-reviewer-dialog-component/assign-reviewer-dialog-component.component';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ReviewService } from '../_service/review.service';

@Component({
  selector: 'app-publishing-process-card',
  templateUrl: './publishing-process-card.component.html',
  styleUrls: ['./publishing-process-card.component.scss']
})
export class PublishingProcessCardComponent implements OnInit {

  @Input()
  publishingProcess: PublishingProcess;
  showAssignSelf = false;
  showAssignReviewer = false;
  showAcceptReject = false;
  showReviews = false;

  constructor(private publishingProcessService: PublishingProcessService,
              private utilService: UtilService,
              private scientificPaperService: ScientificPaperService,
              private reviewService: ReviewService,
              private dialog: MatDialog,
              private toastr: ToastrService) { }

  ngOnInit() {
    this.updateButtonFlags();
  }

  updateButtonFlags() {
    if (this.publishingProcess.editorUsername == null) {
      this.showAssignSelf = true;
    } else if (this.publishingProcess.editorUsername === this.utilService.getLoggedUser()) {
      if (this.publishingProcess.status === 'NEW_SUBMISSION' ||
          this.publishingProcess.status === 'NEW_REVIEWER_NEEDED' ||
          this.publishingProcess.status === 'NEW_REVISION') {
        this.showAssignSelf = false;
        this.showAssignReviewer = true;
      } else if (this.publishingProcess.status === 'REVIEWS_DONE') {
        this.showAcceptReject = true;
      } else {
        this.showAcceptReject = false;
        this.showAssignReviewer = false;
        this.showAssignSelf = false;
      }
    }

    if (this.publishingProcess.finishedReviewsIds.length !== 0) {
      this.showReviews = true;
    }
  }

  assignSelfAsEditor() {
    this.publishingProcessService.assignEditor(this.publishingProcess.processId).subscribe(
      ((response: PublishingProcess) => {
        this.toastr.success('Success', 'Successfully assigned self as editor');
        this.publishingProcess = response;
        this.updateButtonFlags();
      }), (error: any) => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      }
    );
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    dialogConfig.data = {
      assignedReviwersIds: this.publishingProcess.reviewersIds
    }

    const dialogRef = this.dialog.open(AssignReviewerDialogComponentComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      reviewerId => {
        if (reviewerId !== undefined) {
          this.publishingProcessService.assignReviewer(this.publishingProcess.processId, reviewerId).subscribe(
            ((response: PublishingProcess) => {
              this.toastr.success('Success', 'Successfully assigned reviewer');
              console.log(response);
              this.publishingProcess = response;
              this.updateButtonFlags();
            }), ((error: any) => {
              this.toastr.error('Error', 'Some error happend');
              console.log(JSON.stringify(error));
            })
          );
        }
      }
    );
  }

  accept() {
    this.publishingProcessService.updatePaperStatus(this.publishingProcess.processId, 'ACCEPTED').subscribe(
      ((response: PublishingProcess) => {
        this.toastr.success('Success', 'Paper successfully accepted');
        this.publishingProcess = response;
        this.updateButtonFlags();
      }), (error: any) => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      }
    );
  }

  reject() {
    this.publishingProcessService.updatePaperStatus(this.publishingProcess.processId, 'REJECTED').subscribe(
      ((response: PublishingProcess) => {
        this.toastr.success('Success', 'Paper rejected');
        this.publishingProcess = response;
        this.updateButtonFlags();
      }), (error: any) => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      }
    );
  }

  revision() {
    this.publishingProcessService.updatePaperStatus(this.publishingProcess.processId, 'NEW_REVISION').subscribe(
      ((response: PublishingProcess) => {
        this.toastr.success('Success', 'Paper sent on revision');
        location.reload();
      }), (error: any) => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      }
    );
  }

  viewHtmlPaper() {
    this.scientificPaperService.getHtml(this.publishingProcess.latestPaperId);
  }

  viewPdfPaper() {
    this.scientificPaperService.getPdf(this.publishingProcess.latestPaperId);
  }

  viewXmlPaper() {
    this.scientificPaperService.getXml(this.publishingProcess.latestPaperId);
  }

  viewHtmlLetter() {
    this.scientificPaperService.getLetterHtml(this.publishingProcess.latestCoverId);
  }

  viewPdfLetter() {
    this.scientificPaperService.getLetterPdf(this.publishingProcess.latestCoverId);
  }

  viewXmlLetter() {
    this.scientificPaperService.getLetterXml(this.publishingProcess.latestCoverId);
  }

  viewHtmlReviews() {
    this.reviewService.getHtml(this.publishingProcess.finishedReviewsIds);
  }

  viewPdfReviews() {
    this.reviewService.getPdf(this.publishingProcess.finishedReviewsIds);
  }

  viewXmlReviews() {
    this.reviewService.getXml(this.publishingProcess.finishedReviewsIds);
  }
}
