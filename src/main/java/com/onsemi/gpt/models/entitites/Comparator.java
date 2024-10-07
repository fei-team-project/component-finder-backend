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
@Table(name = "COMPARATOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comparator extends BaseComponent {

    @Column(name = "tres Typ (ns)")
    private Float tresTyp;

    @Column(name = "VCC Min (V)")
    private Float vccMin;

    @Column(name = "VCC Max (V)")
    private Float vccMax;

    @Column(name = "VIO Max (mV)")
    private Float vioMax;

    @Column(name = "ICC Typ (mA)")
    private Float iccTyp;

    @Column(name = "Number of Channels")
    private Integer numberOfChannels;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "IO Typ (mA)")
    private Float ioTyp;

    @Column(name = "TA Min (°C)")
    private Integer taMin;

    @Column(name = "TA Max (°C)")
    private Integer taMax;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "OPN, MAXIMUM - Price, MINIMUM - Price, " +
                "MAXIMUM - tres Typ (ns), MINIMUM - tres Typ (ns), " +
                "MAXIMUM - VCC Min (V), MINIMUM - VCC Min (V), " +
                "MAXIMUM - VCC Max (V), MINIMUM - VCC Max (V), " +
                "MAXIMUM - VIO Max (mV), MINIMUM - VIO Max (mV), " +
                "MAXIMUM - ICC Typ (mA), MINIMUM - ICC Typ (mA), " +
                "Number of Channels, Package Type, " +
                "MAXIMUM - IO Typ (mA), MINIMUM - IO Typ (mA), " +
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
                "tres Typ (ns)",
                "VCC Min (V)",
                "VCC Max (V)",
                "VIO Max (mV)",
                "ICC Typ (mA)",
                "Number of Channels",
                "Package Type",
                "IO Typ (mA)",
                "TA Min (°C)",
                "TA Max (°C)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.COMPARATOR;
    }
}

