@TypeDef(
        name = "pg-uuid",
        defaultForType = UUID.class,
        typeClass = PostgresUUIDType.class
)
package de.geoinfoffm.registry.persistence;

import java.util.UUID;

import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;

