USE ASMJava5;
GO
IF COL_LENGTH('accounts','reset_token') IS NULL ALTER TABLE accounts ADD reset_token VARCHAR(255) NULL;
IF COL_LENGTH('accounts','reset_expires_at') IS NULL ALTER TABLE accounts ADD reset_expires_at DATETIME2 NULL;
GO
