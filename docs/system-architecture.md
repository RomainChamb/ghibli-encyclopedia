# System Architecture

## Architecture Style

Monolith (MVC App)

## Architecture Diagram

```mermaid
flowchart LR
    A(["Ghibli Encyclopedia MVC App"])
    A --> B{"Ghibli API"}
    A --> C{"Clock"}
```

_Note: Install the [Mermaid Previewer](https://chromewebstore.google.com/detail/mermaid-previewer) to view the diagram._

## Tech Stack
- **Programming Language:** Java + Spring Boot + Angular (served bu tomcat)
- **Database:** H2

## Repository Strategy

Mono-repo repository structure
