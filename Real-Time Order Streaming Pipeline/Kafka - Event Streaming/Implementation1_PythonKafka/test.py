from faker import Faker
from datetime import datetime

bil = datetime.now()
fake = Faker()

print(bil.isoformat() + "Z")
print(bil.strftime("%Y%m%d%H%M%S"))

catalog = {
    "a": "A",
    "b": "b",
}

print(type(catalog.keys()))