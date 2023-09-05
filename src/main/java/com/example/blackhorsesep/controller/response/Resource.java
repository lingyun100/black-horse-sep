package com.example.blackhorsesep.controller.response;

import java.time.LocalDateTime;

public record Resource(String resourceName, LocalDateTime createDate) {
}
