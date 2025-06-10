import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.scss']
})
export class SubjectComponent implements OnInit {

  @Input() id: number = 0;
  @Input() title: string = '';
  @Input() description: string = '';
  @Output() subscribe = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  onSubscribe(): void {
    this.subscribe.emit(this.id);
  }

}