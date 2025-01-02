CREATE TABLE Sessions (
    ID VARCHAR(255) PRIMARY KEY,
    UserId INTEGER,
    ExpiresAt TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES Users (ID)
                      );