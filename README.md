# ContactSmartly - Full Stack Java Project

A full-stack smartly contact manager where users store contacts with reversible encrypted passwords, manage their profile with picture upload, and admins monitor/manage every account through a dedicated panel.

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.3.0, Spring Security 6 (session-based), Spring Data JPA, Lombok |
| Database | SQLite (dev, file-based) — MySQL/PostgreSQL ready via profiles |
| Auth | Form login + OAuth2 (Google, Facebook) |
| Frontend | React 18 + TypeScript, Vite, Tailwind CSS, React Router, Axios, lucide-react |
| Crypto | AES-256-GCM for contact passwords, BCrypt for account passwords |

## Features
**Users**

- Register (validated) / login with email + password, or one-click Google/Facebook OAuth
- Contacts CRUD: name, username, email, phone, address, notes, web & Facebook links
- Encrypted password storage per contact (AES-GCM at rest; decrypted only via explicit "reveal" endpoint, auto-hidden in UI after 10s)
- Server-side search across name / email / username / phone
- Favourite (star) toggle
- Profile editing + profile picture upload

**Admin**

- List all users with contact counts
- View any user's full profile and all their contacts — no user permission required
- Edit any user, reset any password
- Soft delete ("disable"): user can no longer log in, all data preserved; restorable

**Security**

