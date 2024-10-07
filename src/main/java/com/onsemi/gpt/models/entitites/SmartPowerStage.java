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
@Table(name = "SMART_POWER_STAGE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmartPowerStage extends BaseComponent {

    @Column(name = "Vin Typ (V)")
    private Integer vinTyp;

    @Column(name = "IO Max (A)")
    private Integer ioMax;

    @Column(name = "fSW Max (MHz)")
    private String fswMax;

    @Column(name = "PWM Level (V)")
    private String pwmLevel;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Vcc Typ (V)")
    private Integer vccTyp;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, MINIMUM - Vin Typ (V), MAXIMUM - Vin Typ (V), MINIMUM - IO Max (A), " +
                "MAXIMUM - IO Max (A), fSW Max (MHz), PWM Level (V), Package Type, " +
                "MINIMUM - Vcc Typ (V), MAXIMUM - Vcc Typ (V), Qualification, " +
                "AEC Qualified, PPAP Capable, Halide Free, Lead Free, Status";
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
                "Vin Typ (V)",
                "IO Max (A)",
                "fSW Max (MHz)",
                "PWM Level (V)",
                "Package Type",
                "Vcc Typ (V)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.SMART_POWER_STAGE;
    }
}
