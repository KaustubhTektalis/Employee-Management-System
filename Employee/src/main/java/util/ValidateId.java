package util;

import customExceptions.IdFormatWrongException;

public class ValidateId {
	public static void validateId(String id) throws IdFormatWrongException {
		if (id == null || !id.matches("^TT\\d{5}$")) {
			throw new IdFormatWrongException("Invalid employee ID format");
		}
	}
}
