package com.leander.salesmanagement;

import org.flywaydb.core.Flyway;

public class FlywayRepair {

	public static void main(String[] args) {
		Flyway flyway = Flyway.configure()
				.dataSource("jdbc:mysql://localhost:3306/leanders_sales_management", "root", "root")
				.locations("classpath:db/migration").load();

		flyway.repair();

		System.out.println("Flyway repair completed.");
	}
}