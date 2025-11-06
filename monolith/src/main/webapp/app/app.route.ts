import { Routes } from '@angular/router';
import { MovieList } from './movie-list'
import { Home } from "./home";

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'movies', component: MovieList }
];
