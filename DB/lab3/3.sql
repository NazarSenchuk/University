
-- Таблиця членів бібліотеки (зберігається у першому додатковому файлі)
CREATE TABLE member.Members
(
    MemberID INT IDENTITY(1,1) NOT NULL,
    FirstName NVARCHAR(50) NOT NULL,
    LastName NVARCHAR(50) NOT NULL,
    Email NVARCHAR(100) NOT NULL,
    Phone NVARCHAR(20) NULL,
    RegistrationDate DATE NOT NULL DEFAULT GETDATE(),
    MembershipStatus BIT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Members_MemberID PRIMARY KEY (MemberID),
    CONSTRAINT UQ_Members_Email UNIQUE (Email),
    CONSTRAINT CK_Members_RegistrationDate CHECK (RegistrationDate <= GETDATE())
) ON [DATA1];
GO


CREATE TABLE book.Authors
(
    AuthorID INT IDENTITY(1,1) NOT NULL,
    FirstName NVARCHAR(50) NOT NULL,
    LastName NVARCHAR(50) NOT NULL,
    BirthDate DATE NULL,
    Nationality NVARCHAR(50) NULL,
    CONSTRAINT PK_Authors_AuthorID PRIMARY KEY (AuthorID),
    CONSTRAINT CK_Authors_BirthDate 
) ON [DATA2];
GO


CREATE TABLE book.Books
(
    BookID INT IDENTITY(1,1) NOT NULL,
    Title NVARCHAR(200) NOT NULL,
    AuthorID INT NOT NULL,
    ISBN NVARCHAR(20) NOT NULL,
    PublicationYear INT NOT NULL,
    Genre NVARCHAR(50) NOT NULL,
    TotalCopies INT NOT NULL DEFAULT 1,
    AvailableCopies INT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Books_BookID PRIMARY KEY (BookID),
    CONSTRAINT UQ_Books_ISBN UNIQUE (ISBN),
    CONSTRAINT FK_Books_Authors FOREIGN KEY (AuthorID) 
        REFERENCES book.Authors(AuthorID),
    CONSTRAINT CK_Books_Copies CHECK (AvailableCopies >= 0 AND TotalCopies >= AvailableCopies),
) ON [PRIMARY];
GO


CREATE TABLE loan.Loans
(
    LoanID INT IDENTITY(1,1) NOT NULL,
    MemberID INT NOT NULL,
    BookID INT NOT NULL,
    LoanDate DATE NOT NULL DEFAULT GETDATE(),
    DueDate DATE NOT NULL,
    ReturnDate DATE NULL,
    CONSTRAINT PK_Loans_LoanID PRIMARY KEY (LoanID),
    CONSTRAINT FK_Loans_Members FOREIGN KEY (MemberID) 
        REFERENCES member.Members(MemberID),
    CONSTRAINT FK_Loans_Books FOREIGN KEY (BookID) 
        REFERENCES book.Books(BookID),
    CONSTRAINT UQ_Loans_Active UNIQUE (MemberID, BookID, ReturnDate)
) ON [DATA1];
GO


CREATE TABLE library.Librarians
(
    LibrarianID INT IDENTITY(1,1) NOT NULL,
    FirstName NVARCHAR(50) NOT NULL,
    LastName NVARCHAR(50) NOT NULL,
    Email NVARCHAR(100) NOT NULL,
    HireDate DATE NOT NULL DEFAULT GETDATE(),
    IsActive BIT NOT NULL DEFAULT 1,
    CONSTRAINT PK_Librarians_LibrarianID PRIMARY KEY (LibrarianID),
    CONSTRAINT UQ_Librarians_Email UNIQUE (Email),
    CONSTRAINT CK_Librarians_HireDate CHECK (HireDate <= GETDATE())
) ON [DATA2];
GO