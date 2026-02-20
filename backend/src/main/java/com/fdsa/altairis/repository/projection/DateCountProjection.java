package com.fdsa.altairis.repository.projection;

import java.time.LocalDate;

public interface DateCountProjection {
    LocalDate getCheckIn();
    Long getCount();
}
