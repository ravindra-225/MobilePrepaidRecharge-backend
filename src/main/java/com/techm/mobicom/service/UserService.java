package com.techm.mobicom.service;

import com.techm.mobicom.dto.AdminDTO;
import com.techm.mobicom.dto.RechargeHistoryDTO;
import com.techm.mobicom.dto.UserDTO;
import com.techm.mobicom.models.Admin;
import com.techm.mobicom.models.Recharge;
import com.techm.mobicom.models.User;
import com.techm.mobicom.exception.CustomException;
import com.techm.mobicom.repository.AdminRepository;
import com.techm.mobicom.repository.RechargeRepository;
import com.techm.mobicom.repository.UserRepository;
import com.techm.mobicom.util.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String registerAdmin(AdminDTO adminDTO) {
        validateAdminInput(adminDTO);
        if (adminRepository.findByUsername(adminDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists", HttpStatus.CONFLICT);
        }
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new CustomException("Email already exists", HttpStatus.CONFLICT);
        }

        Admin admin = new Admin();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());

        adminRepository.save(admin);
        return "Admin registered successfully";
    }

    public String registerUser(UserDTO userDTO) {
        validateUserInput(userDTO);
        if (userRepository.findByMobileNumber(userDTO.getMobileNumber()).isPresent()) {
            throw new CustomException("Mobile number already exists", HttpStatus.CONFLICT);
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new CustomException("Email already exists", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
        return "User registered successfully";
    }

    public String validateMobileNumber(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new CustomException("Invalid mobile number; must be 10 digits", HttpStatus.BAD_REQUEST);
        }
        return userRepository.findByMobileNumber(mobileNumber)
                .map(user -> "Mobile number validated successfully")
                .orElseThrow(() -> new CustomException("Mobile number not found", HttpStatus.NOT_FOUND));
    }

    public String validateUser(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new CustomException("Invalid mobile number; must be 10 digits", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException("Mobile number not found", HttpStatus.NOT_FOUND));    
        return jwtUtil.generateToken(mobileNumber, user.getEmail(), "ROLE_USER");
    }

    private void validateAdminInput(AdminDTO adminDTO) {
        if (adminDTO.getUsername() == null || adminDTO.getUsername().isEmpty()) {
            throw new CustomException("Username is required", HttpStatus.BAD_REQUEST);
        }
        if (adminDTO.getPassword() == null || adminDTO.getPassword().length() < 8) {
            throw new CustomException("Password must be at least 8 characters", HttpStatus.BAD_REQUEST);
        }
        if (adminDTO.getEmail() == null || !adminDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new CustomException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
        if (adminDTO.getName() == null || adminDTO.getName().isEmpty()) {
            throw new CustomException("Name is required", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateUserInput(UserDTO userDTO) {
        if (userDTO.getMobileNumber() == null || !userDTO.getMobileNumber().matches("\\d{10}")) {
            throw new CustomException("Mobile number must be 10 digits", HttpStatus.BAD_REQUEST);
        }
        if (userDTO.getEmail() == null || !userDTO.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new CustomException("Invalid email format", HttpStatus.BAD_REQUEST);
        }
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new CustomException("Name is required", HttpStatus.BAD_REQUEST);
        }
    }
    
    public List<RechargeHistoryDTO> getUserRechargeHistory(String mobileNumber) {
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new CustomException("Invalid mobile number; must be 10 digits", HttpStatus.BAD_REQUEST);
        }
        userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException("User not found for mobile: " + mobileNumber, HttpStatus.NOT_FOUND));

        List<Recharge> recharges = rechargeRepository.findByMobileNumber(mobileNumber);

        if (recharges.isEmpty()) {
            throw new CustomException("No recharge history found for mobile: " + mobileNumber, HttpStatus.NOT_FOUND);
        }

        return recharges.stream().map(recharge -> {
            RechargeHistoryDTO dto = new RechargeHistoryDTO();
            dto.setPlanName(recharge.getPlan().getName());
            dto.setPlanDescription(recharge.getPlan().getDescription());
            dto.setAmount(recharge.getAmount());
            dto.setRechargeDate(recharge.getRechargeDate());
            dto.setExpiryDate(recharge.getExpiryDate());
            dto.setPaymentMode(recharge.getPaymentMode().name());
            dto.setStatus(recharge.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
    
}