import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuotedPapersComponent } from './quoted-papers.component';

describe('QuotedPapersComponent', () => {
  let component: QuotedPapersComponent;
  let fixture: ComponentFixture<QuotedPapersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuotedPapersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuotedPapersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
