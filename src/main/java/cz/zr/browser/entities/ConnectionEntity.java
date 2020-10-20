package cz.zr.browser.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connection")
public class ConnectionEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(unique = true)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String hostname;

  @Column(nullable = false)
  private Integer port;

  @Column(nullable = false)
  private String databaseName;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @Column(nullable = false)
  private Date createdAt;
}
