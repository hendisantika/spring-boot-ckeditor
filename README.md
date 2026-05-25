# spring-boot-ckeditor

A simple Spring Boot demo that integrates the [CKEditor](https://ckeditor.com/)
rich-text editor with a MongoDB-backed store. Upload an XML document, have it
parsed into MongoDB via a Spring Batch job, then browse and edit the resulting
content in CKEditor.

## Tech Stack

- **Spring Boot 4.0.6** (Java 21)
- **Spring Data MongoDB** — persistence
- **Spring Batch 6** — XML → JSON → MongoDB upload pipeline
- **Thymeleaf** — server-side templates
- **CKEditor 4** (loaded from CDN) — rich-text editing
- **Dropzone.js** — drag-and-drop file upload

## Prerequisites

- **JDK 21+**
- **Maven 3.9+** — use your system `mvn`, not the bundled `mvnw` wrapper (the
  wrapper is pinned to Maven 3.5.2, which is too old to build this project)
- **MongoDB** running on `localhost:27017` (database `ckeditor`)

Quick MongoDB via Docker:

```bash
docker run -d --name ckeditor-mongo -p 27017:27017 mongo:7
```

## Running

```bash
mvn spring-boot:run
```

The app starts on **http://localhost:8080**.

| Route        | Description                                            |
|--------------|--------------------------------------------------------|
| `/`          | Upload form (drag-and-drop an XML document via Dropzone)|
| `/view/`     | Music viewer — browse songs and edit them in CKEditor   |
| `/api/show/` | Fetch a song's content (used by the editor)             |
| `/api/save/` | Save edited content back to MongoDB                     |

## Configuration

Settings live in `src/main/resources/application.properties` — MongoDB host/port
and database name, Thymeleaf, and multipart upload limits.

## Notes

- CKEditor is currently the end-of-life **4.7.3** CDN build; consider upgrading
  to a supported release.
