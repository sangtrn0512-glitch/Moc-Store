IF
DB_ID(N'ASMJava5') IS NULL CREATE
DATABASE ASMJava5;
GO
USE ASMJava5;
GO
SET NOCOUNT ON;
SET
XACT_ABORT ON;
IF
OBJECT_ID(N'dbo.categories',N'U') IS NOT NULL
    THROW 50001, 'Existing data found. Use migrations instead of running this fresh-install script.', 1;
BEGIN
TRANSACTION;
CREATE TABLE categories
(
    id   INT IDENTITY PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE accounts
(
    username              NVARCHAR(50) PRIMARY KEY,
    password              NVARCHAR(255) NOT NULL,
    full_name             NVARCHAR(100),
    email                 NVARCHAR(255) NOT NULL UNIQUE,
    photo                 NVARCHAR(255),
    activated             BIT NOT NULL DEFAULT 0,
    admin                 BIT NOT NULL DEFAULT 0,
    activation_token      VARCHAR(255),
    activation_expires_at DATETIME2,
    reset_token           VARCHAR(255),
    reset_expires_at      DATETIME2
);
CREATE TABLE products
(
    id          INT IDENTITY PRIMARY KEY,
    name        NVARCHAR(200) NOT NULL,
    image       NVARCHAR(255),
    price       DECIMAL(19, 2) NOT NULL CHECK (price >= 0),
    create_date DATE           NOT NULL DEFAULT CONVERT(DATE, GETDATE()),
    available   BIT            NOT NULL DEFAULT 1,
    discount    DECIMAL(5, 2)  NOT NULL DEFAULT 0 CHECK (discount BETWEEN 0 AND 100),
    category_id INT            NOT NULL REFERENCES categories (id)
);
CREATE TABLE orders
(
    id          BIGINT IDENTITY PRIMARY KEY,
    username    NVARCHAR(50) NOT NULL REFERENCES accounts(username),
    create_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    address     NVARCHAR(500),
    phone       NVARCHAR(20),
    status      NVARCHAR(50) NOT NULL DEFAULT N'PENDING' CHECK(status IN ('PENDING','CONFIRMED','SHIPPING','COMPLETED','CANCELLED'))
);
CREATE TABLE order_details
(
    id         BIGINT IDENTITY PRIMARY KEY,
    order_id   BIGINT         NOT NULL REFERENCES orders (id),
    product_id INT            NOT NULL REFERENCES products (id),
    price      DECIMAL(19, 2) NOT NULL CHECK (price >= 0),
    quantity   INT            NOT NULL CHECK (quantity > 0),
    CONSTRAINT UQ_order_product UNIQUE (order_id, product_id)
);
CREATE INDEX IX_products_category ON products (category_id);
CREATE INDEX IX_orders_username ON orders (username, create_date);
CREATE INDEX IX_details_product ON order_details (product_id);
INSERT
categories(name) VALUES(N'Bình hoa');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 01',N'vase.svg',130000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 02',N'vase.svg',140000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 03',N'vase.svg',150000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 04',N'vase.svg',160000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 05',N'vase.svg',170000,10,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 06',N'vase.svg',180000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 07',N'vase.svg',190000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 08',N'vase.svg',200000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 09',N'vase.svg',210000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 10',N'vase.svg',220000,10,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 11',N'vase.svg',230000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 12',N'vase.svg',240000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 13',N'vase.svg',250000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 14',N'vase.svg',260000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 15',N'vase.svg',270000,10,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 16',N'vase.svg',280000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 17',N'vase.svg',290000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 18',N'vase.svg',300000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 19',N'vase.svg',310000,0,1);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Bình gốm Mộc 20',N'vase.svg',320000,10,1);
INSERT
categories(name) VALUES(N'Đèn trang trí');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 01',N'lamp.svg',150000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 02',N'lamp.svg',160000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 03',N'lamp.svg',170000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 04',N'lamp.svg',180000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 05',N'lamp.svg',190000,10,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 06',N'lamp.svg',200000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 07',N'lamp.svg',210000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 08',N'lamp.svg',220000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 09',N'lamp.svg',230000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 10',N'lamp.svg',240000,10,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 11',N'lamp.svg',250000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 12',N'lamp.svg',260000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 13',N'lamp.svg',270000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 14',N'lamp.svg',280000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 15',N'lamp.svg',290000,10,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 16',N'lamp.svg',300000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 17',N'lamp.svg',310000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 18',N'lamp.svg',320000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 19',N'lamp.svg',330000,0,2);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đèn bàn Mộc 20',N'lamp.svg',340000,10,2);
INSERT
categories(name) VALUES(N'Tranh treo tường');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 01',N'frame.svg',170000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 02',N'frame.svg',180000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 03',N'frame.svg',190000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 04',N'frame.svg',200000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 05',N'frame.svg',210000,10,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 06',N'frame.svg',220000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 07',N'frame.svg',230000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 08',N'frame.svg',240000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 09',N'frame.svg',250000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 10',N'frame.svg',260000,10,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 11',N'frame.svg',270000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 12',N'frame.svg',280000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 13',N'frame.svg',290000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 14',N'frame.svg',300000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 15',N'frame.svg',310000,10,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 16',N'frame.svg',320000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 17',N'frame.svg',330000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 18',N'frame.svg',340000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 19',N'frame.svg',350000,0,3);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Tranh treo tường Mộc 20',N'frame.svg',360000,10,3);
INSERT
categories(name) VALUES(N'Nến thơm');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 01',N'candle.svg',190000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 02',N'candle.svg',200000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 03',N'candle.svg',210000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 04',N'candle.svg',220000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 05',N'candle.svg',230000,10,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 06',N'candle.svg',240000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 07',N'candle.svg',250000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 08',N'candle.svg',260000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 09',N'candle.svg',270000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 10',N'candle.svg',280000,10,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 11',N'candle.svg',290000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 12',N'candle.svg',300000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 13',N'candle.svg',310000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 14',N'candle.svg',320000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 15',N'candle.svg',330000,10,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 16',N'candle.svg',340000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 17',N'candle.svg',350000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 18',N'candle.svg',360000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 19',N'candle.svg',370000,0,4);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Nến thơm Mộc 20',N'candle.svg',380000,10,4);
INSERT
categories(name) VALUES(N'Đồng hồ');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 01',N'clock.svg',210000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 02',N'clock.svg',220000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 03',N'clock.svg',230000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 04',N'clock.svg',240000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 05',N'clock.svg',250000,10,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 06',N'clock.svg',260000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 07',N'clock.svg',270000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 08',N'clock.svg',280000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 09',N'clock.svg',290000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 10',N'clock.svg',300000,10,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 11',N'clock.svg',310000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 12',N'clock.svg',320000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 13',N'clock.svg',330000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 14',N'clock.svg',340000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 15',N'clock.svg',350000,10,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 16',N'clock.svg',360000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 17',N'clock.svg',370000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 18',N'clock.svg',380000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 19',N'clock.svg',390000,0,5);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Đồng hồ gỗ Mộc 20',N'clock.svg',400000,10,5);
INSERT
categories(name) VALUES(N'Chậu cây');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 01',N'plant.svg',230000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 02',N'plant.svg',240000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 03',N'plant.svg',250000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 04',N'plant.svg',260000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 05',N'plant.svg',270000,10,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 06',N'plant.svg',280000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 07',N'plant.svg',290000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 08',N'plant.svg',300000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 09',N'plant.svg',310000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 10',N'plant.svg',320000,10,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 11',N'plant.svg',330000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 12',N'plant.svg',340000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 13',N'plant.svg',350000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 14',N'plant.svg',360000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 15',N'plant.svg',370000,10,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 16',N'plant.svg',380000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 17',N'plant.svg',390000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 18',N'plant.svg',400000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 19',N'plant.svg',410000,0,6);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Chậu cây Mộc 20',N'plant.svg',420000,10,6);
INSERT
categories(name) VALUES(N'Gối trang trí');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 01',NULL,250000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 02',NULL,260000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 03',NULL,270000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 04',NULL,280000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 05',NULL,290000,10,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 06',NULL,300000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 07',NULL,310000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 08',NULL,320000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 09',NULL,330000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 10',NULL,340000,10,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 11',NULL,350000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 12',NULL,360000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 13',NULL,370000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 14',NULL,380000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 15',NULL,390000,10,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 16',NULL,400000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 17',NULL,410000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 18',NULL,420000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 19',NULL,430000,0,7);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gối tựa Mộc 20',NULL,440000,10,7);
INSERT
categories(name) VALUES(N'Thảm trang trí');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 01',NULL,270000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 02',NULL,280000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 03',NULL,290000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 04',NULL,300000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 05',NULL,310000,10,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 06',NULL,320000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 07',NULL,330000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 08',NULL,340000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 09',NULL,350000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 10',NULL,360000,10,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 11',NULL,370000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 12',NULL,380000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 13',NULL,390000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 14',NULL,400000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 15',NULL,410000,10,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 16',NULL,420000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 17',NULL,430000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 18',NULL,440000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 19',NULL,450000,0,8);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Thảm trải sàn Mộc 20',NULL,460000,10,8);
INSERT
categories(name) VALUES(N'Gương trang trí');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 01',NULL,290000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 02',NULL,300000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 03',NULL,310000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 04',NULL,320000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 05',NULL,330000,10,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 06',NULL,340000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 07',NULL,350000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 08',NULL,360000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 09',NULL,370000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 10',NULL,380000,10,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 11',NULL,390000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 12',NULL,400000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 13',NULL,410000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 14',NULL,420000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 15',NULL,430000,10,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 16',NULL,440000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 17',NULL,450000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 18',NULL,460000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 19',NULL,470000,0,9);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Gương treo Mộc 20',NULL,480000,10,9);
INSERT
categories(name) VALUES(N'Giỏ lưu trữ');
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 01',NULL,310000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 02',NULL,320000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 03',NULL,330000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 04',NULL,340000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 05',NULL,350000,10,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 06',NULL,360000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 07',NULL,370000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 08',NULL,380000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 09',NULL,390000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 10',NULL,400000,10,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 11',NULL,410000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 12',NULL,420000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 13',NULL,430000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 14',NULL,440000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 15',NULL,450000,10,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 16',NULL,460000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 17',NULL,470000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 18',NULL,480000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 19',NULL,490000,0,10);
INSERT
products(name,image,price,discount,category_id) VALUES(N'Giỏ đan Mộc 20',NULL,500000,10,10);
INSERT
accounts(username,password,full_name,email,activated,admin) VALUES(N'admin',N'!LOGIN_DISABLED!',N'Tài khoản mẫu 0',N'admin@example.invalid',0,1);
INSERT
accounts(username,password,full_name,email,activated,admin) VALUES(N'customer01',N'!LOGIN_DISABLED!',N'Tài khoản mẫu 1',N'customer01@example.invalid',0,0);
INSERT
accounts(username,password,full_name,email,activated,admin) VALUES(N'customer02',N'!LOGIN_DISABLED!',N'Tài khoản mẫu 2',N'customer02@example.invalid',0,0);
INSERT
accounts(username,password,full_name,email,activated,admin) VALUES(N'customer03',N'!LOGIN_DISABLED!',N'Tài khoản mẫu 3',N'customer03@example.invalid',0,0);
INSERT
accounts(username,password,full_name,email,activated,admin) VALUES(N'customer04',N'!LOGIN_DISABLED!',N'Tài khoản mẫu 4',N'customer04@example.invalid',0,0);
INSERT
accounts(username,password,full_name,email,activated,admin) VALUES(N'customer05',N'!LOGIN_DISABLED!',N'Tài khoản mẫu 5',N'customer05@example.invalid',0,0);
COMMIT;
GO
-- Sample accounts are disabled. Register and activate a real account, then grant admin using the setup guide.
