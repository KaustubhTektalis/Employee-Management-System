package service;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;

public final class PasswordHasher {

	private PasswordHasher() {
	}

	public static String hash(String plainPassword) {
		if (plainPassword == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}

		return Hashing.sha256().hashString(plainPassword.trim(), StandardCharsets.UTF_8).toString();
	}
}
