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
@Table(name = "POWER_DIODE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerDiode extends BaseComponent {

    @Column(name = "VRRM (V)")
    private Integer vrrm;

    @Column(name = "IF(ave) (A)")
    private Float ifAve;

    @Column(name = "IFSM Max (A)")
    private Float ifsmMax;

    @Column(name = "Cj Max (pF)")
    private Float cjMax;

    @Column(name = "Diode Type")
    private String diodeType;

    @Column(name = "Configuration")
    private String configuration;

    @Column(name = "Technology")
    private String technology;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "VF Max (V)")
    private Float vfMax;

    @Column(name = "IR (Max) (µA)")
    private Float irMax;

    @Column(name = "trr Max (ns)")
    private Integer trrMax;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, VRRM (V), " +
                "MINIMUM - IF(ave) (A), MAXIMUM - IF(ave) (A), " +
                "MINIMUM - IFSM Max (A), MAXIMUM - IFSM Max (A), " +
                "Cj Max (pF), Diode Type, Configuration, Technology, " +
                "Package Type, MINIMUM - VF Max (V), MAXIMUM - VF Max (V), " +
                "MINIMUM - IR (Max) (µA), MAXIMUM - IR (Max) (µA), " +
                "MINIMUM - trr Max (ns), MAXIMUM - trr Max (ns), " +
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
                "VRRM (V)",
                "IF(ave) (A)",
                "IFSM Max (A)",
                "Cj Max (pF)",
                "Diode Type",
                "Configuration",
                "Technology",
                "Package Type",
                "VF Max (V)",
                "IR (Max) (µA)",
                "trr Max (ns)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.POWER_DIODE;
    }
}
