package testings;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import service.LoginAndAccess;
import service.PasswordMethods;

class PasswordMethodsTest {

	@BeforeEach
	void clearContext() {
		LoginAndAccess.clearLoginContext();
	}

	@Test
	void testSetAndGetLoginContext() {
		LoginAndAccess.setLoginContext("TT25001", List.of("Admin", "Manager"));
		assertEquals("TT25001",LoginAndAccess.getLoggedInId());
		assertTrue(LoginAndAccess.hasRole("Admin"));
		assertTrue(LoginAndAccess.hasRole("Manager"));
		assertFalse(LoginAndAccess.hasRole("Employee"));
	}

	@Test
	void testHashConsistency() {
		String pass = "Default123";
		String hashed = PasswordMethods.hash(pass);

		assertTrue(BCrypt.checkpw(pass, hashed));
	}

	@Test
	void testRandomPasswordGenerator() {
		String pw1 = PasswordMethods.randomPasswordGenerator();
		String pw2 = PasswordMethods.randomPasswordGenerator();

		assertNotNull(pw1);
		assertNotNull(pw2);
		assertNotEquals(pw1, pw2);
		assertTrue(pw1.length() <= 12);
		assertTrue(pw2.length() <= 12);
	}

	@Test
	void testClearLoginContext() {
		LoginAndAccess.setLoginContext("TT25001", List.of("Admin"));
		LoginAndAccess.clearLoginContext();

		assertNull(LoginAndAccess.getLoggedInId());
		assertTrue(LoginAndAccess.getLoggedInRoles().isEmpty());
	}
}