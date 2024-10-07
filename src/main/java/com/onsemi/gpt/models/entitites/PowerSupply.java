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
@Table(name = "POWER_SUPPLY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerSupply extends BaseComponent {

    @Column(name = "SPICE Live Model")
    private String spiceLiveModel;

    @Column(name = "Power Conversion")
    private String powerConversion;

    @Column(name = "VOUT (min) (V)")
    private Float voutMin;

    @Column(name = "VOUT (max) (V)")
    private Float voutMax;

    @Column(name = "VINDC/RMS (max) (V)")
    private Float vindcRmsMax;

    @Column(name = "VINDC/RMS (min) (V)")
    private Float vindcRmsMin;

    @Column(name = "IOUT (A)")
    private Float iout;

    @Column(name = "Iq Typ (mA)")
    private Float iqTyp;

    @Column(name = "fSW Typ (kHz)")
    private Integer fswTyp;

    @Column(name = "Topology")
    private String topology;

    @Column(name = "Control Mode")
    private String controlMode;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Qualification")
    private String qualification;

    @Column(name = "VCC Min (V)")
    private Float vccMin;

    @Column(name = "VCC Max (V)")
    private Float vccMax;

    @Column(name = "UVLO (V)")
    private Float uvlo;

    @Column(name = "Stand-by Mode")
    private Boolean standbyMode;

    @Column(name = "Drive Capability Source")
    private Integer driveCapabilitySource;

    @Column(name = "Drive Capability Sink")
    private Integer driveCapabilitySink;

    @Column(name = "Latch")
    private Boolean latch;

    @Column(name = "Shutdown Pin")
    private String shutdownPin;

    @Column(name = "Efficiency (%)")
    private Float efficiency;

    @Column(name = "Phases")
    private String phases;

    @Column(name = "VDO Typ (V)")
    private Float vdoTyp;

    @Column(name = "PSRR (dB)")
    private Float psrr;

    @Column(name = "Noise (µVrms)")
    private Float noise;

    @Column(name = "Application")
    private String application;

    @Column(name = "Polarity")
    private String polarity;

    @Column(name = "Output")
    private String output;

    @Column(name = "Enable")
    private Boolean enable;

    @Column(name = "PowerGood")
    private Boolean powerGood;

    @Column(name = "Short Circuit Protection")
    private Boolean shortCircuitProtection;

    @Column(name = "Soft Start")
    private Boolean softStart;

    @Column(name = "fJitter Typ (%)")
    private Float fjitterTyp;

    @Column(name = "RDS(ON) Typ (Ω)")
    private Float rdsOnTyp;

    @Column(name = "V(BR)DSS Max (V)")
    private Integer vbrDssMax;

    @Column(name = "IPeak (mA)")
    private Integer iPeak;

    @Column(name = "HV Start-up Min (V)")
    private Boolean hvStartupMin;

    @Column(name = "DSS (mA)")
    private Float dss;

    @Column(name = "Over Power Compensation")
    private Boolean overPowerCompensation;

    @Column(name = "Brown-out")
    private Boolean brownOut;

    @Column(name = "PFC Mode")
    private String pfcMode;

    @Column(name = "Frequency Operation")
    private String frequencyOperation;

    @Column(name = "UVP")
    private Boolean uvp;

    @Column(name = "Inhibition")
    private Boolean inhibition;

    @Column(name = "Vref Typ (V)")
    private Float vrefTyp;

    @Column(name = "TA Max (°C)")
    private Integer taMax;

    @Column(name = "TA Min (°C)")
    private Integer taMin;

    @Override
    public String getAttributesAsString() {
        return "SPICE Live Model, MINIMUM - Price, MAXIMUM - Price, Power Conversion, " +
                "MINIMUM - VOUT (min) (V), MAXIMUM - VOUT (min) (V), " +
                "MINIMUM - VOUT (max) (V), MAXIMUM - VOUT (max) (V), " +
                "MINIMUM - VINDC/RMS (max) (V), MAXIMUM - VINDC/RMS (max) (V), " +
                "MINIMUM - VINDC/RMS (min) (V), MAXIMUM - VINDC/RMS (min) (V), " +
                "MINIMUM - IOUT (A), MAXIMUM - IOUT (A), " +
                "MINIMUM - Iq Typ (mA), MAXIMUM - Iq Typ (mA), " +
                "MINIMUM - fSW Typ (kHz), MAXIMUM - fSW Typ (kHz), " +
                "Topology, Control Mode, Package Type, Qualification, " +
                "MINIMUM - VCC Min (V), MAXIMUM - VCC Min (V), " +
                "MINIMUM - VCC Max (V), MAXIMUM - VCC Max (V), " +
                "MINIMUM - UVLO (V), MAXIMUM - UVLO (V), " +
                "Stand-by Mode, " +
                "MINIMUM - Drive Capability Source, MAXIMUM - Drive Capability Source, " +
                "MINIMUM - Drive Capability Sink, MAXIMUM - Drive Capability Sink, " +
                "Latch, Shutdown Pin, MINIMUM - Efficiency, MAXIMUM - Efficiency, " +
                "Phases, MINIMUM - VDO Typ (V), MAXIMUM - VDO Typ (V), " +
                "MINIMUM - PSRR (dB), MAXIMUM - PSRR (dB), " +
                "MINIMUM - Noise (µVrms), MAXIMUM - Noise (µVrms), " +
                "Application, Polarity, Output, Enable, PowerGood, " +
                "Short Circuit Protection, Soft Start, " +
                "MINIMUM - fJitter Typ (%), MAXIMUM - fJitter Typ (%), " +
                "MINIMUM - RDS(ON) Typ (Ω), MAXIMUM - RDS(ON) Typ (Ω), " +
                "MINIMUM - V(BR)DSS Max (V), MAXIMUM - V(BR)DSS Max (V), " +
                "MINIMUM - IPeak (mA), MAXIMUM - IPeak (mA), " +
                "HV Start-up Min (V), " +
                "MINIMUM - DSS (mA), MAXIMUM - DSS (mA), " +
                "Over Power Compensation, Brown-out, PFC Mode, " +
                "Frequency Operation, UVP, Inhibition, " +
                "MINIMUM - Vref Typ (V), MAXIMUM - Vref Typ (V), " +
                "MINIMUM - TA Max (°C), MAXIMUM - TA Max (°C), " +
                "MINIMUM - TA Min (°C), MAXIMUM - TA Min (°C), " +
                "AEC Qualified, PPAP Capable, Halide Free, " +
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
                "SPICE Live Model",
                "Power Conversion",
                "VOUT (min) (V)",
                "VOUT (max) (V)",
                "VINDC/RMS (max) (V)",
                "VINDC/RMS (min) (V)",
                "IOUT (A)",
                "Iq Typ (mA)",
                "fSW Typ (kHz)",
                "Topology",
                "Control Mode",
                "Package Type",
                "Qualification",
                "VCC Min (V)",
                "VCC Max (V)",
                "UVLO (V)",
                "Stand-by Mode",
                "Drive Capability Source",
                "Drive Capability Sink",
                "Latch",
                "Shutdown Pin",
                "Efficiency (%)",
                "Phases",
                "VDO Typ (V)",
                "PSRR (dB)",
                "Noise (µVrms)",
                "Application",
                "Polarity",
                "Output",
                "Enable",
                "PowerGood",
                "Short Circuit Protection",
                "Soft Start",
                "fJitter Typ (%)",
                "RDS(ON) Typ (Ω)",
                "V(BR)DSS Max (V)",
                "IPeak (mA)",
                "HV Start-up Min (V)",
                "DSS (mA)",
                "Over Power Compensation",
                "Brown-out",
                "PFC Mode",
                "Frequency Operation",
                "UVP",
                "Inhibition",
                "Vref Typ (V)",
                "TA Max (°C)",
                "TA Min (°C)"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.POWER_SUPPLY;
    }
}
