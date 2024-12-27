# HelpSystem

The **HelpSystem** is designed to make it easier for Arizona State University (ASU) to provide current, accurate, and relevant information to CSE 360 students. By offering an intuitive interface and robust backend functionality, this system ensures students can access articles, resources, and support tailored to their needs.

## Features

### Database-Driven Articles
- **Secure Storage**: Articles are stored in an H2 database, ensuring scalability and persistence.
- **Search by Groups**: Articles can be categorized by groups, allowing users to filter content relevant to their role (e.g., students, instructors, admins).
- **Detailed Metadata**: Each article includes:
  - Title
  - Author
  - Abstract
  - Keywords
  - Body content
  - References
  - Group association (`articleGroups`)

### Encryption and Security
- All sensitive data is encrypted using robust encryption utilities.
- Ensures secure storage and retrieval of articles.

### User Roles
- Supports multiple roles like students, instructors, and admins, each with tailored access to content and functionalities.

### Search and Management
- **Group-Based Search**: Quickly find articles by group for targeted results.
- **Article Management**: Add, view, and organize articles efficiently.

## Technologies Used

### Backend
- **Java**: Core application logic.
- **H2 Database**: Lightweight and embedded database solution.
- **JDBC**: Database connectivity.
- **Bouncy Castle**: Advanced cryptographic functions for data security.

### Frontend
- **JavaFX**: For building an interactive graphical user interface.

### Utilities
- **EncryptionHelper**: Manages encryption and decryption for sensitive information.
- **Java Standard Libraries**: For file I/O, collections, and utilities.
