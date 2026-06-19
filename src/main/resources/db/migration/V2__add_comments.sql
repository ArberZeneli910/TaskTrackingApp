CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP,
    task_id BIGINT,
    author_id BIGINT,
    CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id)
);