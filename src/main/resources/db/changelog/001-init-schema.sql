CREATE TABLE student (
                         id UUID PRIMARY KEY,
                         version INT,
                         create_date TIMESTAMP,
                         update_date TIMESTAMP,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         student_no BIGINT NOT NULL UNIQUE
);

CREATE TABLE test (
                      id UUID PRIMARY KEY,
                      version INT,
                      create_date TIMESTAMP,
                      update_date TIMESTAMP,
                      title VARCHAR(255) NOT NULL
);

CREATE TABLE question (
                          id UUID PRIMARY KEY,
                          version INT,
                          create_date TIMESTAMP,
                          update_date TIMESTAMP,
                          test_id UUID NOT NULL,
                          content VARCHAR(1000) NOT NULL
);

CREATE TABLE choice (
                        id UUID PRIMARY KEY,
                        version INT,
                        create_date TIMESTAMP,
                        update_date TIMESTAMP,
                        question_id UUID NOT NULL,
                        content VARCHAR(1000),
                        correct_choice BOOLEAN NOT NULL
);

CREATE TABLE student_answer (
                                id UUID PRIMARY KEY,
                                version INT,
                                create_date TIMESTAMP,
                                update_date TIMESTAMP,
                                student_id UUID NOT NULL,
                                test_id UUID NOT NULL,
                                question_id UUID NOT NULL,
                                choice_id UUID NOT NULL
);

CREATE TABLE test_attendance (
                                 id UUID PRIMARY KEY,
                                 version INT,
                                 create_date TIMESTAMP,
                                 update_date TIMESTAMP,
                                 student_id UUID NOT NULL,
                                 test_id UUID NOT NULL
);

ALTER TABLE question
    ADD CONSTRAINT fk_question_test
        FOREIGN KEY (test_id) REFERENCES test(id);

ALTER TABLE choice
    ADD CONSTRAINT fk_choice_question
        FOREIGN KEY (question_id) REFERENCES question(id);

ALTER TABLE student_answer
    ADD CONSTRAINT fk_answer_student
        FOREIGN KEY (student_id) REFERENCES student(id);

ALTER TABLE student_answer
    ADD CONSTRAINT fk_answer_test
        FOREIGN KEY (test_id) REFERENCES test(id);

ALTER TABLE student_answer
    ADD CONSTRAINT fk_answer_question
        FOREIGN KEY (question_id) REFERENCES question(id);

ALTER TABLE student_answer
    ADD CONSTRAINT fk_answer_choice
        FOREIGN KEY (choice_id) REFERENCES choice(id);

ALTER TABLE test_attendance
    ADD CONSTRAINT fk_attendance_student
        FOREIGN KEY (student_id) REFERENCES student(id);

ALTER TABLE test_attendance
    ADD CONSTRAINT fk_attendance_test
        FOREIGN KEY (test_id) REFERENCES test(id);
