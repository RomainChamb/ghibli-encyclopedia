import {Component, OnInit, signal} from "@angular/core";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-home',
  imports: [
    RouterLink
  ],
  styles: `
    .title {
      width: 100%;
      text-align: center;
      margin-top: 70px;
    }
  `,
  template: `
    <div class="title">
      <button class="btn" [routerLink]="['/movies']">Access the movie list</button>
    </div>
  `
})
export class Home implements OnInit {
  appName = signal('');

  ngOnInit(): void {
    this.appName.set('Ghibli Encyclopedia');
  }
}
