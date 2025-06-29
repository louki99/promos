package ma.foodplus.ordering.system.pos.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private Long terminalId; // Made optional for admin users

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getTerminalId() { return terminalId; }
    public void setTerminalId(Long terminalId) { this.terminalId = terminalId; }
} 