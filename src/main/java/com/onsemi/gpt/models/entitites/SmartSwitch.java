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
@Table(name = "SMART_SWITCH")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmartSwitch extends BaseComponent {

    @Column(name = "RDS(ON) Typ at 25°C (mΩ)")
    private Integer rdsOnTypAt25C;

    @Column(name = "ILIM Typ at 25°C (A)")
    private Float ilimTypAt25C;

    @Column(name = "V(BR)DSS Min (V)")
    private Integer vbrDSSMin;

    @Column(name = "PD Max (W)")
    private Float pdMax;

    @Column(name = "Switch configuration")
    private String switchConfiguration;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "RDS(ON) Max at 150°C (mΩ)")
    private Integer rdsOnMaxAt150C;

    @Column(name = "ILIM Typ at 150°C (A)")
    private Float ilimTypAt150C;

    @Column(name = "Single Pulse Energy at 150°C (mJ)")
    private String singlePulseEnergyAt150C;

    @Column(name = "Diagnostic capability")
    private String diagnosticCapability;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, MINIMUM - RDS(ON) Typ at 25°C (mΩ), MAXIMUM - RDS(ON) Typ at 25°C (mΩ), " +
                "MINIMUM - ILIM Typ at 25°C (A), MAXIMUM - ILIM Typ at 25°C (A), V(BR)DSS Min (V), PD Max (W), " +
                "Switch configuration, Package Type, MINIMUM - RDS(ON) Max at 150°C (mΩ), MAXIMUM - RDS(ON) Max at 150°C (mΩ), " +
                "MINIMUM - ILIM Typ at 150°C (A), MAXIMUM - ILIM Typ at 150°C (A), " +
                "MINIMUM - Single Pulse Energy at 150°C (mJ), MAXIMAL - Single Pulse Energy at 150°C (mJ) ," +
                "Diagnostic capability, Qualification, AEC Qualified, PPAP Capable, Halide Free, Lead Free, Status";
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
                "RDS(ON) Typ at 25°C (mΩ)",
                "ILIM Typ at 25°C (A)",
                "V(BR)DSS Min (V)",
                "PD Max (W)",
                "Switch configuration",
                "Package Type",
                "RDS(ON) Max at 150°C (mΩ)",
                "ILIM Typ at 150°C (A)",
                "Single Pulse Energy at 150°C (mJ)",
                "Diagnostic capability",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.SMART_SWITCH;
    }
}
