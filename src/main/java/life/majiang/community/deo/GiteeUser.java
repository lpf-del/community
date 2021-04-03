package life.majiang.community.deo;

import org.springframework.stereotype.Component;

@Component
public class GiteeUser {
    private String name;
    private Long id;
    private String bio;
    private String email;
    private String avatar_url;

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public GiteeUser(String name, Long id, String bio, String email, String avatar_url) {
        this.name = name;
        this.id = id;
        this.bio = bio;
        this.email = email;
        this.avatar_url = avatar_url;
    }

    public GiteeUser() {
    }

    @Override
    public String toString() {
        return "GiteeUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }
}
