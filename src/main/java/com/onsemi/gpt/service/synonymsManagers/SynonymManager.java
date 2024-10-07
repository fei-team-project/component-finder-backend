package com.onsemi.gpt.service.synonymsManagers;

import com.onsemi.gpt.models.Component;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Service
public class SynonymManager {

    private final Map<Component, List<String>> componentSynonyms;

    public SynonymManager() {
        this.componentSynonyms = new HashMap<>();
        initializeComponentSynonyms();
    }

    private void initializeComponentSynonyms() {
        componentSynonyms.put(Component.ANALOG_BJT, analogBjtSynonyms);
        componentSynonyms.put(Component.CLOCK_GENERATION, clockGenerationSynonyms);
        componentSynonyms.put(Component.COMPARATOR, comparatorSynonyms);
        componentSynonyms.put(Component.DIGITAL_BJT, digitalBjtSynonyms);
        componentSynonyms.put(Component.GATE_DRIVER, gateDriverSynonyms);
        componentSynonyms.put(Component.IGBT, igbtSynonyms);
        componentSynonyms.put(Component.IMAGE_SENSORS, imageSensorsSynonyms);
        componentSynonyms.put(Component.IPM, ipmSynonyms);
        componentSynonyms.put(Component.JFET, jfetSynonyms);
        componentSynonyms.put(Component.MOSFET, mosfetSynonyms);
        componentSynonyms.put(Component.OP_AMP, opAmpSynonyms);
        componentSynonyms.put(Component.PIM, pimSynonyms);
        componentSynonyms.put(Component.POWER_SUPPLY, powerSupplySynonyms);
        componentSynonyms.put(Component.POWER_DIODE, powerDiodeSynonyms);
        componentSynonyms.put(Component.PROTECTED_POWER_SWITCHES, protectedPowerSwitchesSynonyms);
        componentSynonyms.put(Component.RF_BJT, rfBjtSynonyms);
        componentSynonyms.put(Component.SMALL_SIGNAL_DIODE, smallSignalDiodeSynonyms);
        componentSynonyms.put(Component.SMART_POWER_STAGE, smartPowerStageSynonyms);
        componentSynonyms.put(Component.SMART_SWITCH, smartSwitchSynonyms);
        componentSynonyms.put(Component.ZENER_DIODE, zenerDiodeSynonyms);
    }

    public Set<String> getSynonymsForComponent(Component component) {
        return new HashSet<>(componentSynonyms.getOrDefault(component, Collections.emptyList()));
    }

    private final List<String> analogBjtSynonyms = List.of(
            "analog bipolar junction transistor",
            "bipolar transistor",
            "npn transistor",
            "pnp transistor",
            "npn bipolar transistor",
            "npn bipolar junction transistor",
            "pnp bipolar transistor",
            "pnp bipolar junction transistor",
            "bipolar junction device",
            "bipolar semiconductor transistor",
            "junction transistor",
            "traditional transistor",
            "classic transistor",
            "conventional transistor",
            "discrete transistor",
            "bi-polar transistor",
            "small-signal transistor",
            "low-power transistor",
            "signal transistor",
            "amplifying transistor",
            "current-controlled transistor",
            "three-layer transistor",
            "two-junction transistor",
            "vintage transistor",
            "analog bjt"
    );

    private final List<String> clockGenerationSynonyms = List.of(
            "clock oscillator",
            "frequency generator",
            "clock source",
            "clock circuit",
            "clock driver",
            "clock module",
            "clock multiplier",
            "clock synthesizer",
            "clock controller",
            "clock distribution circuit",
            "timing generator",
            "clock signal generator",
            "clock pulse generator",
            "clock multiplier generator",
            "clock divider",
            "clock management unit",
            "frequency synthesizer",
            "pulse generator",
            "crystal oscillator",
            "clock waveform generator",
            "timing circuit",
            "clock and timing solution",
            "oscillator circuit",
            "clock driver ic",
            "clock distribution network"
    );

    private final List<String> comparatorSynonyms = List.of(
            "voltage comparator",
            "voltage comparator ic",
            "comparator amplifier",
            "comparing amplifier",
            "analog comparator",
            "voltage reference comparator",
            "differential amplifier",
            "precision comparator",
            "zero-drift comparator",
            "analog voltage comparator",
            "high-speed comparator",
            "window comparator",
            "hysteresis comparator",
            "operational amplifier-based comparator",
            "differential voltage comparator",
            "threshold detector",
            "voltage level detector",
            "voltage window detector",
            "voltage threshold comparator",
            "high-gain amplifier",
            "voltage slicer",
            "voltage-level sensing circuit",
            "precision voltage comparator",
            "voltage-difference detector",
            "dual differential comparator"
    );

    private final List<String> digitalBjtSynonyms = List.of(
            "digital bipolar junction transistor",
            "logic transistor",
            "switching bjt",
            "ttl transistor",
            "ecl transistor",
            "emitter-coupled logic transistor",
            "dtl transistor",
            "diode-transistor logic transistor",
            "htl transistor",
            "high threshold logic transistor",
            "digital switching transistor",
            "binary transistor",
            "fast bjt",
            "low-delay bjt",
            "high-speed switching transistor",
            "digital amplifier transistor",
            "npn bipolar digital transistor",
            "pnp bipolar digital transistor",
            "digital bipolar transistor",
            "bipolar digital transistor",
            "digital bjt"
    );

