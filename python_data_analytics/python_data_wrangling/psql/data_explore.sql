-- Show table schema

\d+ retail;

--Show first 10 rows

SELECT * FROM retail limit 10;

--Check # of records 
SELECT COUNT(invoice_no)
FROM retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id)
FROM retail;

-- Invoice date range (min/max dates)
SELECT MIN(invoice_date) AS min, MAX(invoice_date) AS max
FROM retail;

-- Number of SKU/merchants
SELECT COUNT(DISTINCT stock_code)
FROM retail;

-- Calculate avg invoice amnt excluding invoices with a negative amnt
SELECT AVG(invoice_total)
FROM (
	SELECT invoice_no,
       		SUM(quantity * unit_price) AS invoice_total
	FROM retail
	GROUP BY invoice_no 
	HAVING SUM(quantity * unit_price) > 0);

-- Calculate total revenue (sum of unit_price)
SELECT SUM(unit_price*quantity) AS sum
FROM retail;

-- Calculate total revenue by YYYYMM 
SELECT EXTRACT(YEAR  FROM invoice_date) * 100+ EXTRACT(MONTH FROM invoice_date) AS yyyymm, SUM(quantity * unit_price)
FROM retail
GROUP BY yyyymm
--CAST(yyyymm AS INTEGER)
ORDER BY yyyymm;

