# Fetch Test

An android application coding challenge to demonstrate my skills and 
best practices in Android development 

## Key Libraries

### Jetpack Compose
I opted to use Compose as the UI framework for the project, because its simple, declarative
paradigm lends itself well to any scope, both big and small. Its status as the preferred UI library
for Android certainly helped as well.

### Ktor
Ktor is a lightweight and highly customizable networking library, built with Kotlin in mind. 
While not necessary for this project, it provides a clean and concise way to construct API routes.
I chose Ktor for this project, not only as an abstraction in making HTTP connections, but also because
it has built in support for serializing various data formats, in my case using KotlinX Serialization.

### Koin
Koin is yet another lightweight library, this time for dependency injection. Koin makes short work
of declaring dependencies, and has out-of-the-box integrations with both Ktor and Compose. It's
easier to set up and get going than heavier libraries like Hilt, making it an easy choice for a
small project.

## Design Choices

### MVVM
I structured the app around the MVVM (Model-View-ViewModel) pattern. MVVM is a natural fit for Compose,
keeping UI logic in the composables and ensuring that business logic and state are all
handled in the ViewModel. This separation makes the codebase easier to maintain, test, and extend. In this instance,
the ViewModel exposes UI state as a flow, which the composables simply observe and render; no need 
to worry about lifecycle headaches or manual state management. While MVVM may have been a tad overkill
for a project of this scope, it's nevertheless a good showcase of a well-architected app.

### Network Abstractions
I handled networking through a dedicated API service abstraction. This keeps the ViewModel clean
and focused on presentation logic, while the API service takes care of all the HTTP details.
By abstracting the network layer, it’s easy to swap out implementations, mock data for testing,
or handle errors. Ktor’s flexibility made this especially straightforward. Again, while such abstractions
may not be necessary for this project, they're a good way to keep the codebase clean and maintainable.

### Using Libraries
When I initially saw the contents of the challenge, I struggled with the question of whether I should use external libraries
or not. The challenge was simple enough; all I needed to do was make an HTTP request and convert the JSON to a usable format.
And while that wouldn't be terribly difficult to implement, I needed to make sure I was focusing on the important parts of
the challenge, rather than the more trivial details. A carpenter doesn't need to make his own tools from scratch, and 
neither do software engineers. In this case, using Ktor and KotlinX Serialization heavily reduced the amount of code I 
needed to write, freeing up time to focus on design and architecture.