- Session-cookie authentication (HttpOnly JSESSIONID)
- Role-based access: ROLE_USER, ROLE_ADMIN
- Ownership-scoped queries (user A can never read user B's contacts — IDOR-safe)
- Global REST error handling: 404 for missing/foreign resources, 400 {field: message} for validation

## Architecture

flowchart TB
    User([User]) --> SPA["React SPA (Vite)<br/>localhost:5173"]
    SPA -->|"fetch /api/**<br/>credentials: include"| API["Spring Boot API :5454"]
    SPA -.->|"full-page redirect"| OAuth["Google / Facebook<br/>OAuth Consent"]
    OAuth -.->|"callback + cookie"| SPA

    API --> Security{"Security<br/>Filter Chain"}
    Security -->|"/api/auth/**"| Public["Public"]
    Security -->|"/api/admin/**"| Admin["ADMIN only"]
    Security -->|"/api/**"| Auth["Authenticated"]
    Public --> Controllers["Controllers"]
    Admin --> Controllers
    Auth --> Controllers
    Controllers --> Services["Services"]
    Services --> JPA["Repositories (JPA)"]
    JPA -->|JDBC| DB[("contactmgmt.db<br/>SQLite")]
    Services --> Uploads[("uploads/ dir<br/>served at /uploads/**")]

- Stateless JSON API + server-side sessions. The session cookie is the credential; 
React asks GET /api/auth/me on load to restore login state.
- Two origins in dev (5173 → 5454) → CORS is configured with allowCredentials(true), and Vite proxies /uploads, /authenticate, /do-logout, /oauth2 to the backend so images and OAuth redirects work seamlessly.


## Project Structure

## Getting Started

- JDK 21, Maven 3.8+
- Node 18+ / npm
- Google & Facebook OAuth credentials (optional — form login works without)


## Backend Setup

1. Configure secrets — either set environment variables or fill the defaults in
```application.properties:```           

| Variable / property | Purpose |
| :--- | :--- |
| `googleClientId` / `googleClientSecret` | Google OAuth |
| `facebookClientId` / `facebookClientSecret` | Facebook OAuth |
| `APP_ENCRYPTION_KEY` | AES key for contact passwords (**set a real one; losing it makes stored passwords permanently undecryptable**) |
| `ADMIN_EMAIL` | Account to promote to admin at startup (optional) |

2. Key properties(```application.properties```)     
```properties
server.port=5454
spring.profiles.active=sqlite
app.cors.allowed-origins=http://localhost:5173,http://localhost:3000
app.upload.dir=uploads
app.encryption.key=${APP_ENCRYPTION_KEY:dev-only-aes-key-change-me}
app.admin.email=${ADMIN_EMAIL:}
```

3. ```application.sqlite.properties``` must contain:        
```properties
spring.datasource.url=jdbc:sqlite:contactmgmt.db
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
```

4. Run:
```bash
mvn clean spring-boot:run
```
Backend starts on http://localhost:5454. Schema is auto-created on first boot.

## Frontend Setup

```bash
cd frontend-contactMgmtSys
npm install
echo "VITE_API_URL=http://localhost:5454/api" > .env
npm run dev
```
App runs on http://localhost:5173.       
⚠️ Restart Vite after any .env change Vite reads it only at startup.

### Some Important Tips     
There is deliberately no API to become admin. Promotion happens at startup:

- Register a normal account through the UI.
- Set app.admin.email=that@email.com (or the ADMIN_EMAIL env var).
- Restart the backend → console prints Promoted ... to ADMIN.
- Log out and back in as that user (roles load into the session at login time).
- The Admin link appears in the navbar → /admin. Remove the property afterwards if you like — the role is in the DB now.

## API Reference

## Security Model
| Concern | Mechanism |
| :--- | :--- |
| Account passwords | BCrypt (one-way) at registration & reset |
| Contact passwords | **AES-256-GCM**, key from `app.encryption.key`, stored as `Base64(iv[12] + ciphertext+tag)`. Tamper-detecting; decrypt fails loudly on wrong key. |
| Login | `DaoAuthenticationProvider` + BCrypt match; sessions via `JSESSIONID` (HttpOnly) |
| Authorization | `/api/admin/**` → `hasRole("ADMIN")`; everything else `/api/**` → authenticated |
| IDOR | Every contact query is `findByIdAndUserUserId(id, ownerId)` — a foreign id returns `404`, never the data |
| Disabled accounts | Soft delete flips `enabled=false`; Spring Security blocks login, `/auth/me` returns 401 → client logs out |
| Admin promotion | Startup-only via `ADMIN_EMAIL`; no self-service endpoint |
| Frontend guards | `ProtectedRoute` / `AdminRoute` are **UX only** — the backend is the enforcement layer |


## Database Schema (dev/SQLite)
| Table | Columns |
| :--- | :--- |
| `users` | `user_id` (PK, UUID string), `user_name`, `email` (unique), `phone_number` (unique), `about`, `profile_pic`, `password` (BCrypt), `enabled`, `email_verified`, `phone_number_verified`, `providers` (SELF/GOOGLE/FACEBOOK/GITHUB), `provider_user_id` |
| `users_roles` | `user_id` FK, `role_list` (`ROLE_USER`, `ROLE_ADMIN`) |
| `contacts` | `id` (PK, UUID string), `user_name`, `username`, `email`, `phone_number`, `address`, `description`, `picture`, `fav`, `web_link`, `facebook_link`, `encrypted_password` (AES), `user_id` FK — unique(`user_id`,`email`) |
| `social_links` | `id` (auto), `link`, `title`, `contact_id` FK |

## Troubleshooting (field-tested)
| Symptom | Cause → Fix |
| :--- | :--- |
| `Port 5454 was already in use` | Zombie previous instance serving old code → `kill -9 $(lsof -t -i:5454)` (Win: `netstat -ano \| findstr :5454` + `taskkill /PID <pid> /F`), then start |
| Deleted/changed class still behaving old | Stale `target/` → always `mvn clean` after class changes |
| Login 401 from React despite correct password | `fetch`/axios sent **JSON** to `/authenticate` → must be `x-www-form-urlencoded` params |
| Register 500 | Check backend console's deepest `Caused by:`; usually missing `ddl-auto` or stale DB file → fix properties, delete `contactmgmt.db` in dev |
| `.env` change ignored | Restart `npm run dev` |
| Profile picture 404 in UI | Missing `'/uploads': 'http://localhost:5454'` in Vite proxy |
| Admin promoted but panel 403s | Session predates promotion → log out, log back in |
| Contact passwords won't decrypt | `app.encryption.key` changed since they were stored — keys and data are mathematically bound |


**Notes:**      
 The OAuth success redirect is currently hardcoded to http://localhost:5173/ in OAuthAuthenticationSuccessHandler — externalize it to a property before deploying. Dev-only defaults (dev-only-aes-key-change-me, literal admin email) must never reach production.