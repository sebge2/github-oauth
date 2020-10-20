import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardHeaderActionsComponent } from './card-header-actions.component';

describe('CardHeaderActionsComponent', () => {
  let component: CardHeaderActionsComponent;
  let fixture: ComponentFixture<CardHeaderActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardHeaderActionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardHeaderActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
