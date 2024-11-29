CREATE TABLE Sessions (
    ID VARCHAR PRIMARY KEY,
    UserId INTEGER,
    ExpiresAt TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES Users(ID)
                      );