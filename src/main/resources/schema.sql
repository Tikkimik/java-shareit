CREATE TABLE IF NOT EXISTS users (
    user_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name  VARCHAR(255) NOT NULL,
    user_email VARCHAR(512) NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (user_id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (user_email)
);

CREATE TABLE IF NOT EXISTS requests (
    id           BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    description  VARCHAR(4000) NOT NULL,
    requester_id BIGINT NOT NULL,
    date_created TIMESTAMP NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_requester_id FOREIGN KEY (requester_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS items (
    item_id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_name         VARCHAR(100) NOT NULL,
    item_description  VARCHAR(4000) NOT NULL,
    item_is_available Boolean NOT NULL,
    item_owner_id     BIGINT NOT NULL,
    item_request_id   BIGINT,
    CONSTRAINT FK_ITEMS_TO_USERS FOREIGN KEY(item_owner_id) REFERENCES users(user_id), UNIQUE(item_id),
    CONSTRAINT FK_ITEM_REQUEST FOREIGN KEY (item_request_id) REFERENCES requests
);

CREATE TABLE IF NOT EXISTS booking (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    booker_id  BIGINT NOT NULL,
    item_id    BIGINT NOT NULL,
    status     VARCHAR(50),
    CONSTRAINT FK_BOOKINGS_TO_USERS FOREIGN KEY(booker_id) REFERENCES users(user_id),
    CONSTRAINT FK_BOOKINGS_TO_ITEMS FOREIGN KEY(item_id) REFERENCES items(item_id)
);

CREATE TABLE IF NOT EXISTS comments (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text       VARCHAR(4000) not null,
    item_id    BIGINT NOT NULL,
    author_id  BIGINT NOT NULL,
    created    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT FK_COMMENTS_TO_USERS FOREIGN KEY(author_id) REFERENCES users(user_id),
    CONSTRAINT FK_COMMENTS_TO_ITEMS FOREIGN KEY(item_id) REFERENCES items(item_id)
);