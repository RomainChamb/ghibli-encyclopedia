import {Component} from '@angular/core';
import {RouterModule} from '@angular/router';
import {MatMenuModule} from "@angular/material/menu";

@Component({
  selector: 'seed-root',
  styles: [`
    .sidenav-container {
      height: 100vh;
      display: flex;
      flex-direction: column;
    }

    header, footer {
      text-align: center;
      padding: 16px;
    }

    header img {
      display: block;
      margin: 0 auto;
    }

    .content-container {
      flex: 1;
      display: flex;
      overflow: hidden;
    }

    .main-content {
      flex: 1;
      padding: 16px;
      overflow-y: auto;
    }

    mat-toolbar {
      position: sticky;
      top: 0;
      z-index: 2;
    }
  `],
  template: `
      <header>
        <img
          alt="Ghibli logo"
          src="/content/images/ghibli-logo.webp"
          width="100"
          height="100"
        />
        <h1>Ghibli Encyclopedia : A sandbox project to practice ATDD</h1>

        <button matButton [matMenuTriggerFor]="menu" id="menu">Menu</button>
        <mat-menu #menu="matMenu">
          <button mat-menu-item [routerLink]="['/']" id="home">Home</button>
          <button mat-menu-item [routerLink]="['/movies']" id="movies">Movies List</button>
          <button mat-menu-item [routerLink]="['/favorites']" id="favorites">Favorites</button>
        </mat-menu>
      </header>

      <router-outlet />

      <footer>
        Data from <a href="https://ghibliapi.vercel.app/">Ghibli API</a>
      </footer>
  `,
  imports: [RouterModule,
    MatMenuModule
  ],
})
export class App{
}

