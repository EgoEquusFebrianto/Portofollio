# Retail Kafka Producer – Executable Runner

Project ini menyediakan **Kafka Producer berbasis Java** yang dijalankan melalui file executable (`run.bat` dan `run.sh`) untuk memudahkan eksekusi lintas sistem operasi.

Aplikasi dijalankan sebagai **Executable JAR** dan menerima parameter runtime melalui command-line argument.

---

## 📦 Prerequisites

Sebelum menjalankan aplikasi, pastikan:

- **Java 11 atau lebih baru** sudah terinstal
- File `RetailProducerApp.jar` berada pada direktori yang sama dengan script
- Kafka Broker sudah berjalan dan dapat diakses

---

## 🧩 Parameter Aplikasi

Aplikasi membutuhkan **3 parameter wajib** dengan urutan sebagai berikut:

```text
<User> <bootstrap-server> <topic-name>
```
```text
contoh: 
./run.bat kudadiri localhost:9092,localhost:9093,localhost:9094 testing