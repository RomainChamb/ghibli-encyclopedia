import {Component, inject} from "@angular/core";
import {MovieListItem} from "./movie-list-item";
import {Movie} from "./movie"
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs";
import {toSignal} from "@angular/core/rxjs-interop";

type MovieApi = {
  id: string;
  title: string;
  original_title: string;
  original_title_romanised: string;
  description: string;
  director: string;
  producer: string;
  release_date: string;
  running_time: string;
  rt_score: string;
  people: string[];
  species: string[];
  locations: string[];
  vehicles: string[];
  url: string;
}

@Component({
  selector: 'app-movie-list',
  imports: [MovieListItem],
  styles: ``,
  template : `
    @for (movie of movies(); track movie.id) {
      <app-movie-list-item  [movie]="movie" id="movie-list"/>
    }
  `
})
export class MovieList {
  private readonly _http = inject(HttpClient);

  movies = toSignal(this._http.get<Movie[]>('/api/movies').pipe(map(toMovieList)), {initialValue: []});

}

const toMovieList = (input: unknown): Movie[] => {
  if(!Array.isArray(input)) return [];
  return input.filter((item) => isMovieApi(item)).map(
    (movieApi): Movie => ({
      id: movieApi.id,
      title: movieApi.title,
      originalTitle: movieApi.original_title,
      originalTitleRomanised: movieApi.original_title_romanised,
      description: movieApi.description,
      director: movieApi.director,
      producer: movieApi.producer,
      releaseDate: Number(movieApi.release_date),
      runningTime: Number(movieApi.running_time),
      rtScore: Number(movieApi.rt_score),
      people: movieApi.people,
      species: movieApi.species,
      locations: movieApi.locations,
      vehicles: movieApi.vehicles
    })
  );
};

const isMovieApi = (input: unknown): input is MovieApi => {
  return (typeof input === 'object'
    && input !== null
    && [
      'id',
      'title',
      'original_title',
      'original_title_romanised',
      'description',
      'director',
      'producer',
      'release_date',
      'running_time',
      'rt_score',
      'people',
      'species',
      'locations',
      'vehicles',
    ].every((key) => key in input)
  );
};
