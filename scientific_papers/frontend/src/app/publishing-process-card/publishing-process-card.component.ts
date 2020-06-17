import { Component, OnInit, Input } from '@angular/core';
import { PublishingProcess } from '../_model/publishingProcess.model';
import { UtilService } from '../_service/util.service';
import { ToastrService } from 'ngx-toastr';
import { PublishingProcessService } from '../_service/publishing-process.service';

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

  constructor(private publishingProcessService: PublishingProcessService,
              private utilService: UtilService,
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
      }
    }
  }

  assignSelfAsEditor() {
    this.publishingProcessService.assignEditor(this.publishingProcess.processId).subscribe(
      (resoonse => {
        this.toastr.success('Success', 'Successfully assigned self as editor');
        console.log(resoonse);
        this.publishingProcess = resoonse;
        this.updateButtonFlags();
      }), (error => {
        this.toastr.error('Error', 'Some error happend');
        console.log(JSON.stringify(error));
      })
    );
  }
}
