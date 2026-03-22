package com.sss.cartnest.admincontrollers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sss.cartnest.adminservices.AdminBusinessService;

@RestController
@RequestMapping("/admin/business")
public class AdminBusinessController {

	@Autowired
	private AdminBusinessService adminBusinessService;

	// GET /admin/business/monthly?month=3&year=2026
	@GetMapping("/monthly")
	public ResponseEntity<?> getMonthlyBusiness(
			@RequestParam int month,
			@RequestParam int year) {
		try {
			Map<String, Object> report = adminBusinessService.calculateMonthlyBusiness(month, year);
			return ResponseEntity.ok(report);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Something went wrong"));
		}
	}

	// GET /admin/business/yearly?year=2026
	@GetMapping("/yearly")
	public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {
		try {
			Map<String, Object> report = adminBusinessService.calculateYearlyBusiness(year);
			return ResponseEntity.ok(report);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Something went wrong"));
		}
	}

	// GET /admin/business/daily?date=2026-03-22
	@GetMapping("/daily")
	public ResponseEntity<?> getDailyBusiness(@RequestParam String date) {
		try {
			LocalDate localDate = LocalDate.parse(date);
			Map<String, Object> report = adminBusinessService.calculateDailyBusiness(localDate);
			return ResponseEntity.ok(report);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Something went wrong"));
		}
	}

	// GET /admin/business/overall
	@GetMapping("/overall")
	public ResponseEntity<?> getOverallBusiness() {
		try {
			Map<String, Object> report = adminBusinessService.calculateOverallBusiness();
			return ResponseEntity.ok(report);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Something went wrong"));
		}
	}
}