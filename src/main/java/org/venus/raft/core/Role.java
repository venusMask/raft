package org.venus.raft.core;

/**
 * Raft Role
 */
public final class Role {

    public static final Role LEADER = new Role("leader");

    public static final Role FOLLOWER = new Role("follower");

    public static final Role CANDIDATE = new Role("candidate");

    private final String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}
