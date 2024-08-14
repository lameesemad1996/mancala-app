// utils.js
import DOMPurify from 'dompurify';

export const sanitizeAndValidatePlayerName = (name, enqueueSnackbar, maxLength = 15) => {
    // Sanitize the input using DOMPurify
    const sanitizedInput = DOMPurify.sanitize(name);

    // Trim any leading or trailing whitespace
    const trimmedName = sanitizedInput.trim();

    // Check if the name is empty
    if (!trimmedName) {
        enqueueSnackbar('Name cannot be empty.', { variant: "warning" });
        return {
            isValid: false,
            sanitizedName: trimmedName,
            error: 'Name cannot be empty.',
        };
    }

    // Check if the name is too short or too long
    if (trimmedName.length < 2 || trimmedName.length > maxLength) {
        enqueueSnackbar(`Name must be between 2 and ${maxLength} characters long.`, { variant: "warning" });
        return {
            isValid: false,
            sanitizedName: trimmedName,
            error: `Name must be between 2 and ${maxLength} characters long.`,
        };
    }

    // Check if the name contains only alphabetic characters (a-z, A-Z)
    const namePattern = /^[a-zA-Z]+$/;
    if (!namePattern.test(trimmedName)) {
        enqueueSnackbar('Name can only contain alphabetic characters (a-z, A-Z).', { variant: "warning" });
        return {
            isValid: false,
            sanitizedName: trimmedName,
            error: 'Name can only contain alphabetic characters (a-z, A-Z).',
        };
    }

    return {
        isValid: true,
        sanitizedName: trimmedName
    };
};
