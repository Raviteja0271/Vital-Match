import requests

SUPABASE_URL = "https://wcoipyffkhhvcpknrqpb.supabase.co"
SUPABASE_KEY = "sb_publishable_dE6gJYL2yGgxS9lIEKoqHA_FDooPDHg"

headers = {
    "apikey": SUPABASE_KEY,
    "Authorization": f"Bearer {SUPABASE_KEY}",
    "Content-Type": "application/json",
    "Prefer": "return=minimal"
}

tables = [
    "notifications",
    "reports",
    "donations",
    "blood_requests",
    "emergency_requests",
    "profiles"
]

print("=====================================================")
print(" CLEARING ALL SUPABASE DATABASE DATA...")
print("=====================================================")

for t in tables:
    url = f"{SUPABASE_URL}/rest/v1/{t}?id=neq.00000000-0000-0000-0000-000000000000"
    res = requests.delete(url, headers=headers)
    print(f"  • Cleared Table '{t}': HTTP {res.status_code}")

print("\nAll database tables reset to 0 records!")
