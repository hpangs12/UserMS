package com.userms.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Entity for User
 * <p>
 * This class provides Entity for User to interact with the database.
 * </p>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="users")
public class MyUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String emailId;

    private String firstName;
    private String lastName;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_addresses",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "address")
	private List<String> address;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
	private Set<String> roles;
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;   // default true
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private LocalDateTime lastLoginAt;
	
}
