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
@Table(name = "DIGITAL_BJT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DigitalBjt extends BaseComponent {

    @Column(name = "Vi(on) Min (V)")
    private Float viOnMin;

    @Column(name = "Vi(off) Max (V)")
    private Float viOffMax;

    @Column(name = "R1 (kΩ)")
    private Float r1Kohms;

    @Column(name = "R2 (kΩ)")
    private Float r2Kohms;

    @Column(name = "IC Continuous (A)")
    private Float icContinuous;

    @Column(name = "Configuration")
    private String configuration;

    @Column(name = "Polarity")
    private String polarity;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "V(BR)CEO Min (V)")
    private Integer vbrceoMin;

    @Column(name = "hFE Min")
    private Integer hfeMin;

    @Column(name = "R1/R2 Typ")
    private Float r1R2Typ;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MAXIMUM - Price, MINIMUM - Price" +
                "MAXIMUM - Vi(on) Min (V), MINIMUM - Vi(on) Min (V), " +
                "MAXIMUM - Vi(off) Max (V), MINIMUM - Vi(off) Max (V), " +
                "MAXIMUM - R1 (kΩ), MINIMUM - R1 (kΩ), " +
                "MAXIMUM - R2 (kΩ), MINIMUM - R2 (kΩ), " +
                "IC Continuous (A), Configuration, Polarity, Package Type, " +
                "MAXIMUM - V(BR)CEO Min (V), MINIMUM - V(BR)CEO Min (V), " +
                "MAXIMUM - hFE Min, MINIMUM - hFE Min, " +
                "R1/R2 Typ, Qualification, AEC Qualified, PPAP Capable, " +
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
                "Vi(on) Min (V)",
                "Vi(off) Max (V)",
                "R1 (kΩ)",
                "R2 (kΩ)",
                "IC Continuous (A)",
                "Configuration",
                "Polarity",
                "Package Type",
                "V(BR)CEO Min (V)",
                "hFE Min",
                "R1/R2 Typ",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.DIGITAL_BJT;
    }
}
