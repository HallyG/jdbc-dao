package io.github.hallyg.domain;

public class User {
  private Long id;
  private String email;
  private String username;
  private String password;

  public User() {}

  public User(Long id, String email) {
    this.id = id;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (!(o instanceof User)) return false;

    final User other = (User) o;
    if (id == null) {
      return other.id == null;
    }

    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    if (id == null) {
      return 0;
    }

    return id.hashCode();
  }

  @Override
  public String toString() {
    return String.format("User[id=%d,email=%s]", id, email);
  }
}
