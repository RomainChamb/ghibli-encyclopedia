# System Behaviour (Use Cases)

## Use case diagram

<img src="images/use-case-diagram.svg" alt="Use Case Diagram" />

Primary Actor :
- Visitor

Secondary Actor :
- Gihbli Api
- System Clock

## Use Case Narrative: View movie list

### Use case name

View movie list

### Primary Actor

Visitor

### Goal

The visitor successfully view the movie list

### Main success scenario

1. The visitor access to the application page
2. The system display the movie list

## Use Case Narrative: Filter movie list

### Use case name 

Filter the movie list

### Goal

The visitor can filter the movie list by movie title

### Preconditions
- The visitor has accessed the application page and view the movie list

### Main success scenario

1. The visitor enter a movie name in the search bar
2. The system display only the movie corresponding to the search keyword

## Use Case Narrative : View movie details

### Use case name

View the movie details

### Goal

The visitor can view the movie details

### Preconditions
- The visitor has accessed the application page and view the movie list

### Main success scenario
1. The visitor click on one movie
2. The system fetch the movie details from the API
3. The system display the movie details

## Use Case Narrative : Add movie to favorite

### Use case name

Add movie to favorite

### Goal

The visitor can add a movie to his favorite

### Preconditions
- The visitor has accessed the application page and view the movie list

### Main success scenario
1. The visitor click on the add to favorite button
2. The system retrieve the actual date
3. The system save the movie to favorite
4. notification inform the visitor that the movie has been added to his favorite

### Postconditions
- The favorite is saved into the system
- The visitor receive a notification

## Use Case Narrative : Remove movie from favorite

### Use case name

Remove movie from favorite

### Goal

The visitor can remove a movie from his favorite

### Preconditions
- The visitor has accessed the application page and view the movie list

### Main success scenario
1. The visitor click on the remove from favorite button
2. The system remove the movie from favorite
3. A notification inform the visitor that the movie has been removed from his favorite

### Postconditions
- The favorite is removed from the system
- The visitor receive a notification

## Use Case Narrative : View favorite

### Use case name

View favorite

### Goal

The visitor can view his favorite

### Preconditions
- The visitor has accessed the application page and view the movie list

### Main success
1. The visitor click on the favorite button
2. The system redirect the visitor to the favorite page
3. The system display his favorite
