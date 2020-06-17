import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoverLetterEditorComponent } from './cover-letter-editor.component';

describe('CoverLetterEditorComponent', () => {
  let component: CoverLetterEditorComponent;
  let fixture: ComponentFixture<CoverLetterEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoverLetterEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoverLetterEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
