import {Component, OnInit, signal} from "@angular/core";

@Component({
  selector: 'seed-home',
  imports: [],
  styles: ``,
  template: ``
})
export class Home implements OnInit {
  appName = signal('');

  ngOnInit(): void {
    this.appName.set('Ghibli Encyclopedia');
  }
}
