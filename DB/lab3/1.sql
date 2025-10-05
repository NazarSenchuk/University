
CREATE DATABASE LibraryManagementSystem
ON PRIMARY
(
    NAME = LibraryManagementSystem_Data,
    FILENAME = '/tmp/LibraryManagementSystem.mdf',
    SIZE = 50MB,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 10MB
),
FILEGROUP DATA1
(
    NAME = LibraryManagementSystem_Data1,
    FILENAME = '/tmp/LibraryManagementSystem1.ndf',
    SIZE = 25MB,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5MB
),
FILEGROUP DATA2
(
    NAME = LibraryManagementSystem_Data2,
    FILENAME = '/tmp/LibraryManagementSystem2.ndf',
    SIZE = 25MB,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5MB
)
LOG ON
(
    NAME = LibraryManagementSystem_Log,
    FILENAME = '/tmp/LibraryManagementSystem.ldf',
    SIZE = 25MB,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5MB
);
GO

USE LibraryManagementSystem;
GO