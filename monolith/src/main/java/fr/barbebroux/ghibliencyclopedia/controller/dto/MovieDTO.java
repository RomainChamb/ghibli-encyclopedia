package fr.barbebroux.ghibliencyclopedia.controller.dto;

public class MovieDTO {
  private String id;
  private String title;
  private String original_title;
  private String original_title_romanised;
  private String description;
  private String director;
  private String producer;
  private String release_date;
  private String running_time;
  private String rt_score;
  private String[] people;
  private String[] species;
  private String[] locations;
  private String[] vehicles;
  private String url;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOriginal_title() {
    return original_title;
  }

  public void setOriginal_title(String original_title) {
    this.original_title = original_title;
  }

  public String getOriginal_title_romanised() {
    return original_title_romanised;
  }

  public void setOriginal_title_romanised(String original_title_romanised) {
    this.original_title_romanised = original_title_romanised;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getProducer() {
    return producer;
  }

  public void setProducer(String producer) {
    this.producer = producer;
  }

  public String getRelease_date() {
    return release_date;
  }

  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }

  public String getRunning_time() {
    return running_time;
  }

  public void setRunning_time(String running_time) {
    this.running_time = running_time;
  }

  public String getRt_score() {
    return rt_score;
  }

  public void setRt_score(String rt_score) {
    this.rt_score = rt_score;
  }

  public String[] getPeople() {
    return people;
  }

  public void setPeople(String[] people) {
    this.people = people;
  }

  public String[] getSpecies() {
    return species;
  }

  public void setSpecies(String[] species) {
    this.species = species;
  }

  public String[] getLocations() {
    return locations;
  }

  public void setLocations(String[] locations) {
    this.locations = locations;
  }

  public String[] getVehicles() {
    return vehicles;
  }

  public void setVehicles(String[] vehicles) {
    this.vehicles = vehicles;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
