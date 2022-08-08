package com.cargo.booking.nsi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonLocale implements Serializable {
    String en;
    String ru;
}
