CREATE TABLE Locations (
    ID INTEGER PRIMARY KEY,
    Name VARCHAR NOT NULL,
    UsedId INTEGER,
    Latitude DECIMAL,
    Longitude DECIMAL,
    FOREIGN KEY (UsedId) REFERENCES Users(ID)
                       );