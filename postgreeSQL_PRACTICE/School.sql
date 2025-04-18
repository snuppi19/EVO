Create schema school;


CREATE TABLE school.role (
    role_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE, 
    description TEXT, 	
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL
);
CREATE TABLE school.teacher (
    teacher_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    subject VARCHAR(100) NOT NULL ,
    role_id INT NOT NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES school.role(role_id) ON DELETE RESTRICT-- tránh xóa khi còn tham chiếu ở bảng cha
);

CREATE TABLE school.class (
    class_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    teacher_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id) REFERENCES school.teacher(teacher_id) ON DELETE SET NULL
);

CREATE TABLE school.student (
    student_id INT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    dob DATE CHECK (dob <= CURRENT_DATE),
    class_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL,
	CONSTRAINT pk_student PRIMARY KEY (student_id, dob),
    CONSTRAINT fk_class FOREIGN KEY (class_id) REFERENCES school.class(class_id) ON DELETE SET NULL
)PARTITION BY RANGE (dob);



--patrition by range dob 
CREATE TABLE school.student_2000 PARTITION OF school.student
    FOR VALUES FROM ('2000-01-01') TO ('2001-01-01');

CREATE TABLE school.student_2001 PARTITION OF school.student
    FOR VALUES FROM ('2001-01-01') TO ('2002-01-01');

CREATE TABLE school.student_2002 PARTITION OF school.student
    FOR VALUES FROM ('2002-01-01') TO ('2003-01-01');



CREATE INDEX idx_class_teacher_id ON school.class(teacher_id);
CREATE INDEX idx_teacher_role_id ON school.teacher(role_id);
CREATE INDEX idx_teacher_subject ON school.teacher(subject);
CREATE INDEX idx_student_name_dob on school.student(first_name,last_name,dob);-- composite index khi ma truy van 2  hoặc nhiều du lieu thuong xuyen 
CREATE INDEX idx_student_class_id ON school.student(class_id);


--procedures 
CREATE OR REPLACE PROCEDURE school.add_teacher(
    pfirst_name VARCHAR,
    plast_name VARCHAR,
    psubject VARCHAR,
    prole_id INT
)
LANGUAGE plpgsql-- ngôn ngữ mặc định của postgreeSQL// có thể if, loop,... mà sql thì không viết được 
AS
$$
BEGIN
    INSERT INTO school.teacher (first_name, last_name, subject, role_id)
    VALUES (pfirst_name, plast_name, psubject, prole_id);
END;
$$;

--functions 
CREATE OR REPLACE FUNCTION school.count_students_in_class(
    fclass_id INT
)
RETURNS INT
LANGUAGE SQL
AS $$
    SELECT COUNT(*) FROM school.student a
    WHERE a.class_id = fclass_id AND deleted_at IS NULL;
$$;

-- view cơ bản 
CREATE VIEW student_view AS
SELECT student_id, first_name || ' ' || last_name AS full_name, dob
FROM school.student
WHERE deleted_at IS NULL;--các bản ghi chưa bị xóa

SELECT * FROM student_view;

--Triggers // lưu lại thời gian và thông tin học sinh bị xóa
CREATE TABLE student_log (
  log_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  student_id INT,
  action TEXT,
  log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE FUNCTION log_student_delete() 
RETURNS TRIGGER 
LANGUAGE plpgsql
AS 
$$
BEGIN
  INSERT INTO student_log (student_id, action)
  VALUES (OLD.student_id, 'DELETE');
  RETURN OLD;
END;
$$ 

CREATE TRIGGER trg_student_delete
AFTER DELETE ON school.student
FOR EACH ROW EXECUTE FUNCTION log_student_delete();


--Transaction 
DO $$ 
BEGIN
    BEGIN
        INSERT INTO school.teacher (first_name, last_name, subject, role_id)
        VALUES ('Alice', 'Smith', 'Mathematics', 2);

        INSERT INTO school.student (first_name, last_name, dob, class_id)
        VALUES ('Bob', 'Johnson', '2005-08-15', 1);

        INSERT INTO school.student (first_name, last_name, dob, class_id)
        VALUES ('Charlie', 'Brown', '2006-01-25', 999);
		
        COMMIT;
    EXCEPTION
        WHEN others THEN
            -- rollback lại transaction
            ROLLBACK;
            RAISE NOTICE 'Transaction has been rolled back.';
    END;
END $$;

--Lock

 -- a/ row-lock 
 
 BEGIN;

SELECT * FROM school.student
WHERE class_id = 1
FOR UPDATE;

UPDATE school.student
SET first_name = 'John'
WHERE student_id = 1;

COMMIT;

-- b/ table-level 

  BEGIN;

LOCK TABLE school.student IN ACCESS EXCLUSIVE MODE;

UPDATE school.student
SET deleted_at = CURRENT_TIMESTAMP
WHERE student_id = 1;

COMMIT;


-- join 
 -- tạo bảng có thông tin học sinh và lớp, thầy cô 
 SELECT 
    s.student_id,
    s.first_name || ' ' || s.last_name AS student_name,
    c.class_name,
    t.first_name || ' ' || t.last_name AS teacher_name,
    s.dob
FROM 
    school.student s
LEFT JOIN 
    school.class c ON s.class_id = c.class_id
LEFT JOIN 
    school.teacher t ON c.teacher_id = t.teacher_id
WHERE 
    s.deleted_at IS NULL
ORDER BY 
    s.dob desc;


--Phân quyền User 
CREATE ROLE admin LOGIN PASSWORD 'admin123';
CREATE ROLE teacher LOGIN PASSWORD 'teacher123';
   -- cấp quyền
 GRANT USAGE, CREATE ON SCHEMA school TO admin; --cho phép truy cập schema school và chức năng create tạo mới
-- Cấp quyền FULL cho admin trên tất cả các bảng
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA school TO admin;

-- cấp quyền cho phép teacher có thể đọc bảng teacher và student
GRANT SELECT ON school.student TO teacher;
GRANT SELECT ON school.teacher TO teacher;
--thu hồi quyền của teacher trên bảng student 
REVOKE SELECT ON school.student FROM teacher;
REVOKE INSERT, UPDATE, DELETE ON school.teacher FROM teacher;



 



