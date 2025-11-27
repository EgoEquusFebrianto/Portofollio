create schema if not exists retail_transaction;

set search_path to retail_transaction;
show search_path;

CREATE TABLE retail_transaction_dataset (
    TransactionID INTEGER not null,
    Category varchar(15),
    Store varchar(15) NOT NULL,
    Product varchar(45) NOT NULL,
    SalesBeforeDiscount NUMERIC(10,2),
    Discount INTEGER,
    TransactionDate DATE,
    Margin NUMERIC(10,2),
    Revenue NUMERIC(10,2),
    CategoryDiscount varchar(15) NOT NULL,
    ProfitEstimate NUMERIC(10,2),
	constraint pk_retail_transaction primary key(TransactionID)
);

CREATE TABLE sales_per_store_and_category (
    Store varchar(15) NOT NULL,
    Category varchar(15),
    TotalRevenue NUMERIC(10, 2),
    AverageDiscount NUMERIC(10, 2)
);

CREATE TABLE daily_summary (
    Category varchar(15),
    TransactionDate DATE,
    TotalRevenue NUMERIC(10, 2),
    AverageDiscount NUMERIC(10, 2),
    ProfitEstimate NUMERIC(10, 2)
);

CREATE TABLE monthly_summary (
    Month varchar(20),
    TotalRevenue NUMERIC(10, 2),
    AverageDiscount NUMERIC(10, 2),
    TotalProfitEstimate NUMERIC(10, 2)
);

select * from retail_transaction_dataset;
select * from sales_per_store_and_category;
select * from daily_summary;
select * from monthly_summary;