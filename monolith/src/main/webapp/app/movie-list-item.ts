import {Component, input} from "@angular/core";
import {Movie} from "./movie"

@Component({
  selector: 'app-movie-list-item',
  styles: `
    :host {
      padding: 1rem;
      border: 1px solid var(--ghibli-surface);
      border-radius: var(--radius);
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      section {
        margin-top: 0.5rem;
      }
      p {
        margin: 0.25rem 0;
        font-size: 0.95rem;
        color: var(--ghibli-text);
      }
    }
  `,
  template: `
    @let _movie=movie();
    <div class="card">
      <h2>{{_movie.title}}</h2>
      <section>
        <h3>Movie Details</h3>
          <p>Original Title: {{_movie.originalTitle}}</p>
          <p>Original Title Romanized: {{_movie.originalTitleRomanised}}</p>
          <p>Description: {{_movie.description}}</p>
          <p>Release Date: {{_movie.releaseDate}}</p>
          <p>Running time: {{_movie.runningTime}}</p>
          <p>RT Score: {{_movie.rtScore}}</p>
      </section>
      <section>
        <h3>Credits</h3>
          <p>Director: {{_movie.director}}</p>
          <p>Producer: {{_movie.producer}}</p>
      </section>
      <section>
        <h3>Related Links</h3>
        <ul>
          <li><a href="{{_movie.people}}">People</a></li>
          <li><a href="{{_movie.species}}">Species</a></li>
          <li><a href="{{_movie.locations}}">Locations</a></li>
          <li><a href="{{_movie.vehicles}}">Vehicles</a></li>
        </ul>
      </section>
    </div>
  `,
})
export class MovieListItem {
  readonly movie = input.required<Readonly<Movie>>();
}