    private final List<String> gateDriverSynonyms = List.of(
            "power amplifier",
            "gate driver ic",
            "gate driver integrated circuit",
            "high-voltage driver",
            "mosfet driver",
            "gate control circuit",
            "high-speed driver",
            "power device driver",
            "switching amplifier",
            "voltage amplifier",
            "pwm driver",
            "pulse-width modulation driver",
            "current amplifier",
            "motor driver",
            "high-current amplifier",
            "high-power amplifier",
            "gate control module",
            "phase driver",
            "voltage-level shifter",
            "power stage driver",
            "power electronics interface",
            "digital gate driver"
    );

    private final List<String> igbtSynonyms = List.of(
            "insulated gate bipolar transistor",
            "igbt transistor",
            "igbt module",
            "igbt power module",
            "power igbt",
            "high-power transistor",
            "high-voltage transistor",
            "igbt semiconductor",
            "power switching device",
            "high-voltage switching transistor",
            "insulated power transistor",
            "igbt semiconductor device",
            "high-current transistor",
            "power amplifier transistor",
            "igbt power switch",
            "igbt switch",
            "igbt driver",
            "igbt power electronic component",
            "high-power switching element"
    );

    private final List<String> imageSensorsSynonyms = List.of(
            "image sensor",
            "cmos image sensors",
            "active pixel sensors",
            "solid-state image sensors",
            "digital image sensors",
            "cmos imaging devices",
            "semiconductor image sensors",
            "cmos camera sensors",
            "cmos imaging technology",
            "complementary metal-oxide-semiconductor sensors",
            "digital camera sensors",
            "cmos vision sensors",
            "cmos image capture technology",
            "cmos imaging arrays",
            "solid-state camera sensors",
            "cmos image detector",
            "cmos pixel arrays",
            "active pixel technology",
            "cmos visual sensing devices",
            "cmos imaging chips",
            "cmos optical sensors"
    );

    private final List<String> ipmSynonyms = List.of(
            "smart power module",
            "integrated power module",
            "intelligent power device",
            "advanced power module",
            "intelligent power controller",
            "integrated power circuit",
            "smart power device",
            "advanced power electronics module",
            "ipd",
            "integrated power control unit",
            "intelligent power system",
            "power module with intelligence",
            "smart power conversion module",
            "power semiconductor integration",
            "advanced power control unit",
            "intelligent power conversion device",
            "integrated power electronics module",
            "intelligent power module"
    );

    private final List<String> jfetSynonyms = List.of(
            "junction field-effect transistor",
            "junction fet",
            "jfet transistor",
            "jfet device",
            "junction field-effect semiconductor",
            "jfet transduction element",
            "jfet transistor device",
            "jfet solid-state component",
            "jfet amplifier",
            "jfet switch",
            "jfet voltage-controlled device",
            "jfet signal amplifier",
            "jfet signal switch",
            "junction field-effect transducer"
    );

    private final List<String> mosfetSynonyms = List.of(
            "sic mosfet",
            "mos fet",
            "mos-fet",
            "metal-oxide-semiconductor field-effect transistor",
            "metal-oxide-semiconductor transistor",
            "mos transistor",
            "mos device",
            "semiconductor device",
            "power transistor",
            "switching transistor",
            "solid-state switch",
            "electronic switch",
            "high-frequency transistor",
            "integrated circuit transistor",
            "mos-fet",
            "low-power mosfet",
            "n-channel mosfet",
            "p-channel mosfet",
            "enhancement-mode mosfet",
            "depletion-mode mosfet",
            "voltage-controlled transistor",
            "metal-oxide-semiconductor fet",
            "mos field-effect transistor",
            "mosfet transistor",
            "high-speed mosfet",
            "high-power mosfet",
            "low-voltage mosfet"
    );

    private final List<String> opAmpSynonyms = List.of(
            "operational amplifier",
            "op-amp",
            "op amp",
            "op-amp ic",
            "op-amp chip",
            "op amp ic",
            "op amp chip",
            "op amp amplifier",
            "op-amp amplifier",
            "operational amp",
            "operational amplification device",
            "op amp circuit",
            "op amp component",
            "op amp microchip",
            "op amp module",
            "op amp opener",
            "op amp buffer",
            "op amp comparator",
            "op amp inverter",
            "op amp signal processor",
            "op-amp circuit",
            "op-amp component",
            "op-amp microchip",
            "op-amp module",
            "op-amp opener",
            "op-amp buffer",
            "op-amp comparator",
            "op-amp inverter",
            "op-amp signal processor",
            "voltage amplification device"
    );

