package de.geoinfoffm.registry.core.security;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.ChildrenExistException;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

public class RegistryMutableAclService extends RegistryAclService implements MutableAclService
{
    //~ Instance fields ================================================================================================

    private boolean foreignKeysInDatabase = true;
    private final AclCache aclCache;
    private String deleteEntryByObjectIdentityForeignKey = "delete from acl_entry where acl_object_identity=?";
    private String deleteObjectIdentityByPrimaryKey = "delete from acl_object_identity where uuid=?";
//    private String classIdentityQuery = "call identity()";
//    private String sidIdentityQuery = "call identity()";
    private String insertClass = "insert into acl_class (uuid, class) values (?, ?)";
    private String insertEntry = "insert into acl_entry "
        + "(uuid, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)"
        + "values (?, ?, ?, ?, ?, ?, ?, ?)";
    private String insertObjectIdentity = "insert into acl_object_identity "
        + "(uuid, object_id_class, object_id_identity, owner_sid, entries_inheriting) " + "values (?, ?, ?, ?, ?)";
    private String insertSid = "insert into acl_sid (uuid, principal, sid) values (?, ?, ?)";
    private String selectClassPrimaryKey = "select uuid from acl_class where class=?";
    private String selectObjectIdentityPrimaryKey = "select acl_object_identity.uuid from acl_object_identity, acl_class "
        + "where acl_object_identity.object_id_class = acl_class.uuid and acl_class.class=? "
        + "and acl_object_identity.object_id_identity = ?";
    private String selectSidPrimaryKey = "select uuid from acl_sid where principal=? and sid=?";
    private String updateObjectIdentity = "update acl_object_identity set "
        + "parent_object = ?, owner_sid = ?, entries_inheriting = ?" + " where uuid = ?";

    //~ Constructors ===================================================================================================

