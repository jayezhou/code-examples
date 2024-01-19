package com.tpzwl.be.api.security.model;

import com.tpzwl.be.api.model.RoleCount;

import jakarta.persistence.*;

@NamedNativeQuery(name = "Role.countByRole",
query = "SELECT r.id, r.name, COUNT(r.*) count FROM roles r GROUP BY r.id ORDER by r.id",
resultSetMapping = "Mapping.RoleCount")
@SqlResultSetMapping(name = "Mapping.RoleCount",
   classes = @ConstructorResult(targetClass = RoleCount.class,
                                columns = {@ColumnResult(name = "id"), @ColumnResult(name = "name", type=String.class), 
                                		@ColumnResult(name = "count")
                                           }))
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(length = 40)
  private EnumRole name;

  public Role() {

  }

  public Role(EnumRole name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public EnumRole getName() {
    return name;
  }

  public void setName(EnumRole name) {
    this.name = name;
  }
}