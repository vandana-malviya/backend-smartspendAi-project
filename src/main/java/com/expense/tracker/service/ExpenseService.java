package com.expense.tracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.tracker.model.Expense;
import com.expense.tracker.model.User; // ✅ FIXED IMPORT
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.repository.UserRepository;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AIService aiService;

    // ✅ SAVE EXPENSE
    public Expense saveExpense(Expense expense) {
        return repository.save(expense);
    }

    // ✅ USER-WISE GET
    public List<Expense> getUserExpenses(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repository.findByUser(user);
    }

    // ✅ DELETE
    public String deleteExpense(Long id) {
        repository.deleteById(id);
        return "Deleted Successfully";
    }

    // ✅ TOTAL EXPENSE (USER-WISE)
    public double getTotalExpense(String username) {
        List<Expense> expenses = getUserExpenses(username);

        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    // ✅ TOP CATEGORY (USER-WISE)
    public String getTopCategory(String username) {
        List<Expense> expenses = getUserExpenses(username);

        Map<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            map.put(e.getCategory(),
                map.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }

        String topCategory = null;
        double max = 0;

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                topCategory = entry.getKey();
            }
        }

        return topCategory;
    }

    // ✅ CATEGORY-WISE
    public Map<String, Double> getCategoryWiseExpense(String username) {
        List<Expense> expenses = getUserExpenses(username);

        Map<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            map.put(e.getCategory(),
                map.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }

        return map;
    }

    // ✅ HIGHEST EXPENSE
    public Expense getHighestExpense(String username) {
        List<Expense> expenses = getUserExpenses(username);

        Expense maxExpense = null;

        for (Expense e : expenses) {
            if (maxExpense == null || e.getAmount() > maxExpense.getAmount()) {
                maxExpense = e;
            }
        }

        return maxExpense;
    }

    // ✅ DATE FILTER
    public List<Expense> getExpenseByDate(String username, java.time.LocalDate date) {
        List<Expense> expenses = getUserExpenses(username);

        List<Expense> result = new java.util.ArrayList<>();

        for (Expense e : expenses) {
            if (e.getDate() != null && e.getDate().equals(date)) {
                result.add(e);
            }
        }

        return result;
    }

    // ✅ MONTHLY
    public Map<String, Double> getMonthlyExpense(String username) {
        List<Expense> expenses = getUserExpenses(username);

        Map<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            if (e.getDate() != null) {
                String month = e.getDate().getMonth().toString();

                map.put(month,
                    map.getOrDefault(month, 0.0) + e.getAmount());
            }
        }

        return map;
    }

    // ✅ BUDGET CHECK
    public String checkBudget(String username, double limit) {
        double total = getTotalExpense(username);

        if (total > limit) {
            return "⚠️ Budget exceeded";
        } else {
            return "✅ Within budget";
        }
    }

    // ✅ SMART INSIGHT
    public String getSmartInsight(String username) {
        String top = getTopCategory(username);
        double total = getTotalExpense(username);

        return "You spend most on " + top + " and total is " + total;
    }

    // ✅ ADVANCED AI
    public String getAdvancedAIInsight(String username) {

        List<Expense> expenses = getUserExpenses(username);

        if (expenses.isEmpty()) {
            return "No expense data available";
        }

        double total = 0;
        Map<String, Double> map = new HashMap<>();

        for (Expense e : expenses) {
            total += e.getAmount();

            map.put(
                e.getCategory(),
                map.getOrDefault(e.getCategory(), 0.0) + e.getAmount()
            );
        }

        String topCategory = "";
        double max = 0;

        for (String key : map.keySet()) {
            if (map.get(key) > max) {
                max = map.get(key);
                topCategory = key;
            }
        }

        double percentage = (max / total) * 100;

        if (percentage > 50) {
            return "⚠️ High spending on " + topCategory +
                   ". Try reducing. Offer: discounts on " + topCategory;
        } 
        else if (percentage > 30) {
            return "📊 Moderate spending on " + topCategory;
        } 
        else {
            return "✅ Balanced spending";
        }
    }

    // ✅ REAL AI
    public String getRealAIInsight(String username) {

        List<Expense> expenses = getUserExpenses(username);

        if (expenses.isEmpty()) {
            return "No expense data available";
        }

        double total = 0;
        StringBuilder data = new StringBuilder();

        for (Expense e : expenses) {
            total += e.getAmount();

            data.append(e.getCategory())
                .append(": ")
                .append(e.getAmount())
                .append(", ");
        }

        String prompt = "User expenses: " + data +
                ". Total: " + total +
                ". Give smart financial advice.";

        return aiService.getAIResponse(prompt);
    }
    

