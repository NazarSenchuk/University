
INSERT INTO book.Authors (FirstName, LastName, BirthDate, Nationality)
VALUES 
    ('Тарас', 'Шевченко', '1814-03-09', 'Українська'),
    ('Леся', 'Українка', '1871-02-25', 'Українська'),
    ('Іван', 'Франко', '1856-08-27', 'Українська');
GO

INSERT INTO member.Members (FirstName, LastName, Email, Phone)
VALUES 
    ('Петро', 'Петренко', 'petro@example.com', '+380501234567'),
    ('Марія', 'Іваненко', 'maria@example.com', '+380671234568'),
    ('Олександр', 'Коваленко', 'olexandr@example.com', NULL);
GO


INSERT INTO book.Books (Title, AuthorID, ISBN, PublicationYear, Genre, TotalCopies, AvailableCopies)
VALUES 
    ('Кобзар', 1, '978-617-123-456-7', 2020, 'Поезія', 5, 5),
    ('Лісова пісня', 2, '978-617-123-457-4', 2019, 'Драма', 3, 3),
    ('Захар Беркут', 3, '978-617-123-458-1', 2018, 'Історичний роман', 4, 4);
GO


INSERT INTO library.Librarians (FirstName, LastName, Email, HireDate)
VALUES 
    ('Анна', 'Сидоренко', 'anna@library.com', '2020-01-15'),
    ('Віктор', 'Мельник', 'viktor@library.com', '2021-03-20');
GO

INSERT INTO loan.Loans (MemberID, BookID, LibrarianID, LoanDate, DueDate)
VALUES (1, 1, 1, GETDATE(), DATEADD(DAY, 14, GETDATE()));
GO

UPDATE book.Books 
SET AvailableCopies = AvailableCopies - 1 
WHERE BookID = 1;
GO