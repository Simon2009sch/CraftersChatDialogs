package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringWithRulesInputAction extends RetryableInputAction<StringWithRulesInputAction> {

    private Function<Player, Consumer<String>> onResponse = p -> response -> {};
    private String regexRule = ".*";

    public StringWithRulesInputAction() {
    }

    public StringWithRulesInputAction(Function<Player, Consumer<String>> onResponse) {
        this.onResponse = onResponse;
    }

    public static StringWithRulesInputAction create(Function<Player, Consumer<String>> onResponse) {
        return new StringWithRulesInputAction(onResponse);
    }
    public static StringWithRulesInputAction create() {
        return new StringWithRulesInputAction();
    }

    @Contract(value = "_ -> new", mutates = "this")
    public StringWithRulesInputAction regexRule(String regexRule) {
        this.regexRule = regexRule;
        return this;
    }
    public String regexRule() {
        return regexRule;
    }

    public StringWithRulesInputAction onResponse(Function<Player, Consumer<String>> onResponse) {
        this.onResponse = onResponse;
        return this;
    }
    public Function<Player, Consumer<String>> onResponse() {
        return onResponse;
    }

    @Override
    protected void onResponse(Player player, String message) {
        String out;
        Pattern rules = Pattern.compile(regexRule);
        Matcher matcher = rules.matcher(message);
        if (!matcher.matches()) {
            handleRetry(player);
        } else {
            out = message;
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }
    }

    @Override
    public StringWithRulesInputAction clone() {
        StringWithRulesInputAction clone = new StringWithRulesInputAction(onResponse);
        clone.setPreActions(preActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setTimeoutActions(timeoutActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setCancelActions(cancelActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.messageAction(messageAction().clone());

        clone.successActions(successActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTryActions(reTryActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTry(reTry());

        clone.maxResponseTime(maxResponseTime());
        clone.syncKey(syncKey());
        clone.setDisabled(isDisabled());
        clone.prompt(prompt());
        clone.reTryMessage(reTryMessage());
        clone.displayOption(displayOption());



        return clone;
    }
}
