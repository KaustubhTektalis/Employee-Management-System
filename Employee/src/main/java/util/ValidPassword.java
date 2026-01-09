package util;

public class ValidPassword {
	public static boolean isValidPassword(String password) {
		if (password == null)
			return false;

		boolean hasUppercase = false;
		boolean hasNumber = false;
		boolean hasSpecial = false;

		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c))
				hasUppercase = true;
			else if (Character.isDigit(c))
				hasNumber = true;
			else if (!Character.isLetterOrDigit(c))
				hasSpecial = true;

			if (hasUppercase && hasNumber && hasSpecial)
				return true;
		}

		return false;
	}
}