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
@Table(name = "ANALOG_BJT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalogBJT extends BaseComponent {

    @Column(name = "IC Continuous (A)")
    private Float icContinuous;

    @Column(name = "VCEO Min (V)")
    private Integer vceoMin;

    @Column(name = "hFE Min")
    private Float hfeMin;

    @Column(name = "fT Min (MHz)")
    private Float ftMin;

    @Column(name = "PTM Max (W)")
    private Float ptmMax;

    @Column(name = "VEBO (V)")
    private Float vebo;

    @Column(name = "Mounting Type")
    private String mountingType;

    @Column(name = "Configuration")
    private String configuration;

    @Column(name = "Polarity")
    private String polarity;

    @Column(name = "Type of General purpose BJT")
    private String typeOfGeneralPurposeBJT;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Analog Transistor Group")
    private String analogTransistorGroup;

    @Column(name = "VBE(sat) (V)")
    private Float vbeSat;

    @Column(name = "VBE(on) (V)")
    private Float vbeOn;

    @Column(name = "VCE(sat) Max (V)")
    private Float vceSatMax;

    @Column(name = "VCBO (V)")
    private Integer vcbo;

    @Column(name = "hFE Max")
    private Float hfeMax;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "Product (WPN), OPN, MAXIMUM - Price, MINIMUM - Price" +
                "MAXIMUM - IC Continuous (A), MINIMUM - IC Continuous (A), " +
                "MAXIMUM - VCEO Min (V), MINIMUM - VCEO Min (V), " +
                "MAXIMUM - hFE Min, MINIMUM - hFE Min, " +
                "MAXIMUM - fT Min (MHz), MINIMUM - fT Min (MHz), " +
                "MAXIMUM - PTM Max (W), MINIMUM - PTM Max (W), " +
                "MAXIMUM - VEBO (V), MINIMUM - VEBO (V), " +
                "Mounting Type, Configuration, Polarity, " +
                "Type of General purpose BJT, Package Type, " +
                "Analog Transistor Group, " +
                "MAXIMUM - VBE(sat) (V), MINIMUM - VBE(sat) (V), " +
                "MAXIMUM - VBE(on) (V), MINIMUM - VBE(on) (V), " +
                "MAXIMUM - VCE(sat) Max (V), MINIMUM - VCE(sat) Max (V), " +
                "MAXIMUM - VCBO (V), MINIMUM - VCBO (V), " +
                "MAXIMUM - hFE Max, MINIMUM - hFE Max, " +
                "Qualification, AEC Qualified, PPAP Capable, " +
                "Halide Free, Lead Free";
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
                "IC Continuous (A)",
                "VCEO Min (V)",
                "hFE Min",
                "fT Min (MHz)",
                "PTM Max (W)",
                "VEBO (V)",
                "Mounting Type",
                "Configuration",
                "Polarity",
                "Type of General purpose BJT",
                "Package Type",
                "Analog Transistor Group",
                "VBE(sat) (V)",
                "VBE(on) (V)",
                "VCE(sat) Max (V)",
                "VCBO (V)",
                "hFE Max",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.ANALOG_BJT;
    }
}
