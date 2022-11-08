package org.sid.billingservice;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.sid.billingservice.feign.CustomerServiceClient;
import org.sid.billingservice.feign.InventoryServiceClient;
import org.sid.billingservice.repositories.BillRepository;
import org.sid.billingservice.repositories.ProductItemRepository;
import org.sid.billingservice.serviceConnect.Customer;
import org.sid.billingservice.serviceConnect.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(
		BillRepository billRepository,
		ProductItemRepository productItemRepository,
		CustomerServiceClient customerServiceClient,
		InventoryServiceClient inventoryServiceClient
	){
		return args -> {
			Customer customer = customerServiceClient.getCustomerById(1L);
			Bill bill1 = billRepository.save(new Bill(null, new Date(), null, customer.getId(), null));
			PagedModel<Product> productPagedModel = inventoryServiceClient.pageProducts();
			productPagedModel.forEach(product -> {
				ProductItem productItem = new ProductItem();
				productItem.setPrice(product.getPrice());
				productItem.setQuantity(1 + new Random().nextInt(100));
				productItem.setBill(bill1);
				productItem.setProductID(product.getId());
				productItem.setProductName(product.getName());
				productItemRepository.save(productItem);
			});
		};
	}
}
