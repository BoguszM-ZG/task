alter table movies.survey_option
    add constraint uq_survey_option_content_question_id unique (content, question_id);