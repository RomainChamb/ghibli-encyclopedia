import { ComponentFixture, ComponentFixtureAutoDetect, TestBed, waitForAsync } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { App } from './app';

describe('App Component', () => {
  let comp: App;
  let fixture: ComponentFixture<App>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      providers: [provideRouter([]), { provide: ComponentFixtureAutoDetect, useValue: true }],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(App);
    comp = fixture.componentInstance;
  });
});
