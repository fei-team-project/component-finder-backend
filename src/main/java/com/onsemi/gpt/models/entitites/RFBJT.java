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
@Table(name = "RF_BJT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RFBJT extends BaseComponent {

    @Column(name = "VCEO Min (V)")
    private Float vceoMin;

    @Column(name = "fT Min (MHz)")
    private Integer ftMin;

    @Column(name = "IC Continuous (A)")
    private Float icContinuous;

    @Column(name = "hFE Min")
    private Integer hfeMin;

    @Column(name = "Polarity")
    private String polarity;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "PTM Max (W)")
    private Float ptmMax;

    @Column(name = "|S21e|2 Typ. (dB)")
    private Float s21e2Typ;

    @Column(name = "NF Typ. (dB)")
    private Float nfTyp;

    @Column(name = "hFE Max")
    private Integer hfeMax;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, MINIMUM - VCEO Min (V), MAXIMUM - VCEO Min (V), " +
                "MINIMUM - fT Min (MHz), MAXIMUM - fT Min (MHz), MINIMUM - IC Continuous (A), " +
                "MAXIMUM - IC Continuous (A), MINIMUM - hFE Min, MAXIMUM - hFE Min, Polarity, " +
                "Package Type, MINIMUM - PTM Max (W), MAXIMUM - PTM Max (W), " +
                "MINIMUM - |S21e|2 Typ. (dB), MAXIMUM - |S21e|2 Typ. (dB), " +
                "MINIMUM - NF Typ. (dB), MAXIMUM - NF Typ. (dB), MINIMUM - hFE Max, MAXIMUM - hFE Max, " +
                "Qualification, AEC Qualified, PPAP Capable, Halide Free, Lead Free, Status";
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
                "VCEO Min (V)",
                "fT Min (MHz)",
                "IC Continuous (A)",
                "hFE Min",
                "Polarity",
                "Package Type",
                "PTM Max (W)",
                "|S21e|2 Typ. (dB)",
                "NF Typ. (dB)",
                "hFE Max",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.RF_BJT;
    }
}
