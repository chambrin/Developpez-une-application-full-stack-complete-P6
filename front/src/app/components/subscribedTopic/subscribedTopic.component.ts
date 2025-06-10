import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-subscribedTopic',
  templateUrl: './subscribedTopic.component.html',
  styleUrls: ['./subscribedTopic.component.scss']
})
export class SubscribedTopicComponent implements OnInit {

  @Input() id: number = 0;
  @Input() title: string = '';
  @Input() description: string = '';

  constructor() { }

  ngOnInit(): void {
  }

}
