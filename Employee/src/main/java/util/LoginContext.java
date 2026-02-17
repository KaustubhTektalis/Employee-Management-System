package util;

import java.util.List;

public class LoginContext {
	private final String empID;
	private final List<String> roles;

	public LoginContext(String empID, List<String> roles) {
		this.empID = empID;
		this.roles = roles == null ? List.of() : List.copyOf(roles);
	}

	public String getEmpID() {
		return empID;
	}

	public List<String> getRoles() {
		return roles;
	}

	public boolean hasRole(String role) {
		return roles.stream().anyMatch(r -> r.equalsIgnoreCase(role));
	}
}
