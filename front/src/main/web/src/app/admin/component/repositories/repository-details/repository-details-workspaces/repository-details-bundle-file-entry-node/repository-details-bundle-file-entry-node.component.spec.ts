import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RepositoryDetailsBundleFileEntryNodeComponent } from './repository-details-bundle-file-entry-node.component';

describe('RepositoryDetailsBundleFileEntryNodeComponent', () => {
  let component: RepositoryDetailsBundleFileEntryNodeComponent;
  let fixture: ComponentFixture<RepositoryDetailsBundleFileEntryNodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RepositoryDetailsBundleFileEntryNodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RepositoryDetailsBundleFileEntryNodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
