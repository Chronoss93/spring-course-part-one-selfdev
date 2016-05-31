CREATE TABLE airdate (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    date varchar(30) NOT NULL,
    event_id int NOT NULL,
    auditorium varchar(30) NOT NULL,
    CONSTRAINT airdate_pk PRIMARY KEY (id)
);

-- Table: event
CREATE TABLE event (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    name varchar(30) NOT NULL,
    base_price numeric NOT NULL,
    rating varchar(30) NOT NULL,
    CONSTRAINT event_pk PRIMARY KEY (id)
);

-- Table: ticket
CREATE TABLE ticket (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    date int NOT NULL,
    seat int NOT NULL,
    price numeric NOT NULL,
    status varchar(30) NOT NULL,
    user1_id int NOT NULL,
    event_id int NOT NULL,
    CONSTRAINT ticket_pk PRIMARY KEY (id)
);

-- Table: user
CREATE TABLE user1 (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY
        (START WITH 1, INCREMENT BY 1),
    first_name varchar(30) NOT NULL,
    second_name varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    birthday varchar(30) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id)
);

ALTER TABLE airdate ADD CONSTRAINT event_airdate FOREIGN KEY (event_id)
    REFERENCES event (id);

-- Reference: event_ticket (table: ticket)
ALTER TABLE ticket ADD CONSTRAINT event_ticket FOREIGN KEY (event_id)
    REFERENCES event (id);

-- Reference: user1_ticket (table: ticket)
ALTER TABLE ticket ADD CONSTRAINT user1_ticket FOREIGN KEY (user1_id)
    REFERENCES user1 (id);


