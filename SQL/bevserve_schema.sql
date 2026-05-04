CREATE DATABASE IF NOT EXISTS bevserve;
USE bevserve;

CREATE TABLE categories (
    Category_ID   VARCHAR(20)   PRIMARY KEY,
    Category_Name VARCHAR(100)  NOT NULL,
    Category_Desc VARCHAR(255)
);

CREATE TABLE users (
    User_ID      VARCHAR(50)   PRIMARY KEY,
    Full_Name    VARCHAR(150)  NOT NULL,
    Email        VARCHAR(150)  UNIQUE NOT NULL,
    Phone_Number VARCHAR(15)   UNIQUE NOT NULL,
    Password     TEXT          NOT NULL,
    Role         VARCHAR(10)   DEFAULT 'user',
    ADDRESS      TEXT,
    IMG_LINK     VARCHAR(255)
);

CREATE TABLE beverages (
    Beverage_ID   VARCHAR(20)    PRIMARY KEY,
    Beverage_Name VARCHAR(150)   NOT NULL,
    Description   TEXT,
    Price         DECIMAL(10,2)  NOT NULL,
    Discount      DECIMAL(5,2)   DEFAULT 0,
    Discount_Amt  DECIMAL(10,2)  DEFAULT 0,
    Stock_Qty     INT            DEFAULT 0,
    Category_ID   VARCHAR(20),
    Image         VARCHAR(255),
    FOREIGN KEY (Category_ID) REFERENCES categories(Category_ID) ON DELETE SET NULL
);

CREATE TABLE carts (
    Cart_ID     VARCHAR(20) PRIMARY KEY,
    User_ID     VARCHAR(50),
    Beverage_ID VARCHAR(20),
    Quantity    INT DEFAULT 1,
    FOREIGN KEY (User_ID)     REFERENCES users(User_ID)      ON DELETE CASCADE,
    FOREIGN KEY (Beverage_ID) REFERENCES beverages(Beverage_ID) ON DELETE CASCADE
);

CREATE TABLE orders (
    Order_ID     VARCHAR(20)    PRIMARY KEY,
    User_ID      VARCHAR(50),
    Total_Amount DECIMAL(10,2),
    Status       VARCHAR(20)    DEFAULT 'Pending',
    City         VARCHAR(100),
    Address      TEXT,
    Payment      VARCHAR(50),
    FOREIGN KEY (User_ID) REFERENCES users(User_ID) ON DELETE SET NULL
);

CREATE TABLE order_items (
    Order_ID      VARCHAR(20),
    Beverage_ID   VARCHAR(20),
    Line_Quantity INT DEFAULT 1,
    PRIMARY KEY (Order_ID, Beverage_ID),
    FOREIGN KEY (Order_ID)     REFERENCES orders(Order_ID)      ON DELETE CASCADE,
    FOREIGN KEY (Beverage_ID)  REFERENCES beverages(Beverage_ID) ON DELETE CASCADE
);

CREATE TABLE inquiry (
    Inquiry_ID VARCHAR(20) PRIMARY KEY,
    User_ID    VARCHAR(50),
    Subject    VARCHAR(200),
    Created_At DATETIME,
    Message    TEXT,
    FOREIGN KEY (User_ID) REFERENCES users(User_ID) ON DELETE CASCADE
);

-- Seed data: admin account (password = Admin@123)
INSERT INTO categories VALUES
('CAT001','Hot Drinks','Warm beverages including teas and coffees'),
('CAT002','Cold Drinks','Chilled and iced beverages'),
('CAT003','Juices','Fresh and packaged fruit juices'),
('CAT004','Smoothies','Blended fruit and dairy smoothies');