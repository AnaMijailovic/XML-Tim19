import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPaperFormComponent } from './add-paper-form.component';

describe('AddPaperFormComponent', () => {
  let component: AddPaperFormComponent;
  let fixture: ComponentFixture<AddPaperFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPaperFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPaperFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
