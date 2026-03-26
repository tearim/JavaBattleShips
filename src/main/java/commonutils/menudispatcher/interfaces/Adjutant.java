package commonutils.menudispatcher.interfaces;

import commonutils.menudispatcher.Message;

import java.util.function.Function;

public interface Adjutant extends Function<String, Message<?>> { }
