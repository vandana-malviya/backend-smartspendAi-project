package com.expense.tracker.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracker.model.Expense;
import com.expense.tracker.model.User;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.ExpenseService;
import com.expense.tracker.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService service;
    @Autowired
    private UserRepository userRepo;
    // ✅ GET ALL (USER-WISE)
    @GetMapping
    public List<Expense> getExpenses(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.getUserExpenses(username);
    }

    // ✅ ADD EXPENSE
   @PostMapping
public Expense addExpense(@RequestBody Expense expense,
                         HttpServletRequest request) {

    String token = request.getHeader("Authorization").substring(7);
    String username = JwtUtil.extractUsername(token);

    User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    expense.setUser(user); // ✅ FIXED

    return service.saveExpense(expense);
}

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {
        return service.deleteExpense(id);
    }

    // ✅ HIGHEST
    @GetMapping("/highest")
    public Expense getHighest(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.getHighestExpense(username);
    }

    // ✅ CATEGORY
    @GetMapping("/category-wise")
    public Map<String, Double> category(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.getCategoryWiseExpense(username);
    }

    // ✅ DATE FILTER
    @GetMapping("/date/{date}")
    public List<Expense> getByDate(@PathVariable String date,
                                  HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.getExpenseByDate(username, LocalDate.parse(date));
    }

    // ✅ MONTHLY
    @GetMapping("/monthly")
    public Map<String, Double> monthly(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.getMonthlyExpense(username);
    }

    // ✅ BUDGET
    @GetMapping("/budget/{amount}")
    public String budget(@PathVariable double amount,
                         HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.checkBudget(username, amount);
    }

    // ✅ AI
    @GetMapping("/ai")
    public String ai(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String username = JwtUtil.extractUsername(token);

        return service.getAdvancedAIInsight(username);
    }


    @GetMapping("/top-category")
public String topCategory(HttpServletRequest request) {

    String token = request.getHeader("Authorization").substring(7);
    String username = JwtUtil.extractUsername(token);

    return service.getTopCategory(username);
}

@GetMapping("/python-ai")
public String getPythonAI(HttpServletRequest request) {

    String token = request.getHeader("Authorization").substring(7);
    String username = JwtUtil.extractUsername(token);

    return service.getPythonAIInsight(username);
}


@GetMapping("/predict")
public String predict(HttpServletRequest request) {

    String token = request.getHeader("Authorization").substring(7);
    String username = JwtUtil.extractUsername(token);

    return service.getPrediction(username);
}


@GetMapping("/compare")
public String compare(HttpServletRequest request) {

    String token = request.getHeader("Authorization").substring(7);
    String username = JwtUtil.extractUsername(token);

    return service.compareMonthlyExpense(username);
}


@GetMapping("/alert/{limit}")
public String alert(@PathVariable double limit,
                    HttpServletRequest request) {

    String token = request.getHeader("Authorization").substring(7);
    String username = JwtUtil.extractUsername(token);

    return service.getSmartAlert(username, limit);
}
}