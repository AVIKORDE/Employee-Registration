package com.example.demo.Controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring6.processor.SpringUErrorsTagProcessor;

import com.example.demo.entity.DepMapping;
import com.example.demo.entity.Depart;
import com.example.demo.entity.User;
import com.example.demo.service.DepDService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	DepDService depDService;

	@GetMapping("/")
	public String home(HttpServletRequest request,Model model) {
		String error=request.getParameter("message");
		if(error!=null)
		{
			 model.addAttribute("errorMessage", error);
		}
		return "index";
	}

	@GetMapping("/list")
	public String listUsers(Model model, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {

			List<User> userList = userService.getAllUsers();
			model.addAttribute("users", userList);
			return "list";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.removeAttribute("loggedInUser");
		return "redirect:/";
	}

	@GetMapping("/registrationts")
	public String registration() {

		return "registrationts";
	}

	@PostMapping("/register")
	public String processRegistrationForm(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("city") String city,
			@RequestParam("country") String country, @RequestParam("designation") String designation,
			@RequestParam("birth_date") String birthDate, @RequestParam("photo") MultipartFile photo,
			@RequestParam("mobileno") String mobileNo, @RequestParam("username") String username, Model model) {
		if (userService.existsByEmail(email)) {
	        model.addAttribute("emailError", "Email is already registered.");

	        return "registrationts"; 
	    }

	    if (userService.existsByUsername(username)) {
	        model.addAttribute("usernameError", "Username is already taken.");

	        return "registrationts";
	    }
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setCountry(country);
		user.setDateofbirth(birthDate);
		user.setDesignation(designation);
		user.setPassword(password);
		user.setCity(city);
		user.setUsername(username);
		user.setMobileno(mobileNo);
		if (photo != null) {
			byte[] bytes;
			try {
				bytes = photo.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				user.setPhoto(blob);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SerialException e) {
				e.printStackTrace();
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}
		userService.saveUser(user);
		return "redirect:/";

	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model, HttpSession session) {
		User user = userService.findByUsername(username);
		if (user != null && userService.verifyPassword(password, user.getPassword())) {
			session.setAttribute("loggedInUser", user);
			return "redirect:/list";
		} else {
			
			model.addAttribute("error", "Invalid username or password");
			String error="Invalid Crediantial ";
			return "redirect:/?message=" + error;

		}
	}

	@GetMapping("/display")
	public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException {

		User image = userService.viewById(id);
		byte[] imageBytes = null;
		imageBytes = image.getPhoto().getBytes(1, (int) image.getPhoto().length());
		if (imageBytes != null) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/viewById")
	public String getMethodName(Model model, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			Optional<User> employee = userService.findById(loggedInUser.getId());
			List<Depart> departments = depDService.findAll();
			model.addAttribute("departments", departments);
			model.addAttribute("employee", employee);
			return "edit";
		}
		return "redirect:/";
	}

	@PostMapping("/update")
	public String updateEmployee(@ModelAttribute User user1, HttpSession session,
			@RequestParam("image") MultipartFile file, @RequestParam("plainPassword") String plainPassword,
			RedirectAttributes redirectAttributes) throws Exception {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {

			userService.updateEmployee(user1, file, plainPassword, loggedInUser.getId());

			return "redirect:/list";

		} else {
			return "redirect:/logout";
		}
	}

	@GetMapping("/depRegistration")
	public String depRegistration(HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			return "depRegistration";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/depRegister")
	public String depReg(@RequestParam("depname") String depName, @RequestParam("mobileno") String mobileno,
			@RequestParam("email") String email, HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			Depart dep = new Depart();
			dep.setEmail(email);
			dep.setDepartmentName(depName);
			dep.setMobileNo(mobileno);
			depDService.saveAll(dep);

			return "redirect:/list";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/updateDepartment/{departmentId}/{employeeId}")
	public ResponseEntity<String> updateDepartment(@PathVariable Long departmentId, HttpSession session,
			@PathVariable Long employeeId) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			depDService.updateDepMapping(departmentId, employeeId);

			return ResponseEntity.ok("Department updated successfully");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
		}
	}

	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			Optional<User> userOptional = userService.findById(loggedInUser.getId());
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				model.addAttribute("user1", user);
				return "profile";
			}
		}
		return "redirect:/";
	}
	@GetMapping("/employees")
    public ResponseEntity<List<Object>> getEmployeesByDepartment(@RequestParam("department") String departmentName) {
        Optional<Depart> department = depDService.getByDepartmentName(departmentName);

        List<Object> obj = new ArrayList<>();

        if (department.isPresent()) {
            List<DepMapping> depMappings = depDService.findByDepart(department.get());
            for (DepMapping depMapping : depMappings) {
                User user = depMapping.getUser();
                if (user != null) {
                    User newUser = new User();
                    newUser.setId(user.getId());
                    newUser.setName(user.getName());
                    newUser.setCity(user.getCity());
                    newUser.setCountry(user.getCountry());
                    newUser.setDateofbirth(user.getDateofbirth());
                    newUser.setDesignation(user.getDesignation());
                    newUser.setEmail(user.getEmail());
                    newUser.setUsername(user.getUsername());
                    newUser.setMobileno(user.getMobileno());
                    obj.add(newUser);
                }
            }
        }

        return ResponseEntity.ok(obj);
    }


}
