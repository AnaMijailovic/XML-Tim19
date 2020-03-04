import { Component, OnInit, Input } from '@angular/core';
import { ScientificPaper } from '../_model/scientificPaper.model';

@Component({
  selector: 'app-paper-card',
  templateUrl: './paper-card.component.html',
  styleUrls: ['./paper-card.component.scss']
})
export class PaperCardComponent implements OnInit {

  @Input()
  paper: ScientificPaper;

  constructor() { }

  ngOnInit() {
  }

}
