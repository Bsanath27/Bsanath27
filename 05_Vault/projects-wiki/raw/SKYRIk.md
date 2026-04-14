# APP ANALYTICS (CLEAN + BUILDABLE)

## 🧭 TOTAL STRUCTURE

### 👨‍✈️ Pilot App

- **6 screens**
- Focus: execution + checklist + status updates

### 🏢 Operator App

- **10 screens**
- Focus: control + assignment + monitoring

---

# 🏢 OPERATOR APP (10 SCREENS)

---

## **O-1: Login**

**Features (3)**

- Email input
- Password input
- Sign in button

👉 Minimal. No need to overbuild.

---

## **O-2: Dashboard**

**Features (5)**

- Stats cards (Pending, Active flights)
- Fleet status summary
- Incoming requests preview list
- View all requests CTA
- Navigation (bottom bar)

👉 This is just **overview, not interaction-heavy**

---

## **O-3: Requests List**

**Features (6)**

- Tab switch (Incoming / My Slots)
- Request cards list
- Status chips (Pending, Confirmed)
- Request metadata (route, pax, date)
- Tap → navigate to detail
- Publish Slot button

👉 Core intake system

---

## **O-4: Request Detail**

**Features (7)**

- Flight details card
- Route info (from/to)
- Status display
- Assign aircraft dropdown
- Assign pilot dropdown
- Confirm button
- Reject button

👉 **Most important screen in entire app**

---

## **O-5: Publish Slot**

**Features (6)**

- Origin input
- Destination input
- Date picker
- Seats stepper
- Notes input
- Publish button

👉 Simple form — no complexity needed

---

## **O-6: Fleet**

**Features (5)**

- Aircraft list
- Status indicator (Ready, Maintenance, In-flight)
- Aircraft basic info
- Tap → detail
- Add aircraft button

---

## **O-7: Aircraft Detail**

**Features (6)**

- Aircraft info (reg, type, capacity)
- Status display
- Toggle maintenance
- Maintenance note input
- Current assignment view
- Edit mode toggle

---

## **O-8: Live Flights**

**Features (4)**

- Map view
- Aircraft markers
- Active flights list
- Tap → see details

👉 Keep map basic. Don’t over-engineer.

---

## **O-9: Flight Notes**

**Features (4)**

- Notes list (chat style)
- Timestamp + role label
- Input field
- Send button

---

## **O-10: Settings**

**Features (4)**

- Org profile info
- Notification toggles
- Account info
- Sign out

---

# 👨‍✈️ PILOT APP (6 SCREENS)

---

## **P-1: My Flights**

**Features (5)**

- Sections (Today / Upcoming / Past)
- Flight cards
- Status chips
- Aircraft info
- Tap → detail

---

## **P-2: Flight Detail (Pre-flight)**

**Features (6)**

- Route info
- Assignment info
- Checklist (7 items)
- Progress counter
- Depart button (disabled/enabled)
- Notes access

👉 This is pilot’s main working screen

---

## **P-3: Flight Detail (En Route)**

**Features (4)**

- Tracking status
- Last update time
- Assignment info
- Land button

---

## **P-4: Flight Detail (Landed)**

**Features (3)**

- Status display
- Tracking stopped info
- Mark complete button

---

## **P-5: Flight Notes**

**Features (4)**

- Notes list
- Role labels
- Input field
- Send button

---

## **P-6: Settings**

**Features (3)**

- Profile info
- Notification toggles
- Sign out

---

# 📊 TOTAL FEATURE COUNT (REALISTIC MVP)

### Operator:

- **~50–55 UI elements / interactions**

### Pilot:

- **~25–30 UI elements**

👉 Combined system:

> **~80 features total (lean MVP)**

---

# 🧠 WHAT THIS MEANS

- This is **perfect MVP scope**
- You can build this in:
    - 2–3 weeks (focused)
    - 1–2 months (polished)

No need to add anything else right now.