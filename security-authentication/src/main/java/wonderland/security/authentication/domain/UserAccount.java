package wonderland.security.authentication.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "User")
@Proxy(lazy = false)
@JsonDeserialize(builder = UserAccount.Builder.class)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String salt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorities",
            joinColumns = @JoinColumn(name = "phoneNumber"),
            inverseJoinColumns = @JoinColumn(name = "roleName"))
    private Set<Role> roles;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    public UserAccount(String phoneNumber, String email, String name, String password, String salt, Set<Role> roles, State state) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.roles = roles;
        this.state = state;
    }

    private UserAccount(Builder builder) {
        id = builder.id;
        phoneNumber = builder.phoneNumber;
        name = builder.name;
        password = builder.password;
        salt = builder.salt;
        email = builder.email;
        roles = builder.roles;
        state = builder.state;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("phoneNumber", phoneNumber)
                .add("name", name)
                .add("password", password)
                .add("salt", salt)
                .add("email", email)
                .add("roles", roles)
                .add("state", state)
                .toString();
    }

    public static Builder newBuilder(UserAccount copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.phoneNumber = copy.getPhoneNumber();
        builder.name = copy.getName();
        builder.password = copy.getPassword();
        builder.salt = copy.getSalt();
        builder.email = copy.getEmail();
        builder.roles = copy.getRoles();
        builder.state = copy.getState();
        return builder;
    }

    public Builder cloneBuilder(){
        return newBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equal(getId(), that.getId()) &&
                Objects.equal(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equal(getName(), that.getName()) &&
                Objects.equal(getPassword(), that.getPassword()) &&
                Objects.equal(getSalt(), that.getSalt()) &&
                Objects.equal(getEmail(), that.getEmail()) &&
                Objects.equal(getRoles(), that.getRoles()) &&
                getState() == that.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getPhoneNumber(), getName(), getPassword(), getSalt(), getEmail(), getRoles(), getState());
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public State getState() {
        return state;
    }

    public static final class Builder {
        private Long id;
        private String phoneNumber;
        private String name;
        private String password;
        private String salt;
        private String email;
        private Set<Role> roles;
        private State state;

        private Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withPhoneNumber(String val) {
            phoneNumber = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public Builder withSalt(String val) {
            salt = val;
            return this;
        }

        public Builder withEmail(String val) {
            email = val;
            return this;
        }

        public Builder withRoles(Set<Role> val) {
            roles = val;
            return this;
        }

        public Builder withState(State val) {
            state = val;
            return this;
        }

        public UserAccount build() {
            return new UserAccount(this);
        }
    }

    //    User() {
//    }

}
