package de.geoinfoffm.registry.core.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.JdbcAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

public class RegistryAclService implements AclService
{
    //~ Static fields/initializers =====================================================================================

    protected static final Log log = LogFactory.getLog(JdbcAclService.class);
    private static final String DEFAULT_SELECT_ACL_WITH_PARENT_SQL = "select obj.object_id_identity as obj_id, class.class as class "
        + "from acl_object_identity obj, acl_object_identity parent, acl_class class "
        + "where obj.parent_object = parent.uuid and obj.object_id_class = class.uuid "
        + "and parent.object_id_identity = ? and parent.object_id_class = ("
        + "select uuid FROM acl_class where acl_class.class = ?)";

    //~ Instance fields ================================================================================================

    protected final JdbcTemplate jdbcTemplate;
    private final LookupStrategy lookupStrategy;
    private String findChildrenSql = DEFAULT_SELECT_ACL_WITH_PARENT_SQL;

    //~ Constructors ===================================================================================================

    public RegistryAclService(DataSource dataSource, LookupStrategy lookupStrategy) {
        Assert.notNull(dataSource, "DataSource required");
        Assert.notNull(lookupStrategy, "LookupStrategy required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.lookupStrategy = lookupStrategy;
    }

    //~ Methods ========================================================================================================

    public List<ObjectIdentity> findChildren(ObjectIdentity parentIdentity) {
        Object[] args = {parentIdentity.getIdentifier(), parentIdentity.getType()};
        List<ObjectIdentity> objects = jdbcTemplate.query(findChildrenSql, args,
                new RowMapper<ObjectIdentity>() {
                    public ObjectIdentity mapRow(ResultSet rs, int rowNum) throws SQLException {
                        String javaType = rs.getString("class");
                        UUID identifier = (UUID)rs.getObject("obj_id");

                        return new ObjectIdentityImpl(javaType, identifier);
                    }
                });

        if (objects.size() == 0) {
            return null;
        }

        return objects;
    }

    public Acl readAclById(ObjectIdentity object, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> map = readAclsById(Arrays.asList(object), sids);
        Assert.isTrue(map.containsKey(object), "There should have been an Acl entry for ObjectIdentity " + object);

        return (Acl) map.get(object);
    }

    public Acl readAclById(ObjectIdentity object) throws NotFoundException {
        return readAclById(object, null);
    }

    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects) throws NotFoundException {
        return readAclsById(objects, null);
    }

    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> result = lookupStrategy.readAclsById(objects, sids);

        // Check every requested object identity was found (throw NotFoundException if needed)
        for (ObjectIdentity oid : objects) {
            if (!result.containsKey(oid)) {
                throw new NotFoundException("Unable to find ACL information for object identity '" + oid + "'");
            }
        }

        return result;
    }

    /**
     * Allows customization of the SQL query used to find child object identities.
     *
     * @param findChildrenSql
     */
    public void setFindChildrenQuery(String findChildrenSql) {
        this.findChildrenSql = findChildrenSql;
    }
}
