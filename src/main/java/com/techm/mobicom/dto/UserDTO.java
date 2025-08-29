	package com.techm.mobicom.dto;
	import lombok.Data;

	import jakarta.validation.constraints.*;

	@Data
	public class UserDTO {
	    @NotNull(message = "Mobile number cannot be null")
	    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
	    private String mobileNumber;

	    @NotNull(message = "Name cannot be null")
	    @Size(min = 1, message = "Name must be provided")
	    private String name;

	    @NotNull(message = "Email cannot be null")
	    @Email(message = "Invalid email format")
	    private String email;
	}