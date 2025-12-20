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

    // ---------- Primary Key Helper  ----------
    private static final Map<String, Map<String, String>> PRIMARY_KEYS = Map.of(
            "Electronics", Map.of(
                    "Smartphone", "PROD_ELEC_01",
                    "Laptop", "PROD_ELEC_02",
                    "Wireless Headphones", "PROD_ELEC_03",
                    "Smart Watch", "PROD_ELEC_04",
                    "Tablet", "PROD_ELEC_05",
                    "Bluetooth Speaker", "PROD_ELEC_06",
                    "Gaming Console", "PROD_ELEC_07"
            ),
            "Fashion", Map.of(
                    "T-Shirt", "PROD_FASH_01",
                    "Jeans", "PROD_FASH_02",
                    "Running Shoes", "PROD_FASH_03",
                    "Winter Jacket", "PROD_FASH_04",
                    "Sunglasses", "PROD_FASH_05",
                    "Backpack", "PROD_FASH_06",
                    "Dress", "PROD_FASH_07"
            ),
            "Sports", Map.of(
                    "Basketball", "PROD_SPORT_01",
                    "Yoga Mat", "PROD_SPORT_02",
                    "Running Shoes", "PROD_SPORT_03",
                    "Dumbbells Set", "PROD_SPORT_04",
                    "Tennis Racket", "PROD_SPORT_05",
                    "Swimming Goggles", "PROD_SPORT_06",
                    "Fitness Tracker", "PROD_SPORT_07"
            ),
            "Home", Map.of(
                    "Coffee Maker", "PROD_HOME_01",
                    "Air Fryer", "PROD_HOME_02",
                    "Vacuum Cleaner", "PROD_HOME_03",
                    "Bedding Set", "PROD_HOME_04",
                    "Kitchen Knife Set", "PROD_HOME_05",
                    "LED Lamp", "PROD_HOME_06",
                    "Blender", "PROD_HOME_07"
            ),
            "Books & Stationery", Map.of(
                    "Novel", "PROD_BOOK_01",
                    "Notebook", "PROD_BOOK_02",
                    "Fountain Pen", "PROD_BOOK_03",
                    "Desk Organizer", "PROD_BOOK_04",
                    "Sketchbook", "PROD_BOOK_05",
                    "Bookmark Set", "PROD_BOOK_06",
                    "Sticky Notes", "PROD_BOOK_07"
            )
    );

    // ---------- Categories References Helper  ----------
    private static final Map<String, String> CATEGORIES = Map.of(
            "Electronics", "CATEGORY01",
            "Fashion", "CATEGORY02",
            "Sports", "CATEGORY03",
            "Home", "CATEGORY04",
            "Books & Stationery", "CATEGORY05"
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
            String productId = PRIMARY_KEYS.get(category).get(productName);

            Item item = new Item();
            item.setProductId(productId);
            item.setProductName(productName);
            item.setCategory(CATEGORIES.get(category));
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