// 🔥 FINAL WORKING PYTHON AI METHOD
public String getPythonAIInsight(String username) {

    try {
        List<Expense> expenses = getUserExpenses(username);

        if (expenses.isEmpty()) {
            return "No expense data available";
        }

        // JSON बनाना
        StringBuilder json = new StringBuilder();
        json.append("{\"expenses\":[");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);

            json.append("{\"category\":\"")
                .append(e.getCategory())
                .append("\",\"amount\":")
                .append(e.getAmount())
                .append("}");

            if (i < expenses.size() - 1) {
                json.append(",");
            }
        }

        json.append("]}");

        // 🔥 PYTHON CALL
        ProcessBuilder pb = new ProcessBuilder("python3", "ai.py");
        Process process = pb.start();

        // 👉 data send
        java.io.OutputStream os = process.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();

        // 👉 WAIT for python to finish 🔥 (IMPORTANT)
        process.waitFor();

        // 👉 FULL OUTPUT READ करो
        java.io.BufferedReader reader =
                new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream())
                );

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        return output.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "AI Error";
    }
}

public String getPrediction(String username) {
    try {
        List<Expense> expenses = getUserExpenses(username);

        StringBuilder json = new StringBuilder();
        json.append("{\"expenses\":[");

        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);

            json.append("{\"amount\":")
                .append(e.getAmount())
                .append("}");

            if (i < expenses.size() - 1) {
                json.append(",");
            }
        }

        json.append("]}");

        ProcessBuilder pb = new ProcessBuilder("python3", "predict.py");
        Process process = pb.start();

        java.io.OutputStream os = process.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();

        process.waitFor();

        java.io.BufferedReader reader =
            new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );

        return reader.readLine();

    } catch (Exception e) {
        return "Prediction error";
    }
}


public String compareMonthlyExpense(String username) {

    List<Expense> expenses = getUserExpenses(username);

    double currentMonthTotal = 0;
    double lastMonthTotal = 0;

    java.time.LocalDate now = java.time.LocalDate.now();

    int currentMonth = now.getMonthValue();
    int lastMonth = now.minusMonths(1).getMonthValue();

    for (Expense e : expenses) {
        if (e.getDate() != null) {

            int expenseMonth = e.getDate().getMonthValue();

            if (expenseMonth == currentMonth) {
                currentMonthTotal += e.getAmount();
            } 
            else if (expenseMonth == lastMonth) {
                lastMonthTotal += e.getAmount();
            }
        }
    }

    if (lastMonthTotal == 0) {
        return "No data for last month to compare";
    }

    double diff = currentMonthTotal - lastMonthTotal;
    double percent = (diff / lastMonthTotal) * 100;

    if (diff > 0) {
        return "📈 Your spending increased by " + String.format("%.2f", percent) + "% compared to last month";
    } else {
        return "📉 Your spending decreased by " + String.format("%.2f", Math.abs(percent)) + "% compared to last month";
    }
}



public String getSmartAlert(String username, double limit) {

    double total = getTotalExpense(username);

    double percent = (total / limit) * 100;

    if (percent >= 100) {
        return "🚨 Budget exceeded! You spent " + total;
    } 
    else if (percent >= 80) {
        return "⚠️ Warning: You have used " + String.format("%.2f", percent) + "% of your budget";
    } 
    else {
        return "✅ You are within safe spending range";
    }
}

}