    public RegistryMutableAclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
        super(dataSource, lookupStrategy);
        Assert.notNull(aclCache, "AclCache required");
        this.aclCache = aclCache;
    }

    //~ Methods ========================================================================================================

    public MutableAcl createAcl(ObjectIdentity objectIdentity) throws AlreadyExistsException {
        Assert.notNull(objectIdentity, "Object Identity required");

        // Check this object identity hasn't already been persisted
        if (retrieveObjectIdentityPrimaryKey(objectIdentity) != null) {
            throw new AlreadyExistsException("Object identity '" + objectIdentity + "' already exists");
        }

        // Need to retrieve the current principal, in order to know who "owns" this ACL (can be changed later on)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid sid = new PrincipalSid(auth);

        // Create the acl_object_identity row
        createObjectIdentity(objectIdentity, sid);

        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval etc)
        Acl acl = readAclById(objectIdentity);
        Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");

        return (MutableAcl) acl;
    }

    /**
     * Creates a new row in acl_entry for every ACE defined in the passed MutableAcl object.
     *
     * @param acl containing the ACEs to insert
     */
    protected void createEntries(final MutableAcl acl) {
        if(acl.getEntries().isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate(insertEntry,
            new BatchPreparedStatementSetter() {
                public int getBatchSize() {
                    return acl.getEntries().size();
                }

                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    AccessControlEntry entry_ = acl.getEntries().get(i);
                    Assert.isTrue(entry_ instanceof AccessControlEntryImpl, "Unknown ACE class");
                    AccessControlEntryImpl entry = (AccessControlEntryImpl) entry_;

                    stmt.setObject(1, UUID.randomUUID());
                    stmt.setObject(2, acl.getId());
                    stmt.setInt(3, i);
                    stmt.setObject(4, createOrRetrieveSidPrimaryKey(entry.getSid(), true));
                    stmt.setInt(5, entry.getPermission().getMask());
                    stmt.setBoolean(6, entry.isGranting());
                    stmt.setBoolean(7, entry.isAuditSuccess());
                    stmt.setBoolean(8, entry.isAuditFailure());
                }
            });
    }

    /**
     * Creates an entry in the acl_object_identity table for the passed ObjectIdentity. The Sid is also
     * necessary, as acl_object_identity has defined the sid column as non-null.
     *
     * @param object to represent an acl_object_identity for
     * @param owner for the SID column (will be created if there is no acl_sid entry for this particular Sid already)
     */
    protected void createObjectIdentity(ObjectIdentity object, Sid owner) {
        UUID sidId = createOrRetrieveSidPrimaryKey(owner, true);
        UUID classId = createOrRetrieveClassPrimaryKey(object.getType(), true);
        jdbcTemplate.update(insertObjectIdentity, UUID.randomUUID(), classId, object.getIdentifier(), sidId, Boolean.TRUE);
    }

    /**
     * Retrieves the primary key from {@code acl_class}, creating a new row if needed and the
     * {@code allowCreate} property is {@code true}.
     *
     * @param type to find or create an entry for (often the fully-qualified class name)
     * @param allowCreate true if creation is permitted if not found
     *
     * @return the primary key or null if not found
     */
    protected UUID createOrRetrieveClassPrimaryKey(String type, boolean allowCreate) {
        List<UUID> classIds = jdbcTemplate.queryForList(selectClassPrimaryKey, new Object[] {type}, UUID.class);

        if (!classIds.isEmpty()) {
            return classIds.get(0);
        }

        if (allowCreate) {
        	UUID uuid = UUID.randomUUID();
        	
            jdbcTemplate.update(insertClass, uuid, type);
            Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(),
                    "Transaction must be running");
            return uuid;
        }

        return null;
    }

    /**
     * Retrieves the primary key from acl_sid, creating a new row if needed and the allowCreate property is
     * true.
     *
     * @param sid to find or create
     * @param allowCreate true if creation is permitted if not found
     *
     * @return the primary key or null if not found
     *
     * @throws IllegalArgumentException if the <tt>Sid</tt> is not a recognized implementation.
     */
    protected UUID createOrRetrieveSidPrimaryKey(Sid sid, boolean allowCreate) {
        Assert.notNull(sid, "Sid required");

        String sidName;
        boolean sidIsPrincipal = true;

        if (sid instanceof PrincipalSid) {
            sidName = ((PrincipalSid) sid).getPrincipal();
        } else if (sid instanceof GrantedAuthoritySid) {
            sidName = ((GrantedAuthoritySid) sid).getGrantedAuthority();
            sidIsPrincipal = false;
        } else {
            throw new IllegalArgumentException("Unsupported implementation of Sid");
        }

        List<UUID> sidIds = jdbcTemplate.queryForList(selectSidPrimaryKey,
                new Object[] {Boolean.valueOf(sidIsPrincipal), sidName},  UUID.class);

        if (!sidIds.isEmpty()) {
            return sidIds.get(0);
        }

        if (allowCreate) { 
        	UUID uuid = UUID.randomUUID();
            jdbcTemplate.update(insertSid, uuid, Boolean.valueOf(sidIsPrincipal), sidName);
            Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), "Transaction must be running");
            return uuid;
        }

        return null;
    }

    public void deleteAcl(ObjectIdentity objectIdentity, boolean deleteChildren) throws ChildrenExistException {
        Assert.notNull(objectIdentity, "Object Identity required");
        Assert.notNull(objectIdentity.getIdentifier(), "Object Identity doesn't provide an identifier");

        if (deleteChildren) {
            List<ObjectIdentity> children = findChildren(objectIdentity);
            if (children != null) {
                for (ObjectIdentity child : children) {
                    deleteAcl(child, true);
                }
            }
        } else {
            if (!foreignKeysInDatabase) {
                // We need to perform a manual verification for what a FK would normally do
                // We generally don't do this, in the interests of deadlock management
                List<ObjectIdentity> children = findChildren(objectIdentity);
                if (children != null) {
                    throw new ChildrenExistException("Cannot delete '" + objectIdentity + "' (has " + children.size()
                            + " children)");
                }
            }
        }

        UUID oidPrimaryKey = retrieveObjectIdentityPrimaryKey(objectIdentity);

        // Delete this ACL's ACEs in the acl_entry table
        deleteEntries(oidPrimaryKey);

        // Delete this ACL's acl_object_identity row
        deleteObjectIdentity(oidPrimaryKey);

        // Clear the cache
        aclCache.evictFromCache(objectIdentity);
    }

    /**
     * Deletes all ACEs defined in the acl_entry table belonging to the presented ObjectIdentity primary key.
     *
     * @param oidPrimaryKey the rows in acl_entry to delete
     */
    protected void deleteEntries(UUID oidPrimaryKey) {
        jdbcTemplate.update(deleteEntryByObjectIdentityForeignKey, oidPrimaryKey);
    }

    /**
     * Deletes a single row from acl_object_identity that is associated with the presented ObjectIdentity primary key.
     * <p>
     * We do not delete any entries from acl_class, even if no classes are using that class any longer. This is a
     * deadlock avoidance approach.
     *
     * @param oidPrimaryKey to delete the acl_object_identity
     */
    protected void deleteObjectIdentity(UUID oidPrimaryKey) {
        // Delete the acl_object_identity row
        jdbcTemplate.update(deleteObjectIdentityByPrimaryKey, oidPrimaryKey);
    }

    /**
     * Retrieves the primary key from the acl_object_identity table for the passed ObjectIdentity. Unlike some
     * other methods in this implementation, this method will NOT create a row (use {@link
     * #createObjectIdentity(ObjectIdentity, Sid)} instead).
     *
     * @param oid to find
     *
     * @return the object identity or null if not found
     */
    protected UUID retrieveObjectIdentityPrimaryKey(ObjectIdentity oid) {
        try {
            return jdbcTemplate.queryForObject(selectObjectIdentityPrimaryKey, new Object[] { oid.getType(), oid.getIdentifier() }, UUID.class);
        } catch (DataAccessException notFound) {
            return null;
        }
    }

    /**
     * This implementation will simply delete all ACEs in the database and recreate them on each invocation of
     * this method. A more comprehensive implementation might use dirty state checking, or more likely use ORM
     * capabilities for create, update and delete operations of {@link MutableAcl}.
     */
    public MutableAcl updateAcl(MutableAcl acl) throws NotFoundException {
        Assert.notNull(acl.getId(), "Object Identity doesn't provide an identifier");

        // Delete this ACL's ACEs in the acl_entry table
        deleteEntries(retrieveObjectIdentityPrimaryKey(acl.getObjectIdentity()));

        // Create this ACL's ACEs in the acl_entry table
        createEntries(acl);

        // Change the mutable columns in acl_object_identity
        updateObjectIdentity(acl);

        // Clear the cache, including children
        clearCacheIncludingChildren(acl.getObjectIdentity());

        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval etc)
        return (MutableAcl) super.readAclById(acl.getObjectIdentity());
    }

    private void clearCacheIncludingChildren(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "ObjectIdentity required");
        List<ObjectIdentity> children = findChildren(objectIdentity);
        if (children != null) {
            for (ObjectIdentity child : children) {
                clearCacheIncludingChildren(child);
            }
        }
        aclCache.evictFromCache(objectIdentity);
    }

    /**
     * Updates an existing acl_object_identity row, with new information presented in the passed MutableAcl
     * object. Also will create an acl_sid entry if needed for the Sid that owns the MutableAcl.
     *
     * @param acl to modify (a row must already exist in acl_object_identity)
     *
     * @throws NotFoundException if the ACL could not be found to update.
     */
    protected void updateObjectIdentity(MutableAcl acl) {
        UUID parentId = null;

        if (acl.getParentAcl() != null) {
            Assert.isInstanceOf(ObjectIdentityImpl.class, acl.getParentAcl().getObjectIdentity(),
                "Implementation only supports ObjectIdentityImpl");

            ObjectIdentityImpl oii = (ObjectIdentityImpl) acl.getParentAcl().getObjectIdentity();
            parentId = retrieveObjectIdentityPrimaryKey(oii);
        }

        Assert.notNull(acl.getOwner(), "Owner is required in this implementation");

        UUID ownerSid = createOrRetrieveSidPrimaryKey(acl.getOwner(), true);
        int count = jdbcTemplate.update(updateObjectIdentity,
                parentId, ownerSid, Boolean.valueOf(acl.isEntriesInheriting()), acl.getId());

        if (count != 1) {
            throw new NotFoundException("Unable to locate ACL to update");
        }
    }

    public void setDeleteEntryByObjectIdentityForeignKeySql(String deleteEntryByObjectIdentityForeignKey) {
        this.deleteEntryByObjectIdentityForeignKey = deleteEntryByObjectIdentityForeignKey;
    }

    public void setDeleteObjectIdentityByPrimaryKeySql(String deleteObjectIdentityByPrimaryKey) {
        this.deleteObjectIdentityByPrimaryKey = deleteObjectIdentityByPrimaryKey;
    }

    public void setInsertClassSql(String insertClass) {
        this.insertClass = insertClass;
    }

    public void setInsertEntrySql(String insertEntry) {
        this.insertEntry = insertEntry;
    }

    public void setInsertObjectIdentitySql(String insertObjectIdentity) {
        this.insertObjectIdentity = insertObjectIdentity;
    }

    public void setInsertSidSql(String insertSid) {
        this.insertSid = insertSid;
    }

    public void setClassPrimaryKeyQuery(String selectClassPrimaryKey) {
        this.selectClassPrimaryKey = selectClassPrimaryKey;
    }

    public void setObjectIdentityPrimaryKeyQuery(String selectObjectIdentityPrimaryKey) {
        this.selectObjectIdentityPrimaryKey = selectObjectIdentityPrimaryKey;
    }

    public void setSidPrimaryKeyQuery(String selectSidPrimaryKey) {
        this.selectSidPrimaryKey = selectSidPrimaryKey;
    }

    public void setUpdateObjectIdentity(String updateObjectIdentity) {
        this.updateObjectIdentity = updateObjectIdentity;
    }

    /**
     * @param foreignKeysInDatabase if false this class will perform additional FK constrain checking, which may
     * cause deadlocks (the default is true, so deadlocks are avoided but the database is expected to enforce FKs)
     */
    public void setForeignKeysInDatabase(boolean foreignKeysInDatabase) {
        this.foreignKeysInDatabase = foreignKeysInDatabase;
    }
}
