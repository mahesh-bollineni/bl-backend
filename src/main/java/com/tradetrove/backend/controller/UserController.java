package com.tradetrove.backend.controller;

import com.tradetrove.backend.model.User;
import com.tradetrove.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginData) {
        if (loginData == null || loginData.getEmail() == null || loginData.getPassword() == null) {
            return ResponseEntity.status(400).body("Missing credentials");
        }

        String adminEmail = "bollineni.mahesh2006@gmail.com";
        String adminPass = "Mahesh@123";

        // 1. Check Admin Credentials
        if (adminEmail.equalsIgnoreCase(loginData.getEmail().trim()) && 
            adminPass.equals(loginData.getPassword())) {
            
            User admin = new User();
            admin.setId(0L);
            admin.setUsername("Super Admin");
            admin.setEmail(adminEmail);
            admin.setRole("ADMIN");
            return ResponseEntity.ok(admin);
        }

        // 2. Check Database for regular users
        Optional<User> userOpt = userRepository.findByEmail(loginData.getEmail().trim());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginData.getPassword())) {
            return ResponseEntity.ok(userOpt.get());
        }
        
        return ResponseEntity.status(401).body("Invalid Credentials");
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User Deleted");
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id, 
            @RequestParam(value="whatsapp", required=false) String whatsapp,
            @RequestParam(value="address", required=false) String address,
            @RequestParam(value="about", required=false) String about,
            @RequestParam(value="image", required=false) MultipartFile file) throws IOException {
        
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();
        if(whatsapp != null) user.setWhatsappNumber(whatsapp);
        if(address != null) user.setAddress(address);
        if(about != null) user.setAbout(about);
        
        if(file != null && !file.isEmpty()) {
            user.setProfileImageData(file.getBytes());
            user.setProfileImageType(file.getContentType());
        }
        return ResponseEntity.ok(userRepository.save(user));
    }
}