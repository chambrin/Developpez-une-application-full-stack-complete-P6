import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-message',
  templateUrl: './user-message.component.html',
  styleUrl: './user-message.component.scss'
})
export class UserMessageComponent implements OnInit {
  @Input() authorName: string = '';
  @Input() messageText: string = '';
  public isMobileDisplay: boolean = false;

  constructor(
    private screenObserver: BreakpointObserver
  ) { }
  
  ngOnInit(): void {
    this.screenObserver.observe(['(max-width: 900px)']).subscribe(result => {
      this.isMobileDisplay = result.matches;
    });
  }
}