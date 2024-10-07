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
@Table(name = "SMALL_SIGNAL_DIODE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmallSignalDiode extends BaseComponent {

    @Column(name = "VR Min (V)")
    private Integer vrMin;

    @Column(name = "IF Max (A)")
    private Float ifMax;

    @Column(name = "IR Max (µA)")
    private Float irMax;

    @Column(name = "Configuration")
    private String configuration;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "VF Max (V)")
    private Float vfMax;

    @Column(name = "trr Max (ns)")
    private Integer trrMax;

    @Column(name = "Cj Max (pF)")
    private Float cjMax;

    @Column(name = "RS Max (Ω)")
    private Float rsMax;

    @Column(name = "Type")
    private String type;

    @Column(name = "Diode Type")
    private String diodeType;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, MINIMUM - VR Min (V), MAXIMUM - VR Min (V), " +
                "MINIMUM - IF Max (A), MAXIMUM - IF Max (A), MINIMUM - IR Max (µA), MAXIMUM - IR Max (µA), " +
                "Configuration, Package Type, MINIMUM - VF Max (V), MAXIMUM - VF Max (V), " +
                "MINIMUM - trr Max (ns), MAXIMUM - trr Max (ns), MINIMUM - Cj Max (pF), MAXIMUM - Cj Max (pF), " +
                "MINIMUM - RS Max (Ω), MAXIMUM - RS Max (Ω), Type, Diode Type, Qualification, " +
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
                "VR Min (V)",
                "IF Max (A)",
                "IR Max (µA)",
                "Configuration",
                "Package Type",
                "VF Max (V)",
                "trr Max (ns)",
                "Cj Max (pF)",
                "RS Max (Ω)",
                "Type",
                "Diode Type",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.SMALL_SIGNAL_DIODE;
    }
}
