package de.geoinfoffm.registry.api;

import java.util.Map;
import java.util.concurrent.Callable;

public interface ParameterizedCallable<T> extends Callable<T>
{
	Map<String, Object> getParameters();
}
