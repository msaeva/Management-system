ALTER TABLE projects
    ADD CONSTRAINT unique_abbreviation UNIQUE (abbreviation);
