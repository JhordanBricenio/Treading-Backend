package com.codej.model;

import com.codej.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled= false;
    private VerificationType setSendTo;
}
