# Auto-Finder
Created : April 2016

This is an Android implementation of my Car Finder project, created in Android Studio.

This project was created while I was searching for a new car. I had a variety of personalized and complicated criterias for finding a car, but most notably the price and how long the vehicle had left on it's warranty.

This program allowed me to search the three most popular car sites for cars that I was interested in, both new and used. The search was done concurrently through use of AsyncTasks The algorithm calculated the time left on the warranty based on the mileage and date of the car. It used information based on my personal driving habits to calculate the shorter warranty length between year and mileage for each specific manufacturer.

After finding eligible vehicles, it formatted the results as an HTML page to include pertinent information as well as links to the actual vehicles, and loaded them in a WebView
