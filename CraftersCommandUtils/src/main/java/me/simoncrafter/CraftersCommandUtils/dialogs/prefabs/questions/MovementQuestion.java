package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.questions;

import me.simoncrafter.CraftersCommandUtils.Clamp;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.*;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions.FloatInputAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.FloatInputButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MovementQuestion extends AbstractQuestion<MovementQuestion> {





    private @NotNull Button plusXButton = Button.create().text(Component.text("[←]", NamedTextColor.RED));  // CONFIGURABLE
    private @NotNull Button minusXButton = Button.create().text(Component.text("[→]", NamedTextColor.RED));  // CONFIGURABLE
    private @NotNull Button plusYButton = Button.create().text(Component.text("[↑]", NamedTextColor.GREEN));  // CONFIGURABLE
    private @NotNull Button minusYButton = Button.create().text(Component.text("[↓]", NamedTextColor.GREEN));  // CONFIGURABLE
    private @NotNull Button plusZButton = Button.create().text(Component.text("[↑]", NamedTextColor.BLUE));  // CONFIGURABLE
    private @NotNull Button minusZButton = Button.create().text(Component.text("[↓]", NamedTextColor.BLUE));  // CONFIGURABLE
    private @NotNull Button plusRollButton = Button.create().text(Component.text("[+]", NamedTextColor.RED));  // CONFIGURABLE
    private @NotNull Button minusRollButton = Button.create().text(Component.text("[-]", NamedTextColor.RED));  // CONFIGURABLE
    private @NotNull Button plusPitchButton = Button.create().text(Component.text("[+]", NamedTextColor.GREEN));  // CONFIGURABLE
    private @NotNull Button minusPitchButton = Button.create().text(Component.text("[-]", NamedTextColor.GREEN));  // CONFIGURABLE
    private @NotNull Button plusYawButton = Button.create().text(Component.text("[+]", NamedTextColor.BLUE));  // CONFIGURABLE
    private @NotNull Button minusYawButton = Button.create().text(Component.text("[-]", NamedTextColor.BLUE));  // CONFIGURABLE

    private @NotNull Button exitButton = Button.create().text(Component.text("[X]", NamedTextColor.RED)).addAction(exitAction).addAction(ClearCharAction.create()).addAction(MessageAction.create(Component.text("Exited Movement Dialog", NamedTextColor.RED)));  // CONFIGURABLE
    
    private @NotNull FloatInputButton setXButton = FloatInputButton.create().text(Component.text("X", NamedTextColor.RED, TextDecoration.BOLD));  // CONFIGURABLE
    private @NotNull FloatInputButton setYButton = FloatInputButton.create().text(Component.text("Y", NamedTextColor.GREEN, TextDecoration.BOLD));  // CONFIGURABLE
    private @NotNull FloatInputButton setZButton = FloatInputButton.create().text(Component.text("Z", NamedTextColor.BLUE, TextDecoration.BOLD));  // CONFIGURABLE

    private @NotNull FloatInputButton setRollButton = FloatInputButton.create().text(Component.text("ROLL", NamedTextColor.RED, TextDecoration.BOLD));  // CONFIGURABLE
    private @NotNull FloatInputButton setPitchButton = FloatInputButton.create().text(Component.text("PITCH", NamedTextColor.GREEN, TextDecoration.BOLD));  // CONFIGURABLE
    private @NotNull FloatInputButton setYawButton = FloatInputButton.create().text(Component.text("YAW", NamedTextColor.BLUE, TextDecoration.BOLD));  // CONFIGURABLE


    //enabled states, config and clamping

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
    private Clamp<Float> xClamp = Clamp.create(Float.class);  // CONFIGURABLE
    private Clamp<Float> yClamp = Clamp.create(Float.class);  // CONFIGURABLE
    private Clamp<Float> zClamp = Clamp.create(Float.class);  // CONFIGURABLE

    // POSITION DATA
    private float xPosition = 0;  // CONFIGURABLE
    private float yPosition = 0;  // CONFIGURABLE
    private float zPosition = 0;  // CONFIGURABLE

    // ROTATION DATA
    private float roll = 0;  // CONFIGURABLE
    private float pitch = 0;  // CONFIGURABLE
    private float yaw = 0;  // CONFIGURABLE


    public MovementQuestion() {
    }

    @Contract(value = "-> new")
    public static MovementQuestion create() {
        return new MovementQuestion();
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
    public MovementQuestion xPosition(float xPosition) {
        this.xPosition = xPosition;
        return this;
    }
    public float xPosition() {
        return xPosition;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion yPosition(float yPosition) {
        this.yPosition = yPosition;
        return this;
    }

    public float yPosition() {
        return yPosition;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion zPosition(float zPosition) {
        this.zPosition = zPosition;
        return this;
    }
    public float zPosition() {
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
    public MovementQuestion setLocation(float xPosition, float yPosition, float zPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
        return this;
    }
    @Contract(value = "_, _, _, _, _, _ -> this", mutates = "this")
    public MovementQuestion setLocation(float xPosition, float yPosition, float zPosition, float roll, float pitch, float yaw) {
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
        this.xPosition = (float) location.getX();
        this.yPosition = (float) location.getY();
        this.zPosition = (float) location.getZ();
        return this;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setLocationAndRotation(Location location) {
        this.xPosition = (float) location.getX();
        this.yPosition = (float) location.getY();
        this.zPosition = (float) location.getZ();
        this.roll =  location.getPitch();
        this.pitch = location.getPitch();
        this.yaw =   location.getYaw();
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
    public MovementQuestion setSetXButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setXButton.floatInputButton(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetXButtonAction(FloatInputAction onClick) {
        setXButton.floatInputButton(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setXButtonAction(FloatInputButton button) {
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
    public FloatInputButton setXButton() {
        return setXButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setYButton.floatInputButton(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYButtonAction(FloatInputAction onClick) {
        setYButton.floatInputButton(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setYButtonAction(FloatInputButton button) {
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

    public FloatInputButton setYButton() {
        return setYButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetZButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setZButton.floatInputButton(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetZButtonAction(FloatInputAction onClick) {
        setZButton.floatInputButton(onClick);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setZButtonAction(FloatInputButton button) {
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
    public FloatInputButton setZButton() {
        return setZButton;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetRollButtonAction(@NotNull Function<Player, Consumer<Float>> onClick) {
        setRollButton.floatInputButton(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetRollButtonAction(FloatInputAction onClick) {
        setRollButton.floatInputButton(onClick);
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
        setPitchButton.floatInputButton(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetPitchButtonAction(FloatInputAction onClick) {
        setPitchButton.floatInputButton(onClick);
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
        setYawButton.floatInputButton(FloatInputAction.create(onClick));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion setSetYawButtonAction(FloatInputAction onClick) {
        setYawButton.floatInputButton(onClick);
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

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToRotationButtons(IAction action) {
        plusRollButton.addAction(action);
        plusPitchButton.addAction(action);
        plusYawButton.addAction(action);
        minusRollButton.addAction(action);
        minusPitchButton.addAction(action);
        minusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromRotationButtons(IAction action) {
        plusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        minusRollButton.removeAction(action);
        minusPitchButton.removeAction(action);
        minusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToPlusRotationButtons(IAction action) {
        plusRollButton.addAction(action);
        plusPitchButton.addAction(action);
        plusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromPlusRotationButtons(IAction action) {
        plusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToMinusRotationButtons(IAction action) {
        minusRollButton.addAction(action);
        minusPitchButton.addAction(action);
        minusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromMinusRotationButtons(IAction action) {
        minusRollButton.removeAction(action);
        minusPitchButton.removeAction(action);
        minusYawButton.removeAction(action);
        return this;
    }



    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToRollButtons(IAction action) {
        List<IAction> actions = plusRollButton.actions();
        actions.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromRollButtons(IAction action) {
        List<IAction> actions = plusRollButton.actions();
        actions.remove(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToPitchButtons(IAction action) {
        List<IAction> actions = plusPitchButton.actions();
        actions.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromPitchButtons(IAction action) {
        List<IAction> actions = plusPitchButton.actions();
        actions.remove(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToYawButtons(IAction action) {
        List<IAction> actions = plusYawButton.actions();
        actions.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromYawButtons(IAction action) {
        List<IAction> actions = plusYawButton.actions();
        actions.remove(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToXButtons(IAction action) {
        List<IAction> actions = plusXButton.actions();
        actions.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromXButtons(IAction action) {
        List<IAction> actions = plusXButton.actions();
        actions.remove(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToYButtons(IAction action) {
        List<IAction> actions = plusYButton.actions();
        actions.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromYButtons(IAction action) {
        List<IAction> actions = plusYButton.actions();
        actions.remove(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToZButtons(IAction action) {
        List<IAction> actions = plusZButton.actions();
        actions.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromZButtons(IAction action) {
        List<IAction> actions = plusZButton.actions();
        actions.remove(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToMovementButtons(IAction action) {
        plusXButton.addAction(action);
        minusXButton.addAction(action);
        plusYButton.addAction(action);
        minusYButton.addAction(action);
        plusZButton.addAction(action);
        minusZButton.addAction(action);
        plusRollButton.addAction(action);
        minusRollButton.addAction(action);
        plusPitchButton.addAction(action);
        minusPitchButton.addAction(action);
        plusYawButton.addAction(action);
        minusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromMovementButtons(IAction action) {
        plusXButton.removeAction(action);
        minusXButton.removeAction(action);
        plusYButton.removeAction(action);
        minusYButton.removeAction(action);
        plusZButton.removeAction(action);
        minusZButton.removeAction(action);
        plusRollButton.removeAction(action);
        minusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        minusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        minusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToMinusMovementButtons(IAction action) {
        minusXButton.addAction(action);
        minusYButton.addAction(action);
        minusZButton.addAction(action);
        minusRollButton.addAction(action);
        minusPitchButton.addAction(action);
        minusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromMinusMovementButtons(IAction action) {
        minusXButton.removeAction(action);
        minusYButton.removeAction(action);
        minusZButton.removeAction(action);
        minusRollButton.removeAction(action);
        minusPitchButton.removeAction(action);
        minusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToPlusMovementButtons(IAction action) {
        plusXButton.addAction(action);
        plusYButton.addAction(action);
        plusZButton.addAction(action);
        plusRollButton.addAction(action);
        plusPitchButton.addAction(action);
        plusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromPlusMovementButtons(IAction action) {
        plusXButton.removeAction(action);
        plusYButton.removeAction(action);
        plusZButton.removeAction(action);
        plusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToAllOperatorButtons(IAction action) {
        plusXButton.addAction(action);
        minusXButton.addAction(action);
        plusYButton.addAction(action);
        minusYButton.addAction(action);
        plusZButton.addAction(action);
        minusZButton.addAction(action);
        plusRollButton.addAction(action);
        minusRollButton.addAction(action);
        plusPitchButton.addAction(action);
        minusPitchButton.addAction(action);
        plusYawButton.addAction(action);
        minusYawButton.addAction(action);

        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromAllOperatorButtons(IAction action) {
        plusXButton.removeAction(action);
        minusXButton.removeAction(action);
        plusYButton.removeAction(action);
        minusYButton.removeAction(action);
        plusZButton.removeAction(action);
        minusZButton.removeAction(action);
        plusRollButton.removeAction(action);
        minusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        minusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        minusYawButton.removeAction(action);
        plusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        minusRollButton.removeAction(action);
        minusPitchButton.removeAction(action);
        minusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToAllSetButtons(IAction action) {
        setXButton.addAction(action);
        setYButton.addAction(action);
        setZButton.addAction(action);
        setRollButton.addAction(action);
        setPitchButton.addAction(action);
        setYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromAllSetButtons(IAction action) {
        setXButton.addAction(action);
        setYButton.addAction(action);
        setZButton.addAction(action);
        setRollButton.addAction(action);
        setPitchButton.addAction(action);
        setYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToAllMovementOperatorButtons(IAction action) {
        plusXButton.addAction(action);
        minusXButton.addAction(action);
        plusYButton.addAction(action);
        minusYButton.addAction(action);
        plusZButton.addAction(action);
        minusZButton.addAction(action);

        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromAllMovementOperatorButtons(IAction action) {
        plusXButton.removeAction(action);
        minusXButton.removeAction(action);
        plusYButton.removeAction(action);
        minusYButton.removeAction(action);
        plusZButton.removeAction(action);
        minusZButton.removeAction(action);

        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToAllRotationOperatorButtons(IAction action) {
        plusRollButton.addAction(action);
        minusRollButton.addAction(action);
        plusPitchButton.addAction(action);
        minusPitchButton.addAction(action);
        plusYawButton.addAction(action);
        minusYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromAllRotationOperatorButtons(IAction action) {
        plusRollButton.removeAction(action);
        minusRollButton.removeAction(action);
        plusPitchButton.removeAction(action);
        minusPitchButton.removeAction(action);
        plusYawButton.removeAction(action);
        minusYawButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToAllSetMovementButtons(IAction action) {
        setXButton.addAction(action);
        setYButton.addAction(action);
        setZButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromAllSetMovementButtons(IAction action) {
        setXButton.removeAction(action);
        setYButton.removeAction(action);
        setZButton.removeAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion addActionToAllSetRotationButtons(IAction action) {
        setRollButton.addAction(action);
        setPitchButton.addAction(action);
        setYawButton.addAction(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion removeActionFromAllSetRotationButtons(IAction action) {
        setRollButton.removeAction(action);
        setPitchButton.removeAction(action);
        setYawButton.removeAction(action);
        return this;
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
    public MovementQuestion xClamp(Clamp<Float> clamp) {
        this.xClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion xClamp(float lower, float upper) {
        this.xClamp = Clamp.create(Float.class, lower, upper);
        return this;
    }

    public Clamp<Float> xClamp() {
        return this.xClamp;
    }

    // --- Y ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion yClamp(Clamp<Float> clamp) {
        this.yClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion yClamp(float lower, float upper) {
        this.yClamp = Clamp.create(Float.class, lower, upper);
        return this;
    }

    public Clamp<Float> yClamp() {
        return this.yClamp;
    }

    // --- Z ---

    @Contract(value = "_ -> this", mutates = "this")
    public MovementQuestion zClamp(Clamp<Float> clamp) {
        this.zClamp = clamp;
        return this;
    }

    @Contract(value = "_, _ -> this", mutates = "this")
    public MovementQuestion zClamp(float lower, float upper) {
        this.zClamp = Clamp.create(Float.class, lower, upper);
        return this;
    }

    public Clamp<Float> zClamp() {
        return this.zClamp;
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


        Component content = Component.text().resetStyle().build();


        // add disable action
        addDefualtOnClickFunctions(player);


        content = content.append(Component.text("Movement Options   ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(exitButton.compile()));
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



        setXButton.floatInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setYButton.setDisabled(true);
            setZButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setPitchButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setYButton.floatInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
            setXButton.setDisabled(true);
            setZButton.setDisabled(true);
            setRollButton.setDisabled(true);
            setPitchButton.setDisabled(true);
            setYawButton.setDisabled(true);
        }));
        setZButton.floatInputAction().addPostAction(disableAction).addPreAction(CustomAction.create(p -> {
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

        setXButton.floatInputButton(setXButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setYButton.floatInputButton(setYButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setZButton.floatInputButton(setZButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setRollButton.floatInputButton(setRollButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setPitchButton.floatInputButton(setPitchButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
        setYawButton.floatInputButton(setYawButton.floatInputAction().addSuccessAction(reloadAction).addTimeoutAction(reloadAction).maxResponseTime(60).addCancelAction(reloadAction).reTry(true));
    }

    private void addHoverTextToButtons() {
        plusXButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(xPosition, NamedTextColor.RED, TextDecoration.BOLD)));
        minusXButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(xPosition, NamedTextColor.RED, TextDecoration.BOLD)));
        plusYButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(yPosition, NamedTextColor.GREEN, TextDecoration.BOLD)));
        minusYButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(yPosition, NamedTextColor.GREEN, TextDecoration.BOLD)));
        plusZButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(zPosition, NamedTextColor.BLUE, TextDecoration.BOLD)));
        minusZButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(zPosition, NamedTextColor.BLUE, TextDecoration.BOLD)));
        plusRollButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(roll, NamedTextColor.RED, TextDecoration.BOLD)));
        minusRollButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(roll, NamedTextColor.RED, TextDecoration.BOLD)));
        plusPitchButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(pitch, NamedTextColor.GREEN, TextDecoration.BOLD)));
        minusPitchButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(pitch, NamedTextColor.GREEN, TextDecoration.BOLD)));
        plusYawButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(yaw, NamedTextColor.BLUE, TextDecoration.BOLD)));
        minusYawButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(yaw, NamedTextColor.BLUE, TextDecoration.BOLD)));
        setXButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(xPosition, NamedTextColor.RED, TextDecoration.BOLD)));
        setYButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(yPosition, NamedTextColor.GREEN, TextDecoration.BOLD)));
        setZButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(zPosition, NamedTextColor.BLUE, TextDecoration.BOLD)));
        setRollButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(roll, NamedTextColor.RED, TextDecoration.BOLD)));
        setPitchButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(pitch, NamedTextColor.GREEN, TextDecoration.BOLD)));
        setYawButton.hoverText(Component.text("Value: ", NamedTextColor.YELLOW, TextDecoration.BOLD).append(Component.text(yaw, NamedTextColor.BLUE, TextDecoration.BOLD)));
    }

    private Component handleEnabledMovementButton() {
        Component content = Component.empty();
        Component line1 = Component.text("         ");
        Component line2 = Component.text("        ");
        Component line3 = Component.text("        ");


        if (!STATEenableplusXButton && !STATEenableminusXButton) {
            line1 = line1.append(setXButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(setXButton.hoverText()));
        }else {
            line1 = line1.append(setXButton.compile());
        }

        if (STATEenableplusXButton) {
            line2 = line2.append(Component.text("")).append(plusXButton.compile());
        }else {
            line2 = line2.append(Component.text("")).append(plusXButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(plusXButton.hoverText()));
        }

        if (STATEenableminusXButton) {
            line3 = line3.append(Component.text("")).append(minusXButton.compile());
        }else {
            line3 = line3.append(Component.text("")).append(minusXButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(minusXButton.hoverText()));
        }


        if (!STATEenableplusYButton && !STATEenableminusYButton) {
            line1 = line1.append(Component.text("         ").append(setYButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(setYButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("         ")).append(setYButton.compile());
        }

        if (STATEenableplusYButton) {
            line2 = line2.append(Component.text("       ")).append(plusYButton.compile());
        }else {
            line2 = line2.append(Component.text("       ")).append(plusYButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(plusYButton.hoverText()));
        }

        if (STATEenableminusYButton) {
            line3 = line3.append(Component.text("       ")).append(minusYButton.compile());
        }else {
            line3 = line3.append(Component.text("       ")).append(minusYButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(minusYButton.hoverText()));
        }



        if (!STATEenableplusZButton && !STATEenableminusZButton) {
            line1 = line1.append(Component.text("        ").append(setZButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(setZButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("        ")).append(setZButton.compile());
        }

        if (STATEenableplusZButton) {
            line2 = line2.append(Component.text("      ")).append(plusZButton.compile());
        }else {
            line2 = line2.append(Component.text("      ")).append(plusZButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(plusZButton.hoverText()));
        }

        if (STATEenableminusZButton) {
            line3 = line3.append(Component.text("      ")).append(minusZButton.compile());
        }else {
            line3 = line3.append(Component.text("      ")).append(minusZButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(minusZButton.hoverText()));
        }

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
            line1 = line1.append(Component.text("").append(setRollButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(setRollButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("")).append(setRollButton.compile());
        }

        if (STATEenableplusRollButton) {
            line2 = line2.append(Component.text("")).append(plusRollButton.compile());
        }else {
            line2 = line2.append(Component.text("")).append(plusRollButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(plusRollButton.hoverText()));
        }

        if (STATEenableminusRollButton) {
            line2 = line2.appendSpace().append(minusRollButton.compile());
        }else {
            line2 = line2.appendSpace().append(minusRollButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(minusRollButton.hoverText()));
        }

        if (!STATEenableplusPitchButton && !STATEenableminusPitchButton) {
            line1 = line1.append(Component.text("  ").append(setPitchButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(setPitchButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("  ")).append(setPitchButton.compile());
        }

        if (STATEenableplusPitchButton) {
            line2 = line2.append(Component.text("  ")).append(plusPitchButton.compile());
        }else {
            line2 = line2.append(Component.text("  ")).append(plusPitchButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(plusPitchButton.hoverText()));
        }

        if (STATEenableminusPitchButton) {
            line2 = line2.appendSpace().append(minusPitchButton.compile());
        }else {
            line2 = line2.appendSpace().append(minusPitchButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(minusPitchButton.hoverText()));
        }

        if (!STATEenableplusYawButton && !STATEenableminusYawButton) {
            line1 = line1.append(Component.text("   ").append(setYawButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(setYawButton.hoverText())));
        }else {
            line1 = line1.append(Component.text("   ")).append(setYawButton.compile());
        }

        if (STATEenableplusYawButton) {
            line2 = line2.append(Component.text("  ")).append(plusYawButton.compile());
        }else {
            line2 = line2.append(Component.text("  ")).append(plusYawButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(plusYawButton.hoverText()));
        }

        if (STATEenableminusYawButton) {
            line2 = line2.appendSpace().append(minusYawButton.compile());
        }else {
            line2 = line2.appendSpace().append(minusYawButton.text().color(NamedTextColor.DARK_GRAY).hoverEvent(minusYawButton.hoverText()));
        }

        content = content.append(line1).appendNewline();
        content = content.append(line2);
        return content;

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


}
