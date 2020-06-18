import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ScientificPaper } from '../_model/scientificPaper.model';
import { MatDialog } from '@angular/material';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { UtilService } from '../_service/util.service';
import { CoverLetterService } from '../_service/cover-letter.service';

@Component({
  selector: 'app-paper-card',
  templateUrl: './paper-card.component.html',
  styleUrls: ['./paper-card.component.scss']
})
export class PaperCardComponent implements OnInit {

  @Input()
  paper: ScientificPaper;

  @Input()
  authorView: boolean;

  constructor( public dialog: MatDialog,
               private spService: ScientificPaperService,
               private clService: CoverLetterService,
               private utilService: UtilService,
               private toastr: ToastrService,
               private router: Router) { }

  ngOnInit() {
  }

  withdrawPaper(paperId: string) {
    this.spService.withdrawPaper(paperId).subscribe(
      (response => {
        this.toastr.success('Success', 'Scientific paper withrawn');
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

  confirmWithdraw(title: string, paperId: string) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
     data: 'Are you sure you want to withdraw paper  ' + title + '?'
    });

    dialogRef.afterClosed().subscribe(result => {
      if ( result === true ) {
        this.withdrawPaper(paperId);
       }

    });

  }

  addRevision(paperTitle: string, processId: string ) {
      localStorage.setItem('revisionData', JSON.stringify({paperTitle, processId}));
      this.router.navigate(['/add-paper']);
  }

  viewHtml() {
    this.spService.getHtml(this.paper.id);
  }

  viewPdf() {
    this.spService.getPdf(this.paper.id);
  }

  viewXml() {
    this.spService.getXml(this.paper.id);
  }

  viewLetterHtml() {
    this.spService.getLetterHtml(this.paper.id);
  }

  viewLetterPdf() {
    this.spService.getLetterPdf(this.paper.id);
  }

  viewLetterXml() {
    this.spService.getLetterXml(this.paper.id);
  }

  metadataRdf() {
    this.spService.getMetadataRdf(this.paper.id).subscribe(
      (response => {
            this.utilService.openAsXml(response);
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

  metadataJson() {
    this.spService.getMetadataJson(this.paper.id).subscribe(
      (response => {
        this.utilService.openAsJson(response);
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

  quotedBy() {
    this.router.navigate(['/paper-quoted-by/' + this.paper.id]);
  }
}
