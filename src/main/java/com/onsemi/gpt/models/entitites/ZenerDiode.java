package com.onsemi.gpt.models.entitites;

import com.onsemi.gpt.models.BaseComponent;
import com.onsemi.gpt.models.Component;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ZENER_DIODE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZenerDiode extends BaseComponent {

    @Column(name = "VZ Typ (V)")
    private Float vzTyp;

    @Column(name = "PD Max (W)")
    private Float pdMax;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, MINIMUM - VZ Typ (V), MAXIMUM - VZ Typ (V), " +
                "MINIMUM - PD Max (W), MAXIMUM - PD Max (W), " +
                "Package Type, Qualification, AEC Qualified, PPAP Capable, Halide Free, " +
                "Lead Free, Status";
    }


    @Override
    public List<String> getColumns(){
        return new ArrayList<>(List.of(
                "Product (WPN)",
                "Datasheet",
                "OPN",
                "Price",
                "Description",
                "AEC Qualified",
                "PPAP Capable",
                "Halide Free",
                "Lead Free",
                "Status",
                "VZ Typ (V)",
                "PD Max (W)",
                "Package Type",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.ZENER_DIODE;
    }
}
