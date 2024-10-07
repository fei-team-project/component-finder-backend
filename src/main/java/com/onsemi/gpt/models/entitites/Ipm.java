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
@Table(name = "IPM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ipm extends BaseComponent {

    @Column(name = "Rated Voltage Min (V)")
    private Integer ratedVoltageMinV;

    @Column(name = "VCE(sat) @ 25 °C Typ (V)")
    private Float vceSat25CTypV;

    @Column(name = "IO @ 25 °C Min (A)")
    private Float io25CMinA;

    @Column(name = "RDS(on) (Ω)")
    private Float rdsOn;

    @Column(name = "Family")
    private String family;

    @Column(name = "Topology")
    private String topology;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Substrate")
    private String substrate;

    @Column(name = "Input Logic")
    private String inputLogic;

    @Column(name = "Shutdown Pin")
    private Boolean shutdownPin;

    @Column(name = "Emitter Configuration")
    private String emitterConfiguration;

    @Column(name = "Mounting Type")
    private String mountingType;

    @Column(name = "PMAX (W)")
    private Integer pMaxW;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "OPN, MAXIMUM - Price, MINIMUM - Price, Description, " +
                "MAXIMUM - Rated Voltage Min (V), MINIMUM - Rated Voltage Min (V), " +
                "MAXIMUM - VCE(sat) @ 25 °C Typ (V), " +
                "MINIMUM - VCE(sat) @ 25 °C Typ (V), " +
                "MINIMUM - IO @ 25 °C Min (A), " +
                "MAXIMUM - IO @ 25 °C Min (A), " +
                "MAXIMUM - RDS(on) (Ω), MINIMUM - RDS(on) (Ω), " +
                "Family, Topology, Package Type, Substrate, " +
                "Input Logic, Shutdown Pin, " +
                "Emitter Configuration, Mounting Type, " +
                "MAXIMUM - PMAX (W), MINIMUM - PMAX (W), " +
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
                "Rated Voltage Min (V)",
                "VCE(sat) @ 25 °C Typ (V)",
                "IO @ 25 °C Min (A)",
                "RDS(on) (Ω)",
                "Family",
                "Topology",
                "Package Type",
                "Substrate",
                "Input Logic",
                "Shutdown Pin",
                "Emitter Configuration",
                "Mounting Type",
                "PMAX (W)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.IPM;
    }
}

