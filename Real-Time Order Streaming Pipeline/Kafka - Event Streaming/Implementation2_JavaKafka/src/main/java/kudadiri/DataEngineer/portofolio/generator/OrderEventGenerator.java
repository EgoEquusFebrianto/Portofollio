package kudadiri.DataEngineer.portofolio.generator;

import com.github.javafaker.Faker;
import kudadiri.DataEngineer.portofolio.model.*;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class OrderEventGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    // ---------- Catalog ----------
    private static final Map<String, List<String>> CATALOG = Map.of(
            "Electronics", List.of(
                    "Smartphone", "Laptop", "Wireless Headphones",
                    "Smart Watch", "Tablet", "Bluetooth Speaker",
                    "Gaming Console"
            ),
            "Fashion", List.of(
                    "T-Shirt", "Jeans", "Running Shoes",
                    "Winter Jacket", "Sunglasses", "Backpack", "Dress"
            ),
            "Sports", List.of(
                    "Basketball", "Yoga Mat", "Running Shoes",
                    "Dumbbells Set", "Tennis Racket",
                    "Swimming Goggles", "Fitness Tracker"
            ),
            "Home", List.of(
                    "Coffee Maker", "Air Fryer", "Vacuum Cleaner",
                    "Bedding Set", "Kitchen Knife Set",
                    "LED Lamp", "Blender"
            ),
            "Books & Stationery", List.of(
                    "Novel", "Notebook", "Fountain Pen",
                    "Desk Organizer", "Sketchbook",
                    "Bookmark Set", "Sticky Notes"
            )
    );

    // ---------- Price Range ----------
    private static final Map<String, int[]> PRICE_RANGE = Map.of(
            "Electronics", new int[]{500_000, 15_000_000},
            "Fashion", new int[]{50_000, 2_000_000},
            "Sports", new int[]{100_000, 3_000_000},
            "Home", new int[]{150_000, 5_000_000},
            "Books & Stationery", new int[]{20_000, 500_000}
    );

    public static OrderEvent generate() {

        String timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        String uniqueNumber = String.valueOf(System.currentTimeMillis());

        // ===== Event Level =====
        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType(randomEventType());
        event.setEventTimestamp(timeStamp);

        // ===== Order Level =====
        Order order = new Order();
        order.setOrderId("ORD-" + uniqueNumber);
        order.setOrderTimestamp(timeStamp);
        order.setStatus(randomOrderStatus());
        order.setCurrency("IDR");

        // ===== Customer Level =====
        Customer customer = new Customer();
        customer.setCustomerId("CUS-" + uniqueNumber);
        customer.setCustomerName(faker.name().fullName());
        customer.setEmail(faker.internet().emailAddress());
        customer.setCountry(faker.country().name());

        // ===== Items Level =====
        List<Item> items = new ArrayList<>();
        double totalAmount = 0.0;

        int itemCount = ThreadLocalRandom.current().nextInt(1, 5);

        for (int i = 0; i < itemCount; i++) {
            String category = randomCategory();
            String productName = randomProductName(category);
            int qty = ThreadLocalRandom.current().nextInt(1, 6);
            double unitPrice = randomPrice(category);

            Item item = new Item();
            item.setProductId("SKU-" + uniqueNumber + "-" + i);
            item.setProductName(productName);
            item.setCategory(category);
            item.setQty(qty);
            item.setUnitPrice(unitPrice);

            totalAmount += qty * unitPrice;
            items.add(item);
        }

        order.setTotalAmount(round(totalAmount));

        // ===== Payment Level =====
        Payment payment = new Payment();
        payment.setPaymentType(randomPaymentType());
        payment.setSuccess("payment_success".equals(event.getEventType()));

        // ===== Assemble =====
        event.setOrder(order);
        event.setCustomer(customer);
        event.setItems(items);
        event.setPayment(payment);

        return event;

    }

    private static String randomPaymentType() {
        return List.of(
                "bank_transfer",
                "e-wallet",
                "credit_card"
        ).get(random.nextInt(3));
    }

    private static double round(double totalAmount) {
        return Math.round(totalAmount * 100.0) / 100.0;
    }

    private static double randomPrice(String category) {
        int[] range = PRICE_RANGE.get(category);
        return ThreadLocalRandom.current().nextDouble(range[0], range[1]);
    }

    private static String randomOrderStatus() {
        return List.of(
                "pending",
                "processed",
                "shipped",
                "completed",
                "canceled"
        ).get(random.nextInt(5));
    }

    private static String randomCategory() {
        List<String> keys = new ArrayList<>(CATALOG.keySet());
        return keys.get(random.nextInt(keys.size()));
    }

    private static String randomProductName(String category) {
        List<String> products = CATALOG.get(category);
        return products.get(random.nextInt(products.size()));
    }


    private static String randomEventType() {
        return List.of(
                "pending",
                "processed",
                "shipped",
                "completed",
                "canceled"
        ).get(random.nextInt(5));

    }

    private static String randomEventStatus() {
        return List.of(
                "bank_transfer",
                "e-wallet",
                "credit_card"
        ).get(random.nextInt(3));
    }
}
