CREATE TABLE movies.survey
(
    id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE movies.survey_question
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    content   VARCHAR(255) NOT NULL,
    survey_id BIGINT       NOT NULL,
    FOREIGN KEY (survey_id) REFERENCES movies.survey (id) ON DELETE CASCADE
);


CREATE TABLE movies.survey_option
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    content     VARCHAR(255) NOT NULL,
    question_id BIGINT       NOT NULL,
    FOREIGN KEY (question_id) REFERENCES movies.survey_question (id) ON DELETE CASCADE
);

CREATE TABLE movies.survey_answer
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT       NOT NULL,
    answer      BIGINT NOT NULL,
    FOREIGN KEY (answer) REFERENCES movies.survey_option (id) ON DELETE CASCADE,
    user_id     VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES movies.survey_question (id) ON DELETE CASCADE,
    constraint uq_survey_answer_user_question unique (user_id, question_id)

);