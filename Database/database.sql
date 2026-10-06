IF
DB_ID(N'ASMJava5') IS NULL CREATE
DATABASE ASMJava5;
GO
USE ASMJava5;
GO
CREATE TABLE categories
(
    id   INT IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL
);
CREATE TABLE accounts
(
    username  NVARCHAR(50) PRIMARY KEY,
    password  NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100),
    email     NVARCHAR(255) NOT NULL UNIQUE,
    photo     NVARCHAR(255),
    activated BIT NOT NULL DEFAULT 0,
    admin     BIT NOT NULL DEFAULT 0
);
CREATE TABLE products
(
    id          INT IDENTITY PRIMARY KEY,
    name        NVARCHAR(200) NOT NULL,
    image       NVARCHAR(255),
    price       DECIMAL(19, 2) NOT NULL CHECK (price >= 0),
    create_date DATE,
    available   BIT            NOT NULL DEFAULT 1,
    discount    DECIMAL(5, 2)  NOT NULL DEFAULT 0 CHECK (discount BETWEEN 0 AND 100),
    category_id INT            NOT NULL REFERENCES categories (id)
);
CREATE TABLE orders
(
    id          BIGINT IDENTITY PRIMARY KEY,
    username    NVARCHAR(50) NOT NULL REFERENCES accounts(username),
    create_date DATETIME2 NOT NULL,
    address     NVARCHAR(500),
    phone       NVARCHAR(20),
    status      NVARCHAR(50)
);
CREATE TABLE order_details
(
    id         BIGINT IDENTITY PRIMARY KEY,
    order_id   BIGINT         NOT NULL REFERENCES orders (id),
    product_id INT            NOT NULL REFERENCES products (id),
    price      DECIMAL(19, 2) NOT NULL CHECK (price >= 0),
    quantity   INT            NOT NULL CHECK (quantity > 0)
);
GO
-- Chua co du lieu mau: bo sung 10 loai, 200 san pham, 5 nguoi dung va 1 admin.
