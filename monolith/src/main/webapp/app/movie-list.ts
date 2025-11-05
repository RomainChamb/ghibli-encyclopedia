import {Component} from "@angular/core";
import {MovieListItem} from "./movie-list-item";
import {Movie} from "./movie"

@Component({
  selector: 'app-movie-list',
  imports: [MovieListItem],
  styles: ``,
  template : `
    @for (movie of movies; track movie.id) {
      <app-movie-list-item  [movie]="movie"/>
    }
  `
})
export class MovieList {
  movies: Movie[] = [{
    id: "1", title: "test", originalTitle: "test", originalTitleRomanised: "toto", description: "",
    director: "",
    producer: "",
    releaseDate: "",
    runningTime: "",
    rtScore: "",
    people: [],
    species: [],
    locations: [],
    vehicles: []
  }, {
    id: "2", title: "", originalTitle: "", originalTitleRomanised: "", description: "",
    director: "",
    producer: "",
    releaseDate: "",
    runningTime: "",
    rtScore: "",
    people: [],
    species: [],
    locations: [],
    vehicles: []
  }];
}
