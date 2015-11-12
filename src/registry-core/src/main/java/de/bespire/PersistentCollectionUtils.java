package de.bespire;

import java.util.Collection;

import org.hibernate.collection.internal.AbstractPersistentCollection;

public abstract class PersistentCollectionUtils
{
	public static boolean equals(Collection<?> a, Collection<?> b) {
		if (a == null && b != null || a != null && b == null) {
			return false;
		}
		else if (a == null && b == null) {
			return true;
		}
		
		Collection<?> collectionA = a;
		Collection<?> collectionB = b;
		
		if (a instanceof AbstractPersistentCollection) {
			AbstractPersistentCollection bag = (AbstractPersistentCollection)a;
			if (bag.getStoredSnapshot() instanceof Collection) {
				collectionA = (Collection<?>)bag.getStoredSnapshot();
			}
		}
		else if (b instanceof AbstractPersistentCollection) {
			AbstractPersistentCollection bag = (AbstractPersistentCollection)b;
			if (bag.getStoredSnapshot() instanceof Collection) {
				collectionB = (Collection<?>)bag.getStoredSnapshot();
			}
		}
		
		return collectionA.equals(collectionB);
	}
}
