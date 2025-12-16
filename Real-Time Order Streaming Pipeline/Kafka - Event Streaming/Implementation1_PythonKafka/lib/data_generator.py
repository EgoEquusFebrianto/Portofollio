from faker import Faker
import uuid
import random
from datetime import datetime

fake = Faker()
catalog = {
    "Electronics": [
        "Smartphone", "Laptop", "Wireless Headphones",
        "Smart Watch", "Tablet", "Bluetooth Speaker",
        "Gaming Console"
    ],
    "Fashion": [
        "T-Shirt", "Jeans", "Running Shoes",
        "Winter Jacket", "Sunglasses", "Backpack", "Dress"
    ],
    "Sports": [
        "Basketball", "Yoga Mat", "Running Shoes",
        "Dumbbells Set", "Tennis Racket",
        "Swimming Goggles", "Fitness Tracker"
    ],
    "Home": [
        "Coffee Maker", "Air Fryer", "Vacuum Cleaner",
        "Bedding Set", "Kitchen Knife Set",
        "LED Lamp", "Blender"
    ],
    "Books & Stationery": [
        "Novel", "Notebook", "Fountain Pen",
        "Desk Organizer", "Sketchbook",
        "Bookmark Set", "Sticky Notes"
    ]
}

def price_by_category(category):
    price_range = {
        "Electronics": (500_000, 15_000_000),
        "Fashion": (50_000, 2_000_000),
        "Sports": (100_000, 3_000_000),
        "Home": (150_000, 5_000_000),
        "Books & Stationery": (20_000, 500_000)
    }
    low, high = price_range[category]
    return round(random.uniform(low, high), 2)

def generate_order_event():
    time_now = datetime.now()
    unique_number = time_now.strftime("%Y%m%d%H%M%S")
    timestamp = time_now.isoformat() + "Z"
    event_choice = ["order_create", "order_updated", "payment_success", "payment_failed"]
    order_choice = ["pending", "processed", "shipped", "completed", "canceled"]

    # Events Level
    event_id = str(uuid.uuid4())
    event_type = random.choice(event_choice)

    # Orders Level
    order_id = f"ORD-{unique_number}{fake.random_int(100, 999)}"
    order_status = random.choice(order_choice)
    currency = "IDR"

    # Customers Level
    customer_id = f"CUS-{unique_number}{fake.random_int(100, 999)}"
    customer = {
        "customer_id": customer_id,
        "customer_name": fake.name(),
        "email": fake.email(),
        "country": fake.country()
    }

    # Items Level
    items = []
    total_amount = 0.0

    for _ in range(random.randint(1, 4)):
        category = random.choice(list(catalog.keys()))
        product_name = random.choice(catalog[category])
        qty = random.randint(1, 5)
        unit_price = price_by_category(category)

        item_total = qty * unit_price
        total_amount += item_total

        items.append({
            "product_id": f"SKU-{unique_number}{fake.random_int(100, 999)}",
            "product_name": product_name,
            "category": category,
            "qty": qty,
            "unit_price": unit_price
        })
    
    total_amount = round(total_amount, 2)
    payment = {
        "payment_type": random.choice(["bank_transfer", "e-wallet", "credit_card"]),
        "success": event_type == "payment_success"
    }

    event = {
        "event_id": event_id,
        "event_type": event_type,
        "event_timestamp": timestamp,

        "order": {
            "order_id": order_id,
            "order_timestamp": timestamp,
            "status": order_status,
            "total_amount": total_amount,
            "currency": currency
        },

        "customer": customer,
        "items": items,
        "payment": payment
    }

    return event