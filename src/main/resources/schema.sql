-- Departments Table
CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name CHARACTER VARYING(255)
);

-- Employees Table
CREATE TABLE employee (
    id bigint AUTO_INCREMENT PRIMARY KEY NOT NULL,
    email CHARACTER VARYING(255),
    name CHARACTER VARYING(255),
    department_id BIGINT,
    CONSTRAINT fk_department_id FOREIGN KEY (department_id) REFERENCES department (id)
);