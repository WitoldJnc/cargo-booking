package com.cargo.booking.calculate.dto.participant;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class SettingsDto {
    private List<UUID> cargoAgentAirlines = new ArrayList<>();

}
