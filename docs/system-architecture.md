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

## Tech Stack
- **Programming Language:** Java + Spring Boot + Thymeleaf
- **Database:** H2

## Repository Strategy

Mono-repo repository structure
