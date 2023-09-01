package com.tpzwl.octopus.api.security.model;

import jakarta.persistence.*;

@Entity(name = "refreshtoken")
@Table(
	/*indexes = { @Index(name="index_token",  columnList="token", unique = true) },*/
	uniqueConstraints = { 
		@UniqueConstraint(name="unique_user_device", columnNames = { "user_id", "deviceType" }),
		@UniqueConstraint(name="unique_token", columnNames = { "token" })
	}
)
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(length = 25)
	private EnumDeviceType deviceType;

	@Column(nullable = false, /*unique = true, */ length = 255)
	private String token;

//	@Column(nullable = false)
//	private Instant expiryDate;

	public RefreshToken() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public EnumDeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(EnumDeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

//	public Instant getExpiryDate() {
//		return expiryDate;
//	}
//
//	public void setExpiryDate(Instant expiryDate) {
//		this.expiryDate = expiryDate;
//	}

}
