# System Behaviour (Use Cases)

## Use case diagram

<img src="images/use-case-diagram.svg" alt="Use Case Diagram" />

Primary Actor :
- User

Secondary Actor :
- Gihbli Api
- System Clock

## Use Case Narrative: View movie list

### Use case name

View movie list

### Primary Actor

User

### Goal

The user successfully view the movie list

### Main success scenario

1. The user access to the application page
2. The system display the movie list

## Use Case Narrative: Filter movie list

### Use case name 

Filter the movie list

### Goal

The user can filter the movie list by movie title

### Preconditions
- The user has accessed the application page and view the movie list

### Main success scenario

1. The user enter a movie name in the search bar
2. The system display only the movie corresponding to the search keyword

## Use Case Narrative : View movie details

### Use case name

View the movie details

### Goal

The user can view the movie details

### Preconditions
- The user has accessed the application page and view the movie list

### Main success scenario
1. The user click on one movie
2. The system fetch the movie details from the API
3. The system display the movie details

## Use Case Narrative : Add movie to favorite

### Use case name

Add movie to favorite

### Goal

The user can add a movie to his favorite

### Preconditions
- The user has accessed the application page and view the movie list

### Main success scenario
1. The user click on the add to favorite button
2. The system retrieve the actual date
3. The system save the movie to favorite
4. notification inform the user that the movie has been added to his favorite

### Postconditions
- The favorite is saved into the system
- The user receive a notification

## Use Case Narrative : Remove movie from favorite

### Use case name

Remove movie from favorite

### Goal

The user can remove a movie from his favorite

### Preconditions
- The user has accessed the application page and view the movie list

### Main success scenario
1. The user click on the remove from favorite button
2. The system remove the movie from favorite
3. A notification inform the user that the movie has been removed from his favorite

### Postconditions
- The favorite is removed from the system
- The user receive a notification

## Use Case Narrative : View favorite

### Use case name

View favorite

### Goal

The user can view his favorite

### Preconditions
- The user has accessed the application page and view the movie list

### Main success
1. The user click on the favorite button
2. The system redirect the user to the favorite page
3. The system display his favorite
