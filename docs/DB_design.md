# Database design

We’ll optimize for:
* Create medications for a user
* Define regimens (med + dosage + schedule window)
* Log intakes (timestamp + status)
* Log symptoms (type + severity + timestamp)
* Produce weekly adherence & symptom trend

Proposed tables (PKs underlined):

* **users**(id), username (unique), password_hash, created_at
* **medications**(id), user_id → users.id (FK), name, form, strength, created_at
* **regimens**(id), user_id (FK), medication_id (FK), dose, times_per_day, start_date, end_date, notes
* **intake_logs**(id), user_id (FK), regimen_id (FK), taken_at (ts), status ENUM(‘TAKEN’,‘MISSED’,‘LATE’), notes
* **symptom_types**(id), name (unique), description
* **symptom_entries**(id), user_id (FK), symptom_type_id (FK), recorded_at (ts), severity INT (1–10), notes

Cardinalities:
* user 1—N medications
* user 1—N regimens; medication 1—N regimens
* regimen 1—N intake_logs
* user 1—N symptom_entries; symptom_type 1—N symptom_entries