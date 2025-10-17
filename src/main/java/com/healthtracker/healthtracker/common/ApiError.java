package com.healthtracker.healthtracker.common;

public record ApiError(int status, String message) {
}
