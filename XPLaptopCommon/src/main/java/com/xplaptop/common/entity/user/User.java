package com.xplaptop.common.entity.user;

import com.xplaptop.common.entity.IdBaseEntity;
import lombok.*;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User extends IdBaseEntity {
	
	@Column(length = 128, nullable = false, unique = true)
	@NonNull
	private String email;
	
	@Column(length = 64, nullable = false)
	@NonNull
	private String password;
	
	@Column(length = 45, nullable = false)
	@NonNull
	private String firstName;
	
	@Column(length = 45, nullable = false)
	@NonNull
	private String lastName;
	
	@Column(length = 64)

	private String photos;
	
	private boolean enabled;
	
	@ManyToMany
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles = new HashSet<>();
	
	public void addRole(Role role) {
		roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", photos=" + photos + ", enabled=" + enabled + ", roles=" + roles + "]";
	}
	
	@Transient
	public String photoPath() {
		if(this.id == null || this.photos == null) {
			return "/images/defaultuserimg.png";
		} else {
			return "/users-photos/"+id+"/"+photos;
		}
	}
	
	@Transient
	public String getFullName() {
		return this.getFirstName()+" "+this.getLastName();
	}
	
	
}
