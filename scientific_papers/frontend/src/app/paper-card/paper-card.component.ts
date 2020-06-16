import { Component, OnInit, Input } from '@angular/core';
import { ScientificPaper } from '../_model/scientificPaper.model';
import { MatDialog } from '@angular/material';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { ScientificPaperService } from '../_service/scientific-paper.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-paper-card',
  templateUrl: './paper-card.component.html',
  styleUrls: ['./paper-card.component.scss']
})
export class PaperCardComponent implements OnInit {

  @Input()
  paper: ScientificPaper;

  constructor( public dialog: MatDialog,
               private spService: ScientificPaperService,
               private toastr: ToastrService) { }

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

}
