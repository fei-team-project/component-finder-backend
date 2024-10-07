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
@Table(name = "PROTECTED_POWER_SWITCHES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProtectedPowerSwitch extends BaseComponent {

    @Column(name = "Product Family")
    private String productFamily;

    @Column(name = "IOUT Max (A)")
    private Float ioutMax;

    @Column(name = "Vin Min (V)")
    private Float vinMin;

    @Column(name = "Vin Max (V)")
    private Float vinMax;

    @Column(name = "RDS(ON) Typ (mΩ)")
    private Float rdsOnTyp;

    @Column(name = "Features", length = 512)
    private String features;

    @Column(name = "Current Limit (FIXED/ADJ/NO)")
    private String currentLimit;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Iq (µA)")
    private Float iq;

    @Column(name = "Input / Output")
    private String inputOutput;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, Product Family, " +
                "MINIMUM - IOUT Max (A), MAXIMUM - IOUT Max (A), " +
                "MINIMUM - Vin Min (V), MAXIMUM - Vin Min (V), " +
                "MINIMUM - Vin Max (V), MAXIMUM - Vin Max (V), " +
                "MINIMUM - RDS(ON) Typ (mΩ), MAXIMUM - RDS(ON) Typ (mΩ), " +
                "Features, Current Limit (FIXED/ADJ/NO), Package Type, " +
                "MINIMUM - Iq (µA), MAXIMUM - Iq (µA), Input / Output, " +
                "Qualification, AEC Qualified, PPAP Capable, Halide Free, " +
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
                "Product Family",
                "IOUT Max (A)",
                "Vin Min (V)",
                "Vin Max (V)",
                "RDS(ON) Typ (mΩ)",
                "Features",
                "Current Limit (FIXED/ADJ/NO)",
                "Package Type",
                "Iq (µA)",
                "Input / Output",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.PROTECTED_POWER_SWITCHES;
    }
}
