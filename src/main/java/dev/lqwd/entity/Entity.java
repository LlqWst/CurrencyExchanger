package dev.lqwd.entity;

import dev.lqwd.exceptions.custom_exceptions.BadRequestException;

import static dev.lqwd.exceptions.ErrorMessages.INTERNAL_ERROR;

abstract class Entity {
    protected <T> T requireNonNull(T value) {
        if (value == null) {
            throw new BadRequestException(INTERNAL_ERROR.getMessage());
        }
        return value;
    }
}
