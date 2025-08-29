//package com.techm.mobicom.service;
//
//import com.techm.mobicom.dto.UserDTO;
//import com.techm.mobicom.exception.ValidationException;
//import com.techm.mobicom.models.User;
//import com.techm.mobicom.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegisterUser_Success() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setMobileNumber("1234567890");
//        userDTO.setPassword("password");
//        userDTO.setName("Test User");
//        userDTO.setEmail("test@example.com");
//
//        User user = new User();
//        user.setMobileNumber("1234567890");
//        user.setPassword("encoded");
//        user.setName("Test User");
//        user.setEmail("test@example.com");
//        user.setRole(User.Role.USER);
//
//        when(userRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("password")).thenReturn("encoded");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        User result = userService.registerUser(userDTO);
//
//        assertNotNull(result);
//        assertEquals("1234567890", result.getMobileNumber());
//        verify(userRepository).save(any(User.class));
//    }
//
//    @Test
//    void testRegisterUser_DuplicateMobileNumber() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setMobileNumber("1234567890");
//
//        when(userRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(new User()));
//
//        assertThrows(ValidationException.class, () -> userService.registerUser(userDTO));
//    }
//
//    @Test
//    void testValidateMobileNumber_Success() {
//        User user = new User();
//        user.setMobileNumber("1234567890");
//
//        when(userRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(user));
//
//        User result = userService.validateMobileNumber("1234567890");
//
//        assertNotNull(result);
//        assertEquals("1234567890", result.getMobileNumber());
//    }
//}