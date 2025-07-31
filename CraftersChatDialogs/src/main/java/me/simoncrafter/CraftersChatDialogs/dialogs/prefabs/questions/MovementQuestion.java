package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions;

import me.simoncrafter.CraftersChatDialogs.Clamp;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOptions;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.DoubleInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.FloatInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.DoubleInputButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.FloatInputButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MovementQuestion extends AbstractQuestion<MovementQuestion> {





    private @NotNull Button plusXButton = Button.create();  // CONFIGURABLE
    private @NotNull Button minusXButton = Button.create();  // CONFIGURABLE
    private @NotNull Button plusYButton = Button.create();  // CONFIGURABLE
    private @NotNull Button minusYButton = Button.create();  // CONFIGURABLE
    private @NotNull Button plusZButton = Button.create();  // CONFIGURABLE
    private @NotNull Button minusZButton = Button.create();  // CONFIGURABLE
    private @NotNull Button plusRollButton = Button.create();  // CONFIGURABLE
    private @NotNull Button minusRollButton = Button.create();  // CONFIGURABLE
    private @NotNull Button plusPitchButton = Button.create();  // CONFIGURABLE
    private @NotNull Button minusPitchButton = Button.create();  // CONFIGURABLE
    private @NotNull Button plusYawButton = Button.create();  // CONFIGURABLE
    private @NotNull Button minusYawButton = Button.create();  // CONFIGURABLE

    private @NotNull Button exitButton = Button.create();  // CONFIGURABLE
    
    private @NotNull DoubleInputButton setXButton = DoubleInputButton.create();  // CONFIGURABLE
    private @NotNull DoubleInputButton setYButton = DoubleInputButton.create();  // CONFIGURABLE
    private @NotNull DoubleInputButton setZButton = DoubleInputButton.create();  // CONFIGURABLE

    private @NotNull FloatInputButton setRollButton = FloatInputButton.create();  // CONFIGURABLE
    private @NotNull FloatInputButton setPitchButton = FloatInputButton.create();  // CONFIGURABLE
    private @NotNull FloatInputButton setYawButton = FloatInputButton.create();  // CONFIGURABLE

    private @NotNull DoubleInputButton setMovementStepButton = DoubleInputButton.create();  // CONFIGURABLE
    private @NotNull FloatInputButton setRotationStepButton = FloatInputButton.create();  // CONFIGURABLE

    //enabled states, config and clamping


    private boolean showSetMovementStepButton = false;  // CONFIGURABLE
    private boolean showSetRotationStepButton = false;  // CONFIGURABLE

    private double movementStep = Double.NaN;  // CONFIGURABLE
    private float rotationStep = Float.NaN;  // CONFIGURABLE

    // ROTATION: enabled states
    private boolean STATEenableplusRollButton = true;
    private boolean STATEenableplusPitchButton = true;
    private boolean STATEenableplusYawButton = true;
    private boolean STATEenableminusRollButton = true;
    private boolean STATEenableminusPitchButton = true;
    private boolean STATEenableminusYawButton = true;

    // ROTATION: enabled configs
    private boolean enableRoll = true;  // CONFIGURABLE
    private boolean enablePitch = true;  // CONFIGURABLE
    private boolean enableYaw = true;  // CONFIGURABLE
    private boolean showRotation = true;  // CONFIGURABLE
    
    // ROTATION: clamping
    private Clamp<Float> rollClamp = Clamp.create(Float.class);  // CONFIGURABLE
    private Clamp<Float> pitchClamp = Clamp.create(Float.class);  // CONFIGURABLE
    private Clamp<Float> yawClamp = Clamp.create(Float.class);  // CONFIGURABLE

    // POSITION: enabled states
    private boolean STATEenableplusXButton = true;
    private boolean STATEenableminusXButton = true;
    private boolean STATEenableplusYButton = true;
    private boolean STATEenableminusYButton = true;
    private boolean STATEenableplusZButton = true;
    private boolean STATEenableminusZButton = true;

    // POSITION: enabled configs
    private boolean enableXPosition = true;  // CONFIGURABLE
    private boolean enableYPosition = true;  // CONFIGURABLE
    private boolean enableZPosition = true;  // CONFIGURABLE

    // POSITION: clamping
    private Clamp<Double> xClamp = Clamp.create(Double.class);  // CONFIGURABLE
    private Clamp<Double> yClamp = Clamp.create(Double.class);  // CONFIGURABLE
    private Clamp<Double> zClamp = Clamp.create(Double.class);  // CONFIGURABLE

    // POSITION DATA
    private double xPosition = 0;  // CONFIGURABLE
    private double yPosition = 0;  // CONFIGURABLE
    private double zPosition = 0;  // CONFIGURABLE

    // ROTATION DATA
    private float roll = 0;  // CONFIGURABLE
    private float pitch = 0;  // CONFIGURABLE
    private float yaw = 0;  // CONFIGURABLE


    public MovementQuestion() {
    }

    @Contract(value = "-> new")
    public static MovementQuestion create() {
        return new MovementQuestion().displayOption(DisplayOptions.DEFAULT.colorPalette(DisplayOptions.ColorPalettes.YELLOW_ISH.override(DisplayOptions.ColorPalettes.Overrides.XYZ)));
    }



    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addExitButtonAction(IAction action) {
        exitButton.addAction(action);
        return this;
    }
    public List<IAction> exitButtonActions() {
        return exitButton.actions();
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeExitButtonAction(IAction action) {
        exitButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setExitButtonAction(List<IAction> actions) {
        exitButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion movementStep(double movementStep) {
        this.movementStep = movementStep;
        return this;
    }
    public double movementStep() {
        return movementStep;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion rotationStep(float rotationStep) {
        this.rotationStep = rotationStep;
        return this;
    }
    public float rotationStep() {
        return rotationStep;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion showMovementStepButton(boolean showMovementStepButton) {
        this.showSetMovementStepButton = showMovementStepButton;
        return this;
    }
    public boolean showMovementStepButton() {
        return showSetMovementStepButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion showRotationStepButton(boolean showRotationStepButton) {
        this.showSetRotationStepButton = showRotationStepButton;
        return this;
    }
    public boolean showRotationStepButton() {
        return showSetRotationStepButton;
    }


    public DoubleInputAction movementStepButtonDoubleInputAction() {
        return setMovementStepButton.doubleInputAction();
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion movementStepButtonDoubleInputAction(DoubleInputAction action) {
        this.setMovementStepButton.doubleInputAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion movementStepButtonDoubleInputAction(Function<Player, Consumer<Double>> action) {
        this.setMovementStepButton.doubleInputAction(DoubleInputAction.create(action));
        return this;
    }
    public DoubleInputButton movementStepButton() {
        return setMovementStepButton;
    }
    public @NotNull List<@NotNull IAction> movementStepButtonActions() {
        return setMovementStepButton.actions();
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion movementStepButton(DoubleInputButton button) {
        this.setMovementStepButton = button;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion rotationStepButtonFloatInputAction(FloatInputAction action) {
        this.setRotationStepButton.floatInputAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion rotationStepButtonFloatInputAction(Function<Player, Consumer<Float>> action) {
        this.setRotationStepButton.floatInputAction(FloatInputAction.create(action));
        return this;
    }
    public FloatInputAction rotationStepButtonFloatInputAction() {
        return setRotationStepButton.floatInputAction();
    }
    public FloatInputButton rotationStepButton() {
        return setRotationStepButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion rotationStepButton(FloatInputButton button) {
        this.setRotationStepButton = button;
        return this;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion plusXButton(Button plusXButton) {
        this.plusXButton = plusXButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setPlusXButtonActions(@NotNull List<@NotNull IAction> actions) {
        plusXButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removePlusXButtonAction(@NotNull IAction action) {
        plusXButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addPlusXButtonAction(@NotNull IAction action) {
        plusXButton.addAction(action);
        return this;
    }

    public Button plusXButton() {
        return plusXButton;
    }

    public @NotNull List<@NotNull IAction> plusXButtonActions() {
        return plusXButton.actions();
    }

// -- minusXButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion minusXButton(Button minusXButton) {
        this.minusXButton = minusXButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setMinusXButtonActions(@NotNull List<@NotNull IAction> actions) {
        minusXButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeMinusXButtonAction(@NotNull IAction action) {
        minusXButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addMinusXButtonAction(@NotNull IAction action) {
        minusXButton.addAction(action);
        return this;
    }

    public Button minusXButton() {
        return minusXButton;
    }

    public @NotNull List<@NotNull IAction> minusXButtonActions() {
        return minusXButton.actions();
    }

// -- plusYButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion plusYButton(Button plusYButton) {
        this.plusYButton = plusYButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setPlusYButtonActions(@NotNull List<@NotNull IAction> actions) {
        plusYButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removePlusYButtonAction(@NotNull IAction action) {
        plusYButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addPlusYButtonAction(@NotNull IAction action) {
        plusYButton.addAction(action);
        return this;
    }

    public Button plusYButton() {
        return plusYButton;
    }

    public @NotNull List<@NotNull IAction> plusYButtonActions() {
        return plusYButton.actions();
    }

// -- minusYButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion minusYButton(Button minusYButton) {
        this.minusYButton = minusYButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setMinusYButtonActions(@NotNull List<@NotNull IAction> actions) {
        minusYButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeMinusYButtonAction(@NotNull IAction action) {
        minusYButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addMinusYButtonAction(@NotNull IAction action) {
        minusYButton.addAction(action);
        return this;
    }

    public Button minusYButton() {
        return minusYButton;
    }

    public @NotNull List<@NotNull IAction> minusYButtonActions() {
        return minusYButton.actions();
    }

// -- plusZButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion plusZButton(Button plusZButton) {
        this.plusZButton = plusZButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setPlusZButtonActions(@NotNull List<@NotNull IAction> actions) {
        plusZButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removePlusZButtonAction(@NotNull IAction action) {
        plusZButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addPlusZButtonAction(@NotNull IAction action) {
        plusZButton.addAction(action);
        return this;
    }

    public Button plusZButton() {
        return plusZButton;
    }

    public @NotNull List<@NotNull IAction> plusZButtonActions() {
        return plusZButton.actions();
    }

// -- minusZButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion minusZButton(Button minusZButton) {
        this.minusZButton = minusZButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setMinusZButtonActions(@NotNull List<@NotNull IAction> actions) {
        minusZButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeMinusZButtonAction(@NotNull IAction action) {
        minusZButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addMinusZButtonAction(@NotNull IAction action) {
        minusZButton.addAction(action);
        return this;
    }

    public Button minusZButton() {
        return minusZButton;
    }

    public @NotNull List<@NotNull IAction> minusZButtonActions() {
        return minusZButton.actions();
    }

// -- plusRollButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion plusRollButton(Button plusRollButton) {
        this.plusRollButton = plusRollButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setPlusRollButtonActions(@NotNull List<@NotNull IAction> actions) {
        plusRollButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removePlusRollButtonAction(@NotNull IAction action) {
        plusRollButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addPlusRollButtonAction(@NotNull IAction action) {
        plusRollButton.addAction(action);
        return this;
    }

    public Button plusRollButton() {
        return plusRollButton;
    }

    public @NotNull List<@NotNull IAction> plusRollButtonActions() {
        return plusRollButton.actions();
    }

// -- minusRollButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion minusRollButton(Button minusRollButton) {
        this.minusRollButton = minusRollButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setMinusRollButtonActions(@NotNull List<@NotNull IAction> actions) {
        minusRollButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeMinusRollButtonAction(@NotNull IAction action) {
        minusRollButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addMinusRollButtonAction(@NotNull IAction action) {
        minusRollButton.addAction(action);
        return this;
    }

    public Button minusRollButton() {
        return minusRollButton;
    }

    public @NotNull List<@NotNull IAction> minusRollButtonActions() {
        return minusRollButton.actions();
    }

// -- plusPitchButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion plusPitchButton(Button plusPitchButton) {
        this.plusPitchButton = plusPitchButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setPlusPitchButtonActions(@NotNull List<@NotNull IAction> actions) {
        plusPitchButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removePlusPitchButtonAction(@NotNull IAction action) {
        plusPitchButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addPlusPitchButtonAction(@NotNull IAction action) {
        plusPitchButton.addAction(action);
        return this;
    }

    public Button plusPitchButton() {
        return plusPitchButton;
    }

    public @NotNull List<@NotNull IAction> plusPitchButtonActions() {
        return plusPitchButton.actions();
    }

// -- minusPitchButton --

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion minusPitchButton(Button minusPitchButton) {
        this.minusPitchButton = minusPitchButton;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setMinusPitchButtonActions(@NotNull List<@NotNull IAction> actions) {
        minusPitchButton.setActions(actions);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeMinusPitchButtonAction(@NotNull IAction action) {
        minusPitchButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addMinusPitchButtonAction(@NotNull IAction action) {
        minusPitchButton.addAction(action);
        return this;
    }

    public Button minusPitchButton() {
        return minusPitchButton;
    }

    public @NotNull List<@NotNull IAction> minusPitchButtonActions() {
        return minusPitchButton.actions();
    }



    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion plusYawButton(Button plusYawButton) {
        this.plusYawButton = plusYawButton;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setplusYawButtonActions(@NotNull List<@NotNull IAction> actions) {
        plusYawButton.setActions(actions);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeplusYawButtonAction(@NotNull IAction action) {
        plusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addPlusYawButtonAction(@NotNull IAction action) {
        plusYawButton.addAction(action);
        return this;
    }
    public Button plusYawButton() {
        return plusYawButton;
    }
    public @NotNull List<@NotNull IAction> plusYawButtonActions() {
        return plusYawButton.actions();    
    }
    
    
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion minusYawButton(Button minusYawButton) {
        this.minusYawButton = minusYawButton;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setminusYawButtonActions(@NotNull List<@NotNull IAction> actions) {
        minusYawButton.setActions(actions);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeminusYawButtonAction(@NotNull IAction action) {
        minusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addMinusYawButtonAction(@NotNull IAction action) {
        minusYawButton.addAction(action);
        return this;
    }
    public Button minusYawButton() {
        return minusYawButton;
    }
    public @NotNull List<@NotNull IAction> minusYawButtonActions() {
        return minusYawButton.actions();
    }





    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion showRotation(boolean showRotation) {
        this.showRotation = showRotation;
        return this;
    }
    public boolean showRotation() {
        return showRotation;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion enableRoll(boolean enableRoll) {
        this.enableRoll = enableRoll;
        return this;
    }
    public boolean enableRoll() {
        return enableRoll;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion enablePitch(boolean enablePitch) {
        this.enablePitch = enablePitch;
        return this;
    }
    public boolean enablePitch() {
        return enablePitch;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion enableYaw(boolean enableYaw) {
        this.enableYaw = enableYaw;
        return this;
    }




    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion enableXPosition(boolean enableXPosition) {
        this.enableXPosition = enableXPosition;
        return this;
    }
    public boolean enableXPosition() {
        return enableXPosition;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion enableYPosition(boolean enableYPosition) {
        this.enableYPosition = enableYPosition;
        return this;
    }
    public boolean enableYPosition() {
        return enableYPosition;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion enableZPosition(boolean enableZPosition) {
        this.enableZPosition = enableZPosition;
        return this;
    }
    public boolean enableZPosition() {
        return enableZPosition;
    }

    
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion xPosition(double xPosition) {
        this.xPosition = xPosition;
        return this;
    }
    public double xPosition() {
        return xPosition;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion yPosition(double yPosition) {
        this.yPosition = yPosition;
        return this;
    }

    public double yPosition() {
        return yPosition;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion zPosition(double zPosition) {
        this.zPosition = zPosition;
        return this;
    }
    public double zPosition() {
        return zPosition;
    }



    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion roll(float roll) {
        this.roll = roll;
        return this;
    }
    public float roll() {
        return roll;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }
    public float pitch() {
        return pitch;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion yaw(float yaw) {
        this.yaw = yaw;
        return this;
    }
    public float yaw() {
        return yaw;
    }

    @Contract(value = "_, _, _ -> this", mutates = "this")
    public MovementQuestion setLocation(double xPosition, double yPosition, double zPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
        return this;
    }
    @Contract(value = "_, _, _, _, _, _ -> this", mutates = "this")
    public MovementQuestion setLocation(double xPosition, double yPosition, double zPosition, float roll, float pitch, float yaw) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        return this;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setLocation(Location location) {
        this.xPosition = location.getX();
        this.yPosition = location.getY();
        this.zPosition = location.getZ();
        return this;
    }


    @Contract(value = "_, _, _ -> this", mutates = "this")
    public MovementQuestion setRotation(float roll, float pitch, float yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion setRotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw =   yaw;
        return this;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetXButtonAction(@NotNull Function<Player, Consumer<Double>> onClick) {
        setXButton.doubleInputAction(DoubleInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetXButtonAction(DoubleInputAction onClick) {
        setXButton.doubleInputAction(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setXButtonAction(DoubleInputButton button) {
        setXButton = button;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addSetXButtonAction(IAction action) {
        setXButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeSetXButtonAction(IAction action) {
        setXButton.removeAction(action);
        return this;
    }
    public DoubleInputButton setXButton() {
        return setXButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYButtonAction(@NotNull Function<Player, Consumer<Double>> onClick) {
        setYButton.doubleInputAction(DoubleInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYButtonAction(DoubleInputAction onClick) {
        setYButton.doubleInputAction(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setYButtonAction(DoubleInputButton button) {
        setYButton = button;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addSetYButtonAction(IAction action) {
        setYButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeSetYButtonAction(IAction action) {
        setYButton.removeAction(action);
        return this;
    }

    public DoubleInputButton setYButton() {
        return setYButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetZButtonAction(@NotNull Function<Player, Consumer<Double>> onClick) {
        setZButton.doubleInputAction(DoubleInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetZButtonAction(DoubleInputAction onClick) {
        setZButton.doubleInputAction(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setZButtonAction(DoubleInputButton button) {
        setZButton = button;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addSetZButtonAction(IAction action) {
        setZButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeSetZButtonAction(IAction action) {
        setZButton.removeAction(action);
        return this;
    }
    public DoubleInputButton setZButton() {
        return setZButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetRollButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setRollButton.floatInputAction(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetRollButtonAction(FloatInputAction onClick) {
        setRollButton.floatInputAction(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setRollButtonAction(FloatInputButton button) {
        setRollButton = button;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addSetRollButtonAction(IAction action) {
        setRollButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeSetRollButtonAction(IAction action) {
        setRollButton.removeAction(action);
        return this;
    }
    public FloatInputButton setRollButton() {
        return setRollButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetPitchButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setPitchButton.floatInputAction(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetPitchButtonAction(FloatInputAction onClick) {
        setPitchButton.floatInputAction(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setPitchButtonAction(FloatInputButton button) {
        setPitchButton = button;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addSetPitchButtonAction(IAction action) {
        setPitchButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeSetPitchButtonAction(IAction action) {
        setPitchButton.removeAction(action);
        return this;
    }
    public FloatInputButton setPitchButton() {
        return setPitchButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYawButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setYawButton.floatInputAction(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYawButtonAction(FloatInputAction onClick) {
        setYawButton.floatInputAction(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setYawButtonAction(FloatInputButton button) {
        setYawButton = button;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addSetYawButtonAction(IAction action) {
        setYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeSetYawButtonAction(IAction action) {
        setYawButton.removeAction(action);
        return this;
    }
    public FloatInputButton setYawButton() {
        return setYawButton;
    }





    // --- ROLL ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion rollClamp(Clamp<Float> clamp) {
        this.rollClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion rollClamp(float lower, float upper) {
        this.rollClamp = Clamp.create(Float.class, lower, upper);
        return this;
    }

    public Clamp<Float> rollClamp() {
        return this.rollClamp;
    }

    // --- PITCH ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion pitchClamp(Clamp<Float> clamp) {
        this.pitchClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion pitchClamp(float lower, float upper) {
        this.pitchClamp = Clamp.create(Float.class, lower, upper);
        return this;
    }

    public Clamp<Float> pitchClamp() {
        return this.pitchClamp;
    }

    // --- YAW ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion yawClamp(Clamp<Float> clamp) {
        this.yawClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion yawClamp(float lower, float upper) {
        this.yawClamp = Clamp.create(Float.class, lower, upper);
        return this;
    }

    public Clamp<Float> yawClamp() {
        return this.yawClamp;
    }

    // --- X ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion xClamp(Clamp<Double> clamp) {
        this.xClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion xClamp(double lower, double upper) {
        this.xClamp = Clamp.create(Double.class, lower, upper);
        return this;
    }

    public Clamp<Double> xClamp() {
        return this.xClamp;
    }

    // --- Y ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion yClamp(Clamp<Double> clamp) {
        this.yClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion yClamp(double lower, double upper) {
        this.yClamp = Clamp.create(Double.class, lower, upper);
        return this;
    }

    public Clamp<Double> yClamp() {
        return this.yClamp;
    }

    // --- Z ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion zClamp(Clamp<Double> clamp) {
        this.zClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion zClamp(double lower, double upper) {
        this.zClamp = Clamp.create(Double.class, lower, upper);
        return this;
    }

    public Clamp<Double> zClamp() {
        return this.zClamp;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForXButtons(Consumer<Button> action) {
        action.accept(plusXButton);
        action.accept(minusXButton);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForYButtons(Consumer<Button> action) {
        action.accept(plusYButton);
        action.accept(minusYButton);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForZButtons(Consumer<Button> action) {
        action.accept(plusZButton);
        action.accept(minusZButton);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForRollButtons(Consumer<Button> action) {
        action.accept(plusRollButton);
        action.accept(minusRollButton);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForPitchButtons(Consumer<Button> action) {
        action.accept(plusPitchButton);
        action.accept(minusPitchButton);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForYawButtons(Consumer<Button> action) {
        action.accept(plusYawButton);
        action.accept(minusYawButton);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion doForAllButtons(Consumer<AbstractButton<?>> action) {
        action.accept(plusXButton);
        action.accept(minusXButton);
        action.accept(plusYButton);
        action.accept(minusYButton);
        action.accept(plusZButton);
        action.accept(minusZButton);
        action.accept(plusRollButton);
        action.accept(minusRollButton);
        action.accept(plusPitchButton);
        action.accept(minusPitchButton);
        action.accept(plusYawButton);
        action.accept(minusYawButton);
        action.accept(setXButton);
        action.accept(setYButton);
        action.accept(setZButton);
        action.accept(setRollButton);
        action.accept(setPitchButton);
        action.accept(setYawButton);
        return this;
    }




    @Override
    public void beforeShow(@NotNull Player player) {

        //set states
        STATEenableplusXButton = enableXPosition;
        STATEenableminusXButton = enableXPosition;
        STATEenableplusYButton = enableYPosition;
        STATEenableminusYButton = enableYPosition;
        STATEenableplusZButton = enableZPosition;
        STATEenableminusZButton = enableZPosition;

        STATEenableplusRollButton = enableRoll;
        STATEenableminusRollButton = enableRoll;
        STATEenableplusPitchButton = enablePitch;
        STATEenableminusPitchButton = enablePitch;
        STATEenableplusYawButton = enableYaw;
        STATEenableminusYawButton = enableYaw;

        handleClampDisabling();
        setButtonText();

        Component content = Component.text().resetStyle().build();


        // add disable action
        addDefualtOnClickFunctions(player);


        content = content.append(Component.text("Movement Options   ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(exitButton.compile()));
        content = content.appendNewline();
        content = content.appendNewline();


        content = content.append(handleEnabledMovementButton()).appendNewline();



        if (showRotation) {

            // space between rotation and position
            content = content.appendNewline();

            content = content.append(handleEnabledRotationButton());

        }


        content(content);
    }

    private void handleClampDisabling() {
        if (STATEenableplusXButton && xClamp.upper() && !xClamp.checkUpper(xPosition)) {
            STATEenableplusXButton = false;
        }
        if (STATEenableminusXButton && xClamp.lower() && !xClamp.checkLower(xPosition)) {
            STATEenableminusXButton = false;
        }
        if (STATEenableplusYButton && yClamp.upper() && !yClamp.checkUpper(yPosition)) {
            STATEenableplusYButton = false;
        }
        if (STATEenableminusYButton && yClamp.lower() && !yClamp.checkLower(yPosition)) {
            STATEenableminusYButton = false;
        }
        if (STATEenableplusZButton && zClamp.upper() && !zClamp.checkUpper(zPosition)) {
            STATEenableplusZButton = false;
        }
        if (STATEenableminusZButton && zClamp.lower() && !zClamp.checkLower(zPosition)) {
            STATEenableminusZButton = false;
        }


        if (STATEenableplusRollButton && rollClamp.upper() && !rollClamp.checkUpper(roll)) {
            STATEenableplusRollButton = false;
        }
        if (STATEenableminusRollButton && rollClamp.lower() && !rollClamp.checkLower(roll)) {
            STATEenableminusRollButton = false;
        }
        if (STATEenableplusPitchButton && pitchClamp.upper() && !pitchClamp.checkUpper(pitch)) {
            STATEenableplusPitchButton = false;
        }
        if (STATEenableminusPitchButton && pitchClamp.lower() && !pitchClamp.checkLower(pitch)) {
            STATEenableminusPitchButton = false;
        }
        if (STATEenableplusYawButton && yawClamp.upper() && !yawClamp.checkUpper(yaw)) {
            STATEenableplusYawButton = false;
        }
        if (STATEenableminusYawButton && yawClamp.lower() && !yawClamp.checkLower(yaw)) {
            STATEenableminusYawButton = false;
        }





    }

    private void addDefualtOnClickFunctions(Player player) {
        addReloadActionToButton(player); // for some fucking reason this has to happen first
        addHoverTextToButtons();

        CustomAction disableAction = CustomAction.create(p -> disableButtons());


        plusXButton.addPostAction(disableAction);
        minusXButton.addPostAction(disableAction);
        plusYButton.addPostAction(disableAction);
        minusYButton.addPostAction(disableAction);
        plusZButton.addPostAction(disableAction);
        minusZButton.addPostAction(disableAction);
        plusRollButton.addPostAction(disableAction);
        minusRollButton.addPostAction(disableAction);
        plusPitchButton.addPostAction(disableAction);
        minusPitchButton.addPostAction(disableAction);
        plusYawButton.addPostAction(disableAction);
        minusYawButton.addPostAction(disableAction);



        setXButton.doubleInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setYButton.setDisabled(true);
            setZButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setPitchButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setYButton.doubleInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setXButton.setDisabled(true);
            setZButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setPitchButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setZButton.doubleInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setXButton.setDisabled(true);
            setYButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setPitchButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setRollButton.floatInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setXButton.setDisabled(true);
            setYButton.setDisabled(true);
            setZButton.setDisabled(true);
            setPitchButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setPitchButton.floatInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setXButton.setDisabled(true);
            setYButton.setDisabled(true);
            setZButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setYawButton.floatInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setXButton.setDisabled(true);
            setYButton.setDisabled(true);
            setZButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setPitchButton.setDisabled(true);
        }));



    }

    private void addReloadActionToButton(Player player) {
        CustomAction reloadAction = CustomAction.create(p -> reload(player));

        plusXButton.addPostAction(reloadAction);
        minusXButton.addPostAction(reloadAction);
        plusYButton.addPostAction(reloadAction);
        minusYButton.addPostAction(reloadAction);
        plusZButton.addPostAction(reloadAction);
        minusZButton.addPostAction(reloadAction);
        plusRollButton.addPostAction(reloadAction);
        minusRollButton.addPostAction(reloadAction);
        plusPitchButton.addPostAction(reloadAction);
        minusPitchButton.addPostAction(reloadAction);
        plusYawButton.addPostAction(reloadAction);
        minusYawButton.addPostAction(reloadAction);

        setXButton.doubleInputAction(setXButton.doubleInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setYButton.doubleInputAction(setYButton.doubleInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setZButton.doubleInputAction(setZButton.doubleInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setRollButton.floatInputAction(setRollButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setPitchButton.floatInputAction(setPitchButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setYawButton.floatInputAction(setYawButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));

        exitButton.addAction(exitAction).addAction(ClearChatAction.create()).addAction(MessageAction.create(Component.text("Exited Movement Dialog", displayOption().colorPalette().RED())));

        CustomAction reloadNoCascade = CustomAction.create(p -> reload(player, true));
        setMovementStepButton.doubleInputAction(setMovementStepButton.doubleInputAction().addSuccessAction(reloadNoCascade).addTimeoutAction(reloadNoCascade).maxResponseTime(60).addCancelAction(reloadNoCascade).reTry(true));
        setRotationStepButton.floatInputAction(setRotationStepButton.floatInputAction().addSuccessAction(reloadNoCascade).addTimeoutAction(reloadNoCascade).maxResponseTime(60).addCancelAction(reloadNoCascade).reTry(true));

    }

    private void addHoverTextToButtons() {
        plusXButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(xPosition,  displayOption().colorPalette().get("X"), TextDecoration.BOLD)));
        minusXButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(xPosition,  displayOption().colorPalette().get("X"), TextDecoration.BOLD)));
        plusYButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(yPosition,  displayOption().colorPalette().get("Y"), TextDecoration.BOLD)));
        minusYButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(yPosition,  displayOption().colorPalette().get("Y"), TextDecoration.BOLD)));
        plusZButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(zPosition,  displayOption().colorPalette().get("Z"), TextDecoration.BOLD)));
        minusZButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(zPosition,  displayOption().colorPalette().get("Z"), TextDecoration.BOLD)));
        plusRollButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(roll,  displayOption().colorPalette().get("X"), TextDecoration.BOLD)));
        minusRollButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(roll,  displayOption().colorPalette().get("X"), TextDecoration.BOLD)));
        plusPitchButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(pitch,  displayOption().colorPalette().get("Y"), TextDecoration.BOLD)));
        minusPitchButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(pitch,  displayOption().colorPalette().get("Y"), TextDecoration.BOLD)));
        plusYawButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(yaw,  displayOption().colorPalette().get("Z"), TextDecoration.BOLD)));
        minusYawButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(yaw,  displayOption().colorPalette().get("Z"), TextDecoration.BOLD)));
        setXButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(xPosition,  displayOption().colorPalette().get("X"), TextDecoration.BOLD)));
        setYButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(yPosition,  displayOption().colorPalette().get("Y"), TextDecoration.BOLD)));
        setZButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(zPosition,  displayOption().colorPalette().get("Z"), TextDecoration.BOLD)));
        setRollButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(roll,  displayOption().colorPalette().get("X"), TextDecoration.BOLD)));
        setPitchButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(pitch,  displayOption().colorPalette().get("Y"), TextDecoration.BOLD)));
        setYawButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(yaw,  displayOption().colorPalette().get("Z"), TextDecoration.BOLD)));
        setMovementStepButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(movementStep,  displayOption().colorPalette().MODIFIER(), TextDecoration.BOLD)));
        setRotationStepButton.hoverText(Component.text("Value: ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).append(Component.text(rotationStep,  displayOption().colorPalette().MODIFIER(), TextDecoration.BOLD)));

    }

    private Component handleEnabledMovementButton() {
        Component content = Component.empty();
        Component line1 = Component.text("         ");
        Component line2 = Component.text("        ");
        Component line3 = Component.text("        ");


        if (!STATEenableplusXButton && !STATEenableminusXButton) {
            line1 = line1.append(setXButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(setXButton.hoverText()));
        }else {
            line1 = line1.append(setXButton.compile());
        }

        if (STATEenableplusXButton) {
            line2 = line2.append(Component.text("")).append(plusXButton.compile());
        }else {
            line2 = line2.append(Component.text("")).append(plusXButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(plusXButton.hoverText()));
        }


        if (STATEenableminusXButton) {
            line3 = line3.append(Component.text("")).append(minusXButton.compile());
        }else {
            line3 = line3.append(Component.text("")).append(minusXButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(minusXButton.hoverText()));
        }


        if (!STATEenableplusYButton && !STATEenableminusYButton) {
            line1 = line1.append(Component.text("         ").append(setYButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(setYButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("         ")).append(setYButton.compile());
        }

        if (STATEenableplusYButton) {
            line2 = line2.append(Component.text("       ")).append(plusYButton.compile());
        }else {
            line2 = line2.append(Component.text("       ")).append(plusYButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(plusYButton.hoverText()));
        }

        if (STATEenableminusYButton) {
            line3 = line3.append(Component.text("       ")).append(minusYButton.compile());
        }else {
            line3 = line3.append(Component.text("       ")).append(minusYButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(minusYButton.hoverText()));
        }



        if (!STATEenableplusZButton && !STATEenableminusZButton) {
            line1 = line1.append(Component.text("        ").append(setZButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(setZButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("        ")).append(setZButton.compile());
        }

        if (STATEenableplusZButton) {
            line2 = line2.append(Component.text("      ")).append(plusZButton.compile());
        }else {
            line2 = line2.append(Component.text("      ")).append(plusZButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(plusZButton.hoverText()));
        }

        if (STATEenableminusZButton) {
            line3 = line3.append(Component.text("      ")).append(minusZButton.compile());
        }else {
            line3 = line3.append(Component.text("      ")).append(minusZButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(minusZButton.hoverText()));
        }

        // the step button
        line2 = line2.append(getMovementStepButton());

        content = content.append(line1).appendNewline();
        content = content.append(line2).appendNewline();
        content = content.append(line3);
        return content;
    }

    private Component handleEnabledRotationButton() {
        Component content = Component.empty();
        Component line1 = Component.text("       ");
        Component line2 = Component.text("      ");

        if (!STATEenableplusRollButton && !STATEenableminusRollButton) {
            line1 = line1.append(Component.text("").append(setRollButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(setRollButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("")).append(setRollButton.compile());
        }

        if (STATEenableplusRollButton) {
            line2 = line2.append(Component.text("")).append(plusRollButton.compile());
        }else {
            line2 = line2.append(Component.text("")).append(plusRollButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(plusRollButton.hoverText()));
        }


        if (STATEenableminusRollButton) {
            line2 = line2.appendSpace().append(minusRollButton.compile());
        }else {
            line2 = line2.appendSpace().append(minusRollButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(minusRollButton.hoverText()));
        }

        if (!STATEenableplusPitchButton && !STATEenableminusPitchButton) {
            line1 = line1.append(Component.text("  ").append(setPitchButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(setPitchButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("  ")).append(setPitchButton.compile());
        }

        if (STATEenableplusPitchButton) {
            line2 = line2.append(Component.text("  ")).append(plusPitchButton.compile());
        }else {
            line2 = line2.append(Component.text("  ")).append(plusPitchButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(plusPitchButton.hoverText()));
        }

        if (STATEenableminusPitchButton) {
            line2 = line2.appendSpace().append(minusPitchButton.compile());
        }else {
            line2 = line2.appendSpace().append(minusPitchButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(minusPitchButton.hoverText()));
        }

        if (!STATEenableplusYawButton && !STATEenableminusYawButton) {
            line1 = line1.append(Component.text("   ").append(setYawButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(setYawButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("   ")).append(setYawButton.compile());
        }

        if (STATEenableplusYawButton) {
            line2 = line2.append(Component.text("  ")).append(plusYawButton.compile());
        }else {
            line2 = line2.append(Component.text("  ")).append(plusYawButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(plusYawButton.hoverText()));
        }

        if (STATEenableminusYawButton) {
            line2 = line2.appendSpace().append(minusYawButton.compile());
        }else {
            line2 = line2.appendSpace().append(minusYawButton.text().color(displayOption().colorPalette().DISABLED()).hoverEvent(minusYawButton.hoverText()));
        }

        line2 = line2.append(getRotationStepButton());

        content = content.append(line1).appendNewline();
        content = content.append(line2);
        return content;

    }

    private Component getMovementStepButton() {
        if (!showSetMovementStepButton) return Component.empty();

        Component content = Component.text().resetStyle().build();
        content = content.append(Component.text("      "));
        content = content.append(setMovementStepButton.compile());

        return content;
    }
    private Component getRotationStepButton() {
        if (!showSetRotationStepButton) return Component.empty();

        Component content = Component.text().resetStyle().build();
        content = content.append(Component.text("    "));
        content = content.append(setRotationStepButton.compile());
        return content;
    }

    public void setButtonText() {
        final String increase = "[↑]";
        final String decrease = "[↓]";
        final String left = "[←]";
        final String right = "[→]";
        final String plus = "[+]";
        final String minus = "[-]";

        plusXButton.text(Component.text(left, displayOption().colorPalette().get("X")));
        minusXButton.text(Component.text(right, displayOption().colorPalette().get("X")));
        plusYButton.text(Component.text(increase, displayOption().colorPalette().get("Y")));
        minusYButton.text(Component.text(decrease, displayOption().colorPalette().get("Y")));
        plusZButton.text(Component.text(increase, displayOption().colorPalette().get("Z")));
        minusZButton.text(Component.text(decrease, displayOption().colorPalette().get("Z")));
        plusRollButton.text(Component.text(plus, displayOption().colorPalette().get("X")));
        minusRollButton.text(Component.text(minus, displayOption().colorPalette().get("X")));
        plusPitchButton.text(Component.text(plus, displayOption().colorPalette().get("Y")));
        minusPitchButton.text(Component.text(minus, displayOption().colorPalette().get("Y")));
        plusYawButton.text(Component.text(plus, displayOption().colorPalette().get("Z")));
        minusYawButton.text(Component.text(minus, displayOption().colorPalette().get("Z")));

        setXButton.text(Component.text("X", displayOption().colorPalette().get("X"), TextDecoration.BOLD));
        setYButton.text(Component.text("Y", displayOption().colorPalette().get("Y"), TextDecoration.BOLD));
        setZButton.text(Component.text("Z", displayOption().colorPalette().get("Z"), TextDecoration.BOLD));
        setRollButton.text(Component.text("ROLL", displayOption().colorPalette().get("X"), TextDecoration.BOLD));
        setPitchButton.text(Component.text("PITCH", displayOption().colorPalette().get("Y"), TextDecoration.BOLD));
        setYawButton.text(Component.text("YAW", displayOption().colorPalette().get("Z"), TextDecoration.BOLD));

        setMovementStepButton.text(Component.text("[Step]", displayOption().colorPalette().MODIFIER()));
        setRotationStepButton.text(Component.text("[Step]", displayOption().colorPalette().MODIFIER()));

        exitButton.text(Component.text("[X]", displayOption().colorPalette().RED(), TextDecoration.BOLD));

    }

    public void disableButtons() {
        plusXButton.setDisabled(true);
        minusXButton.setDisabled(true);
        plusYButton.setDisabled(true);
        minusYButton.setDisabled(true);
        plusZButton.setDisabled(true);
        minusZButton.setDisabled(true);
        plusRollButton.setDisabled(true);
        minusRollButton.setDisabled(true);
        plusPitchButton.setDisabled(true);
        minusPitchButton.setDisabled(true);
        plusYawButton.setDisabled(true);
        minusYawButton.setDisabled(true);
        setXButton.setDisabled(true);
        setYButton.setDisabled(true);
        setZButton.setDisabled(true);
        setRollButton.setDisabled(true);
        setPitchButton.setDisabled(true);
        setYawButton.setDisabled(true);
    }

    @Override
    public MovementQuestion clone() {
        MovementQuestion clone = new MovementQuestion();

        // Clone Buttons
        clone.plusXButton = this.plusXButton.clone();
        clone.minusXButton = this.minusXButton.clone();
        clone.plusYButton = this.plusYButton.clone();
        clone.minusYButton = this.minusYButton.clone();
        clone.plusZButton = this.plusZButton.clone();
        clone.minusZButton = this.minusZButton.clone();
        clone.plusRollButton = this.plusRollButton.clone();
        clone.minusRollButton = this.minusRollButton.clone();
        clone.plusPitchButton = this.plusPitchButton.clone();
        clone.minusPitchButton = this.minusPitchButton.clone();
        clone.plusYawButton = this.plusYawButton.clone();
        clone.minusYawButton = this.minusYawButton.clone();
        clone.exitButton = this.exitButton.clone();

        // Clone InputButtons
        clone.setXButton = this.setXButton.clone();
        clone.setYButton = this.setYButton.clone();
        clone.setZButton = this.setZButton.clone();
        clone.setRollButton = this.setRollButton.clone();
        clone.setPitchButton = this.setPitchButton.clone();
        clone.setYawButton = this.setYawButton.clone();
        clone.setMovementStepButton = this.setMovementStepButton.clone();
        clone.setRotationStepButton = this.setRotationStepButton.clone();

        // Copy primitive and boolean fields directly
        clone.showSetMovementStepButton = this.showSetMovementStepButton;
        clone.showSetRotationStepButton = this.showSetRotationStepButton;
        clone.movementStep = this.movementStep;
        clone.rotationStep = this.rotationStep;

        clone.STATEenableplusRollButton = this.STATEenableplusRollButton;
        clone.STATEenableplusPitchButton = this.STATEenableplusPitchButton;
        clone.STATEenableplusYawButton = this.STATEenableplusYawButton;
        clone.STATEenableminusRollButton = this.STATEenableminusRollButton;
        clone.STATEenableminusPitchButton = this.STATEenableminusPitchButton;
        clone.STATEenableminusYawButton = this.STATEenableminusYawButton;

        clone.enableRoll = this.enableRoll;
        clone.enablePitch = this.enablePitch;
        clone.enableYaw = this.enableYaw;
        clone.showRotation = this.showRotation;

        clone.STATEenableplusXButton = this.STATEenableplusXButton;
        clone.STATEenableminusXButton = this.STATEenableminusXButton;
        clone.STATEenableplusYButton = this.STATEenableplusYButton;
        clone.STATEenableminusYButton = this.STATEenableminusYButton;
        clone.STATEenableplusZButton = this.STATEenableplusZButton;
        clone.STATEenableminusZButton = this.STATEenableminusZButton;

        clone.enableXPosition = this.enableXPosition;
        clone.enableYPosition = this.enableYPosition;
        clone.enableZPosition = this.enableZPosition;

        clone.xClamp = this.xClamp.clone();
        clone.yClamp = this.yClamp.clone();
        clone.zClamp = this.zClamp.clone();

        clone.rollClamp = this.rollClamp.clone();
        clone.pitchClamp = this.pitchClamp.clone();
        clone.yawClamp = this.yawClamp.clone();

        clone.xPosition = this.xPosition;
        clone.yPosition = this.yPosition;
        clone.zPosition = this.zPosition;

        clone.roll = this.roll;
        clone.pitch = this.pitch;
        clone.yaw = this.yaw;

        // If you have other fields like question(), footer(), permission(), etc., clone or copy those too
        clone.question(this.question());
        clone.footer(this.footer());
        clone.permission(this.permission());
        clone.syncKey(this.syncKey());
        clone.onReload(this.onReload());
        clone.player(this.player());
        clone.displayOption(this.displayOption());

        return clone;
    }
}
