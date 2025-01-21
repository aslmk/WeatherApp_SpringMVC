ALTER TABLE Users ADD CONSTRAINT check_min_length check ( length(Login) >= 3 AND length(Password) >= 3 );
