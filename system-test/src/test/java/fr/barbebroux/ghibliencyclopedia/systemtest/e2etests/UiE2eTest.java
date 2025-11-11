package fr.barbebroux.ghibliencyclopedia.systemtest.e2etests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UiE2eTest {

    @Test
    void fetchMovies_shouldDisplayMoviesDataInUI() {
        // DISCLAIMER: This is an example of a badly written test
        // which unfortunately simulates real-life software test projects.
        // This is the starting point for our ATDD Accelerator exercises.

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            
            // Navigate to the home page
            page.navigate("http://localhost:8080");
            
            // 1. Check there's a button with id
            Locator movieListButton = page.locator("#movie-list-button");
            assertThat(movieListButton.isVisible()).as("Movie list access button is visible").isTrue();
            
            // 2. Click the access button
            movieListButton.click();

            
            // 3. Wait for the result to appear and contain actual data
            Locator movieListResult = page.locator("#movie-list");
            movieListResult.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            assertThat(movieListResult.isVisible()).isTrue();


            // 4. Wait for movie items to load (Angular rendering + API call)
            page.waitForTimeout(3000);
            int movieCount = movieListResult.count();
            System.out.println("Movies found: " + movieCount);
            assertThat(movieCount).as("22 movie items should be rendered").isEqualTo(22);

            // 5. Verify each movie item contains required fields
            Locator movieTitles = page.locator("#movie-list h2");
            assertThat(movieTitles.count()).as("Each movie should have a title").isEqualTo(22);

            // Check specific text fields inside the first movie card
            Locator firstMovieCard = movieListResult.first();
            String firstCardText = firstMovieCard.textContent();
            System.out.println("First movie card content: " + firstCardText);


            assertThat(firstCardText)
                    .as("Movie card should contain title field")
                    .contains("Original Title");

            assertThat(firstCardText)
                    .as("Movie card should contain director field")
                    .contains("Director");

            assertThat(firstCardText)
                    .as("Movie card should contain producer field")
                    .contains("Producer");
            
            browser.close();
        }
    }

    @Test
    void addMovieToFavorite_shouldDisplayNotificationInUI() {
        // DISCLAIMER: This is an example of a badly written test
        // which unfortunately simulates real-life software test projects.
        // This is the starting point for our ATDD Accelerator exercises.

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();

            // Navigate to the home page
            page.navigate("http://localhost:8080");

            // 1. Check there's a button with id
            Locator movieListButton = page.locator("#movie-list-button");
            assertThat(movieListButton.isVisible())
                    .as("Movie list access button is visible")
                    .isTrue();

            // 2. Click the access button
            movieListButton.click();


            // 3. Wait for the result to appear and contain actual data
            Locator movieListResult = page.locator("#movie-list");
            movieListResult.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            assertThat(movieListResult.isVisible()).isTrue();


            // 4. Wait for movie items to load (Angular rendering + API call)
            page.waitForTimeout(3000);
            int movieCount = movieListResult.count();
            System.out.println("Movies found: " + movieCount);
            assertThat(movieCount)
                    .as("22 movie items should be rendered")
                    .isEqualTo(22);


            // Identify first film
            Locator firstMovieCard = movieListResult.first();

            // 5. Click on favorite button
            Locator favoriteButton = firstMovieCard.locator("#favorite");
            assertThat(favoriteButton.isVisible())
                    .as("Add to favorite button should be visible")
                    .isTrue();
            favoriteButton.click();

            // 6. Check that the notification appears
            Locator notification = page.locator("#notification");
            notification.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            assertThat(notification.isVisible())
                    .as("Notification should appear after adding favorite")
                    .isTrue();

            String notificationText = notification.textContent();
            System.out.println("Notification text: " + notificationText);
            assertThat(notificationText)
                    .as("Notification should contain text")
                    .isEqualTo("Added to favorite");


            browser.close();
        }
    }

    @Test
    void RemoveMovieFromFavorite_shouldDisplayNotificationInUI() {
        // DISCLAIMER: This is an example of a badly written test
        // which unfortunately simulates real-life software test projects.
        // This is the starting point for our ATDD Accelerator exercises.

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();

            // Navigate to the home page
            page.navigate("http://localhost:8080");

            // 1. Check there's a button with id
            Locator movieListButton = page.locator("#movie-list-button");
            assertThat(movieListButton.isVisible())
                    .as("Movie list access button is visible")
                    .isTrue();

            // 2. Click the access button
            movieListButton.click();


            // 3. Wait for the result to appear and contain actual data
            Locator movieListResult = page.locator("#movie-list");
            movieListResult.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            assertThat(movieListResult.isVisible()).isTrue();


            // 4. Wait for movie items to load (Angular rendering + API call)
            page.waitForTimeout(3000);
            int movieCount = movieListResult.count();
            System.out.println("Movies found: " + movieCount);
            assertThat(movieCount)
                    .as("22 movie items should be rendered")
                    .isEqualTo(22);


            // Identify first film
            Locator firstMovieCard = movieListResult.first();

            // 5. Click on favorite button
            Locator favoriteButton = firstMovieCard.locator("#favorite");
            assertThat(favoriteButton.isVisible())
                    .as("Add to favorite button should be visible")
                    .isTrue();
            favoriteButton.click();

            // 6. Check that the notification appears
            Locator notification = page.locator("#notification");
            notification.waitFor(new Locator.WaitForOptions().setTimeout(3000));
            assertThat(notification.isVisible())
                    .as("Notification should appear after adding favorite")
                    .isTrue();

            // 7. Click on favorite button to remove from favorite
            favoriteButton.click();
            assertThat(notification.isVisible())
                    .as("Notification should appear after adding favorite")
                    .isTrue();

            Locator removedNotification = page.locator("#notification");
            String notificationText = notification.textContent();
            System.out.println("Notification text: " + notificationText);
            assertThat(notificationText)
                    .as("Notification should contain text")
                    .isEqualTo("Removed from favorite");


            browser.close();
        }
    }
}