package dev.lqwd.dto;

import dev.lqwd.exceptions.custom_exceptions.BadRequestException;

import static dev.lqwd.exceptions.ErrorMessages.INTERNAL_ERROR;

public abstract class Dto {
    protected <T> T requireNonNull(T value) {
        if (value == null) {
            throw new BadRequestException(INTERNAL_ERROR.getMessage());
        }
        return value;
    }
}
