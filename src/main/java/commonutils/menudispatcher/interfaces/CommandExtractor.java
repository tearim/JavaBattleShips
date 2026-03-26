package commonutils.menudispatcher.interfaces;

import commonutils.menudispatcher.Message;

import java.util.function.Function;

public interface CommandExtractor extends Function<Message<?>, String> { }
