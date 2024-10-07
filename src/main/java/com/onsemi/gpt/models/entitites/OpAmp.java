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
@Table(name = "OP_AMP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpAmp extends BaseComponent {

    @Column(name = "GBW Typ (MHz)")
    private Float gbwTyp;

    @Column(name = "SR Typ (V/µs)")
    private Float srTyp;

    @Column(name = "VOS Max (mV)")
    private Float vosMax;

    @Column(name = "eN (nV/√Hz)")
    private Float en;

    @Column(name = "Iq Typ (mA)")
    private Float iqTyp;

    @Column(name = "Channels")
    private Float channels;

    @Column(name = "Rail to Rail")
    private String railToRail;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "VS Min (V)")
    private Float vsMin;

    @Column(name = "VS Max (V)")
    private Float vsMax;

    @Column(name = "Ibias Typ (pA)")
    private Float ibiasTyp;

    @Column(name = "CMRR Typ (dB)")
    private Float cmrrTyp;

    @Column(name = "IO Typ (mA)")
    private Float ioTyp;

    @Column(name = "ΔVOS/ΔT (μV/°C)")
    private Float deltaVosPerDeltaT;

    @Column(name = "Minimum Operating Temperature (°C)")
    private Float minOperatingTemp;

    @Column(name = "Maximum Operating Temperature (°C)")
    private Float maxOperatingTemp;

    @Column(name = "Operational Amplifiers Group")
    private String opAmpGroup;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, MINIMUM - GBW Typ (MHz), MAXIMUM - GBW Typ (MHz)" +
                "MINIMUM - SR Typ (V/µs), MAXIMUM - SR Typ (V/µs)," +
                "MINIMUM - VOS Max (mV), MAXIMUM - VOS Max (mV), MINIMUM - eN (nV/√Hz), " +
                "MAXIMUM - eN (nV/√Hz),MINIMUM - Iq Typ (mA), MAXIMUM - Iq Typ (mA)" +
                "MINIMUM - Channels, MAXIMUM - Channels, Rail to Rail, Package Type, " +
                "MINIMUM - VS Min (V), MAXIMUM - VS Min (V), MINIMUM - VS Max (V), MAXIMUM - VS Max (V), " +
                "MINIMUM - Ibias Typ (pA), MAXIMUM - Ibias Typ (pA), CMRR Typ (dB), " +
                "MINIMUM - IO Typ (mA), MAXIMUM - IO Typ (mA), ΔVOS/ΔT (μV/°C), " +
                "MINIMUM - Minimum Operating Temperature (°C), MAXIMUM - Minimum Operating Temperature (°C), " +
                "MINIMUM - Maximum Operating Temperature (°C), MAXIMUM - Maximum Operating Temperature (°C), " +
                "Operational Amplifiers Group, Qualification, " +
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
                "GBW Typ (MHz)",
                "SR Typ (V/µs)",
                "VOS Max (mV)",
                "eN (nV/√Hz)",
                "Iq Typ (mA)",
                "Channels",
                "Rail to Rail",
                "Package Type",
                "VS Min (V)",
                "VS Max (V)",
                "Ibias Typ (pA)",
                "CMRR Typ (dB)",
                "IO Typ (mA)",
                "ΔVOS/ΔT (μV/°C)",
                "Minimum Operating Temperature (°C)",
                "Maximum Operating Temperature (°C)",
                "Operational Amplifiers Group",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.OP_AMP;
    }
}

