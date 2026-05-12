import sys
import json
from sklearn.linear_model import LinearRegression
import numpy as np

try:
    input_data = sys.stdin.read()
    data = json.loads(input_data)

    expenses = data.get("expenses", [])

    if len(expenses) < 2:
        print("Not enough data for prediction")
        sys.exit()

    # X = index (time), Y = amount
    X = []
    Y = []

    for i, e in enumerate(expenses):
        X.append([i])
        Y.append(e["amount"])

    model = LinearRegression()
    model.fit(X, Y)

    next_index = len(expenses)
    prediction = model.predict([[next_index]])

    print(f"📊 Predicted next expense: ₹{prediction[0]:.2f}")

except Exception as e:
    print("Error:", str(e))