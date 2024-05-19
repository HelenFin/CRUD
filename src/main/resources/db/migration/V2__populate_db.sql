INSERT INTO worker (Name, Birthday, Level, Salary) VALUES
('Ivan Petrov', '1990-05-15', 'Trainee', 800),
('Julia Sudorivna', '1985-10-20', 'Junior', 1200),
('Ivan Ivanov', '1980-03-25', 'Middle', 2500),
('Olena Koval', '1978-12-10', 'Senior', 6000),
('Dimi Solonchuk', '1995-07-03', 'Trainee', 900),
('Nata Petrivna', '1992-09-08', 'Junior', 1300),
('Andy Kovalenko', '1987-06-12', 'Middle', 2800),
('Viki Lus', '1983-04-17', 'Senior', 7000),
('Sasha Shevchenko', '1993-02-28', 'Junior', 1100),
('Maria Valentunivna', '1989-11-05', 'Middle', 2700);

INSERT INTO client (Name) VALUES
('Xaxa "ABC"'),
('Kombinat "XYZ"'),
('TOV "123"'),
('Tech Opt.'),
('GlobalLogic.');

INSERT INTO project (CLIENT_ID, START_DATE, FINISH_DATE) VALUES
(1, '2023-01-01', '2023-04-15'),
(2, '2023-02-10', '2024-05-20'),
(3, '2023-03-05', '2023-06-30'),
(4, '2023-04-20', '2024-03-10'),
(5, '2023-05-15', '2023-08-25'),
(3, '2023-06-01', '2023-09-10'),
(2, '2023-07-10', '2023-12-05'),
(3, '2023-08-20', '2024-02-15'),
(4, '2023-09-05', '2024-04-30'),
(5, '2023-10-10', '2024-01-20');

INSERT INTO project_worker (PROJECT_ID, WORKER_ID) VALUES
(1, 1), (1, 2), (1, 3),
(2, 4), (2, 5), (2, 6),
(3, 7), (3, 8),
(4, 9), (4, 10),
(5, 1), (5, 3), (5, 5),
(6, 2), (6, 4),
(7, 6), (7, 8), (7, 10),
(8, 1), (8, 3), (8, 5), (8, 7),
(9, 2), (9, 4), (9, 6), (9, 8),
(10, 3), (10, 5), (10, 7), (10, 9);
