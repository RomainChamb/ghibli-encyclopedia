import {Component, inject, input, signal} from "@angular/core";
import {Movie} from "./movie"
import {HttpClient} from "@angular/common/http";

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
      .notification { color: green; margin-top: 0.5rem; font-weight: bold; }
      .error { color: red; margin-top: 0.5rem; font-weight: bold; }
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
        <p>Running time: {{_movie.runningTime}} seconds</p>
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
      <button (click)="toggleFavorite(_movie.id)" class="btn">Add to favorite</button>
      <!-- Notification -->
      @if (notification()) {
        <div [class.error]="isError()" class="notification">
          {{ notification() }}
        </div>
      }
    </div>
  `,
})
export class MovieListItem {
  private readonly _http = inject(HttpClient);
  readonly movie = input.required<Readonly<Movie>>();

  notification = signal<string | null>(null);
  isError = signal(false);

  toggleFavorite(id: string) {
    this._http.post<FavoriteResponse>(`api/favorites`, {id: id}).subscribe({
      next: (response) => {
        this.isError.set(false);
        this.notification.set(response.message);
        setTimeout(() => this.notification.set(null), 3000);
      },
      error: () => {
        this.isError.set(true);
        this.notification.set('Failed to add favorites');
        setTimeout(() => this.notification.set(null), 3000);
      }
    });
  }
}

type FavoriteResponse = {
  movieId: string;
  message: string;
  added: boolean;
}
