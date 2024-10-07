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
@Table(name = "GATE_DRIVER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GateDriver extends BaseComponent {

    @Column(name = "Power Switch")
    private String powerSwitch;

    @Column(name = "IPK SRC Typ (A)")
    private Float ipkSrcTyp;

    @Column(name = "IPK SNK Typ (A)")
    private Float ipkSnkTyp;

    @Column(name = "Vclass Max (V)")
    private Float vclassMax;

    @Column(name = "VCC Max (V)")
    private Float vccMax;

    @Column(name = "Isolation Type")
    private String isolationType;

    @Column(name = "Topology")
    private String topology;

    @Column(name = "Number of Outputs")
    private Integer numberOfOutputs;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Visolation Max (V)")
    private Float visolationMax;

    @Column(name = "Rise Time (ns)")
    private Float riseTime;

    @Column(name = "Fall Time (ns)")
    private Float fallTime;

    @Column(name = "Turn On Prop. Delay Typ (ns)")
    private Integer turnOnPropDelayTyp;

    @Column(name = "Turn Off Prop. Delay Typ (ns)")
    private Integer turnOffPropDelayTyp;

    @Column(name = "Delay Matching")
    private Float delayMatching;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MAXIMUM - Price, MINIMUM - Price, " +
                "Power Switch, " +
                "MAXIMUM - IPK SRC Typ (A), MINIMUM - IPK SRC Typ (A), " +
                "MAXIMUM - IPK SNK Typ (A), MINIMUM - IPK SNK Typ (A), " +
                "MAXIMUM - Vclass Max (V), MINIMUM - Vclass Max (V), " +
                "MAXIMUM - VCC Max (V), MINIMUM - VCC Max (V), " +
                "Isolation Type, Topology, " +
                "MAXIMUM - Number of Outputs, MINIMUM - Number of Outputs, " +
                "Package Type, " +
                "MAXIMUM - Visolation Max (V), MINIMUM - Visolation Max (V), " +
                "MAXIMUM - Rise Time (ns), MINIMUM - Rise Time (ns), " +
                "MAXIMUM - Fall Time (ns), MINIMUM - Fall Time (ns), " +
                "MAXIMUM - Turn On Prop. Delay Typ (ns), MINIMUM - Turn On Prop. Delay Typ (ns), " +
                "MAXIMUM - Turn Off Prop. Delay Typ (ns), MINIMUM - Turn Off Prop. Delay Typ (ns), " +
                "MAXIMUM - Delay Matching, MINIMUM - Delay Matching, " +
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
                "Power Switch",
                "IPK SRC Typ (A)",
                "IPK SNK Typ (A)",
                "Vclass Max (V)",
                "VCC Max (V)",
                "Isolation Type",
                "Topology",
                "Number of Outputs",
                "Package Type",
                "Visolation Max (V)",
                "Rise Time (ns)",
                "Fall Time (ns)",
                "Turn On Prop. Delay Typ (ns)",
                "Turn Off Prop. Delay Typ (ns)",
                "Delay Matching",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.GATE_DRIVER;
    }
}
