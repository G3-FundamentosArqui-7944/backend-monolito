# 📦 Git Commit Message Template – TravelMatch Project

This template standardizes TravelMatch team commit messages following **GitFlow** + **Conventional Commits**, using **Bounded Contexts (BCs)** as the `scope`.

---

## 📘 Commit Structure

```
<type>(<bounded-context>): <short message in english>
```

### Example:

```
feat(shared): initialize core shared layer with OpenAPI, naming strategy and audit support
```

---

## 🧱 Commit Types (`<type>`)

|    Type     |                           Purpose                           |
|:-----------:|:-----------------------------------------------------------:|
|   `feat`    |           New feature or capability in the system           |
|    `fix`    |                           Bug fix                           |
|   `docs`    |        Changes to code documentation, API, or README        |
|   `style`   | Changes that don't affect logic (formatting, spaces, etc.)  |
| `refactor`  |       Code improvement without changing its behavior        |
|   `test`    |              Addition or modification of tests              |
|   `chore`   |         Configuration, maintenance, or minor tasks          |
|   `build`   |   Changes related to dependencies, build tools, packaging   |
|    `ci`     |             Changes in CI/CD pipelines or tools             |
|   `perf`    |                  Performance improvements                   |

---

## 🧩 List of Bounded Contexts (`<bounded-context>`)

|        Bounded Context (BC)        |      Owner       |                                       Description                                        |
|:----------------------------------:|:----------------:|:----------------------------------------------------------------------------------------:|
|               `iam`                |   Jhon Galvez    |             Manages **authentication** and **authorization** for all users.              |
|      `bookings-and-payments`       |  Cesar Linares   |     Handles **reservations**, **itinerary management**, and **payment processing**.      |
|             `agencies`             |  Mathias Aspajo  |           Manages **agency information** and **services offered by agencies**.           |
|           `experiences`            |  Jorge Guevara   |        Manages **details of tourist experiences**, such as tours and activities.         |
|     `profiles-and-preferences`     |   Jhon Galvez    |       Stores and manages **tourist profiles** and their **personal preferences**.        |
|           `geolocation`            |  Farid Briceño   | Provides **location-based services** and **geospatial data** for tourists and agencies.  |
| `service-operative-and-monitoring` |   Jhon Galvez    |         Oversees **operational aspects** of services and **system monitoring**.          |

---

## ✍️ Writing Rules

* Use English.
* Start with an infinitive verb (implement, add, fix, improve, etc.).
* Do not use an initial capital letter or a period at the end.
* Be specific and clear in a few words.
* If necessary, add a detailed description in the commit body.

---

## 📎 Copy and Paste Template

```
<type>(<bounded-context>): <what was done> <optional details>
```

Example:

```
feat(auth): implement login and registration flow with JWT
```

---

## 💡 Tips for Pull Requests

* Use the same format in the PR title.
* If it spans multiple commits, summarize the main goal.
* Include a checklist if applicable (tests, coverage, etc.).

---

## 🛠️ Recommended Tools

* [Commitizen](https://github.com/commitizen/cz-cli): interactive commit assistant
* [Husky](https://typicode.github.io/husky/): Git hooks to ensure commit formatting
* [Conventional Changelog](https://github.com/conventional-changelog): generates automatic changelogs

---

*Last updated: 2025-06-05*