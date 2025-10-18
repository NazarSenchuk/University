
USE master;
GO

IF EXISTS(SELECT * FROM sys.databases WHERE name = 'LibraryManagementSystem')
BEGIN
    ALTER DATABASE LibraryManagementSystem SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE LibraryManagementSystem;
END
GO

