import { Routes } from '@angular/router';
import { MovieList } from './movie-list'
import { Home } from "./home";
import { FavoriteList } from "./favorite-list";

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'movies', component: MovieList },
  { path: 'favorites', component: FavoriteList}
];
