USE ASMJava5;
GO
IF COL_LENGTH('accounts','activation_token') IS NULL ALTER TABLE accounts ADD activation_token VARCHAR(255) NULL;
IF COL_LENGTH('accounts','activation_expires_at') IS NULL ALTER TABLE accounts ADD activation_expires_at DATETIME2 NULL;
GO
