import pandas as pd
import numpy as np

np.random.seed(42)
n = 50

transaction_ids = range(1, n+1)
stores = np.random.choice(["Store A", "Store B", "Store C"], n)

# mapping produk ↔ kategori
product_category_map = {
    "Electronics": ["Laptop", "Phone", "TV", "Headphones"],
    "Clothing": ["Shirt", "Pants", "Shoes"],
    "Home": ["Sofa", "Table", "Chair"],
    "Sports": ["Ball", "Racket", "Shoes Sport"],
    "Beauty": ["Perfume", "Lotion", "Makeup"]
}

categories = np.random.choice(list(product_category_map.keys()), n)
products = [np.random.choice(product_category_map[cat]) for cat in categories]

# sales & discount
sales_before_discount = np.round(np.random.uniform(100, 1000, n), 2)
discount = np.random.choice([0, 2, 4, 5, 10], n)

dates = pd.date_range("2025-08-01", periods=n, freq="D")

df = pd.DataFrame({
    "TransactionID": transaction_ids,
    "Store": stores,
    "Category": categories,
    "Product": products,
    "SalesBeforeDiscount": sales_before_discount,
    "Discount": discount,
    "TransactionDate": dates
})

# tambahkan missing values
df.loc[5, "Store"] = None
df.loc[10, "Category"] = None
df.loc[15, "Product"] = None

# tambahkan duplicated values
df = pd.concat([df, df.iloc[[3, 7, 10]]], ignore_index=True)

# tambahkan anomali
df.loc[25, "SalesBeforeDiscount"] = -200   # tidak valid
df.loc[30, "Discount"] = 999              # diskon aneh

df.to_csv("retail_transactions.csv", index=False)
# df.head(10)