package com.onsemi.gpt.models;

import com.onsemi.gpt.models.entitites.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum Component {
    MOSFET("MOSFET", new Mosfet()),
    POWER_SUPPLY("POWER_SUPPLY", new PowerSupply()),
    PIM("PIM", new Pim()),
    IGBT("IGBT", new Igbt()),
    IPM("IPM", new Ipm()),
    IMAGE_SENSORS("IMAGE_SENSORS", new ImageSensors()),
    OP_AMP("OP_AMP", new OpAmp()),
    SMART_POWER_STAGE("SMART_POWER_STAGE", new SmartPowerStage()),
    SMART_SWITCH("SMART_SWITCH", new SmartSwitch()),
    GATE_DRIVER("GATE_DRIVER", new GateDriver()),
    JFET("JFET", new Jfet()),
    ANALOG_BJT("ANALOG_BJT", new AnalogBJT()),
    RF_BJT("RF_BJT", new RFBJT()),
    POWER_DIODE("POWER_DIODE", new PowerDiode()),
    SMALL_SIGNAL_DIODE("SMALL_SIGNAL_DIODE", new SmallSignalDiode()),
    ZENER_DIODE("ZENER_DIODE", new ZenerDiode()),
    COMPARATOR("COMPARATOR", new Comparator()),
    DIGITAL_BJT("DIGITAL_BJT", new DigitalBjt()),
    PROTECTED_POWER_SWITCHES("PROTECTED_POWER_SWITCHES", new ProtectedPowerSwitch()),
    CLOCK_GENERATION("CLOCK_GENERATION", new ClockGeneration());

    private final String tableName;
    private final BaseComponent baseComponent;

    public String getAttributesAsString() {
        return baseComponent.getAttributesAsString();
    }

    public List<String> getTableColumns() {
        return baseComponent.getColumns();
    }

}

