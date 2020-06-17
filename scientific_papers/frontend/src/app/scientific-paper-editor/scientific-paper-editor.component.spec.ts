import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScientificPaperEditorComponent } from './scientific-paper-editor.component';

describe('ScientificPaperEditorComponent', () => {
  let component: ScientificPaperEditorComponent;
  let fixture: ComponentFixture<ScientificPaperEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScientificPaperEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScientificPaperEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
