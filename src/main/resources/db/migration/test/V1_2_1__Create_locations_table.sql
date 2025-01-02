CREATE TABLE Locations (
    ID INTEGER PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    UserId INTEGER,
    Latitude DECIMAL,
    Longitude DECIMAL,
    FOREIGN KEY (UserId) REFERENCES Users (ID)
                       );