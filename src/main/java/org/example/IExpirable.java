package org.example;

import java.time.LocalDate;

interface IExpirable extends IProduct {
    LocalDate getExpiryDate();
    boolean isExpired();
}
