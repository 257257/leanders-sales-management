CREATE TABLE sales (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    sale_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_sales PRIMARY KEY (id),

    CONSTRAINT fk_sales_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id),

    CONSTRAINT fk_sales_product
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT chk_sales_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_sales_unit_price
        CHECK (unit_price >= 0),

    CONSTRAINT chk_sales_total_amount
        CHECK (total_amount >= 0)
);