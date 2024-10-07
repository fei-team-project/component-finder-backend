package com.onsemi.gpt.models.entitites;

import com.onsemi.gpt.models.BaseComponent;
import com.onsemi.gpt.models.Component;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLOCK_GENERATION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClockGeneration extends BaseComponent {

    @Column(name = "Input Interface Logic")
    private String inputInterfaceLogic;

    @Column(name = "Output Interface Logic")
    private String outputInterfaceLogic;

    @Column(name = "fout Typ (MHz)")
    private Float foutTyp;

    @Column(name = "fin Typ (MHz)")
    private Float finTyp;

    @Column(name = "VCC Typ (V)")
    private Float vccTyp;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "tJitter(Cy-Cy) Typ (ps)")
    private Integer tJitterCyCyTyp;

    @Column(name = "tJitter(Period) Typ (ps)")
    private Float tJitterPeriodTyp;

    @Column(name = "tJitter(&#934;) Typ (ps)")
    private Float tJitterDeltaTyp;

    @Column(name = "tR & tF Typ (ps)")
    private Integer tRTFTyp;

    @Column(name = "tR & tF Max (ps)")
    private Integer tRTFMax;

    @Column(name = "TA Min (°C)")
    private Integer taMin;

    @Column(name = "TA Max (°C)")
    private Integer taMax;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MAXIMUM - Price, MINIMUM - Price" +
                "Input Interface Logic, Output Interface Logic, " +
                "MAXIMUM - fout Typ (MHz), MINIMUM - fout Typ (MHz), " +
                "MAXIMUM - fin Typ (MHz), MINIMUM - fin Typ (MHz), " +
                "MAXIMUM - VCC Typ (V), MINIMUM - VCC Typ (V), " +
                "Package Type, " +
                "MAXIMUM - tJitter(Cy-Cy) Typ (ps), MINIMUM - tJitter(Cy-Cy) Typ (ps), " +
                "MAXIMUM - tJitter(Period) Typ (ps), MINIMUM - tJitter(Period) Typ (ps), " +
                "MAXIMUM - tJitter(Δ) Typ (ps), MINIMUM - tJitter(Δ) Typ (ps), " +
                "MAXIMUM - tR & tF Typ (ps), MINIMUM - tR & tF Typ (ps), " +
                "MAXIMUM - tR & tF Max (ps), MINIMUM - tR & tF Max (ps), " +
                "MAXIMUM - TA Min (°C), MINIMUM - TA Min (°C), " +
                "MAXIMUM - TA Max (°C), MINIMUM - TA Max (°C), " +
                "Qualification, AEC Qualified, PPAP Capable, " +
                "Halide Free, Lead Free, Status";
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
                "Input Interface Logic",
                "Output Interface Logic",
                "fout Typ (MHz)",
                "fin Typ (MHz)",
                "VCC Typ (V)",
                "Package Type",
                "tJitter(Cy-Cy) Typ (ps)",
                "tJitter(Period) Typ (ps)",
                "tJitter(&#934;) Typ (ps)",
                "tR & tF Typ (ps)",
                "tR & tF Max (ps)",
                "TA Min (°C)",
                "TA Max (°C)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.CLOCK_GENERATION;
    }
}
