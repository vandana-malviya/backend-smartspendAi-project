import sys
import json

try:
    # 👉 Java से input पढ़ो
    input_data = sys.stdin.read()

    if not input_data:
        print("No input received")
        sys.exit()

    data = json.loads(input_data)

    expenses = data.get("expenses", [])

    if not expenses:
        print("No expense data available")
        sys.exit()

    total = sum(e["amount"] for e in expenses)

    category_map = {}

    for e in expenses:
        cat = e["category"]
        category_map[cat] = category_map.get(cat, 0) + e["amount"]

    # 👉 highest category निकालो
    top_category = max(category_map, key=category_map.get)
    max_spent = category_map[top_category]

    percentage = (max_spent / total) * 100

    # 🎯 AI logic (INSIDE try)
    if percentage > 50:
        result = f"⚠️ You spend {percentage:.2f}% on {top_category}. Reduce it."
    elif percentage > 30:
        result = f"📊 Moderate spending on {top_category}."
    else:
        result = "✅ Your spending is balanced."

    # 🎁 OFFER LOGIC (INSIDE try)
    if percentage > 50:
        offer = f"💡 Try reducing {top_category} expenses and use cashback apps."
    elif percentage > 30:
        offer = f"📊 Plan budget for {top_category} and look for discounts."
    else:
        offer = "🎉 Great! Use reward cards for extra savings."

    # 👉 FINAL OUTPUT
    print(result + " | " + offer)

except Exception as e:
    print("Error:", str(e))