    private final List<String> pimSynonyms = List.of(
            "power integrated module",
            "integrated power device",
            "power module",
            "power integration module",
            "integrated power component",
            "power integration device",
            "integrated powers system",
            "power electronics integration module",
            "power electronics package",
            "power electronics assembly",
            "integrated power electronics unit",
            "power ic",
            "power integrated circuit",
            "power packaging",
            "power device module"
    );

    private final List<String> powerSupplySynonyms = List.of(
            "power conversion circuit",
            "ac/dc converter",
            "dc/dc converter",
            "power conversion unit",
            "power conversion system",
            "voltage regulator",
            "power adapter",
            "power inverter",
            "voltage converter",
            "power management circuit",
            "power electronics circuit",
            "power conditioning circuit",
            "power transformer",
            "voltage stabilizer",
            "rectifier circuit",
            "voltage regulating circuit",
            "power control circuit",
            "voltage conversion circuit",
            "power rectification circuit",
            "power modulation circuit"
    );

    private final List<String> powerDiodeSynonyms = List.of(
            "discrete power diode",
            "power rectifier",
            "power semiconductor diode",
            "high-power diode",
            "high-current diode",
            "high-voltage diode",
            "rectifying diode",
            "high-power rectifier",
            "power switching diode",
            "fast recovery diode",
            "schottky diode",
            "avalanche diode",
            "power blocking diode",
            "power clamping diode",
            "power schottky diode",
            "high-power rectifying diode",
            "high-power switching diode",
            "barrier diode"
    );

    private final List<String> protectedPowerSwitchesSynonyms = List.of(
            "protected power switch",
            "efuse",
            "electronic fuse",
            "ecoswitch",
            "eco-friendly switch",
            "intellimax",
            "intelligent maximizer",
            "smart power switches",
            "electronic circuit breakers",
            "power protection devices",
            "programmable power switches",
            "integrated power switching solutions",
            "fault-protected power switches",
            "power switching and control units",
            "digital power switches",
            "intelligent power distribution",
            "power management solutions",
            "solid-state power switches",
            "fault-tolerant power switches",
            "circuit protection switches",
            "power supply control modules",
            "energy-efficient power switches",
            "advanced power switching technology"
    );

    private final List<String> rfBjtSynonyms = List.of(
            "rf bipolar junction transistor",
            "high-frequency bjt",
            "rf npn transistor",
            "rf pnp transistor",
            "radio frequency bjt",
            "high-frequency rf transistor",
            "rf bipolar transistor",
            "rf transistor amplifier",
            "rf transistor device",
            "rf small-signal transistor",
            "rf amplification transistor",
            "rf bipolar device",
            "high-speed bjt",
            "high-frequency bipolar device",
            "rf power transistor",
            "rf bjt"
    );

    private final List<String> smallSignalDiodeSynonyms = List.of(
            "small signal diode",
            "small signal rectifier",
            "small signal semiconductor diode",
            "radio frequency diode",
            "rf diode",
            "small-signal switching diode",
            "low-power diode",
            "low-current diode",
            "low-voltage diode",
            "signal diode",
            "general purpose diode",
            "small-signal rectifying diode",
            "small-signal rf detector",
            "small-signal switching device",
            "rf signal diode",
            "rf detector diode",
            "low-level rf diode",
            "low-power signal diode",
            "low-frequency diode",
            "low-signal voltage diode"
    );

    private final List<String> smartPowerStageSynonyms = List.of(
            "integrated driver and mosfet",
            "integrated driver and mosfets",
            "integrated driver mosfet combo",
            "integrated power stage",
            "integrated driver and power transistor",
            "integrated mosfet and driver",
            "smart power ic",
            "power stage module",
            "smart power block",
            "all-in-one power module",
            "smart power combo",
            "integrated power transistor and driver",
            "power stage combination",
            "driver-mosfet integrated circuit",
            "integrated motor driver and mosfet",
            "intelligent power transistor and driver",
            "smart power hybrid",
            "sps"
    );

    private final List<String> smartSwitchSynonyms = List.of(
            "smart self-protected mosfet switch",
            "intelligent mosfet switch",
            "self-protecting mosfet switch",
            "mosfet-based smart switch",
            "smart power mosfet switch",
            "smart solid-state switch",
            "self-protection mosfet switch",
            "smart high-side switch",
            "mosfet smart load switch",
            "intelligent power switch",
            "mosfet switching device with smart features",
            "self-protected solid-state switch",
            "smart high-voltage switch",
            "mosfet-based self-protection switch",
            "integrated smart power switch",
            "mosfet circuit breaker with intelligence",
            "smart overcurrent protection mosfet switch",
            "self-protected mosfet power control"
    );

    private final List<String> zenerDiodeSynonyms = List.of(
            "voltage stabilizing reverse diode",
            "zener voltage regulator",
            "zener regulating diode",
            "voltage reference diode",
            "zener diode regulator",
            "voltage-stabilized diode",
            "zener voltage control diode",
            "voltage clamping diode",
            "zener reverse-biased diode",
            "voltage reference regulator",
            "zener voltage clamp",
            "reverse-biased voltage diode",
            "voltage limiting diode",
            "zener voltage-setting diode",
            "reverse avalanche diode"
    );
}

