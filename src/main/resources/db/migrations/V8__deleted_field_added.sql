ALTER TABLE tasks
    ADD deleted bool DEFAULT FALSE;

ALTER TABLE projects
    ADD deleted bool DEFAULT FALSE;

ALTER TABLE teams
    ADD deleted bool DEFAULT FALSE;

ALTER TABLE meetings
    ADD deleted bool DEFAULT FALSE;

ALTER TABLE comments
    ADD deleted bool DEFAULT FALSE;