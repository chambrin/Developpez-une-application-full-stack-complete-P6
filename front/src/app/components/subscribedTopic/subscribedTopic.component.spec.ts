import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscribedTopicComponent } from './subscribedTopic.component';

describe('SubscribedTopicComponent', () => {
  let component: SubscribedTopicComponent;
  let fixture: ComponentFixture<SubscribedTopicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubscribedTopicComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubscribedTopicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
