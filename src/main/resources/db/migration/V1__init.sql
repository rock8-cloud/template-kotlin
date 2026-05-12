CREATE TABLE space_target (
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(255)     NOT NULL,
    type                 VARCHAR(100)     NOT NULL,
    distance_light_years DOUBLE PRECISION NOT NULL
);

INSERT INTO space_target (name, type, distance_light_years) VALUES
    ('Sirius',              'White Dwarf',         8.6),
    ('Large Magellanic Cloud', 'Irregular Galaxy', 163000),
    ('Crab Nebula',         'Supernova Remnant',   6523);
