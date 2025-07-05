package ecommerce.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Expirable {
    boolean isExpired();
    LocalDate  getExpireAt();
}
