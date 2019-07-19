package be.sgerard.poc.githuboauth.model.i18n.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Collections.unmodifiableCollection;

/**
 * @author Sebastien Gerard
 */
@Entity(name = "translation_bundle_file")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"workspace", "location"})
        }
)
public class BundleFileEntity {

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workspace")
    private WorkspaceEntity workspace;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String location;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<BundleKeyEntity> keys = new HashSet<>();

    @Version
    private int version;

    BundleFileEntity() {
    }

    public BundleFileEntity(WorkspaceEntity workspace, String name, String location) {
        this.id = UUID.randomUUID().toString();

        this.workspace = workspace;
        this.workspace.addFile(this);

        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorkspaceEntity getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceEntity workspace) {
        this.workspace = workspace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Collection<BundleKeyEntity> getKeys() {
        return unmodifiableCollection(keys);
    }

    void addKey(BundleKeyEntity keyEntity) {
        this.keys.add(keyEntity);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}