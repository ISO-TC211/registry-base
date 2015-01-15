package de.geoinfoffm.registry.core;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.xml.bind.annotation.XmlAccessType;

@Inherited @Retention(RUNTIME) @Target({ FIELD, METHOD })
public @interface XmlAccessor
{
    XmlAccessType value();
}
